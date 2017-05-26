package the_fireplace.caterpillar.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.Reference;
import the_fireplace.caterpillar.parts.PartsDecoration;
import the_fireplace.caterpillar.tileentity.TileEntityDrillHead;

public class BlockDecoration extends BlockDrillBase
{
	public BlockDecoration(){
		super();
		this.movementTicks = 25;
	}

	@Override
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state)
	{
		if (!worldIn.isRemote)
		{
			TileEntityDrillHead cater = Caterpillar.getCaterpillar(worldIn, pos, state.getValue(FACING));
			if (cater != null)
			{
				PartsDecoration thisSection = cater.decoration;
				thisSection.howclose = 0;
			}
		}
	}
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		if (!worldIn.isRemote)
		{
			TileEntityDrillHead cater = Caterpillar.getCaterpillar(worldIn, pos, state.getValue(FACING));
			if (cater != null)
			{
				PartsDecoration thisSection = cater.decoration;
				thisSection.howclose = 2;
				int[] movingXZ = Caterpillar.instance.getWayMoving(state);

				thisSection.countindex = this.getCountIndex(movingXZ, pos);
			}
		}
	}
	@Override
	protected void fired(World worldIn, BlockPos pos, IBlockState state, TileEntityDrillHead myCat, int[] movingXZ, int count)
	{
		if (myCat == null)
		{
			Reference.printDebug("Drill Head not found, but Block was fired.");
			return;
		}
		PartsDecoration thisSection = myCat.decoration;
		thisSection.howclose = 2;
		ItemStack[] whattoPlace = thisSection.placementMap.get(count);
		thisSection.countindex = count;
		int slot = 7;
		if (movingXZ[1] == -1 || movingXZ[0] == 1)
		{
			slot = 0;
		}
		for (int j = -1; j < 2; j++) {
			for (int i = -1; i < 2; i++) {
				if (i != 0 || j != 0)
				{
					int toporB = i;
					if (movingXZ[1] == -1 || movingXZ[0] == 1)
					{
						toporB = -1 * i;
					}
					//north and
					BlockPos loc = pos.add(j*Math.abs(movingXZ[1]) + movingXZ[0], toporB, j*Math.abs(movingXZ[0]) +  movingXZ[1]);
					ItemStack thisone = whattoPlace[slot];
					if (movingXZ[1] == -1 || movingXZ[0] == 1)
					{
						slot++;
					}
					else
					{
						slot--;
					}
					if (thisone != null)
					{
						Block toPlaceC = Block.getBlockFromItem(thisone.getItem());
						if (toPlaceC != null)
						{
							if (toPlaceC.getDefaultState() != null)
							{

								this.takeOutMatsandPlace(worldIn, loc, toPlaceC.getStateFromMeta(thisone.getItemDamage()));
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void updateCat(TileEntityDrillHead cat){
		cat.decoration.howclose = 2;
	}
}