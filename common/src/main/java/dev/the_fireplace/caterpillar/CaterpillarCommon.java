package dev.the_fireplace.caterpillar;

import dev.the_fireplace.caterpillar.network.NetworkRegistry;
import dev.the_fireplace.caterpillar.registry.*;

public final class CaterpillarCommon {
    public static void init() {
        NetworkRegistry.init();

        BlocksRegistry.init();
        ItemsRegistry.init();
        CreativeModeTabsRegistry.init();
        BlockEntityTypesRegistry.init();
        MenuTypesRegistry.init();
    }
}
