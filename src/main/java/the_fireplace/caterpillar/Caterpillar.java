package the_fireplace.caterpillar;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import the_fireplace.caterpillar.blocks.BlockDrillBase;
import the_fireplace.caterpillar.blocks.BlockDrillHeads;
import the_fireplace.caterpillar.containers.CaterpillarData;
import the_fireplace.caterpillar.network.GUIHandler;
import the_fireplace.caterpillar.tools.NBTTools;
import the_fireplace.caterpillar.inits.InitBlocks;
import the_fireplace.caterpillar.network.PacketDispatcher;
import the_fireplace.caterpillar.proxy.ProxyCommon;
import the_fireplace.caterpillar.tabs.TabCaterpillar;
import the_fireplace.caterpillar.tileentity.TileEntityDrillComponent;
import the_fireplace.caterpillar.timers.TimerMain;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map.Entry;

@Mod(name = Caterpillar.MODNAME, modid = Caterpillar.MODID, guiFactory=Reference.guiFactory, updateJSON = "http://thefireplace.bitnamiapp.com/jsons/simplycaterpillar.json", acceptedMinecraftVersions = "[1.9.4,1.10.2]")
public class Caterpillar
{
	public static final String MODID = "simplycaterpillar";
	public static final String MODNAME = "Simply Caterpillar";
	@Instance(Caterpillar.MODID)
	public static Caterpillar instance;

	public static final CreativeTabs tabCaterpillar = new TabCaterpillar();
	public int saveCount = 0;
	public TimerMain modTimer;
	boolean dev = false;

	private HashMap<String, CaterpillarData> mainContainers;
	private CaterpillarData selectedCaterpillar;

	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
	public static ProxyCommon proxy;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		dev = event.getModMetadata().version.equals("${version}");
		Config.init(event.getSuggestedConfigurationFile());

		Reference.MainNBT = new NBTTools(MODID);

		this.mainContainers = new HashMap<>();

		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GUIHandler());

		this.modTimer = new TimerMain();

		PacketDispatcher.registerPackets();

		InitBlocks.init();
		InitBlocks.register();
		proxy.registerRenders();
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		GameRegistry.registerTileEntity(TileEntityDrillComponent.class, "DrillHead");
		MinecraftForge.EVENT_BUS.register(new CommonEvents());
		this.recipes();
		Reference.cleanModsFolder();
		Config.load();
		Reference.ModTick.scheduleAtFixedRate(this.modTimer, 10, 10);
	}

	private void recipes() {
		this.addRecipe(InitBlocks.drillbase, "C C", "CRC", "CPC", 'C', "cobblestone", 'R', "dustRedstone", 'P', "ingotIron");
		this.addRecipe(InitBlocks.drillheads, "PPP", " D ", " F ", 'D', InitBlocks.drillbase,  'P', "ingotIron", 'F', Blocks.FURNACE);
		this.addRecipe(InitBlocks.reinforcements, " P ", "PDP", " P ", 'D', InitBlocks.drillbase, 'P', Blocks.PISTON);
		this.addRecipe(InitBlocks.decoration, "PDP", 'D', InitBlocks.drillbase, 'P', Blocks.DISPENSER);
		this.addRecipe(InitBlocks.collector, "D", "H", 'D', InitBlocks.drillbase, 'H', Blocks.HOPPER);
		this.addRecipe(InitBlocks.storage, "PDP", 'D', InitBlocks.drillbase, 'P', Blocks.CHEST);
		this.addRecipe(InitBlocks.incinerator, "F", "D", "P", 'D', InitBlocks.drillbase, 'F', Blocks.FURNACE, 'P', Items.LAVA_BUCKET);
	}

	private void addRecipe(Block block, Object... args){
		GameRegistry.addRecipe(new ShapedOreRecipe(block, args));
	}

	public String getCaterpillarID(int[] movingXZ, BlockPos pos)
	{
		int firstID = movingXZ[1] * pos.getX() + movingXZ[0] * pos.getZ();
		int secondID = pos.getY();
		int third = 0;
		if (movingXZ[0] != 0)
		{
			third = movingXZ[0] + 2;
		}
		if (movingXZ[1] != 0)
		{
			third = movingXZ[1] + 3;
		}
		return firstID + "," + secondID + "," + third;
	}

	public int[] getWayMoving(IBlockState state) {
		try {
			int[] movingXZ = {0, 0};
			if (state.getValue(BlockDrillHeads.FACING) == EnumFacing.EAST)
			{
				movingXZ[0] = -1;
			}
			if (state.getValue(BlockDrillHeads.FACING) == EnumFacing.WEST)
			{
				movingXZ[0] = 1;
			}
			if (state.getValue(BlockDrillHeads.FACING) == EnumFacing.NORTH)
			{
				movingXZ[1] = 1;
			}
			if (state.getValue(BlockDrillHeads.FACING) == EnumFacing.SOUTH)
			{
				movingXZ[1] = -1;
			}

			return movingXZ;
		} catch (Exception e) {
			return new int[]{-2, -2};
		}
	}

	public CaterpillarData getSelectedCaterpillar()
	{
		return this.selectedCaterpillar;
	}
	public void setSelectedCaterpillar(CaterpillarData selectedcat)
	{
		this.selectedCaterpillar = selectedcat;
	}
	public void removeSelectedCaterpillar()
	{
		this.selectedCaterpillar = null;
	}
	public void removeCaterpillar(String CaterpillarID)
	{
		Caterpillar.instance.mainContainers.remove(CaterpillarID);
		this.removeSelectedCaterpillar();
	}

	public boolean doesHaveCaterpillar(String CaterpillarID)
	{
		try {
			return this.mainContainers.containsKey(CaterpillarID);
		} catch (Exception e) {
			return false;
		}
	}

	public boolean doesHaveCaterpillar(BlockPos pos, IBlockState state)
	{
		int[] movingXZ = this.getWayMoving(state);
		if (movingXZ[0] == -2 || movingXZ[1] == -2)
		{
			Reference.printDebug("Null: facing");
			return false;
		}
		String CatID = this.getCaterpillarID(movingXZ, pos);
		return this.doesHaveCaterpillar(CatID);
	}

	public void putContainerCaterpillar(CaterpillarData conCat, World objworld) {
		IBlockState thisState =  objworld.getBlockState(conCat.pos);
		int[] movingXZ = this.getWayMoving(thisState);
		if (movingXZ[0] == -2 || movingXZ[1] == -2)
		{
			Reference.printDebug("Null: facing");
		}
		String CatID = this.getCaterpillarID(movingXZ, conCat.pos);
		this.putContainerCaterpillar(CatID, conCat);
	}

	public void putContainerCaterpillar(String CaterpillarID, CaterpillarData conCat) {
		this.mainContainers.put(CaterpillarID, conCat);
	}

	public CaterpillarData getContainerCaterpillar(String caterpillarID) {
		return this.mainContainers.get(caterpillarID);
	}

	public CaterpillarData getContainerCaterpillar(BlockPos pos, World objWorld)
	{
		IBlockState thisState =  objWorld.getBlockState(pos);
		int[] movingXZ = this.getWayMoving(thisState);
		if (movingXZ[0] == -2 || movingXZ[1] == -2)
		{
			Reference.printDebug("Null: facing");
			return null;
		}
		String catID = this.getCaterpillarID(movingXZ, pos);
		return this.getContainerCaterpillar(catID);
	}

	@Nullable
	public CaterpillarData getContainerCaterpillar(BlockPos pos, IBlockState thisState)
	{
		int[] movingXZ = this.getWayMoving(thisState);
		if (movingXZ[0] == -2 || movingXZ[1] == -2)
		{
			Reference.printDebug("Null: facing");
			return null;
		}
		String catID =this.getCaterpillarID(movingXZ, pos);
		return this.getContainerCaterpillar(catID);
	}

	public void saveNBTDrills()
	{
		if (Reference.loaded)
		{
			NBTTagCompound tmpNBT = new NBTTagCompound();
			int i = 0;
			for (Entry<String, CaterpillarData> key : this.mainContainers.entrySet()) {
				CaterpillarData conCat = key.getValue();
				tmpNBT.setTag("caterpillar" + i++, conCat.writeNBTCaterpillar());
			}
			tmpNBT.setInteger("count", i);
			Reference.MainNBT.saveNBTSettings(tmpNBT, Reference.MainNBT.getFolderLocationWorld(), "DrillHeads.dat");
		}
	}
	@Nullable
	private World getCaterpillarWorld(BlockPos pos){
		if (FMLCommonHandler.instance().getMinecraftServerInstance().worlds != null)
		{
			if (FMLCommonHandler.instance().getMinecraftServerInstance().worlds.length >0)
			{
				for (WorldServer worldServer : FMLCommonHandler.instance().getMinecraftServerInstance().worlds) {
					IBlockState state  = worldServer.getBlockState(pos);
					if (state.getBlock() instanceof BlockDrillBase)
					{
						return worldServer;
					}
				}
			}
		}
		return null;
	}

	public void readNBTDrills()
	{
		NBTTagCompound tmpNBT =  Reference.MainNBT.readNBTSettings(Reference.MainNBT.getFolderLocationWorld(), "DrillHeads.dat");
		this.mainContainers.clear();

		if (tmpNBT.hasKey("count"))
		{
			int size = tmpNBT.getInteger("count");
			for(int i=0;i<size;i++)
			{
				CaterpillarData conCata = CaterpillarData.readCaterpiller(tmpNBT.getCompoundTag("caterpillar" + i));
				conCata.tabs.selected = GuiTabs.MAIN;
				World objWorld = this.getCaterpillarWorld(conCata.pos);
				if (objWorld != null)
				{
					IBlockState state = objWorld.getBlockState(conCata.pos);
					if (state.getBlock() instanceof BlockDrillBase)
					{
						int[] movingXZ = this.getWayMoving(state);
						if (movingXZ[0] != -2 && movingXZ[1] != -2)
						{
							this.mainContainers.put(conCata.name, conCata);
						}
					}
				}
				else
				{
					Reference.printDebug("Load error NBT Drills");
				}
			}
		}
	}

	public void reset() {
		Reference.printDebug("Resetting....");
		Reference.loaded = false;
		this.modTimer.inSetup = false;
		this.mainContainers.clear();
	}
	public ItemStack[] getInventory(CaterpillarData caterpillarData, GuiTabs selected)
	{
		if (caterpillarData != null)
		{
			switch (selected.value) {
			case 0:
				return caterpillarData.inventory;
			case 1:
				return caterpillarData.decoration.getSelectedInventory();
			case 2:
				return caterpillarData.reinforcement.reinforcementMap;
			case 3:
				return caterpillarData.incinerator.placementMap;
			default:
				break;
			}
		}
		return null;
	}

	public enum Replacement {
		AIR(0, proxy.translateToLocal("replacement1")),
		WATER(1, proxy.translateToLocal("replacement2")),
		LAVA(2, proxy.translateToLocal("replacement3")),
		FALLINGBLOCKS(3, proxy.translateToLocal("replacement4")),
		ALL(4, proxy.translateToLocal("replacement5"));
		public final int value;
		public final String name;

		Replacement(int value, String name) {
			this.value = value;
			this.name = name;
		}
	}
	public enum GuiTabs {
		MAIN(0, proxy.translateToLocal("tabs1"), false, new ResourceLocation(MODID  + ":textures/gui/guicatapiller.png")),
		DECORATION(1, proxy.translateToLocal("tabs2"), true, new ResourceLocation(MODID  + ":textures/gui/guidecoration.png")),
		REINFORCEMENT(2, proxy.translateToLocal("tabs3"), true, new ResourceLocation(MODID  + ":textures/gui/guireinfocement.png")),
		INCINERATOR(3, proxy.translateToLocal("tabs4"), true, new ResourceLocation(MODID  + ":textures/gui/guiincinerator.png"));
		public final int value;
		public final String name;
		public final boolean isCrafting;
		public final ResourceLocation guiTextures;

		GuiTabs(int value, String name, boolean isCrafting, ResourceLocation guiTextures) {
			this.value = value;
			this.name = name;
			this.guiTextures = guiTextures;
			this.isCrafting = isCrafting;
		}
	}
}
