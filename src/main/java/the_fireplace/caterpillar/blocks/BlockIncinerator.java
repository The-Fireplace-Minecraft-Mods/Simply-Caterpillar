package the_fireplace.caterpillar.blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.Reference;
import the_fireplace.caterpillar.containers.CaterpillarData;
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
			CaterpillarData cater = Caterpillar.instance.getContainerCaterpillar(pos, state);
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
			CaterpillarData cater = Caterpillar.instance.getContainerCaterpillar(pos, worldIn);
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
		CaterpillarData myCat =  Caterpillar.instance.getContainerCaterpillar(catID);

		if (myCat != null)
		{
			PartsIncinerator thisSection = myCat.incinerator;
			thisSection.howclose = 2;
		}

		int middleIndex = (myCat.inventory.size() - 2) / 2;
		middleIndex += 2;
		NonNullList<ItemStack> myCatInv = myCat.inventory;

		for (int i = middleIndex; i < myCatInv.size(); i++) {
			for (ItemStack element : myCat.incinerator.placementMap) {
				if (element != null)
				{
					//TODO
					if (myCatInv.get(i) != null)
					{
						if (myCatInv.get(i).getItem().equals(element.getItem()) && myCatInv.get(i).getItemDamage() == element.getItemDamage())
						{
							//TODO
							myCatInv.get(i) = null;
							break;
						}
					}
				}
			}
		}
	}

	@Override
	public void updateCat(CaterpillarData cat){
		cat.incinerator.howclose = 2;
	}
}