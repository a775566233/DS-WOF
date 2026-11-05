package org.wgx.dswof.dragonsurvival.dragon_ability;

import by.dragonsurvivalteam.dragonsurvival.common.capability.DragonStateProvider;
import by.dragonsurvivalteam.dragonsurvival.common.codecs.MiscCodecs;
import by.dragonsurvivalteam.dragonsurvival.registry.DSAttributes;
import by.dragonsurvivalteam.dragonsurvival.registry.dragon.ability.DragonAbilityInstance;
import by.dragonsurvivalteam.dragonsurvival.registry.dragon.ability.entity_effects.AbilityEntityEffect;
import by.dragonsurvivalteam.dragonsurvival.util.ExperienceUtils;
import by.dragonsurvivalteam.dragonsurvival.util.Expression;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import org.jetbrains.annotations.NotNull;
import org.wgx.dswof.DsWof;
import org.slf4j.Logger;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public record IceBreathAbilityEntityEffect(
        // 伤害类型
        Holder<DamageType> damageType,
        // 基础伤害
        LevelBasedValue baseDamage,
        // 增长间隔刻
        int growthIntervalTicks,
        // 每单位时间增长数
        LevelBasedValue growthPerUnit,
        // 伤害最大值
        LevelBasedValue maxDamage,
        // 对冰冻免疫的实体伤害衰减倍数
        float freezeImmuneMultiplier,
        // 吐息中断失效刻
        int resetAfterTicks,
        // 施法者伤害缩放属性，与 DS 内置 damage 效果一致
        Holder<Attribute> scale,
        Expression expression
) implements AbilityEntityEffect {

    public static final Expression DEFAULT_EXPRESSION = new Expression(
            "MIN(max_damage, amount + growth_per_unit * exposure_ticks / growth_interval_ticks) * scale"
    );

    // 加载时声明的变量必须与 calculateDamage 中实际注入的变量一致。
    public static final Codec<Expression> EXPRESSION_CODEC = MiscCodecs.expressionCodec(
            "amount", "scale", "ability_level", "experience_level", "experience_points",
            "growth", "current_movement_speed", "growth_per_unit", "growth_interval_ticks",
            "max_damage", "exposure_ticks", "exposure_seconds"
    );

    public static final MapCodec<IceBreathAbilityEntityEffect> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            DamageType.CODEC.fieldOf("damage_type").forGetter(IceBreathAbilityEntityEffect::damageType),
            LevelBasedValue.CODEC.fieldOf("base_damage").forGetter(IceBreathAbilityEntityEffect::baseDamage),
            Codec.intRange(1, Integer.MAX_VALUE).optionalFieldOf("growth_interval_ticks", 10)
                    .forGetter(IceBreathAbilityEntityEffect::growthIntervalTicks),
            LevelBasedValue.CODEC.fieldOf("growth_per_second").forGetter(IceBreathAbilityEntityEffect::growthPerUnit),
            LevelBasedValue.CODEC.fieldOf("max_damage").forGetter(IceBreathAbilityEntityEffect::maxDamage),
            Codec.FLOAT.optionalFieldOf("freeze_immune_multiplier", 0.5f)
                    .forGetter(IceBreathAbilityEntityEffect::freezeImmuneMultiplier),
            Codec.intRange(0, Integer.MAX_VALUE).optionalFieldOf("reset_after_ticks", 10)
                    .forGetter(IceBreathAbilityEntityEffect::resetAfterTicks),
            Attribute.CODEC.optionalFieldOf("scale", DSAttributes.DRAGON_ABILITY_DAMAGE)
                    .forGetter(IceBreathAbilityEntityEffect::scale),
            EXPRESSION_CODEC.optionalFieldOf("expression", DEFAULT_EXPRESSION)
                    .forGetter(IceBreathAbilityEntityEffect::expression)
    ).apply(instance, IceBreathAbilityEntityEffect::new));

    //自定义效果每次被调用时，以施法者 UUID、目标 UUID 和技能 ID 组成 key，记录 lastSeen 与 exposureTicks：
    private static final Map<Key, State> STATES = new HashMap<>();
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Set<String> REPORTED_INVALID_EXPRESSIONS = new HashSet<>();
    private record Key(UUID dragonUUID, UUID targetUUID, String abilityId, ResourceKey<Level> dimension) {}
    private record State(long lastSeen, long exposureTicks, long expiresAt) {}

    @Override
    public void apply(ServerPlayer dragon, DragonAbilityInstance dragonAbilityInstance, Entity entity) {
        if (!(entity instanceof LivingEntity target)) {
            return;
        }
        // ServerLevel 由服务器管理；这里不能使用 try-with-resources 关闭世界。
        ServerLevel serverLevel = dragon.serverLevel();
        long now = serverLevel.getGameTime();
        Key key = new Key(dragon.getUUID(), target.getUUID(), dragonAbilityInstance.id(), serverLevel.dimension());
        State state = STATES.get(key);
        long elapsed = state == null ? 0 : now - state.lastSeen();
        long exposureTicks = state == null || elapsed < 0 || elapsed > resetAfterTicks
                ? 0
                : state.exposureTicks() + elapsed;
        STATES.put(key, new State(now, exposureTicks, now + resetAfterTicks));

        float damage;
        try {
            damage = calculateDamage(dragon, dragonAbilityInstance.level(), exposureTicks);
        } catch (RuntimeException exception) {
            // 加载验证不保证运行时不会除零等；同一公式每次服务器运行只报告一次。
            if (REPORTED_INVALID_EXPRESSIONS.add(expression.getExpression())) {
                LOGGER.warn("Ice breath expression failed: {}", expression.getExpression(), exception);
            }
            return;
        }

        // 此倍率在表达式求值之后统一应用，避免自定义公式遗漏冻结免疫减伤。
        if (!target.canFreeze()) {
            damage *= freezeImmuneMultiplier;
        } else {
/*
            // 原版细雪造成的冻结由实体的 ticksFrozen（冻结积累值）控制，不是药水效果，因此不存在可填入 "effects" 的 "minecraft:freezing"。
            // getTicksFrozen() 获取当前冻结积累值
            int current = target.getTicksFrozen();
            // 获取完全冻结所需的值，原版通常为 140
            int threshold = target.getTicksRequiredToFreeze();
            // 离开细雪时原版每 tick 先减 2，再检查完全冻结并尝试造成伤害。
            // 当前吐息每 10 tick 命中；额外 40 点可覆盖命中间隔，不能只封顶到 threshold。
            // 不降低其他效果已经积累的更高冻结值。
            target.setTicksFrozen(Math.min(threshold + 500, current + 200));
            */
        }
        if (Float.isFinite(damage) && damage > 0.0F) {
            target.hurt(new DamageSource(damageType, dragon), damage);
            dragon.setLastHurtMob(target);
        }
    }

    private float calculateDamage(ServerPlayer dragon, int abilityLevel, long exposureTicks) {
        // Expression 可变；当前仅在服务端主线程执行，并且每次求值都设置全部变量。
        expression.setVariable("amount", BigDecimal.valueOf(baseDamage.calculate(abilityLevel)));
        expression.setVariable("scale", BigDecimal.valueOf(dragon.getAttributeValue(scale)));
        expression.setVariable("ability_level", BigDecimal.valueOf(abilityLevel));
        expression.setVariable("experience_level", BigDecimal.valueOf(dragon.experienceLevel));
        expression.setVariable("experience_points", BigDecimal.valueOf(ExperienceUtils.getTotalExperience(dragon)));
        expression.setVariable("growth", BigDecimal.valueOf(DragonStateProvider.getData(dragon).getGrowth()));
        expression.setVariable("current_movement_speed", BigDecimal.valueOf(dragon.getDeltaMovement().length()));
        expression.setVariable("growth_per_unit", BigDecimal.valueOf(growthPerUnit.calculate(abilityLevel)));
        expression.setVariable("growth_interval_ticks", BigDecimal.valueOf(growthIntervalTicks));
        expression.setVariable("max_damage", BigDecimal.valueOf(maxDamage.calculate(abilityLevel)));
        expression.setVariable("exposure_ticks", BigDecimal.valueOf(exposureTicks));
        expression.setVariable("exposure_seconds", BigDecimal.valueOf(exposureTicks)
                .divide(BigDecimal.valueOf(20)));
        return expression.eval().floatValue();
    }

    @Override
    public MapCodec<? extends AbilityEntityEffect> entityCodec() {
        return CODEC;
    }

    public static int clearState(@NotNull ServerLevel serverLevel) {
        long now = serverLevel.getGameTime();
        int before = STATES.size();

        STATES.entrySet().removeIf(entry ->
                entry.getKey().dimension().equals(serverLevel.dimension())
                        && now > entry.getValue().expiresAt()
        );

        return before - STATES.size();
    }

    @EventBusSubscriber(modid = DsWof.MODID)
    public static class StateEvents {
        @SubscribeEvent
        public static void onLevelTick(LevelTickEvent.Post event) {
            if (event.getLevel() instanceof ServerLevel level
                    && level.getGameTime() % 20 == 0) {
                clearState(level);
            }
        }

        @SubscribeEvent
        public static void onServerStopped(ServerStoppedEvent event) {
            STATES.clear();
            REPORTED_INVALID_EXPRESSIONS.clear();
        }
    }

}
