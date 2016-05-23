package the_fireplace.caterpillar.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.Reference;
import the_fireplace.caterpillar.containers.ContainerCaterpillar;
import the_fireplace.caterpillar.parts.PartsDecoration;

public class BlockDecoration extends BlockDrillBase
{
	@Override
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state)
	{
		if (Reference.Loaded && !worldIn.isRemote) //!worldIn.isRemote &&
		{
			ContainerCaterpillar cater = Caterpillar.instance.getContainerCaterpillar(pos, state);
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
		if (Reference.Loaded && !worldIn.isRemote)
		{
			ContainerCaterpillar cater = Caterpillar.instance.getContainerCaterpillar(pos, worldIn);
			if (cater != null)
			{
				PartsDecoration thisSection = cater.decoration;
				thisSection.howclose = 2;
				int[] movingXZ = Caterpillar.instance.getWayMoving(state);

				int Count = this.getCountIndex(movingXZ, pos);
				thisSection.countindex = Count;
			}
		}
	}
	@Override
	protected void Fired(World worldIn, BlockPos pos, IBlockState state, String catID, int[] movingXZ, int Count)
	{
		ContainerCaterpillar mycaterpillar = Caterpillar.instance.getContainerCaterpillar(catID);
		if (mycaterpillar == null)
		{
			return;
		}
		PartsDecoration thisSection = mycaterpillar.decoration;
		thisSection.howclose = 2;
		ItemStack[] whattoPlace = thisSection.placementMap.get(Count);
		thisSection.countindex = Count;
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
					BlockPos Wherepos = pos.add(j*Math.abs(movingXZ[1]) + movingXZ[0], toporB, j*Math.abs(movingXZ[0]) +  movingXZ[1]);
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

								this.takeOutMatsandPlace(worldIn, catID, Wherepos, toPlaceC.getStateFromMeta(thisone.getItemDamage()));
							}
						}
					}
				}
			}
		}
	}
}