package org.wgx.dswof.dragonsurvival.mob_effect;

import by.dragonsurvivalteam.dragonsurvival.common.effects.ModifiableMobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class FrostEffect extends ModifiableMobEffect {

    public FrostEffect(MobEffectCategory type, int color, boolean incurable) {
        super(type, color, incurable);
    }

    @Override
    public boolean applyEffectTick(LivingEntity target, int amplifier) {
        if (!target.canFreeze()) {
            return false;
        }
        // 原版离开细雪时每 tick 减少 2，额外补 2 点。
        int frozenTicks = target.getTicksRequiredToFreeze() + 3;
        target.setTicksFrozen(Math.max(target.getTicksFrozen() + 1, frozenTicks));
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}
