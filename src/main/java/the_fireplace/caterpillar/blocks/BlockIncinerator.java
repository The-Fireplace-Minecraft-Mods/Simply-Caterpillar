package the_fireplace.caterpillar.blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.parts.PartsIncinerator;
import the_fireplace.caterpillar.tileentity.TileEntityDrillHead;

public class BlockIncinerator extends BlockDrillBase
{
	public BlockIncinerator(){
		super();
		this.movementTicks = 50;
	}

	@Override
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state)
	{
		if (!worldIn.isRemote)
		{
			TileEntityDrillHead cater = Caterpillar.getCaterpillar(worldIn, pos, state.getValue(FACING));
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
		if (!worldIn.isRemote)
		{
			TileEntityDrillHead cater = Caterpillar.getCaterpillar(worldIn, pos, state.getValue(FACING));
			if (cater != null)
			{
				PartsIncinerator thisSection = cater.incinerator;
				thisSection.howclose = 2;
			}
		}
	}

	@Override
	protected void fired(World worldIn, BlockPos pos, IBlockState state, TileEntityDrillHead myCat, int[] movingXZ, int Count)
	{
		if (myCat != null)
		{
			PartsIncinerator thisSection = myCat.incinerator;
			thisSection.howclose = 2;

			for(ItemStack[] myCatInv : myCat.inventoryPages)
				for (int i = 1; i < myCatInv.length; i++) {
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
	}

	@Override
	public void updateCat(TileEntityDrillHead cat){
		cat.incinerator.howclose = 2;
	}
}