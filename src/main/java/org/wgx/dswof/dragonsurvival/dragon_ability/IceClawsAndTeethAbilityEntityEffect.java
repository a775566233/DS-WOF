package org.wgx.dswof.dragonsurvival.dragon_ability;

import by.dragonsurvivalteam.dragonsurvival.registry.DSAttributes;
import by.dragonsurvivalteam.dragonsurvival.registry.dragon.ability.DragonAbilityInstance;
import by.dragonsurvivalteam.dragonsurvival.registry.dragon.ability.entity_effects.AbilityEntityEffect;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import org.wgx.dswof.registry.dragonsurvival.DsWofAttributes;

import java.util.*;

public record IceClawsAndTeethAbilityEntityEffect(
        // 伤害类型
        String customIcon
) implements AbilityEntityEffect {

    public static final String ID = "ice_claws_and_teeth";

    @Override
    public void apply(ServerPlayer dragon, DragonAbilityInstance dragonAbilityInstance, Entity entity) {
        TARGET_DRAGON_SET.add(dragon.getUUID());
        // TODO 添加状态提示
    }

    private static final Set<UUID> TARGET_DRAGON_SET = new HashSet<>();

    public static final MapCodec<IceClawsAndTeethAbilityEntityEffect> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.optionalFieldOf("custom_icon", "")
                    .forGetter(IceClawsAndTeethAbilityEntityEffect::customIcon)
    ).apply(instance, IceClawsAndTeethAbilityEntityEffect::new));

    @Override
    public MapCodec<? extends AbilityEntityEffect> entityCodec() {
        return CODEC;
    }

    @Override
    public void remove(ServerPlayer dragon, DragonAbilityInstance ability, Entity entity, boolean isAutoRemoval) {
        TARGET_DRAGON_SET.remove(dragon.getUUID());
    }

    public static boolean isTargetDragon(Player dragon) {
        return TARGET_DRAGON_SET.contains(dragon.getUUID());
    }
}
