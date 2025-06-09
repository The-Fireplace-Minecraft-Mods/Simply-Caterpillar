package dev.the_fireplace.caterpillar.client;

import dev.architectury.platform.Platform;
import dev.the_fireplace.caterpillar.registry.client.MenuScreensRegistry;

public class CaterpillarClient {
    public static void init() {
        if (Platform.isFabric()) {
            MenuScreensRegistry.register();
        }
    }
}
