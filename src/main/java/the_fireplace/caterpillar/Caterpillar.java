package the_fireplace.caterpillar;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import the_fireplace.caterpillar.blocks.DrillHeads;
import the_fireplace.caterpillar.config.Config;
import the_fireplace.caterpillar.containers.CaterpillarData;
import the_fireplace.caterpillar.guis.DrillHeadScreen;
import the_fireplace.caterpillar.inits.ModBlocks;
import the_fireplace.caterpillar.inits.ModContainerTypes;
import the_fireplace.caterpillar.inits.ModItems;
import the_fireplace.caterpillar.inits.ModTileEntityTypes;
import the_fireplace.caterpillar.proxy.ClientProxy;
import the_fireplace.caterpillar.proxy.CommonProxy;

import java.util.HashMap;

@Mod("simplycaterpillar")
public class Caterpillar
{
	public static final Logger LOGGER = LogManager.getLogger();
	public static final String MOD_ID = "simplycaterpillar";
	public static Caterpillar instance;

	public int saveCount = 0;
	boolean dev = true;

	private HashMap<String, CaterpillarData> mainContainers;
	private CaterpillarData selectedCaterpillar;

	public static CommonProxy proxy = DistExecutor.runForDist(()-> ClientProxy::new, ()->CommonProxy::new);

	public Caterpillar() {
		instance = this;

		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.client_config);

		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

		ModBlocks.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
		ModItems.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
		ModTileEntityTypes.TILE_ENTITY_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
		ModContainerTypes.CONTAINER_TYPES.register((FMLJavaModLoadingContext.get().getModEventBus()));

		Config.loadConfig(Config.client_config, FMLPaths.CONFIGDIR.get().resolve("simplycaterpillar-client.toml").toString());

		MinecraftForge.EVENT_BUS.register(this);
	}

	private void setup(final FMLCommonSetupEvent event) { }

	private void doClientStuff(final FMLClientSetupEvent event) { }

	@SubscribeEvent
	public void onServerStarting(final FMLServerStartingEvent event) { }

	public String getCaterpillarID(int[] movingXZ, BlockPos pos) {
		int firstID = movingXZ[1] * pos.getX() + movingXZ[0] * pos.getZ();
		int secondID = pos.getY();
		int third = 0;
		if (movingXZ[0] != 0) {
			third = movingXZ[0] + 2;
		}
		if (movingXZ[1] != 0) {
			third = movingXZ[1] + 3;
		}
		return firstID + "," + secondID + "," + third;
	}

	public int[] getWayMoving(BlockState state) {
		try {
			int[] movingXZ = {0, 0};
			switch (state.get(DrillHeads.FACING)) {
				case NORTH:
					movingXZ[1] = 1;
				case EAST:
					movingXZ[0] = -1;
				case SOUTH:
					movingXZ[1] = -1;
				case WEST:
					movingXZ[0] = 1;
				default:
					movingXZ[1] = 1;
			}
			return movingXZ;
		} catch (Exception e) {
			return new int[]{-2, -2};
		}
	}

	public CaterpillarData getSelectedCaterpillar() {
		return this.selectedCaterpillar;
	}

	public void setSelectedCaterpillar(CaterpillarData selectCaterpillar) {
		this.selectedCaterpillar = selectCaterpillar;
	}

	public void removeSelectedCaterpillar() {
		this.selectedCaterpillar = null;
	}

	public void removeCaterpillar(String CaterpillarID) {
		Caterpillar.instance.mainContainers.remove(CaterpillarID);
		this.removeSelectedCaterpillar();
	}

	public boolean doesHaveCaterpillar(String CaterpillarID) {
		try {
			return this.mainContainers.containsKey(CaterpillarID);
		} catch (Exception e) {
			return false;
		}
	}

	public boolean doesHaveCaterpillar(BlockPos pos, BlockState state) {
		int[] movingXZ = this.getWayMoving(state);
		if (movingXZ[0] == -2 || movingXZ[1] == -2) {
			LOGGER.debug("Null: facing");
			return false;
		}
		String CatID = this.getCaterpillarID(movingXZ, pos);
		return this.doesHaveCaterpillar(CatID);
	}

	public void putContainerCaterpillar(CaterpillarData conCat, World objWorld) {
		BlockState thisState = objWorld.getBlockState(conCat.pos);
		int[] movingXZ = this.getWayMoving(thisState);
		if (movingXZ[0] == -2 || movingXZ[1] == -2) {
			LOGGER.debug("Null: facing");
		}
		String CatID = this.getCaterpillarID(movingXZ, conCat.pos);
		this.putContainerCaterpillar(CatID, conCat);
	}

	public void putContainerCaterpillar(String CaterpillardID, CaterpillarData conCat) {
		this.mainContainers.put(CaterpillardID, conCat);
	}

	public CaterpillarData getContainerCaterpillar(String caterpillardID) {
		return this.mainContainers.get(caterpillardID);
	}

	public CaterpillarData getContainerCaterpillar(BlockPos pos, World objWorld)
	{
		BlockState thisState =  objWorld.getBlockState(pos);
		int[] movingXZ = this.getWayMoving(thisState);
		if (movingXZ[0] == -2 || movingXZ[1] == -2)
		{
			LOGGER.debug("Null: facing");
			return null;
		}
		String catID = this.getCaterpillarID(movingXZ, pos);
		return this.getContainerCaterpillar(catID);
	}

	public CaterpillarData getContainerCaterpillar(BlockPos pos, BlockState thisState)
	{
		int[] movingXZ = this.getWayMoving(thisState);
		if (movingXZ[0] == -2 || movingXZ[1] == -2)
		{
			LOGGER.debug("Null: facing");
			return null;
		}
		String catID = this.getCaterpillarID(movingXZ, pos);
		return this.getContainerCaterpillar(catID);
	}

    public NonNullList<ItemStack> getInventory(CaterpillarData caterpillar, DrillHeadScreen.GUI_TABS selected) {
		if (caterpillar != null) {
			switch (selected.value) {
				case 0:
					return caterpillar.inventory;
				case 1:
					return caterpillar.decoration.getSelectedInventory();
				case 2:
					return caterpillar.reinforcement.reinforcementMap;
				case 3:
					return caterpillar.incinerator.placementMap;
				default:
					break;
			}
		}
		return NonNullList.withSize(256, ItemStack.EMPTY);
    }
}
