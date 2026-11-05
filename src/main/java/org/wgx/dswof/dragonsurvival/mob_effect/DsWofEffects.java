package org.wgx.dswof.dragonsurvival.mob_effect;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.wgx.dswof.DsWof;

public class DsWofEffects {
    public static final DeferredRegister<MobEffect> REGISTRY;

    public static Holder<MobEffect> FROST;

    static {
        REGISTRY = DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, DsWof.MODID);

        FROST = REGISTRY.register("frost", () -> new FrostEffect(MobEffectCategory.HARMFUL, 0, true));

    }
}
