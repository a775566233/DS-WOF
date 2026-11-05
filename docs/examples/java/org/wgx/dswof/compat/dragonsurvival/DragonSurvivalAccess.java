package org.wgx.dswof.compat.dragonsurvival;

import by.dragonsurvivalteam.dragonsurvival.common.capability.DragonStateProvider;
import by.dragonsurvivalteam.dragonsurvival.registry.dragon.body.DragonBody;
import by.dragonsurvivalteam.dragonsurvival.server.handlers.PlayerLoginHandler;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;

/**
 * Example adapter for Dragon Survival 2.0.71 on Minecraft 1.21.1.
 * Mutations require the server thread and a request already authorized by the caller.
 */
public final class DragonSurvivalAccess {
    private DragonSurvivalAccess() {}

    public record Snapshot(ResourceLocation species, ResourceLocation body,
                           ResourceLocation stage, double growth) {}

    public static Optional<Snapshot> snapshot(Player player) {
        var state = DragonStateProvider.getData(player);
        if (!state.isDragon()) {
            return Optional.empty();
        }
        return Optional.of(new Snapshot(state.speciesId(), state.bodyId(),
                state.stageId(), state.getGrowth()));
    }

    /** Checks registry/species compatibility; the caller must also check unlock rules. */
    public static boolean changeAuthorizedBody(ServerPlayer player, ResourceLocation id) {
        var state = DragonStateProvider.getData(player);
        if (!state.isDragon()) {
            return false;
        }

        var key = ResourceKey.create(DragonBody.REGISTRY, id);
        var body = player.registryAccess().registryOrThrow(DragonBody.REGISTRY).getHolder(key);
        if (body.isEmpty() || !DragonBody.bodyIsValidForSpecies(body.get(), state.species())) {
            return false;
        }

        state.setBody(player, body.get());
        PlayerLoginHandler.syncComplete(player);
        return true;
    }

    /** Requests gradual growth. DS still applies its growth rules during interpolation. */
    public static boolean requestAuthorizedGrowth(ServerPlayer player, double growth) {
        var state = DragonStateProvider.getData(player);
        if (!state.isDragon() || !Double.isFinite(growth) || growth <= 0) {
            return false;
        }
        state.setDesiredGrowth(player, growth);
        return true;
    }

    /** Intended for an authorized administrative operation, not ordinary natural growth. */
    public static boolean setAuthorizedGrowthImmediately(ServerPlayer player, double growth) {
        if (!requestAuthorizedGrowth(player, growth)) {
            return false;
        }
        var state = DragonStateProvider.getData(player);
        state.setGrowth(player, state.getDesiredGrowth(), true);
        return true;
    }

    public static boolean revertAuthorizedPlayer(ServerPlayer player) {
        var state = DragonStateProvider.getData(player);
        if (!state.isDragon()) {
            return false;
        }
        state.revertToHumanForm(player, false);
        PlayerLoginHandler.syncHandler(player);
        return true;
    }
}
