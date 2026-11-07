package org.wgx.dswof.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.PowderSnowBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.wgx.dswof.dragonsurvival.IceAdaptation;

@Mixin(PowderSnowBlock.class)
public abstract class PowderSnowBlockMixin {
    @Inject(method = "canEntityWalkOnPowderSnow", at = @At("HEAD"), cancellable = true)
    private static void dswof$allowSnowWalking(Entity entity, CallbackInfoReturnable<Boolean> callback) {
        if (IceAdaptation.canMoveInPowderSnow(entity)) {
            callback.setReturnValue(true);
        }
    }

    @Inject(method = "getCollisionShape", at = @At("HEAD"), cancellable = true)
    private void dswof$supportSnowWalker(BlockState state, BlockGetter level, BlockPos pos,
                                        CollisionContext context, CallbackInfoReturnable<VoxelShape> callback) {
        if (context instanceof EntityCollisionContext entityContext
                && IceAdaptation.canMoveInPowderSnow(entityContext.getEntity())) {
            // Support the surface, including after a long fall. Crouching permits deliberate descent.
            callback.setReturnValue(!context.isDescending() && context.isAbove(Shapes.block(), pos, false)
                    ? Shapes.block() : Shapes.empty());
        }
    }

    @Redirect(method = "entityInside", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/Entity;makeStuckInBlock(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/phys/Vec3;)V"))
    private void dswof$skipSnowDrag(Entity entity, BlockState state, Vec3 multiplier) {
        if (!IceAdaptation.canMoveInPowderSnow(entity)) {
            entity.makeStuckInBlock(state, multiplier);
        }
    }
}
