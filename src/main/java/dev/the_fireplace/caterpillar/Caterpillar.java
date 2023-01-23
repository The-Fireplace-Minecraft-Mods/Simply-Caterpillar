package dev.the_fireplace.caterpillar;

import com.mojang.logging.LogUtils;
import dev.the_fireplace.caterpillar.config.ConfigHolder;
import dev.the_fireplace.caterpillar.init.*;
import dev.the_fireplace.caterpillar.network.PacketHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(dev.the_fireplace.caterpillar.Caterpillar.MOD_ID)
@Mod.EventBusSubscriber(modid = dev.the_fireplace.caterpillar.Caterpillar.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Caterpillar {
    public static final String MOD_ID = "simplycaterpillar";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Caterpillar() {
        final ModLoadingContext modLoadingContext = ModLoadingContext.get();
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register Configs
        // modLoadingContext.registerConfig(ModConfig.Type.CLIENT, ConfigHolder.CLIENT_SPEC);
        modLoadingContext.registerConfig(ModConfig.Type.SERVER, ConfigHolder.SERVER_SPEC);

        // Register Deferred Registers
        BlockInit.BLOCKS.register(bus);
        BlockEntityInit.BLOCK_ENTITY_TYPES.register(bus);
        ItemInit.ITEMS.register(bus);
        MenuInit.MENU_TYPES.register(bus);
        EntityInit.ENTITY_TYPES.register(bus);

        bus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(PacketHandler::register);
    }
}
