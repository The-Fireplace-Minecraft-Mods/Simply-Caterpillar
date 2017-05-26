package the_fireplace.caterpillar;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import org.apache.commons.lang3.ArrayUtils;
import the_fireplace.caterpillar.blocks.BlockDrillBase;
import the_fireplace.caterpillar.blocks.BlockDrillHeads;
import the_fireplace.caterpillar.inits.InitBlocks;
import the_fireplace.caterpillar.network.GUIHandler;
import the_fireplace.caterpillar.network.PacketDispatcher;
import the_fireplace.caterpillar.proxy.ProxyCommon;
import the_fireplace.caterpillar.tabs.TabCaterpillar;
import the_fireplace.caterpillar.tileentity.TileEntityDrillHead;
import the_fireplace.caterpillar.tools.NBTTools;

import javax.annotation.Nullable;

@Mod(name = Caterpillar.MODNAME, modid = Caterpillar.MODID, guiFactory=Reference.guiFactory, updateJSON = "http://thefireplace.bitnamiapp.com/jsons/simplycaterpillar.json", acceptedMinecraftVersions = "[1.9.4,1.10.2]")
public class Caterpillar
{
	public static final String MODID = "simplycaterpillar";
	public static final String MODNAME = "Simply Caterpillar";
	@Instance(Caterpillar.MODID)
	public static Caterpillar instance;

	public static final CreativeTabs tabCaterpillar = new TabCaterpillar();
	boolean dev = false;

	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
	public static ProxyCommon proxy;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		dev = event.getModMetadata().version.equals("${version}");
		Config.init(event.getSuggestedConfigurationFile());

		Reference.MainNBT = new NBTTools(MODID);

		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GUIHandler());

		PacketDispatcher.registerPackets();

		InitBlocks.init();
		InitBlocks.register();
		proxy.registerRenders();
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		GameRegistry.registerTileEntity(TileEntityDrillHead.class, "DrillHead");
		MinecraftForge.EVENT_BUS.register(new CommonEvents());
		this.recipes();
		Reference.cleanModsFolder();
		Config.load();
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

	public ItemStack[] getInventory(TileEntityDrillHead caterpillarTe, GuiTabs selected)
	{
		if (caterpillarTe != null)
		{
			switch (selected.value) {
			case 0:
				ItemStack[] initial = new ItemStack[]{caterpillarTe.fuelSlotStack};
				return ArrayUtils.addAll(initial, caterpillarTe.getCurrentInventory());
			case 1:
				return caterpillarTe.decoration.getSelectedInventory();
			case 2:
				return caterpillarTe.reinforcement.reinforcementMap;
			case 3:
				return caterpillarTe.incinerator.placementMap;
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
		MAIN((byte)0, proxy.translateToLocal("tabs1"), false, new ResourceLocation(MODID  + ":textures/gui/guicatapiller.png")),
		DECORATION((byte)1, proxy.translateToLocal("tabs2"), true, new ResourceLocation(MODID  + ":textures/gui/guidecoration.png")),
		REINFORCEMENT((byte)2, proxy.translateToLocal("tabs3"), true, new ResourceLocation(MODID  + ":textures/gui/guireinfocement.png")),
		INCINERATOR((byte)3, proxy.translateToLocal("tabs4"), true, new ResourceLocation(MODID  + ":textures/gui/guiincinerator.png"));
		public final byte value;
		public final String name;
		public final boolean isCrafting;
		public final ResourceLocation guiTextures;

		GuiTabs(byte value, String name, boolean isCrafting, ResourceLocation guiTextures) {
			this.value = value;
			this.name = name;
			this.guiTextures = guiTextures;
			this.isCrafting = isCrafting;
		}
	}

	/**
	 * Gets the drill data for the exact position. Only should be used if you know the exact position of the drill head.
	 * @param worldIn
	 * 	The drill's world
	 * @param pos
	 * 	The drill head's position
	 * @return
	 * 	The tile entity, if one is found, else null
	 */
	@Nullable
	@Deprecated
	public static TileEntityDrillHead getCaterpillar(World worldIn, BlockPos pos){
		if(worldIn.getTileEntity(pos) != null && worldIn.getTileEntity(pos) instanceof TileEntityDrillHead)
			return (TileEntityDrillHead)worldIn.getTileEntity(pos);
		else
			return null;
	}

	/**
	 * Returns the first drill head in the chain, based on the facing of the component.
	 * @param worldIn
	 * 	The world to check
	 * @param pos
	 * 	The position to start at
	 * @param facing
	 * 	The direction to go in
	 * @return
	 * 	The drill head, if one is found, else null
	 */
	@Nullable
	public static TileEntityDrillHead getCaterpillar(World worldIn, BlockPos pos, EnumFacing facing){
		BlockPos pos2 = pos;
		boolean flag = false;
		while(worldIn.getBlockState(pos2).getBlock() instanceof BlockDrillBase || flag){
			//TODO: Verify that the head is facing the same direction as the component
			if(worldIn.getTileEntity(pos2) != null && worldIn.getTileEntity(pos2) instanceof TileEntityDrillHead)
				return (TileEntityDrillHead)worldIn.getTileEntity(pos2);
			pos2.add(facing.getFrontOffsetX(), 0, facing.getFrontOffsetZ());
			Reference.printDebug("Searching for the drill head. Initial Pos: "+pos.toString()+", New Pos: "+pos2.toString());
			//Let it skip 1 block while looking, to allow use with a moving caterpillar.
			if(flag){
				if(worldIn.getBlockState(pos2).getBlock() instanceof BlockDrillBase){
					flag = false;
				}else{
					break;
				}
			}else{
				if(!(worldIn.getBlockState(pos2).getBlock() instanceof BlockDrillBase))
					flag = true;
			}
		}
		return null;
	}
}
