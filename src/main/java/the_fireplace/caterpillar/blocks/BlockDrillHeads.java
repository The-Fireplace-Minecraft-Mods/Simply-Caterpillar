package the_fireplace.caterpillar.blocks;

import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
import the_fireplace.caterpillar.containers.CaterpillarData;
import the_fireplace.caterpillar.guis.GuiDrillHead;
import the_fireplace.caterpillar.inits.InitBlocks;
import the_fireplace.caterpillar.network.PacketDispatcher;
import the_fireplace.caterpillar.network.PacketParticles;

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
		if (Reference.loaded && !worldIn.isRemote)
		{
			int[] movingXZ = Caterpillar.instance.getWayMoving(state);
			String catID = Caterpillar.instance.getCaterpillarID(movingXZ, pos);

			for (int i = -1; i < 2; i++) {
				for (int j = -1; j < 2; j++) {
					BlockPos loc = pos.add(j*Math.abs(movingXZ[1]) + movingXZ[0], i, j*Math.abs(movingXZ[0])+  movingXZ[1]);
					if (worldIn.getBlockState(loc).getBlock().equals((InitBlocks.drill_blades)))
					{
						worldIn.setBlockToAir(loc);
					}
				}
			}

			if (Caterpillar.instance.doesHaveCaterpillar(catID))
			{
				CaterpillarData conCata = Caterpillar.instance.getContainerCaterpillar(catID);
				for (ItemStack element : conCata.inventory) {

					if (element != null)
					{
						Reference.dropItem(worldIn, pos, element);
					}
				}

				Caterpillar.instance.removeCaterpillar(catID);
			}
		}
	}

	@Override
	public void calculateMovement(World worldIn, BlockPos pos, IBlockState state)
	{
		if (Reference.loaded && !worldIn.isRemote)
		{
			int[] movingXZ = Caterpillar.instance.getWayMoving(state);
			String catID = Caterpillar.instance.getCaterpillarID(movingXZ, pos);
			if (!Caterpillar.instance.doesHaveCaterpillar(catID))
			{
				String catId = Caterpillar.instance.getCaterpillarID(movingXZ, pos);
				Caterpillar.instance.putContainerCaterpillar(catId,  new CaterpillarData(pos, catId));
			}
			CaterpillarData thisCat = Caterpillar.instance.getContainerCaterpillar(catID);

			BlockDrillHeads basedrillhead = (BlockDrillHeads)InitBlocks.drillheads;

			if (!Caterpillar.proxy.isServerSide())
			{
				if (Minecraft.getMinecraft().currentScreen instanceof GuiDrillHead)
				{
					return;
				}
			}
			thisCat.headTick++;
			thisCat.movement.total = this.movementTicks + thisCat.movement.value;
			if (thisCat.headTick > movementTicks){
				this.addMoreFuel(catID);

				if (thisCat.burntime < 1 || !thisCat.running)
				{
					return;
				}

				this.fixStorage(pos, thisCat);

				if (thisCat.decoration.howclose > 0)
				{
					thisCat.decoration.howclose--;
				}
				if (thisCat.reinforcement.howclose > 0)
				{
					thisCat.reinforcement.howclose--;
				}
				if (thisCat.incinerator.howclose > 0)
				{
					thisCat.incinerator.howclose--;
				}
				thisCat.headTick = 0;
				thisCat.burntime = thisCat.burntime - thisCat.movement.total;
				thisCat.movement.value = 0;

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
								thisCat.running=false;
								if(!ret) {
									ret=true;
								}
							}
						}
						else if(worldIn.getBlockState(destroyPos).getBlock().equals(Blocks.BARRIER)){
							thisCat.running=false;
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

				thisCat.updatePos(newPlace);

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

	private void fixStorage(BlockPos pos, CaterpillarData thisCat) {
		thisCat.storage.count = 0;
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{

	}

	private void addMoreFuel(String catID) {
		CaterpillarData thisCat = Caterpillar.instance.getContainerCaterpillar(catID);
		if (thisCat != null)
		{
			if (thisCat.burntime < 1)
			{
				//thisCat.burntime = 0;
				ItemStack[] thisGuyInv = thisCat.inventory;
				int i = 0;

				if (TileEntityFurnace.isItemFuel(thisGuyInv[i]))
				{
					ItemStack justOne = new ItemStack(thisGuyInv[i].getItem(), 1, thisGuyInv[i].getItemDamage());
					ItemStack theRest = null;
					if ( thisGuyInv[i].stackSize > 1)
					{
						theRest = new ItemStack(thisGuyInv[i].getItem(), thisGuyInv[i].stackSize - 1, thisGuyInv[i].getItemDamage());
					}
					thisGuyInv[i] = theRest;
					thisCat.maxburntime = TileEntityFurnace.getItemBurnTime(justOne);
					thisCat.burntime += thisCat.maxburntime;
				}
			}
		}
	}
}