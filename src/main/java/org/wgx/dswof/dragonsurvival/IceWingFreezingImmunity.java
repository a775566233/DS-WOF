package org.wgx.dswof.dragonsurvival;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import org.wgx.dswof.DsWof;

/** DS 被动属性控制冻结免疫；冰息的直接伤害由冰息技能自身减半。 */
@EventBusSubscriber(modid = DsWof.MODID)
public final class IceWingFreezingImmunity {
    private static final ResourceKey<DamageType> ICE_BREATH_DAMAGE = ResourceKey.create(
            Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(DsWof.MODID, "ice_breath"));

    private IceWingFreezingImmunity() {
    }

    public static boolean isImmune(Entity entity) {
        return IceAdaptation.isFreezingImmune(entity);
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
