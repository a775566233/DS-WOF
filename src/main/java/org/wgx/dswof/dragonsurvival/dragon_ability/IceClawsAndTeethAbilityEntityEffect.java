package org.wgx.dswof.dragonsurvival.dragon_ability;

import by.dragonsurvivalteam.dragonsurvival.registry.dragon.ability.DragonAbilityInstance;
import by.dragonsurvivalteam.dragonsurvival.registry.dragon.ability.entity_effects.AbilityEntityEffect;
import com.mojang.serialization.MapCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

import java.util.*;

public record IceClawsAndTeethAbilityEntityEffect() implements AbilityEntityEffect {

    public static final String ID = "ice_claws_and_teeth";

    @Override
    public void apply(ServerPlayer dragon, DragonAbilityInstance dragonAbilityInstance, Entity entity) {
        TARGET_DRAGON_SET.add(dragon.getUUID());
    }

    private static final Set<UUID> TARGET_DRAGON_SET = new HashSet<>();

    public static final MapCodec<IceClawsAndTeethAbilityEntityEffect> CODEC =
            MapCodec.unit(new IceClawsAndTeethAbilityEntityEffect());

    @Override
    public MapCodec<? extends AbilityEntityEffect> entityCodec() {
        return CODEC;
    }

    @Override
    public void remove(ServerPlayer dragon, DragonAbilityInstance ability, Entity entity, boolean isAutoRemoval) {
        TARGET_DRAGON_SET.remove(dragon.getUUID());
    }

    public static boolean isTargetDragon(ServerPlayer dragon) {
        return TARGET_DRAGON_SET.contains(dragon.getUUID());
    }
}
