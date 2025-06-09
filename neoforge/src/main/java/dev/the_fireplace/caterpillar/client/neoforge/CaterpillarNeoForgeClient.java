package dev.the_fireplace.caterpillar.client.neoforge;

import dev.the_fireplace.caterpillar.Constants;
import dev.the_fireplace.caterpillar.client.CaterpillarClient;
import dev.the_fireplace.caterpillar.client.screen.IncineratorScreen;
import dev.the_fireplace.caterpillar.registry.MenuTypesRegistry;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(modid = Constants.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class CaterpillarNeoForgeClient {

    @SubscribeEvent
    public static void init(FMLClientSetupEvent event) {
        CaterpillarClient.init();
    }

    @SubscribeEvent
    public static void onRegisterMenuScreens(RegisterMenuScreensEvent event) {
        event.register(MenuTypesRegistry.INCINERATOR.get(), IncineratorScreen::new);
    }
}
