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
import the_fireplace.caterpillar.network.PacketDispatcher;
import the_fireplace.caterpillar.network.packets.clientbound.PacketParticles;
import the_fireplace.caterpillar.tileentity.TileEntityDrillHead;
import the_fireplace.caterpillar.tileentity.TileEntityDrillPart;

import javax.annotation.Nonnull;
import java.util.Hashtable;
import java.util.Random;

public class BlockDrillBase extends BlockContainer {

	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	public Hashtable<BlockPos, Integer> pushticks;
	public int movementTicks;

	public BlockDrillBase() {
		super(Material.IRON);
		this.setSoundType(SoundType.STONE);
		this.setHardness(0.5F);

		this.pushticks = new Hashtable<>();
		//this.setBlockBounds(-1.0F, 0.0F, -1.0F, 2.0F, 3.0F, 2.0F);
		this.movementTicks = 25;
		this.setHarvestLevel("pickaxe", 0);
	}

	@Deprecated
	protected void takeOutMatsandPlace(World worldIn, BlockPos pos, IBlockState state)
	{
		TileEntityDrillHead thisCat = Caterpillar.getCaterpillar(worldIn, pos);
		if (thisCat != null)
		{
			if (state.getBlock().equals(Blocks.AIR))
			{
				worldIn.setBlockToAir(pos);
				return;
			}
			ItemStack[] thisGuyInv = thisCat.getCurrentInventory();

			for (int i = 0; i < 12; i++) {
				if (thisGuyInv[i] != null)
				{
					Block inIvn = Block.getBlockFromItem(thisGuyInv[i].getItem());
					if (inIvn != null)
					{

						if (inIvn.getStateFromMeta(thisGuyInv[i].getItemDamage()).equals(state))
						{
							//ItemStack justOne = new ItemStack(thisGuyInv[i].getItem(), 1, thisGuyInv[i].getItemDamage());
							ItemStack theRest = null;
							if (thisGuyInv[i].stackSize > 1)
							{
								theRest = new ItemStack(thisGuyInv[i].getItem(), thisGuyInv[i].stackSize - 1, thisGuyInv[i].getItemDamage());
							}
							thisGuyInv[i] = theRest;
							worldIn.setBlockState(pos, state);
							return;
						}
					}
				}
			}
		}
	}

	/**
	 * Drops materials and places a block
	 * @param worldIn
	 * 	The world to do this in
	 * @param pos
	 * 	The block position to do this in
	 * @param facing
	 * 	The facing of the caterpillar
	 * @param state
	 * 	The block state to place
	 */
	protected void takeOutMatsandPlace(World worldIn, BlockPos pos, EnumFacing facing, IBlockState state)
	{
		TileEntityDrillHead thisCat = Caterpillar.getCaterpillar(worldIn, pos, facing);
		if (thisCat != null)
		{
			if (state.getBlock().equals(Blocks.AIR))
			{
				worldIn.setBlockToAir(pos);
				return;
			}
			ItemStack[] thisGuyInv = thisCat.getCurrentInventory();

			for (int i = 0; i < 12; i++) {
				if (thisGuyInv[i] != null)
				{
					Block inIvn = Block.getBlockFromItem(thisGuyInv[i].getItem());
					if (inIvn != null)
					{

						if (inIvn.getStateFromMeta(thisGuyInv[i].getItemDamage()).equals(state))
						{
							//ItemStack justOne = new ItemStack(thisGuyInv[i].getItem(), 1, thisGuyInv[i].getItemDamage());
							ItemStack theRest = null;
							if (thisGuyInv[i].stackSize > 1)
							{
								theRest = new ItemStack(thisGuyInv[i].getItem(), thisGuyInv[i].stackSize - 1, thisGuyInv[i].getItemDamage());
							}
							thisGuyInv[i] = theRest;
							worldIn.setBlockState(pos, state);
							return;
						}
					}
				}
			}
		}
	}

	/**
	 * Drops materials and places a block
	 * @param worldIn
	 * 	The world to do this in
	 * @param pos
	 * 	The block position to do this in
	 * @param thisCat
	 * 	The caterpillar TE
	 * @param state
	 * 	The block state to place
	 */
	protected void takeOutMatsandPlace(World worldIn, BlockPos pos, TileEntityDrillHead thisCat, IBlockState state)
	{
		if (thisCat != null)
		{
			if (state.getBlock().equals(Blocks.AIR))
			{
				worldIn.setBlockToAir(pos);
				return;
			}
			ItemStack[] thisGuyInv = thisCat.getCurrentInventory();

			for (int i = 0; i < 12; i++) {
				if (thisGuyInv[i] != null)
				{
					Block inIvn = Block.getBlockFromItem(thisGuyInv[i].getItem());
					if (inIvn != null)
					{

						if (inIvn.getStateFromMeta(thisGuyInv[i].getItemDamage()).equals(state))
						{
							//ItemStack justOne = new ItemStack(thisGuyInv[i].getItem(), 1, thisGuyInv[i].getItemDamage());
							ItemStack theRest = null;
							if (thisGuyInv[i].stackSize > 1)
							{
								theRest = new ItemStack(thisGuyInv[i].getItem(), thisGuyInv[i].stackSize - 1, thisGuyInv[i].getItemDamage());
							}
							thisGuyInv[i] = theRest;
							worldIn.setBlockState(pos, state);
							return;
						}
					}
				}
			}
		}
	}

	protected int getCountIndex(int[] movingXZ, BlockPos loc) {
		int count  = movingXZ[0] * loc.getX() +  movingXZ[1] * loc.getZ();
		String newT = count + "";
		newT = newT.substring(newT.length() - 1, newT.length());
		count = Integer.parseInt(newT);
		if ( movingXZ[0] < 0 ||  movingXZ[1] > 0)
		{
			count = 9 - count;
		}

		return count;
	}

	public void calculateMovement(World worldIn, BlockPos pos, IBlockState state)
	{
		try {
			if (!worldIn.isRemote)
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
					Random rnd = new Random(System.currentTimeMillis());
					if (counter < 10 + rnd.nextInt(10))
					{
						return;
					}
					this.pushticks.remove(pos);

					BlockPos newPlace = pos.add(movingXZ[0], 0, movingXZ[1]);
					TargetPoint targetPoint = new TargetPoint(worldIn.getWorldType().getWorldTypeID(), newPlace.getX(), newPlace.getY(), newPlace.getZ(), 5);
					PacketDispatcher.sendToAllAround(new PacketParticles(EnumParticleTypes.FIREWORKS_SPARK.name(), newPlace.getX(), newPlace.getY(), newPlace.getZ()), targetPoint);

					worldIn.setBlockState(newPlace, state);
					worldIn.setBlockToAir(pos);
					if(Config.enablesounds) {
						worldIn.playSound(null, pos, SoundEvents.BLOCK_PISTON_EXTEND, SoundCategory.BLOCKS, 0.5F, worldIn.rand.nextFloat() * 0.25F + 0.6F);
					}

					int count = this.getCountIndex(movingXZ, newPlace);
					TileEntityDrillHead cater = Caterpillar.getCaterpillar(worldIn, pos, state.getValue(FACING));
					if (cater != null)
					{
						this.fired(worldIn, newPlace, state, cater, movingXZ, count);
						this.setDrag(cater);
						cater.headTick = 0;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {}

	protected void setDrag(TileEntityDrillHead cat){
		cat.movement.value += this.movementTicks;
	}

	protected void fired(World worldIn, BlockPos pos, IBlockState state, TileEntityDrillHead cat, int[] movingXZ, int Count) {}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack held, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		Reference.printDebug("Gui Called; Stage 1");
		if (!worldIn.isRemote)
		{
			TileEntity tileentity = worldIn.getTileEntity(pos);

			Reference.printDebug("Gui Called; Stage 2");
			if (tileentity instanceof TileEntityDrillHead)
			{
				Reference.printDebug("Gui Called; Stage 3");
				TileEntityDrillHead thisCat = Caterpillar.getCaterpillar(worldIn, pos, state.getValue(FACING));
				//PacketDispatcher.sendTo(new PacketRetrieveCatData(thisCat), (EntityPlayerMP)playerIn);
				//TODO: Send data if needed
				if (thisCat != null)
				{
					Reference.printDebug("Gui Called; Stage 4");
					playerIn.openGui(Caterpillar.instance, 0, worldIn, thisCat.getPos().getX(), thisCat.getPos().getY(), thisCat.getPos().getZ());
				}
				else
				{
					Reference.printDebug("Cat Not Found" );
				}
			}else{
				Reference.printDebug("Tile Not Found" );
			}
		}
		return true;
	}
	@Override
	public void onBlockExploded(World world, @Nonnull BlockPos pos, @Nonnull Explosion explosion)
	{}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
	{
		super.onBlockAdded(worldIn, pos, state);
		if (!worldIn.isRemote)
		{
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

	@Override
	@Nonnull
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
	@Nonnull
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, FACING);
	}

	@Override
	@Nonnull
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

	@Override
	@SideOnly(Side.CLIENT)
	@Nonnull
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT_MIPPED;
	}
	@Override
	public boolean canSilkHarvest(World world, BlockPos pos, @Nonnull IBlockState state, EntityPlayer player)
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
				updateCatC(pos, worldIn, state.getValue(FACING));//TODO: Ensure correct value passed
				return;
			}
			if (worldIn.getBlockState(pos.add(0,1,0)).equals(Blocks.AIR.getDefaultState()))
			{
				worldIn.setBlockState(pos.add(0,1,0), state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);
				updateCatC(pos, worldIn, state.getValue(FACING));//TODO: Ensure correct value passed
			}
			else
			{
				Reference.dropBlockAsItem(worldIn, pos, pos, state, 0);
			}
			worldIn.setBlockToAir(pos);
			return;
		}

		worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);
	}
	@Override
	@Nonnull
	public TileEntity createNewTileEntity(@Nonnull World worldIn, int meta)
	{
		return new TileEntityDrillPart();
	}

	private void updateCatC(BlockPos pos, World worldIn, EnumFacing facing){
		TileEntityDrillHead cat = Caterpillar.getCaterpillar(worldIn, pos, facing);
		if (cat != null) {
			Reference.printDebug("Caterpillar found on first attempt, updating");
			updateCat(cat);
		} else {
			cat = Caterpillar.getCaterpillar(worldIn, pos.add(0, 1, 0), facing);
			if (cat != null) {
				Reference.printDebug("Caterpillar found on second attempt, updating");
				updateCat(cat);
			}
		}
	}

	public void updateCat(TileEntityDrillHead cat){

	}
}
