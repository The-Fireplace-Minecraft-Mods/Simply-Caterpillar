package the_fireplace.caterpillar;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import the_fireplace.caterpillar.common.block.DrillHeadBlock;
import the_fireplace.caterpillar.common.block.entity.DrillHeadBlockEntity;
import the_fireplace.caterpillar.common.block.entity.util.CaterpillarBlocksUtil;
import the_fireplace.caterpillar.common.block.util.DrillHeadPart;
import the_fireplace.caterpillar.common.container.CaterpillarContainer;
import the_fireplace.caterpillar.config.ConfigHolder;
import the_fireplace.caterpillar.core.init.BlockInit;
import the_fireplace.caterpillar.core.init.ContainerInit;
import the_fireplace.caterpillar.core.init.ItemInit;
import the_fireplace.caterpillar.core.init.BlockEntityInit;
import the_fireplace.caterpillar.client.screen.util.ScreenTabs;

import java.util.HashMap;

import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;
import static the_fireplace.caterpillar.common.block.DrillHeadBlock.PART;

@Mod(Caterpillar.MOD_ID)
@Mod.EventBusSubscriber(modid = Caterpillar.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Caterpillar
{
	public static final String MOD_ID = "simplycaterpillar";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	public static final CreativeModeTab CATERPILLAR_CREATIVE_MODE_TAB = new CreativeModeTab(Caterpillar.MOD_ID) {
		@Override
		public ItemStack makeIcon() {
			return ItemInit.ITEMS.getEntries().stream().findFirst().get().get().getDefaultInstance();
		}
	};

	private HashMap<BlockPos, CaterpillarContainer> caterpillarContainers;
	private CaterpillarContainer selectedCaterpillar;

	public static Caterpillar instance;

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
		ContainerInit.CONTAINERS.register(bus);

		MinecraftForge.EVENT_BUS.register(this);
	}

	public void removeCaterpillar(BlockPos pos) {
		Caterpillar.instance.caterpillarContainers.remove(pos);
		this.removeSelectedCaterpillar();
	}

	public boolean doesCaterpillarExist(BlockPos pos) {
		return this.caterpillarContainers.containsKey(pos);
	}

	public void addCaterpillar(BlockPos pos, CaterpillarContainer data) {
		this.caterpillarContainers.put(pos, data);
	}

	public CaterpillarContainer getContainerCaterpillar(BlockPos pos) {
		return this.caterpillarContainers.get(pos);
	}

	public CaterpillarContainer getSelectedCaterpillar() {
		return this.selectedCaterpillar;
	}

	public void setSelectedCaterpillar(CaterpillarContainer selectedCaterpillar) {
		this.selectedCaterpillar = selectedCaterpillar;
	}

	public void removeSelectedCaterpillar() {
		this.selectedCaterpillar = null;
	}

	public static VoxelShape calculateShapes(Direction to, VoxelShape shape) {
		final VoxelShape[] buffer = { shape, Shapes.empty() };

		final int times = (to.get2DDataValue() - Direction.NORTH.get2DDataValue() + 4) % 4;
		for (int i = 0; i < times; i++) {
			buffer[0].forAllBoxes((minX, minY, minZ, maxX, maxY,
								   maxZ) -> buffer[1] = Shapes.or(buffer[1], Shapes.create(1 - maxZ, minY, minX, 1 - minZ, maxY, maxX)));
			buffer[0] = buffer[1];
			buffer[1] = Shapes.empty();
		}

		return buffer[0];
	}
}
