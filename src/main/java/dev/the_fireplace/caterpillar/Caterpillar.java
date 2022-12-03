package dev.the_fireplace.caterpillar;

import com.mojang.logging.LogUtils;
import dev.the_fireplace.caterpillar.config.ConfigHolder;
import dev.the_fireplace.caterpillar.network.PacketHandler;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import dev.the_fireplace.caterpillar.init.BlockEntityInit;
import dev.the_fireplace.caterpillar.init.BlockInit;
import dev.the_fireplace.caterpillar.init.ItemInit;
import dev.the_fireplace.caterpillar.init.MenuInit;

@Mod(dev.the_fireplace.caterpillar.Caterpillar.MOD_ID)
@Mod.EventBusSubscriber(modid = dev.the_fireplace.caterpillar.Caterpillar.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Caterpillar {
    public static final String MOD_ID = "simplycaterpillar";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final CreativeModeTab CATERPILLAR_CREATIVE_MODE_TAB = new CreativeModeTab(dev.the_fireplace.caterpillar.Caterpillar.MOD_ID) {
        @Override
        public @NotNull ItemStack makeIcon() {
            return BlockInit.DRILL_HEAD.get().asItem().getDefaultInstance();
        }
    };

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

        bus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(PacketHandler::register);
    }
}
