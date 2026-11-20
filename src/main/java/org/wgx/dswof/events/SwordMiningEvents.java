package org.wgx.dswof.events;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import org.wgx.dswof.DsWof;

@EventBusSubscriber(modid = DsWof.MODID)
public final class SwordMiningEvents {
    private SwordMiningEvents() {}

    @SubscribeEvent
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        Player player = event.getEntity();
        float base = swordDestroySpeed(event.getState());
        event.setNewSpeed(applyVanillaModifiers(player, base));
    }

    /** 与原版剑 tool 规则一致 */
    public static float swordDestroySpeed(BlockState state) {
        if (state.is(Blocks.COBWEB)) {
            return 15.0F;
        }
        return state.is(BlockTags.SWORD_EFFICIENT) ? 1.5F : 1.0F;
    }

    /**
     * 复刻 1.21.1 Player.getDestroySpeed 里基础速度之后的那几步。
     * 基础速度 ≤ 1 时不会加上 mining_efficiency，因此效率附魔、镐的额外效率都不会生效，和剑一致。
     */
    public static float applyVanillaModifiers(Player player, float speed) {
        if (speed > 1.0F) {
            speed += (float) player.getAttributeValue(Attributes.MINING_EFFICIENCY);
        }
        if (MobEffectUtil.hasDigSpeed(player)) {
            speed *= 1.0F + (MobEffectUtil.getDigSpeedAmplification(player) + 1) * 0.2F;
        }
        MobEffectInstance fatigue = player.getEffect(MobEffects.DIG_SLOWDOWN);
        if (fatigue != null) {
            float mul = switch (fatigue.getAmplifier()) {
                case 0 -> 0.3F;
                case 1 -> 0.09F;
                case 2 -> 0.0027F;
                default -> 8.1E-4F;
            };
            speed *= mul;
        }
        speed *= (float) player.getAttributeValue(Attributes.BLOCK_BREAK_SPEED);
        if (player.isEyeInFluid(FluidTags.WATER)) {
            speed *= (float) player.getAttributeValue(Attributes.SUBMERGED_MINING_SPEED);
        }
        if (!player.onGround()) {
            speed /= 5.0F;
        }
        return speed;
    }
}
