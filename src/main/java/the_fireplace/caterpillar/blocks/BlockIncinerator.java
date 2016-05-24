package the_fireplace.caterpillar.blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.Reference;
import the_fireplace.caterpillar.containers.ContainerCaterpillar;
import the_fireplace.caterpillar.parts.PartsIncinerator;

public class BlockIncinerator extends BlockDrillBase
{
	public BlockIncinerator(){
		super();
		this.movementTicks = 50;
	}

	@Override
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state)
	{
		if (Reference.loaded && !worldIn.isRemote)
		{
			ContainerCaterpillar cater = Caterpillar.instance.getContainerCaterpillar(pos, state);
			if (cater != null)
			{
				PartsIncinerator thisSection = cater.incinerator;
				thisSection.howclose = 0;
			}
		}
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		if (Reference.loaded && !worldIn.isRemote)
		{
			ContainerCaterpillar cater = Caterpillar.instance.getContainerCaterpillar(pos, worldIn);
			if (cater != null)
			{
				PartsIncinerator thisSection = cater.incinerator;
				thisSection.howclose = 2;
			}
		}
	}

	@Override
	protected void fired(World worldIn, BlockPos pos, IBlockState state, String catID, int[] movingXZ, int Count)
	{
		ContainerCaterpillar myCat =  Caterpillar.instance.getContainerCaterpillar(catID);

		if (myCat != null)
		{
			PartsIncinerator thisSection = myCat.incinerator;
			thisSection.howclose = 2;
		}

		int middleIndex = (myCat.inventory.length - 2) / 2;
		middleIndex += 2;
		ItemStack[] myCatInv = myCat.inventory;

		for (int i = middleIndex; i < myCatInv.length; i++) {
			for (ItemStack element : myCat.incinerator.placementMap) {
				if (element != null)
				{
					if (myCatInv[i] != null)
					{
						if (myCatInv[i].getItem().equals(element.getItem()) && myCatInv[i].getItemDamage() == element.getItemDamage())
						{
							myCatInv[i] = null;
							break;
						}
					}
				}
			}
		}
	}

	@Override
	public void updateCat(ContainerCaterpillar cat){
		cat.incinerator.howclose = 2;
	}
}