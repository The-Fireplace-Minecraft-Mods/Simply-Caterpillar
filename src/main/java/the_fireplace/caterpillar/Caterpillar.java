package the_fireplace.caterpillar;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import the_fireplace.caterpillar.config.ConfigHolder;
import the_fireplace.caterpillar.init.BlockInit;
import the_fireplace.caterpillar.init.ContainerInit;
import the_fireplace.caterpillar.init.ItemInit;
import the_fireplace.caterpillar.init.BlockEntityInit;

@Mod(Caterpillar.MOD_ID)
public class Caterpillar
{
	public static final String MOD_ID = "simplycaterpillar";

	public static final CreativeModeTab CATERPILLAR_CREATIVE_MODE_TAB = new CreativeModeTab(Caterpillar.MOD_ID) {
		@Override
		public ItemStack makeIcon() {
			return ItemInit.DRILL_BASE_ITEM.get().getDefaultInstance();
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
		BlockEntityInit.BLOCK_ENTITIES.register(bus);
		ItemInit.ITEMS.register(bus);
		ContainerInit.CONTAINER_TYPES.register(bus);

		MinecraftForge.EVENT_BUS.register(this);
	}
}
