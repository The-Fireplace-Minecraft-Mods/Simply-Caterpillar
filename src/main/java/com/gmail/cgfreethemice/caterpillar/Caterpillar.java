package com.gmail.cgfreethemice.caterpillar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.text.html.HTMLDocument.HTMLReader.BlockAction;

import com.gmail.cgfreethemice.caterpillar.blocks.BlockDrillBase;
import com.gmail.cgfreethemice.caterpillar.blocks.BlockDrillHeads;
import com.gmail.cgfreethemice.caterpillar.containers.ContainerCaterpillar;
import com.gmail.cgfreethemice.caterpillar.handlers.HandlerFMLEvents;
import com.gmail.cgfreethemice.caterpillar.handlers.HandlerForgeEvents;
import com.gmail.cgfreethemice.caterpillar.handlers.HandlerGUI;
import com.gmail.cgfreethemice.caterpillar.handlers.HandlerNBTTag;
import com.gmail.cgfreethemice.caterpillar.handlers.HandlerPackets;
import com.gmail.cgfreethemice.caterpillar.inits.InitBlocks;
import com.gmail.cgfreethemice.caterpillar.packets.PacketCaterpillarControls;
import com.gmail.cgfreethemice.caterpillar.parts.PartsGuiWidgets;
import com.gmail.cgfreethemice.caterpillar.proxy.ProxyCommon;
import com.gmail.cgfreethemice.caterpillar.tabs.TabCaterpillar;
import com.gmail.cgfreethemice.caterpillar.tileentity.TileEntityDrillHead;
import com.gmail.cgfreethemice.caterpillar.timers.TimerMain;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.ShapedOreRecipe;

@Mod(name = Reference.Name, modid = Reference.MODID, version = Reference.VERSION, guiFactory=Reference.guiFactory)
public class Caterpillar
{
	@Instance(Reference.MODID)
	public static Caterpillar instance;


	public static final CreativeTabs TabCaterpillar = new TabCaterpillar();
	public int saveCount = 0;
	public TimerMain ModTasks;

	private HashMap<String, ContainerCaterpillar> mainContainers;
	private ContainerCaterpillar selectedCaterpillar;
	//private HashMap<String, ContainerCaterpillar> mainContainersRemote;

	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
	public static ProxyCommon proxy;

	public static SimpleNetworkWrapper network;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		Config.init(event.getSuggestedConfigurationFile());

		Reference.MainNBT = new HandlerNBTTag(Reference.MODID);

		mainContainers = new HashMap<String, ContainerCaterpillar>();
		//mainContainersRemote = new HashMap<String, ContainerCaterpillar>();

		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new HandlerGUI());

		this.ModTasks = new TimerMain();

		network = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MODID);
		network.registerMessage(new HandlerPackets(), PacketCaterpillarControls.class, 0, Side.SERVER);
		network.registerMessage(new HandlerPackets(), PacketCaterpillarControls.class, 0, Side.CLIENT);


	}


	@EventHandler
	public void init(FMLInitializationEvent event)
	{

		this.register();

		this.recipes();

		Reference.cleanModsFolder();
		if (Config.autoupdate)
		{
			Reference.getupdateNBT();

			Reference.getupdaterJAR();
		}

		Reference.ModTick.scheduleAtFixedRate(this.ModTasks, 10, 10);
	}



	private void recipes() {
		addRecipe(InitBlocks.drillbase, "C C", "CRC", "CPC", 'C', "cobblestone", 'R', "dustRedstone", 'P', "plankWood");
		addRecipe(InitBlocks.drillheads, "III", " D ", " F ", 'D', InitBlocks.drillbase, 'I', "ingotIron", 'F', Blocks.furnace);
		addRecipe(InitBlocks.reinforcements, " P ", "PDP", " P ", 'D', InitBlocks.drillbase, 'I', "ingotIron", 'P', Blocks.piston);
		addRecipe(InitBlocks.decoration, "   ", "PDP", "   ", 'D', InitBlocks.drillbase, 'P', Blocks.dispenser);
		addRecipe(InitBlocks.collector, "D", "H", 'D', InitBlocks.drillbase, 'I', "ingotIron", 'H', Blocks.hopper);
		addRecipe(InitBlocks.storage, "PDP", 'D', InitBlocks.drillbase, 'I', "ingotIron", 'P', Blocks.chest);
	}
	private void addRecipe(Block block, Object... args){
		GameRegistry.addRecipe(new ShapedOreRecipe(block, args));
	}
	public String getCatapillarID(int[] movingXZ, BlockPos Wherepos)
	{
		int firstID = movingXZ[1] * Wherepos.getX() + movingXZ[0] * Wherepos.getZ();
		int secondID = Wherepos.getY();
		int third = 0;
		if (movingXZ[0] != 0)
		{
			third = movingXZ[0] + 2;
		}
		if (movingXZ[1] != 0)
		{
			third = movingXZ[1] + 3;
		}
		//Reference.printDebug("Cat ID: " + firstID + "," + secondID + "," + third);
		return firstID + "," + secondID + "," + third;
	}
	public int[] getWayMoving(IBlockState state) {
		int[] movingXZ = {0, 0};
		BlockDrillHeads myDrillHead = ((BlockDrillHeads)InitBlocks.drillheads);
		if (state.getValue(myDrillHead.FACING) == EnumFacing.EAST)
		{
			movingXZ[0] = -1;	//1
		}
		if (state.getValue(myDrillHead.FACING) == EnumFacing.WEST)
		{
			movingXZ[0] = 1;	//3
		}
		if (state.getValue(myDrillHead.FACING) == EnumFacing.NORTH)
		{
			movingXZ[1] = 1;	//4
		}
		if (state.getValue(myDrillHead.FACING) == EnumFacing.SOUTH)
		{
			movingXZ[1] = -1;	//2
		}
		return movingXZ;
	}
	public ContainerCaterpillar getSelectedCaterpillar()
	{
		return this.selectedCaterpillar;
	}
	public void setSelectedCaterpillar(ContainerCaterpillar selectedcat)
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
			if (this.mainContainers.containsKey(CaterpillarID))
			{
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}
	public boolean doesHaveCaterpillar(BlockPos pos)
	{
		IBlockState thisState =  Reference.theWorldServer().getBlockState(pos);
		return doesHaveCaterpillar(pos, thisState);
	}
	public boolean doesHaveCaterpillar(BlockPos pos, IBlockState thisState)
	{
		BlockDrillHeads myDrillHead = ((BlockDrillHeads)InitBlocks.drillheads);
		int[] movingXZ = this.getWayMoving(thisState);
		String CatID = this.getCatapillarID(movingXZ, pos);
		return this.doesHaveCaterpillar(CatID);
	}

	public void putContainerCaterpillar(ContainerCaterpillar conCat) {
		IBlockState thisState =  Reference.theWorldServer().getBlockState(conCat.pos);
		int[] movingXZ = this.getWayMoving(thisState);
		String CatID = this.getCatapillarID(movingXZ, conCat.pos);
		putContainerCaterpillar(CatID, conCat);
	}
	public void putContainerCaterpillar(String CaterpillarID, ContainerCaterpillar conCat) {

		/*if (this.mainContainers.containsKey(conCat))
		{
			this.mainContainers.remove(conCat);
		}*/
		this.mainContainers.put(CaterpillarID, conCat);
		//this.mainContainersRemote.put(CaterpillarID, conCat.clone());
	}

	public ContainerCaterpillar getContainerCaterpillar(String caterpillarID) {
		ContainerCaterpillar conCat = this.mainContainers.get(caterpillarID);
		return conCat;
	}
	public ContainerCaterpillar getContainerCaterpillar(BlockPos pos)
	{
		IBlockState thisState =  Reference.theWorldClient().getBlockState(pos);
		int[] movingXZ = this.getWayMoving(thisState);
		String catID = this.getCatapillarID(movingXZ, pos);
		return this.getContainerCaterpillar(catID);
	}
	public ContainerCaterpillar getContainerCaterpillar(BlockPos pos, IBlockState thisState)
	{
		int[] movingXZ = this.getWayMoving(thisState);
		String catID =this.getCatapillarID(movingXZ, pos);
		return this.getContainerCaterpillar(catID);
	}

	public void saveNBTDrills()
	{
		if (Reference.Loaded)
		{
			Reference.MainNBT.FileName = "DrillHeads.dat";
			NBTTagCompound tmpNBT = new NBTTagCompound();
			int i = 0;
			for (String key : mainContainers.keySet()) {
				ContainerCaterpillar conCat = mainContainers.get(key);
				tmpNBT.setTag("catapillar" + i, conCat.writeNBTCatapillar());
				i++;
			}
			tmpNBT.setInteger("count", i);
			Reference.MainNBT.saveNBTSettings(tmpNBT, Reference.MainNBT.getFolderLocationWorld());
		}
	}
	public void readNBTDrills()
	{
		Reference.MainNBT.FileName = "DrillHeads.dat";
		NBTTagCompound tmpNBT =  Reference.MainNBT.readNBTSettings(Reference.MainNBT.getFolderLocationWorld());
		this.mainContainers.clear();
		//this.mainContainersRemote.clear();

		if (tmpNBT.hasKey("count"))
		{
			int size = tmpNBT.getInteger("count");
			for(int i=0;i<size;i++)
			{
				ContainerCaterpillar conCata = ContainerCaterpillar.readCatapiller(tmpNBT.getCompoundTag("catapillar" + i));
				conCata.tabs.selected = GuiTabs.MAIN;
				if (conCata != null)
				{
					IBlockState state = Reference.theWorldServer().getBlockState(conCata.pos);
					if (state.getBlock() instanceof BlockDrillBase)
					{
						int[] movingXZ = getWayMoving(state);
						this.mainContainers.put(conCata.name, conCata);
						//this.mainContainersRemote.put(conCata.name, conCata.clone());
						for (int j = 0; j < 20; j++) {
							BlockPos bPOS = conCata.pos.add(j * -1 * movingXZ[0], 0, j * -1 * movingXZ[1]);
							Block checkBlock = Reference.theWorldServer().getBlockState(bPOS).getBlock();
						}
					}
				}
				else
				{
					Reference.printDebug("load error NBT Drills");
				}

			}
		}
	}

	public void reset() {
		Reference.printDebug("Resetting....");
		Reference.Loaded = false;
		this.ModTasks.inSetup = false;
		this.mainContainers.clear();
		//this.mainContainersRemote.clear();
	}
	private void register() {
		InitBlocks.init();
		InitBlocks.register();

		GameRegistry.registerTileEntity(TileEntityDrillHead.class, "DrillHead");

		proxy.registerRenders();

		FMLCommonHandler.instance().bus().register(new HandlerFMLEvents());
		MinecraftForge.EVENT_BUS.register(new HandlerForgeEvents());

	}
	public ItemStack[] getInventory(ContainerCaterpillar MyCaterpillar, GuiTabs selected)
	{
		if (MyCaterpillar != null)
		{
			switch (selected.value) {
			case 0:
				//Reference.printDebug("Getting: Main, 0");
				return MyCaterpillar.inventory;
			case 1:
				//Reference.printDebug("Getting: Decoration, 1");
				return MyCaterpillar.decoration.getSelectedInventory();
			case 2:
				//Reference.printDebug("Getting: Reinforcement, 2");
				return MyCaterpillar.reinforcement.reinforcementMap;
			default:
				break;
			}
		}
		return new ItemStack[256];

	}
	public enum Replacement {
		AIR(0, "Air"),
		WATER(1, "Water"),
		LAVE(2, "Lava"),
		SANDGRAVEL(3, "Sand and Gravel"),
		ALL(4, "All");
		public int value;
		public String name;

		private Replacement(int value, String name) {
			this.value = value;
			this.name = name;
		}
	}
	public enum GuiTabs {
		MAIN(0, "Main", false, new ResourceLocation(Reference.MODID  + ":textures/gui/guicatapiller.png")),
		DECORATION(1, "Decoration", true, new ResourceLocation(Reference.MODID  + ":textures/gui/guidecoration.png")),
		REINFORCEMENT(2, "Reinforcement", true, new ResourceLocation(Reference.MODID  + ":textures/gui/guireinfocement.png"));
		public int value;
		public String name;
		public boolean isCrafting;
		public ResourceLocation guiTextures;
		
		private GuiTabs(int value, String name, boolean isCrafting, ResourceLocation guiTextures) {
			this.value = value;
			this.name = name;
			this.guiTextures = guiTextures;
			this.isCrafting = isCrafting;
		}
	}
}
