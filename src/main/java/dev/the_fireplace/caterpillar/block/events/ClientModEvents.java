package dev.the_fireplace.caterpillar.block.events;

import dev.the_fireplace.caterpillar.Caterpillar;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import dev.the_fireplace.caterpillar.screen.DecorationScreen;
import dev.the_fireplace.caterpillar.screen.DrillHeadScreen;
import dev.the_fireplace.caterpillar.screen.IncineratorScreen;
import dev.the_fireplace.caterpillar.screen.ReinforcementScreen;
import dev.the_fireplace.caterpillar.config.ConfigHelper;
import dev.the_fireplace.caterpillar.config.ConfigHolder;
import dev.the_fireplace.caterpillar.init.MenuInit;

@Mod.EventBusSubscriber(modid = Caterpillar.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value =  Dist.CLIENT)
public class ClientModEvents {

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        MenuScreens.register(MenuInit.DECORATION.get(), DecorationScreen::new);
        MenuScreens.register(MenuInit.DRILL_HEAD.get(), DrillHeadScreen::new);
        MenuScreens.register(MenuInit.INCINERATOR.get(), IncineratorScreen::new);
        MenuScreens.register(MenuInit.REINFORCEMENT.get(), ReinforcementScreen::new);
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