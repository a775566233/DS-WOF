package org.wgx.dswof.mixin;

import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.wgx.dswof.dragonsurvival.IceWingFreezingImmunity;

@Mixin(Entity.class)
public abstract class EntityMixin {
    // 同时拦住直接 setTicksFrozen 的技能，并在原版下一次更新时清除已有冻结值。
    @ModifyVariable(method = "setTicksFrozen", at = @At("HEAD"), argsOnly = true)
    private int dswof$clearIceWingFrozenTicks(int ticksFrozen) {
        return IceWingFreezingImmunity.isImmune((Entity) (Object) this) ? 0 : ticksFrozen;
    }
}
