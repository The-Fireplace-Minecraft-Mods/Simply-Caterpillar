package com.gmail.cgfreethemice.caterpillar.blocks;

import java.util.Hashtable;
import java.util.Random;

import com.gmail.cgfreethemice.caterpillar.Caterpillar;
import com.gmail.cgfreethemice.caterpillar.Config;
import com.gmail.cgfreethemice.caterpillar.Reference;
import com.gmail.cgfreethemice.caterpillar.containers.ContainerCaterpillar;
import com.gmail.cgfreethemice.caterpillar.inits.InitBlocks;
import com.gmail.cgfreethemice.caterpillar.tileentity.TileEntityDrillHead;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockDrillBase extends BlockContainer {

	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	public Hashtable<BlockPos, Integer> pushticks;
	public int DraggBurnTime = 0;
	public BlockDrillBase() {
		super(Material.iron);
		setStepSound(soundTypePiston);
		setHardness(0.5F);

		this.pushticks = new Hashtable<BlockPos, Integer>();
		this.DraggBurnTime = 25;
		//this.setBlockBounds(-1.0F, 0.0F, -1.0F, 2.0F, 3.0F, 2.0F);

	}
	protected void takeOutMatsandPlace(World worldIn, String MyId, BlockPos pos, IBlockState IBstate)
	{

		ContainerCaterpillar thisCat = Caterpillar.instance.getContainerCaterpillar(MyId);
		if (thisCat != null)
		{
			if (IBstate.getBlock().equals(Blocks.air))
			{
				worldIn.setBlockToAir(pos);
				return;
			}
			int Middleindex = (thisCat.inventory.length - 1) / 2;

			ItemStack[] thisGuyInv = thisCat.inventory;

			for (int i = 0; i < Middleindex; i++) {
				if (thisGuyInv[i] != null)
				{
					Block inIvn = Block.getBlockFromItem(thisGuyInv[i].getItem());
					if (inIvn != null)
					{

						if (inIvn.getStateFromMeta(thisGuyInv[i].getItemDamage()).equals(IBstate))
						{
							//ItemStack justOne = new ItemStack(thisGuyInv[i].getItem(), 1, thisGuyInv[i].getItemDamage());
							ItemStack theRest = null;
							if ( thisGuyInv[i].stackSize > 1)
							{
								theRest = new ItemStack(thisGuyInv[i].getItem(), thisGuyInv[i].stackSize - 1, thisGuyInv[i].getItemDamage());
							}
							thisGuyInv[i] = theRest;
							worldIn.setBlockState(pos, IBstate);
							return;
						}
					}
				}
			}
		}

	}
	protected int getCountIndex(int[] movingXZ, BlockPos Wherepos) {
		int Count  = movingXZ[0] * Wherepos.getX() +  movingXZ[1] * Wherepos.getZ();
		String newT = Count + "";
		newT = newT.substring(newT.length() - 1, newT.length());
		Count = Integer.parseInt(newT);
		if ( movingXZ[0] < 0 ||  movingXZ[1] > 0)
		{
			Count = 9 - Count;
		}
		
		return Count;
	}



	public void movieMe(World worldIn, BlockPos pos, IBlockState state)
	{
		try {
			if (Reference.Loaded == true && !worldIn.isRemote) // !worldIn.isRemote &&
			{
				if (!(worldIn.getBlockState(pos).getBlock() instanceof BlockDrillBase))
				{
					Reference.theWorldServer().removeTileEntity(pos);
					Reference.printDebug("Tile Entity: Removed " + pos.toString());
					return;
				}

				int[] movingXZ = Caterpillar.instance.getWayMoving(state);
				boolean okToMove = false;

				if (worldIn.getBlockState(pos.add(movingXZ[0], 0, movingXZ[1])).equals(Blocks.air.getDefaultState()) ||
						worldIn.getBlockState(pos.add(movingXZ[0], 0, movingXZ[1])).getBlock().equals(Blocks.flowing_lava) ||
						worldIn.getBlockState(pos.add(movingXZ[0], 0, movingXZ[1])).getBlock().equals(Blocks.flowing_water) ||
						worldIn.getBlockState(pos.add(movingXZ[0], 0, movingXZ[1])).getBlock().equals(Blocks.water) ||
						worldIn.getBlockState(pos.add(movingXZ[0], 0, movingXZ[1])).getBlock().equals(Blocks.lava))
				{
					okToMove = true;
				}
				if (okToMove == true && worldIn.getBlockState(pos.add(movingXZ[0]*2, 0, movingXZ[1]*2)).getBlock() instanceof BlockDrillBase)
				{
					if (!this.pushticks.containsKey(pos))
					{
						this.pushticks.put(pos, 0);
					}

					int counter = this.pushticks.get(pos);
					counter++;
					this.pushticks.put(pos, counter);
					Random rnd = new Random(System.currentTimeMillis());
					if (counter < 10 + rnd.nextInt(10))
					{
						return;
					}
					this.pushticks.remove(pos);




					BlockPos newPOS = pos.add(movingXZ[0], 0, movingXZ[1]);

					Reference.spawnParticles(newPOS, EnumParticleTypes.FIREWORKS_SPARK);
					worldIn.setBlockState(newPOS, state);
					worldIn.setBlockToAir(pos);
					if(Config.enablesounds)
						worldIn.playSoundEffect(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, "tile.piston.out", 0.5F, worldIn.rand.nextFloat() * 0.25F + 0.6F);

					String catID = Caterpillar.instance.getCatapillarID(movingXZ, newPOS);
					int Count = this.getCountIndex(movingXZ, newPOS);
					ContainerCaterpillar thiscater = Caterpillar.instance.getContainerCaterpillar(catID);
					this.Fired(worldIn, newPOS, state, catID, movingXZ, Count);
					if (thiscater != null)
					{
						this.setDrag(thiscater);
						thiscater.headTick = 0;
						if (thiscater.drag.value > thiscater.drag.max)
						{
							if (state.getBlock() instanceof BlockStorage)
							{
								((BlockStorage)InitBlocks.storage).changeStorage(thiscater, -24);
							}
							Reference.dropBlockAsItem(worldIn, newPOS, newPOS, state, 0);
							worldIn.setBlockToAir(newPOS);
						}
					}
					Caterpillar.instance.saveNBTDrills();
					return;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {

	}
	protected void setDrag(ContainerCaterpillar thisCat)
	{
		thisCat.drag.value = thisCat.drag.value + this.DraggBurnTime;
	}
	protected void Fired(World worldIn, BlockPos pos, IBlockState state, String catID, int[] movingXZ, int Count) {}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		//Reference.printDebug("Gui Called: a little: " + Reference.Loaded);
		Reference.printDebug("Gui Called: 1");
		if (Reference.Loaded == true && !worldIn.isRemote)
		{
			Reference.printDebug("Gui Called: 2");
			//if ( && !worldIn.isRemote)
			{
				TileEntity tileentity = worldIn.getTileEntity(pos);

				Reference.printDebug("Gui Called: 3");
				if (tileentity instanceof TileEntityDrillHead)
				{
					Reference.printDebug("Gui Called: 4");
					ContainerCaterpillar thisCat = Caterpillar.instance.getContainerCaterpillar(pos, state); // false
					if (thisCat != null)
					{
						Reference.printDebug("Gui Called: 5");
						playerIn.openGui(Caterpillar.instance, Reference.GUI_ENUM.DRILLHEAD.ordinal(), worldIn, thisCat.pos.getX(), thisCat.pos.getY(), thisCat.pos.getZ());
						Reference.printDebug("Gui Called: " + worldIn.isRemote );
						Reference.printDebug("Gui Called: " + Caterpillar.proxy.isServer());
					}
					else
					{
						Reference.printDebug("Cat Not Found" );
					}
				}
				else
				{
					Reference.printDebug("Tile Not Found" );
				}

			}
		}
		return true;
	}
	@Override
	public void onBlockExploded(World world, BlockPos pos, Explosion explosion)
	{
		//Can't be destroyed by the Explosion
	}
	//*******************************************for facing*******************************************
	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
	{
		super.onBlockAdded(worldIn, pos, state);
		if (!worldIn.isRemote)
		{
			// Rotate block if the front side is blocked
			Block blockToNorth = worldIn.getBlockState(
					pos.north()).getBlock();
			Block blockToSouth = worldIn.getBlockState(
					pos.south()).getBlock();
			Block blockToWest = worldIn.getBlockState(
					pos.west()).getBlock();
			Block blockToEast = worldIn.getBlockState(
					pos.east()).getBlock();
			EnumFacing enumfacing = (EnumFacing)state
					.getValue(FACING);

			if (enumfacing == EnumFacing.NORTH
					&& blockToNorth.isFullBlock()
					&& !blockToSouth.isFullBlock())
			{
				enumfacing = EnumFacing.SOUTH;
			}
			else if (enumfacing == EnumFacing.SOUTH
					&& blockToSouth.isFullBlock()
					&& !blockToNorth.isFullBlock())
			{
				enumfacing = EnumFacing.NORTH;
			}
			else if (enumfacing == EnumFacing.WEST
					&& blockToWest.isFullBlock()
					&& !blockToEast.isFullBlock())
			{
				enumfacing = EnumFacing.EAST;
			}
			else if (enumfacing == EnumFacing.EAST
					&& blockToEast.isFullBlock()
					&& !blockToWest.isFullBlock())
			{
				enumfacing = EnumFacing.WEST;
			}

			worldIn.setBlockState(pos, state
					.withProperty(FACING, enumfacing), 2);
		}

	}
	@Override
	@SideOnly(Side.CLIENT)
	public IBlockState getStateForEntityRender(IBlockState state)
	{
		return getDefaultState().withProperty(FACING, EnumFacing.SOUTH);
	}
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		EnumFacing enumfacing = EnumFacing.getFront(meta);

		if (enumfacing.getAxis() == EnumFacing.Axis.Y)
		{
			enumfacing = EnumFacing.NORTH;
		}

		return getDefaultState().withProperty(FACING, enumfacing);
	}
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return ((EnumFacing)state.getValue(FACING)).getIndex();
	}
	@Override
	protected BlockState createBlockState()
	{
		return new BlockState(this, new IProperty[] {FACING});
	}
	@SideOnly(Side.CLIENT)
	static final class SwitchEnumFacing
	{
		static final int[] enumFacingArray = new int[EnumFacing.values()
		                                             .length];

		static
		{
			try
			{
				enumFacingArray[EnumFacing.WEST.ordinal()] = 1;
			}
			catch (NoSuchFieldError var4)
			{
				;
			}

			try
			{
				enumFacingArray[EnumFacing.EAST.ordinal()] = 2;
			}
			catch (NoSuchFieldError var3)
			{
				;
			}

			try
			{
				enumFacingArray[EnumFacing.NORTH.ordinal()] = 3;
			}
			catch (NoSuchFieldError var2)
			{
				;
			}

			try
			{
				enumFacingArray[EnumFacing.SOUTH.ordinal()] = 4;
			}
			catch (NoSuchFieldError var1)
			{
				// You should improve the error handling here
			}
		}
	}

	//*******************************************for renders*******************************************
	@Override
	public int getRenderType()
	{
		return 3;
	}
	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
	@Override
	public boolean isFullCube()
	{
		return false;
	}
	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
	{
		return true;
	}
	@Override
	@SideOnly(Side.CLIENT)
	public EnumWorldBlockLayer getBlockLayer()
	{
		return EnumWorldBlockLayer.CUTOUT_MIPPED;
	}
	@Override
	protected boolean canSilkHarvest()
	{
		return false;
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		if (!worldIn.isRemote)
		{
			if (worldIn.getBlockState(pos.add(placer.getHorizontalFacing().getFrontOffsetX(), placer.getHorizontalFacing().getFrontOffsetY(),placer.getHorizontalFacing().getFrontOffsetZ())).getBlock() instanceof BlockDrillBase)
			{
				worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);
				return;
			}
			if (worldIn.getBlockState(pos.add(0, 1,0)).equals(Blocks.air.getDefaultState()))
			{
				worldIn.setBlockState(pos.add(0, 1,0), state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);
			}
			else
			{
				Reference.dropBlockAsItem(worldIn, pos, pos, state, 0);
			}
			worldIn.setBlockToAir(pos);
			return ;
		}

		worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);
	}
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntityDrillHead();
	}


}
