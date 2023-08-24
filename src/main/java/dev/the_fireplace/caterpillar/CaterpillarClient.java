package dev.the_fireplace.caterpillar;

import dev.the_fireplace.caterpillar.client.handler.LecternEventHandler;
import dev.the_fireplace.caterpillar.client.renderer.entity.SeatEntityRenderer;
import dev.the_fireplace.caterpillar.client.screen.*;
import dev.the_fireplace.caterpillar.event.KeyInputHandler;
import dev.the_fireplace.caterpillar.init.BlockInit;
import dev.the_fireplace.caterpillar.init.EntityInit;
import dev.the_fireplace.caterpillar.init.MenuInit;
import dev.the_fireplace.caterpillar.network.PacketHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.RenderType;

public class CaterpillarClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(BlockInit.DRILL_HEAD, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockInit.TRANSPORTER, RenderType.cutout());

        MenuScreens.register(MenuInit.DRILL_HEAD, DrillHeadScreen::new);
        MenuScreens.register(MenuInit.DECORATION, DecorationScreen::new);
        MenuScreens.register(MenuInit.INCINERATOR, IncineratorScreen::new);
        MenuScreens.register(MenuInit.REINFORCEMENT, ReinforcementScreen::new);
        MenuScreens.register(MenuInit.TRANSPORTER, TransporterScreen::new);

        KeyInputHandler.register();
        PacketHandler.registerS2CPackets();

        EntityRendererRegistry.register(EntityInit.SEAT, SeatEntityRenderer::new);

        UseBlockCallback.EVENT.register(LecternEventHandler::rightClick);
    }
}
