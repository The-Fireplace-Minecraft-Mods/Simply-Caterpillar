package dev.the_fireplace.caterpillar;

import dev.the_fireplace.caterpillar.registry.BlocksRegistry;
import dev.the_fireplace.caterpillar.registry.CreativeModeTabsRegistry;
import dev.the_fireplace.caterpillar.registry.ItemsRegistry;

public final class Caterpillar {
    public static void init() {
        CreativeModeTabsRegistry.init();
        BlocksRegistry.init();
        ItemsRegistry.init();
    }
}
