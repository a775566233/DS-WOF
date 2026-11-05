package org.wgx.dswof.registry.dragonsurvival;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.wgx.dswof.DsWof;
import org.wgx.dswof.dragonsurvival.mob_effect.FrostEffect;

public final class ModEffects {
    public static final DeferredRegister<MobEffect> REGISTRY =
            DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, DsWof.MODID);

    private ModEffects() {}
}
