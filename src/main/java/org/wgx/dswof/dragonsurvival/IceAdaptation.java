package org.wgx.dswof.dragonsurvival;

import by.dragonsurvivalteam.dragonsurvival.common.capability.DragonStateProvider;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import org.wgx.dswof.registry.dragonsurvival.DsWofAttributes;

/** Traits are granted and removed by DS ability modifiers, rather than a hardcoded species check. */
public final class IceAdaptation {
    private IceAdaptation() {
    }

    public static boolean isFreezingImmune(Entity entity) {
        return hasTrait(entity, DsWofAttributes.FREEZING_IMMUNITY);
    }

    public static boolean canMoveInPowderSnow(Entity entity) {
        return hasTrait(entity, DsWofAttributes.POWDER_SNOW_MOBILITY);
    }

    public static boolean hasIceGrip(Entity entity) {
        return hasTrait(entity, DsWofAttributes.ICE_GRIP);
    }

    private static boolean hasTrait(Entity entity, Holder<Attribute> attribute) {
        if (!(entity instanceof Player player) || !DragonStateProvider.isDragon(player)) {
            return false;
        }
        var instance = player.getAttribute(attribute);
        return instance != null && instance.getValue() > 0;
    }
}
