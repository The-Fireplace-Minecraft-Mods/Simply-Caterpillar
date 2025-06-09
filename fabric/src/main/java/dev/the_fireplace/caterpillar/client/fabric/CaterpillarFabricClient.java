package dev.the_fireplace.caterpillar.client.fabric;

import dev.the_fireplace.caterpillar.client.CaterpillarClient;
import dev.the_fireplace.caterpillar.registry.client.BlockRenderLayers;
import net.fabricmc.api.ClientModInitializer;

public final class CaterpillarFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CaterpillarClient.init();

        BlockRenderLayers.init();
    }
}
