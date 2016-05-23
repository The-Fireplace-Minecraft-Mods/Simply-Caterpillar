package the_fireplace.caterpillar.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.Config;
import the_fireplace.caterpillar.Reference;
import the_fireplace.caterpillar.containers.ContainerCaterpillar;
import the_fireplace.caterpillar.packets.PacketParticles;
import the_fireplace.caterpillar.tileentity.TileEntityDrillHead;

import java.util.Hashtable;
import java.util.Random;

public class BlockDrillBase extends BlockContainer {

	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	public Hashtable<BlockPos, Integer> pushticks;
	public BlockDrillBase() {
		super(Material.IRON);
		this.setSoundType(SoundType.STONE);
		this.setHardness(0.5F);

		this.pushticks = new Hashtable<>();
		//this.setBlockBounds(-1.0F, 0.0F, -1.0F, 2.0F, 3.0F, 2.0F);

	}


	protected void takeOutMatsandPlace(World worldIn, String MyId, BlockPos pos, IBlockState IBstate)
	{

		ContainerCaterpillar thisCat = Caterpillar.instance.getContainerCaterpillar(MyId);
		if (thisCat != null)
		{
			if (IBstate.getBlock().equals(Blocks.AIR))
			{
				worldIn.setBlockToAir(pos);
				return;
			}
			int Middleindex = (thisCat.inventory.length - 2) / 2;
			Middleindex += 2;
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
			if (Reference.Loaded && !worldIn.isRemote) // !worldIn.isRemote &&
			{
				if (!(worldIn.getBlockState(pos).getBlock() instanceof BlockDrillBase))
				{
					worldIn.removeTileEntity(pos);
					Reference.printDebug("Tile Entity: Removed " + pos.toString());
					return;
				}

				int[] movingXZ = Caterpillar.instance.getWayMoving(state);
				boolean okToMove = false;

				if (worldIn.getBlockState(pos.add(movingXZ[0], 0, movingXZ[1])).equals(Blocks.AIR.getDefaultState()) ||
						worldIn.getBlockState(pos.add(movingXZ[0], 0, movingXZ[1])).getBlock().equals(Blocks.FLOWING_LAVA) ||
						worldIn.getBlockState(pos.add(movingXZ[0], 0, movingXZ[1])).getBlock().equals(Blocks.FLOWING_WATER) ||
						worldIn.getBlockState(pos.add(movingXZ[0], 0, movingXZ[1])).getBlock().equals(Blocks.WATER) ||
						worldIn.getBlockState(pos.add(movingXZ[0], 0, movingXZ[1])).getBlock().equals(Blocks.LAVA))
				{
					okToMove = true;
				}
				if (okToMove && worldIn.getBlockState(pos.add(movingXZ[0]*2, 0, movingXZ[1]*2)).getBlock() instanceof BlockDrillBase)
				{
					if (!this.pushticks.containsKey(pos))
					{
						this.pushticks.put(pos, 0);
					}

					int counter = this.pushticks.get(pos);
					counter++;
					this.pushticks.put(pos, counter);
					this.pushticks.remove(pos);

					BlockPos newPOS = pos.add(movingXZ[0], 0, movingXZ[1]);
					BlockPos newPlace = pos.add(movingXZ[0], 0, movingXZ[1]);
					TargetPoint TPoint = new TargetPoint(worldIn.getWorldType().getWorldTypeID(), newPlace.getX(), newPlace.getY(), newPlace.getZ(), 5);
					Caterpillar.network.sendToAllAround(new PacketParticles(EnumParticleTypes.FIREWORKS_SPARK.name(), newPlace.getX(), newPlace.getY(), newPlace.getZ()), TPoint);

					worldIn.setBlockState(newPOS, state);
					worldIn.setBlockToAir(pos);
					if(Config.enablesounds) {
						worldIn.playSound(null, pos, SoundEvents.BLOCK_PISTON_EXTEND, SoundCategory.BLOCKS, 0.5F, worldIn.rand.nextFloat() * 0.25F + 0.6F);
					}

					String catID = Caterpillar.instance.getCaterpillarID(movingXZ, newPOS);
					int Count = this.getCountIndex(movingXZ, newPOS);
					ContainerCaterpillar thiscater = Caterpillar.instance.getContainerCaterpillar(catID);
					this.Fired(worldIn, newPOS, state, catID, movingXZ, Count);
					if (thiscater != null)
					{
						thiscater.headTick = 0;
					}
					Caterpillar.instance.saveNBTDrills();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {

	}

	protected void Fired(World worldIn, BlockPos pos, IBlockState state, String catID, int[] movingXZ, int Count) {}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack held, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		//Reference.printDebug("Gui Called: a little: " + Reference.Loaded);
		Reference.printDebug("Gui Called: 1");
		if (Reference.Loaded && !worldIn.isRemote)
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
						playerIn.openGui(Caterpillar.instance, 0, worldIn, thisCat.pos.getX(), thisCat.pos.getY(), thisCat.pos.getZ());
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
			IBlockState blockToNorth = worldIn.getBlockState(
					pos.north());
			IBlockState blockToSouth = worldIn.getBlockState(
					pos.south());
			IBlockState blockToWest = worldIn.getBlockState(
					pos.west());
			IBlockState blockToEast = worldIn.getBlockState(
					pos.east());
			EnumFacing enumfacing = state
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
	/*@Override
	@SideOnly(Side.CLIENT)
	public IBlockState getStateForEntityRender(IBlockState state)
	{
		return this.getDefaultState().withProperty(FACING, EnumFacing.SOUTH);
	}*/
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		EnumFacing enumfacing = EnumFacing.getFront(meta);

		if (enumfacing.getAxis() == EnumFacing.Axis.Y)
		{
			enumfacing = EnumFacing.NORTH;
		}

		return this.getDefaultState().withProperty(FACING, enumfacing);
	}
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(FACING).getIndex();
	}
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, FACING);
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
			}

			try
			{
				enumFacingArray[EnumFacing.EAST.ordinal()] = 2;
			}
			catch (NoSuchFieldError var3)
			{
			}

			try
			{
				enumFacingArray[EnumFacing.NORTH.ordinal()] = 3;
			}
			catch (NoSuchFieldError var2)
			{
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
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.MODEL;
	}
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}
	/*@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
	{
		return true;
	}*/
	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT_MIPPED;
	}
	@Override
	public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player)
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
			if (worldIn.getBlockState(pos.add(0, 1,0)).equals(Blocks.AIR.getDefaultState()))
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
