package org.wgx.dswof.mixin;

import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.wgx.dswof.dragonsurvival.IceWingFreezingImmunity;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Inject(method = "canFreeze", at = @At("HEAD"), cancellable = true)
    private void dswof$preventIceWingFreezing(CallbackInfoReturnable<Boolean> callback) {
        if (IceWingFreezingImmunity.isImmune((LivingEntity) (Object) this)) {
            callback.setReturnValue(false);
        }
    }
}
