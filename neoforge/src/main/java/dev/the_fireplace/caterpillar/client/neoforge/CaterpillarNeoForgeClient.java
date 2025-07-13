package dev.the_fireplace.caterpillar.client.neoforge;

import dev.the_fireplace.caterpillar.Constants;
import dev.the_fireplace.caterpillar.client.CaterpillarClient;
import dev.the_fireplace.caterpillar.client.screen.DrillHeadScreen;
import dev.the_fireplace.caterpillar.client.screen.IncineratorScreen;
import dev.the_fireplace.caterpillar.client.screen.TransporterScreen;
import dev.the_fireplace.caterpillar.registry.EntitiesRegistry;
import dev.the_fireplace.caterpillar.registry.MenuTypesRegistry;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(modid = Constants.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class CaterpillarNeoForgeClient {

    @SubscribeEvent
    public static void init(FMLClientSetupEvent event) {
        CaterpillarClient.init();
    }

    @SubscribeEvent
    public static void onRegisterMenuScreens(RegisterMenuScreensEvent event) {
        event.register(MenuTypesRegistry.DRILL_HEAD.get(), DrillHeadScreen::new);
        event.register(MenuTypesRegistry.INCINERATOR.get(), IncineratorScreen::new);
        event.register(MenuTypesRegistry.TRANSPORTER.get(), TransporterScreen::new);
    }

    @SubscribeEvent
    public static void onRegisterEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntitiesRegistry.SEAT.get(), NoopRenderer::new);
    }
}
