package org.wgx.dswof.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.PowderSnowBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.wgx.dswof.dragonsurvival.IceWingFreezingImmunity;
import org.wgx.dswof.dragonsurvival.IceAdaptation;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Inject(method = "canFreeze", at = @At("HEAD"), cancellable = true)
    private void dswof$preventIceWingFreezing(CallbackInfoReturnable<Boolean> callback) {
        if (IceWingFreezingImmunity.isImmune((LivingEntity) (Object) this)) {
            callback.setReturnValue(false);
        }
    }

    @Redirect(method = "travel", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/state/BlockState;getFriction(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/Entity;)F"))
    private float dswof$useNormalFrictionOnIce(BlockState state, LevelReader level, BlockPos pos, Entity entity) {
        // 在冰面正常行走
        return state.is(BlockTags.ICE) && IceAdaptation.hasIceGrip(entity)
                ? 0.6F : state.getFriction(level, pos, entity);
    }

    @Redirect(method = "handleRelativeFrictionAndCalculateMovement", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/PowderSnowBlock;canEntityWalkOnPowderSnow(Lnet/minecraft/world/entity/Entity;)Z"))
    private boolean dswof$allowDeliberateSnowDescent(Entity entity) {
        if (IceAdaptation.canMoveInPowderSnow(entity) && entity.isShiftKeyDown()) {
            // Vanilla also pushes snow walkers up on horizontal collision. Let crouching win.
            return false;
        }
        return PowderSnowBlock.canEntityWalkOnPowderSnow(entity);
    }
}
