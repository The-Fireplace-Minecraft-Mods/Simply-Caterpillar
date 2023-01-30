package dev.the_fireplace.caterpillar;

import dev.the_fireplace.caterpillar.client.screen.DrillHeadScreen;
import dev.the_fireplace.caterpillar.init.BlockInit;
import dev.the_fireplace.caterpillar.init.MenuInit;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;

public class CaterpillarClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(BlockInit.DRILL_HEAD, RenderLayer.getCutout());

        HandledScreens.register(MenuInit.DRILL_HEAD, DrillHeadScreen::new);
    }
}
