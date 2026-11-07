package org.wgx.dswof.registry;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import org.wgx.dswof.DsWof;

public final class DsWofDataPacks {
    private DsWofDataPacks() {}

    public static void addPackFinders(final AddPackFindersEvent event) {
        event.addPackFinders(
                ResourceLocation.fromNamespaceAndPath(DsWof.MODID, "data/dswof/datapacks/unlock_wings"),
                PackType.SERVER_DATA,
                Component.literal("DS-WOF - Unlock Wings"),
                PackSource.DEFAULT,
                false,
                Pack.Position.TOP
        );
    }
}
