package the_fireplace.caterpillar.client.events;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.client.screen.DecorationScreen;
import the_fireplace.caterpillar.client.screen.DrillHeadScreen;
import the_fireplace.caterpillar.client.screen.IncineratorScreen;
import the_fireplace.caterpillar.client.screen.ReinforcementScreen;
import the_fireplace.caterpillar.config.ConfigHelper;
import the_fireplace.caterpillar.config.ConfigHolder;
import the_fireplace.caterpillar.core.init.MenuInit;

@Mod.EventBusSubscriber(modid = Caterpillar.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value =  Dist.CLIENT)
public class ClientModEvents {

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        // MenuScreens.register(MenuInit.DECORATION.get(), DecorationScreen::new);
        MenuScreens.register(MenuInit.DRILL_HEAD.get(), DrillHeadScreen::new);
        MenuScreens.register(MenuInit.INCINERATOR.get(), IncineratorScreen::new);
        // MenuScreens.register(MenuInit.REINFORCEMENT.get(), ReinforcementScreen::new);
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