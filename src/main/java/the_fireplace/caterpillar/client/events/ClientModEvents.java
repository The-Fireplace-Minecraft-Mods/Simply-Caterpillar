package the_fireplace.caterpillar.client.events;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.client.screen.*;
import the_fireplace.caterpillar.config.ConfigHelper;
import the_fireplace.caterpillar.config.ConfigHolder;
import the_fireplace.caterpillar.core.init.ContainerInit;

@Mod.EventBusSubscriber(modid = Caterpillar.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value =  Dist.CLIENT)
public class ClientModEvents {

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        MenuScreens.register(ContainerInit.CATERPILLAR.get(), CaterpillarScreen::new);
    }

    /**
     * This method will be called by Forge when a config changes.
     */
    @SubscribeEvent
    public static void onModConfigEvent(final ModConfigEvent event) {
        final ModConfig config = event.getConfig();
        // Rebake the configs when they change
        if (config.getSpec() == ConfigHolder.CLIENT_SPEC) {
            ConfigHelper.bakeClient(config);
            Caterpillar.LOGGER.debug("Baked client config");
        } else if (config.getSpec() == ConfigHolder.SERVER_SPEC) {
            ConfigHelper.bakeServer(config);
            Caterpillar.LOGGER.debug("Baked server config");
        }
    }
}