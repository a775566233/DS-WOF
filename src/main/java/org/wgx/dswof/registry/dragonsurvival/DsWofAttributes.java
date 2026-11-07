package org.wgx.dswof.registry.dragonsurvival;

import by.dragonsurvivalteam.dragonsurvival.common.TimeAttribute;
import by.dragonsurvivalteam.dragonsurvival.registry.datagen.Translation;
import by.dragonsurvivalteam.dragonsurvival.util.Functions;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.PercentageAttribute;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.wgx.dswof.DsWof;

@EventBusSubscriber
public class DsWofAttributes {

    public static final DeferredRegister<Attribute> REGISTRY;
    public static final Holder<Attribute> HEAT_RESISTANCE_TIME;
    public static final Holder<Attribute> ICE_BREATH_MULTIPLIER;
    public static final Holder<Attribute> FREEZING_IMMUNITY;
    public static final Holder<Attribute> POWDER_SNOW_MOBILITY;
    public static final Holder<Attribute> ICE_GRIP;

    @SubscribeEvent
    public static void attachAttributes(EntityAttributeModificationEvent event) {
        event.add(EntityType.PLAYER, HEAT_RESISTANCE_TIME);
        event.add(EntityType.PLAYER, ICE_BREATH_MULTIPLIER);
        event.add(EntityType.PLAYER, FREEZING_IMMUNITY);
        event.add(EntityType.PLAYER, POWDER_SNOW_MOBILITY);
        event.add(EntityType.PLAYER, ICE_GRIP);
    }

    static {
        REGISTRY = DeferredRegister.create(Registries.ATTRIBUTE, DsWof.MODID);
        HEAT_RESISTANCE_TIME = REGISTRY.register("heat_resistance_time", () -> (new TimeAttribute(Translation.Type.ATTRIBUTE.wrap("heat_resistance_time"), Functions.secondsToTicks(10.0F), 0.0F, 16384.0F)).setSyncable(true));
        ICE_BREATH_MULTIPLIER = REGISTRY.register("ice_breath_multiplier", () -> (new PercentageAttribute(Translation.Type.ATTRIBUTE.wrap("ice_breath_multiplier"), 1, 0.1, 100)).setSyncable(true));
        FREEZING_IMMUNITY = registerTrait("freezing_immunity");
        POWDER_SNOW_MOBILITY = registerTrait("powder_snow_mobility");
        ICE_GRIP = registerTrait("ice_grip");
    }

    private static Holder<Attribute> registerTrait(String name) {
        return REGISTRY.register(name, () -> new RangedAttribute("attribute.dswof." + name, 0, 0, 1).setSyncable(true));
    }
}
