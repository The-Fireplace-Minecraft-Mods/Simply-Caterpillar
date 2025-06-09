package dev.the_fireplace.caterpillar.registry.client;

import dev.the_fireplace.caterpillar.platform.fabric.ClientPlatformImpl;
import dev.the_fireplace.caterpillar.registry.BlocksRegistry;
import net.minecraft.client.renderer.RenderType;

public class BlockRenderLayers {
    public static void init() {
        ClientPlatformImpl.registerRenderLayer(BlocksRegistry.DRILL_HEAD, RenderType.cutout());
        ClientPlatformImpl.registerRenderLayer(BlocksRegistry.DRILL_SEAT, RenderType.cutout());
        ClientPlatformImpl.registerRenderLayer(BlocksRegistry.DECORATION, RenderType.cutout());
        ClientPlatformImpl.registerRenderLayer(BlocksRegistry.INCINERATOR, RenderType.cutout());
        ClientPlatformImpl.registerRenderLayer(BlocksRegistry.TRANSPORTER, RenderType.cutout());
    }
}
