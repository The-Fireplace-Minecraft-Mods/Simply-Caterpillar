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
	@Override
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state)
	{
		if (Reference.Loaded && !worldIn.isRemote) //!worldIn.isRemote &&
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
		if (Reference.Loaded && !worldIn.isRemote)
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
		ContainerCaterpillar thisGuy =  Caterpillar.instance.getContainerCaterpillar(catID);

		if (thisGuy != null)
		{
			PartsIncinerator thisSection = thisGuy.incinerator;
			thisSection.howclose = 2;
		}

		int Middleindex = (thisGuy.inventory.length - 2) / 2;
		Middleindex += 2;
		ItemStack[] thisGuyInv = thisGuy.inventory;

		for (int i = Middleindex; i < thisGuyInv.length; i++) {
			for (ItemStack element : thisGuy.incinerator.placementMap) {
				if (element != null)
				{
					if (thisGuyInv[i] != null)
					{
						if (thisGuyInv[i].getItem().equals(element.getItem()) && thisGuyInv[i].getItemDamage() == element.getItemDamage())
						{
							thisGuyInv[i] = null;
							break;
						}
					}
				}
			}
		}
	}
}