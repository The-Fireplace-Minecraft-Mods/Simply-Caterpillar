package the_fireplace.caterpillar.blocks;

import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.Config;
import the_fireplace.caterpillar.Reference;
import the_fireplace.caterpillar.inits.InitBlocks;
import the_fireplace.caterpillar.network.PacketDispatcher;
import the_fireplace.caterpillar.network.packets.clientbound.PacketParticles;
import the_fireplace.caterpillar.tileentity.TileEntityDrillHead;

import javax.annotation.Nonnull;
import java.util.Random;

public class BlockDrillHeads extends BlockDrillBase
{
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

	public BlockDrillHeads(){
		super();
		this.movementTicks = 50;
	}

	@Override
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state)
	{
		if (!worldIn.isRemote)
		{
			int[] movingXZ = Caterpillar.instance.getWayMoving(state);

			for (int i = -1; i < 2; i++) {
				for (int j = -1; j < 2; j++) {
					BlockPos loc = pos.add(j*Math.abs(movingXZ[1]) + movingXZ[0], i, j*Math.abs(movingXZ[0])+  movingXZ[1]);
					if (worldIn.getBlockState(loc).getBlock().equals((InitBlocks.drill_blades)))
					{
						worldIn.setBlockToAir(loc);
					}
				}
			}

			TileEntityDrillHead te = Caterpillar.getCaterpillar(worldIn, pos, state.getValue(FACING));
			if (te != null)
			{
				for(ItemStack[] inventory : te.inventoryPages)
				for (ItemStack element : inventory) {
					if (element != null)
					{
						Reference.dropItem(worldIn, pos, element);
					}
				}
			}
		}
	}

	@Override
	public void calculateMovement(World worldIn, BlockPos pos, IBlockState state)
	{
		if (!worldIn.isRemote)
		{
			int[] movingXZ = Caterpillar.instance.getWayMoving(state);
			TileEntityDrillHead te = Caterpillar.getCaterpillar(worldIn, pos, state.getValue(FACING));
			if (te == null)
			{
				Reference.printDebug("Error: Null TE");
				return;
			}
			BlockDrillHeads basedrillhead = (BlockDrillHeads)InitBlocks.drillheads;

			te.headTick++;
			te.movement.total = this.movementTicks + te.movement.value;
			if (te.headTick > movementTicks){
				this.addMoreFuel(te);

				if (te.burntime < 1 || !te.running)
				{
					return;
				}

				te.storage.storageComponentCount=0;//fixStorage

				if (te.decoration.howclose > 0)
				{
					te.decoration.howclose--;
				}
				if (te.reinforcement.howclose > 0)
				{
					te.reinforcement.howclose--;
				}
				if (te.incinerator.howclose > 0)
				{
					te.incinerator.howclose--;
				}
				te.headTick = 0;
				te.burntime -= te.movement.total;
				te.movement.value = 0;

				boolean ret=false;

				for (int i = -1; i < 2; i++) {
					for (int j = -1; j < 2; j++) {
						BlockPos destroyPos = pos.add(j*Math.abs(movingXZ[1]) + movingXZ[0], i, j*Math.abs(movingXZ[0])+  movingXZ[1]);
						if (worldIn.getBlockState(destroyPos).getBlock().equals(InitBlocks.drill_blades))
						{
							worldIn.setBlockToAir(destroyPos);
						}else if(worldIn.getBlockState(destroyPos).getBlock().equals(Blocks.BEDROCK)){
							if(Config.breakbedrock) {
								worldIn.setBlockToAir(destroyPos);
							} else{
								te.running=false;
								if(!ret) {
									ret=true;
								}
							}
						}
						else if(worldIn.getBlockState(destroyPos).getBlock().equals(Blocks.BARRIER)){
							te.running=false;
							if(!ret) {
								ret=true;
							}
						}
						else
						{
							worldIn.destroyBlock(destroyPos, true);
						}
					}
				}
				if(ret) {
					return;
				}

				BlockPos newPlace = pos.add(movingXZ[0], 0, movingXZ[1]);
				TargetPoint targetPoint = new TargetPoint(worldIn.getWorldType().getWorldTypeID(), newPlace.getX(), newPlace.getY(), newPlace.getZ(), 5);
				PacketDispatcher.sendToAllAround(new PacketParticles(EnumParticleTypes.FLAME.name(), newPlace.getX(), newPlace.getY(), newPlace.getZ()), targetPoint);

				worldIn.setBlockState(newPlace, basedrillhead.getDefaultState().withProperty(FACING, state.getValue(FACING)));

				te.updatePos(newPlace);

				worldIn.setBlockToAir(pos);
				if(Config.enablesounds) {
					worldIn.playSound(null, pos, SoundEvents.BLOCK_PISTON_EXTEND, SoundCategory.BLOCKS, 0.5F, worldIn.rand.nextFloat() * 0.25F + 0.6F);
				}

				for (int i = -1; i < 2; i++) {
					for (int j = -1; j < 2; j++) {
						BlockPos loc = newPlace.add(j*Math.abs(movingXZ[1]) + movingXZ[0], i, j*Math.abs(movingXZ[0])+  movingXZ[1]);

						if (worldIn.getBlockState(loc).getBlock().equals(Blocks.FLOWING_LAVA) ||
								worldIn.getBlockState(loc).getBlock().equals(Blocks.FLOWING_WATER) ||
								worldIn.getBlockState(loc).equals(Blocks.SAND.getDefaultState()) ||
								worldIn.getBlockState(loc).equals(Blocks.GRAVEL.getDefaultState()) ||
								worldIn.getBlockState(loc).equals(Blocks.LAVA.getDefaultState()) ||
								worldIn.getBlockState(loc).equals(Blocks.WATER.getDefaultState()) ||
								worldIn.getBlockState(loc).equals(Blocks.AIR.getDefaultState())
								)
						{
							worldIn.setBlockState(loc, InitBlocks.drill_blades.getDefaultState());
						}
					}
				}
			}
		}
	}

	/**
	 * Get the Item that this Block should drop when harvested.
	 * 
	 * @param fortune the level of the Fortune enchantment on the player's tool
	 */
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return Item.getItemFromBlock(InitBlocks.drillheads);
	}

	/**
	 * Get the damage value that this Block should drop
	 */
	@Override
	public int damageDropped(IBlockState state)
	{
		return 0;
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{

	}
	@Override
	@Nonnull
	public TileEntity createNewTileEntity(@Nonnull World worldIn, int meta)
	{
		return new TileEntityDrillHead();
	}

	private void addMoreFuel(@Nonnull TileEntityDrillHead te) {
		if (te.burntime < 1)
		{
			//thisCat.burntime = 0;

			if (TileEntityFurnace.isItemFuel(te.fuelSlotStack))
			{
				ItemStack justOne = new ItemStack(te.fuelSlotStack.getItem(), 1, te.fuelSlotStack.getItemDamage());
				ItemStack theRest = null;
				if (te.fuelSlotStack.stackSize > 1)
				{
					theRest = new ItemStack(te.fuelSlotStack.getItem(), te.fuelSlotStack.stackSize - 1, te.fuelSlotStack.getItemDamage());
				}
				te.fuelSlotStack = theRest;
				te.maxburntime = TileEntityFurnace.getItemBurnTime(justOne);
				te.burntime += te.maxburntime;
			}
		}
	}
}