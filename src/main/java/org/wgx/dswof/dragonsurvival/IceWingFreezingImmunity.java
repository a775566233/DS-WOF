package org.wgx.dswof.dragonsurvival;

import by.dragonsurvivalteam.dragonsurvival.common.capability.DragonStateProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import org.wgx.dswof.DsWof;

/** 冰翼龙免疫冻结状态和冻结伤害；冰息的直接伤害由技能自身减半。 */
@EventBusSubscriber(modid = DsWof.MODID)
public final class IceWingFreezingImmunity {
    private static final ResourceLocation ICE_WINGS_SPECIES =
            ResourceLocation.fromNamespaceAndPath(DsWof.MODID, "ice_wings_dragon");
    private static final ResourceKey<DamageType> ICE_BREATH_DAMAGE = ResourceKey.create(
            Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(DsWof.MODID, "ice_breath"));

    private IceWingFreezingImmunity() {
    }

    public static boolean isImmune(Entity entity) {
        if (!(entity instanceof Player player)) {
            return false;
        }
        var dragon = DragonStateProvider.getData(player);
        return dragon.isDragon() && ICE_WINGS_SPECIES.equals(dragon.speciesKey().location());
    }

    @SubscribeEvent
    public static void onIncomingDamage(LivingIncomingDamageEvent event) {
        // 冰息是独立的直接伤害；即使外部数据包给它加了冻结标签，这里也不取消它。
        if (event.getSource().is(DamageTypeTags.IS_FREEZING)
                && !event.getSource().is(ICE_BREATH_DAMAGE)
                && isImmune(event.getEntity())) {
            event.setCanceled(true);
        }
    }
}
