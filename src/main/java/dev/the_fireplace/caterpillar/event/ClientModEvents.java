package dev.the_fireplace.caterpillar.event;

import dev.the_fireplace.caterpillar.Caterpillar;
import dev.the_fireplace.caterpillar.client.KeyBinding;
import dev.the_fireplace.caterpillar.client.renderer.entity.SeatEntityRenderer;
import dev.the_fireplace.caterpillar.client.screen.*;
import dev.the_fireplace.caterpillar.config.ConfigHelper;
import dev.the_fireplace.caterpillar.config.ConfigHolder;
import dev.the_fireplace.caterpillar.init.BlockInit;
import dev.the_fireplace.caterpillar.init.EntityInit;
import dev.the_fireplace.caterpillar.init.MenuInit;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Caterpillar.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        MenuScreens.register(MenuInit.DECORATION.get(), DecorationScreen::new);
        MenuScreens.register(MenuInit.DRILL_HEAD.get(), DrillHeadScreen::new);
        MenuScreens.register(MenuInit.INCINERATOR.get(), IncineratorScreen::new);
        MenuScreens.register(MenuInit.REINFORCEMENT.get(), ReinforcementScreen::new);
        MenuScreens.register(MenuInit.TRANSPORTER.get(), TransporterScreen::new);
    }

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        EntityRenderers.register(EntityInit.SEAT.get(), SeatEntityRenderer::new);
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
    public static void registerTabs(CreativeModeTabEvent.Register event) {
        event.registerCreativeModeTab(new ResourceLocation(Caterpillar.MOD_ID, "caterpillar"), builder -> builder
                .title(Component.translatable("itemGroup." + Caterpillar.MOD_ID))
                .icon(() -> new ItemStack(BlockInit.DRILL_HEAD.get()))
                .displayItems((featureFlags, output, hasOp) -> {
                    output.accept(BlockInit.DRILL_BASE.get());
                    output.accept(BlockInit.DRILL_HEAD.get());
                    output.accept(BlockInit.DRILL_SEAT.get());
                    output.accept(BlockInit.COLLECTOR.get());
                    output.accept(BlockInit.REINFORCEMENT.get());
                    output.accept(BlockInit.INCINERATOR.get());
                    output.accept(BlockInit.STORAGE.get());
                    output.accept(BlockInit.DECORATION.get());
                    output.accept(BlockInit.TRANSPORTER.get());
                })
        );
    }
}