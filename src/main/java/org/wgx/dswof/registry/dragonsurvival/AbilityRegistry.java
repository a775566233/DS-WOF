package org.wgx.dswof.registry.dragonsurvival;

import by.dragonsurvivalteam.dragonsurvival.registry.dragon.ability.entity_effects.AbilityEntityEffect;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.wgx.dswof.DsWof;
import org.wgx.dswof.dragonsurvival.dragon_ability.ClawOfFrostAbilityEntityEffect;
import org.wgx.dswof.dragonsurvival.dragon_ability.IceBreathAbilityEntityEffect;
import org.wgx.dswof.dragonsurvival.dragon_ability.IceClawsAndTeethAbilityEntityEffect;

@EventBusSubscriber(modid = DsWof.MODID)
public class AbilityRegistry {
    @SubscribeEvent
    public static void register(RegisterEvent event) {
        if (event.getRegistry() == AbilityEntityEffect.REGISTRY) {
            event.register(AbilityEntityEffect.REGISTRY_KEY,
                    ResourceLocation.fromNamespaceAndPath(DsWof.MODID, IceBreathAbilityEntityEffect.ID),
                    () -> IceBreathAbilityEntityEffect.CODEC);
            event.register(AbilityEntityEffect.REGISTRY_KEY,
                    ResourceLocation.fromNamespaceAndPath(DsWof.MODID, ClawOfFrostAbilityEntityEffect.ID),
                    () -> ClawOfFrostAbilityEntityEffect.CODEC);
            event.register(AbilityEntityEffect.REGISTRY_KEY,
                    ResourceLocation.fromNamespaceAndPath(DsWof.MODID, IceClawsAndTeethAbilityEntityEffect.ID),
                    () -> IceClawsAndTeethAbilityEntityEffect.CODEC);
        }
    }
}
