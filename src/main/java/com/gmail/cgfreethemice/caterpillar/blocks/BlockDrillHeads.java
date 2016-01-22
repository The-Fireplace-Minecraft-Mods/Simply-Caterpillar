package com.gmail.cgfreethemice.caterpillar.blocks;

import java.util.Random;

import com.gmail.cgfreethemice.caterpillar.Caterpillar;
import com.gmail.cgfreethemice.caterpillar.Config;
import com.gmail.cgfreethemice.caterpillar.Reference;
import com.gmail.cgfreethemice.caterpillar.containers.ContainerCaterpillar;
import com.gmail.cgfreethemice.caterpillar.guis.GuiDrillHead;
import com.gmail.cgfreethemice.caterpillar.tileentity.TileEntityDrillHead;

import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class BlockDrillHeads extends BlockDrillBase
{
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	public BlockDrillHeads()

	{
		super();

		this.DraggBurnTime = 50;
	}
	@Override
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state)
	{
		if (Reference.Loaded == true && !worldIn.isRemote) //!worldIn.isRemote &&
		{

			int[] movingXZ = Caterpillar.instance.getWayMoving(state);
			String catID = Caterpillar.instance.getCatapillarID(movingXZ, pos);

			// this destroys the barrier blocks, placed by the drill head
			for (int i = -1; i < 2; i++) {
				for (int j = -1; j < 2; j++) {
					BlockPos Wherepos = pos.add(j*Math.abs(movingXZ[1]) + movingXZ[0], i, j*Math.abs(movingXZ[0])+  movingXZ[1]);
					if (worldIn.getBlockState(Wherepos).getBlock().equals((Blocks.barrier)))
					{
						worldIn.setBlockToAir(Wherepos);
					}
				}
			}
			//

			if (Caterpillar.instance.doesHaveCaterpillar(catID))
			{
				ContainerCaterpillar conCata = Caterpillar.instance.getContainerCaterpillar(catID);
				for (ItemStack element : conCata.inventory) {

					if (element != null)
					{
						Reference.dropItem(worldIn, pos, element);
					}
				}

				/*for (conCata.decoration.selected = 0; conCata.decoration.selected < 10; conCata.decoration.selected++) {
					for (ItemStack element : conCata.decoration.getSelectedInventory()) {

						if (element != null)
						{
							Reference.dropItem(worldIn, pos, element);
						}
					}
				}
				 */
				Caterpillar.instance.removeCaterpillar(catID);
			}
		}
	}

	@Override
	public void movieMe(World worldIn, BlockPos pos, IBlockState state)
	{
		try {
			if (Reference.Loaded == true && !worldIn.isRemote)// !worldIn.isRemote &&
			{
				TileEntityDrillHead heretoSet = null;
				int[] movingXZ = Caterpillar.instance.getWayMoving(state);
				String catID = Caterpillar.instance.getCatapillarID(movingXZ, pos);
				if (!Caterpillar.instance.doesHaveCaterpillar(catID))
				{
					String catapillar = Caterpillar.instance.getCatapillarID(movingXZ, pos);
					Caterpillar.instance.putContainerCaterpillar(catapillar,  new ContainerCaterpillar(pos, catapillar));
					heretoSet = (TileEntityDrillHead) worldIn.getTileEntity(pos);
				}
				ContainerCaterpillar thisCat = Caterpillar.instance.getContainerCaterpillar(catID);

				thisCat.drag.max = 250;
				thisCat.headTick++;
				thisCat.drag.total = this.DraggBurnTime + thisCat.drag.value;
				if (thisCat.headTick > 50)
				{

					this.addMoreFuel(catID, worldIn.isRemote);

					if (!Caterpillar.proxy.isServer())
					{
						if (Minecraft.getMinecraft().currentScreen instanceof GuiDrillHead)
						{
							return;
						}
					}
					if (thisCat.burntime < 1 || thisCat.running == false)
					{
						return;
					}

					this.fixStorage(pos, thisCat, worldIn.isRemote);


					thisCat.headTick = 0;
					thisCat.burntime = thisCat.burntime - thisCat.drag.total;
					Reference.printDebug(thisCat.burntime + "," + thisCat.drag.total);
					thisCat.drag.value = 0;

					boolean ret=false;

					for (int i = -1; i < 2; i++) {
						for (int j = -1; j < 2; j++) {
							BlockPos destryPos = pos.add(j*Math.abs(movingXZ[1]) + movingXZ[0], i, j*Math.abs(movingXZ[0])+  movingXZ[1]);
							if (worldIn.getBlockState(destryPos).getBlock().equals(Blocks.barrier))
							{
								worldIn.setBlockToAir(destryPos);
							}else if(worldIn.getBlockState(destryPos).getBlock().equals(Blocks.bedrock)){
								if(Config.breakbedrock)
									worldIn.setBlockToAir(destryPos);
								else{
									thisCat.running=false;
									if(!ret)
										ret=true;
								}
							}
							else
							{
								worldIn.destroyBlock(destryPos, true);
							}
						}
					}
					if(ret)
						return;
					BlockPos newPlace = pos.add(movingXZ[0], 0, movingXZ[1]);

					Reference.spawnParticles(newPlace, EnumParticleTypes.FLAME);

					worldIn.setBlockState(newPlace, state);
					thisCat.updatePos(newPlace);

					worldIn.setBlockToAir(pos);
					if(Config.enablesounds)
						worldIn.playSoundEffect(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, "tile.piston.out", 0.5F, worldIn.rand.nextFloat() * 0.25F + 0.6F);

					for (int i = -1; i < 2; i++) {
						for (int j = -1; j < 2; j++) {
							BlockPos Wherepos = newPlace.add(j*Math.abs(movingXZ[1]) + movingXZ[0], i, j*Math.abs(movingXZ[0])+  movingXZ[1]);

							if (worldIn.getBlockState(Wherepos).getBlock().equals(Blocks.flowing_lava) ||
									worldIn.getBlockState(Wherepos).getBlock().equals(Blocks.flowing_water) ||
									worldIn.getBlockState(Wherepos).equals(Blocks.sand.getDefaultState()) ||
									worldIn.getBlockState(Wherepos).equals(Blocks.gravel.getDefaultState()) ||
									worldIn.getBlockState(Wherepos).equals(Blocks.lava.getDefaultState()) ||
									worldIn.getBlockState(Wherepos).equals(Blocks.water.getDefaultState()) ||
									worldIn.getBlockState(Wherepos).equals(Blocks.air.getDefaultState())
									)
							{
								worldIn.setBlockState(Wherepos, Blocks.barrier.getDefaultState());
							}
						}
					}


				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void fixStorage(BlockPos pos, ContainerCaterpillar thisCat, boolean isRemote) {
		/*//has to move once to currect storage
		if (thisCat.storage.count != thisCat.storage.added)
		{
			int changeAmount = thisCat.storage.count - thisCat.storage.added;
			Reference.printDebug("Storage Mix Up: " +  thisCat.storage.count + ", " + thisCat.storage.added + ", " + changeAmount);
			((BlockStorage)InitBlocks.storage).changeStorage(pos, changeAmount);
			Caterpillar.instance.saveCount = 6001;
		}
		 */
		thisCat.storage.count = 0;
	}
	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{

	}
	private void addMoreFuel(String catID, boolean isRemote) {
		ContainerCaterpillar thisCat = Caterpillar.instance.getContainerCaterpillar(catID);
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

					return;
				}

			}
		}

	}

}