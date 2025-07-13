package dev.the_fireplace.caterpillar.client;

import dev.the_fireplace.caterpillar.registry.client.EntityRenderersRegistry;
import dev.the_fireplace.caterpillar.registry.client.MenuScreensRegistry;

public class CaterpillarClient {
    public static void init() {
        // TODO: At the moment, only works for Fabric
        EntityRenderersRegistry.register();
        MenuScreensRegistry.register();
    }
}
