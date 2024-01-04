package dev.the_fireplace.caterpillar.event;

import dev.the_fireplace.caterpillar.Caterpillar;
import dev.the_fireplace.caterpillar.client.KeyBinding;
import dev.the_fireplace.caterpillar.client.handler.LecternEventHandler;
import dev.the_fireplace.caterpillar.client.renderer.entity.SeatEntityRenderer;
import dev.the_fireplace.caterpillar.client.screen.*;
import dev.the_fireplace.caterpillar.config.ConfigHelper;
import dev.the_fireplace.caterpillar.config.ConfigHolder;
import dev.the_fireplace.caterpillar.network.packet.server.OpenBookGuiS2CPacket;
import dev.the_fireplace.caterpillar.registry.EntityRegistry;
import dev.the_fireplace.caterpillar.registry.MenuRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = Caterpillar.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        MenuScreens.register(MenuRegistry.DECORATION.get(), DecorationScreen::new);
        MenuScreens.register(MenuRegistry.DRILL_HEAD.get(), DrillHeadScreen::new);
        MenuScreens.register(MenuRegistry.INCINERATOR.get(), IncineratorScreen::new);
        MenuScreens.register(MenuRegistry.REINFORCEMENT.get(), ReinforcementScreen::new);
        MenuScreens.register(MenuRegistry.TRANSPORTER.get(), TransporterScreen::new);
    }

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        EntityRenderers.register(EntityRegistry.SEAT.get(), SeatEntityRenderer::new);
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

    @SubscribeEvent
    public static void onKeyRegister(RegisterKeyMappingsEvent event) {
        event.register(KeyBinding.TOGGLE_TUTORIAL_KEY);
    }

    @SubscribeEvent
    public static void onInitialize(FMLCommonSetupEvent event) {
        /**
         * TODO: Re-enable when fixed
         *
         *  MinecraftForge.EVENT_BUS.addListener((PlayerInteractEvent.RightClickBlock e) -> {
         *             var result = LecternEventHandler.rightClick(e.getEntity(), e.getLevel(), e.getHand(), e.getHitVec());
         *             if (result.consumesAction()) {
         *                 e.setCanceled(true);
         *                 e.setCancellationResult(result);
         *             }
         *         });
         */
    }
}