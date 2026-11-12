package org.wgx.dswof.dragonsurvival.dragon_ability;

import by.dragonsurvivalteam.dragonsurvival.common.capability.DragonStateProvider;
import by.dragonsurvivalteam.dragonsurvival.common.codecs.MiscCodecs;
import by.dragonsurvivalteam.dragonsurvival.registry.DSAttributes;
import by.dragonsurvivalteam.dragonsurvival.registry.dragon.ability.DragonAbilityInstance;
import by.dragonsurvivalteam.dragonsurvival.registry.dragon.ability.entity_effects.AbilityEntityEffect;
import by.dragonsurvivalteam.dragonsurvival.util.ExperienceUtils;
import by.dragonsurvivalteam.dragonsurvival.util.Expression;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import org.wgx.dswof.DsWof;
import org.wgx.dswof.registry.dragonsurvival.DsWofAttributes;

import java.math.BigDecimal;

public record ClawOfFrostAbilityEntityEffect(
        // 施法者伤害缩放属性，与 DS 内置 damage 效果一致
        Holder<Attribute> scale,
        float baseMultiplier,
        // 动态表达式
        Expression expression
) implements AbilityEntityEffect {

    public static final Expression DEFAULT_EXPRESSION = new Expression(
            "base_multiplier * (1 + (ability_level - 1) * 0.6)"
    );

    public static final Codec<Expression> EXPRESSION_CODEC = MiscCodecs.expressionCodec(
            "amount", "scale", "ability_level", "experience_level", "experience_points",
            "growth", "base_multiplier"
    );

    public static final MapCodec<ClawOfFrostAbilityEntityEffect> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Attribute.CODEC.optionalFieldOf("scale", DSAttributes.DRAGON_ABILITY_DAMAGE)
                    .forGetter(ClawOfFrostAbilityEntityEffect::scale),
            Codec.FLOAT.optionalFieldOf("base_multiplier", 0.5f)
                    .forGetter(ClawOfFrostAbilityEntityEffect::baseMultiplier),
            EXPRESSION_CODEC.optionalFieldOf("expression", DEFAULT_EXPRESSION)
                    .forGetter(ClawOfFrostAbilityEntityEffect::expression)
    ).apply(instance, ClawOfFrostAbilityEntityEffect::new));

    public static final String ID = "claw_of_frost";
    private static final ResourceLocation BONUS_ID = ResourceLocation.fromNamespaceAndPath(DsWof.MODID, ID);

    @Override
    public void apply(ServerPlayer dragon, DragonAbilityInstance dragonAbilityInstance, Entity entity) {
        syncMeleeBonus(dragon, 0);
        syncMeleeBonus(dragon, calculateDamage(dragon, dragonAbilityInstance.level()));
    }

    @Override
    public MapCodec<? extends AbilityEntityEffect> entityCodec() {
        return CODEC;
    }

    @Override
    public void remove(ServerPlayer dragon, DragonAbilityInstance ability, Entity entity, boolean isAutoRemoval) {
        syncMeleeBonus(dragon, 0);
        AbilityEntityEffect.super.remove(dragon, ability, entity, isAutoRemoval);
    }

    private float calculateDamage(ServerPlayer dragon, int abilityLevel) {
        // Expression 可变；当前仅在服务端主线程执行，并且每次求值都设置全部变量。
        expression.setVariable("base_multiplier", BigDecimal.valueOf(baseMultiplier));
        expression.setVariable("scale", BigDecimal.valueOf(dragon.getAttributeValue(scale)));
        expression.setVariable("ability_level", BigDecimal.valueOf(abilityLevel));
        expression.setVariable("experience_level", BigDecimal.valueOf(dragon.experienceLevel));
        expression.setVariable("experience_points", BigDecimal.valueOf(ExperienceUtils.getTotalExperience(dragon)));
        expression.setVariable("growth", BigDecimal.valueOf(DragonStateProvider.getData(dragon).getGrowth()));
        return expression.eval().floatValue();
    }

    public void syncMeleeBonus(ServerPlayer dragon, double multiplier) {
        var damage = dragon.getAttribute(Attributes.ATTACK_DAMAGE);
        if (damage == null) return;
        // multiplier：0=无加成，1.2=120%，1.4=140%
        double bonus = multiplier <= 1.0 ? 0.0 : multiplier - 1.0;
        if (bonus == 0.0) {
            damage.removeModifier(BONUS_ID);
            return;
        }
        // 同一 ID 会替换旧修饰符，不会叠多层，也不动别人的修饰符
        damage.addOrUpdateTransientModifier(new AttributeModifier(
                BONUS_ID,
                bonus,
                AttributeModifier.Operation.ADD_MULTIPLIED_BASE
        ));
    }
}
