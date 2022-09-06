package the_fireplace.caterpillar;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import the_fireplace.caterpillar.config.ConfigHolder;
import the_fireplace.caterpillar.core.init.BlockInit;
import the_fireplace.caterpillar.core.init.MenuInit;
import the_fireplace.caterpillar.core.init.ItemInit;
import the_fireplace.caterpillar.core.init.BlockEntityInit;
import the_fireplace.caterpillar.core.network.PacketHandler;

@Mod(Caterpillar.MOD_ID)
@Mod.EventBusSubscriber(modid = Caterpillar.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Caterpillar
{
	public static final String MOD_ID = "simplycaterpillar";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	public static final CreativeModeTab CATERPILLAR_CREATIVE_MODE_TAB = new CreativeModeTab(Caterpillar.MOD_ID) {
		@Override
		public ItemStack makeIcon() {
			return BlockInit.DRILL_HEAD.get().asItem().getDefaultInstance();
		}
	};

	public Caterpillar() {
		final ModLoadingContext modLoadingContext = ModLoadingContext.get();
		final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

		// Register Configs
		modLoadingContext.registerConfig(ModConfig.Type.CLIENT, ConfigHolder.CLIENT_SPEC);
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
		event.enqueueWork(() -> {
			PacketHandler.register();
		});
	}
}
