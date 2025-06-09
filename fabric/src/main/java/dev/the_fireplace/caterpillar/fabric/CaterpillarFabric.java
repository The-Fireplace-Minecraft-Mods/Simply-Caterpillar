package dev.the_fireplace.caterpillar.fabric;

import dev.the_fireplace.caterpillar.CaterpillarCommon;
import net.fabricmc.api.ModInitializer;

public final class CaterpillarFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        CaterpillarCommon.init();
    }
}
