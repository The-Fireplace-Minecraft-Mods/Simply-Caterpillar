package com.gmail.cgfreethemice.caterpillar.blocks;

import com.gmail.cgfreethemice.caterpillar.Caterpillar;

import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockDecoration extends BlockDrillBase
{
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	public BlockDecoration()
	{
		super();

		this.DraggBurnTime = 25;


	}
	@Override
	protected void Fired(World worldIn, BlockPos pos, IBlockState state, String catID, int[] movingXZ, int Count)
	{
		ItemStack[] whattoPlace = Caterpillar.instance.getContainerCaterpillar(catID).decoration.placementMap.get(Count);
		int slot = 7;

		for (int j = -1; j < 2; j++) {
			for (int i = -1; i < 2; i++) {
				if (i != 0 || j != 0)
				{
					BlockPos Wherepos = pos.add(j*Math.abs(movingXZ[1]) + movingXZ[0], i, j*Math.abs(movingXZ[0]) +  movingXZ[1]);
					ItemStack thisone = whattoPlace[slot];
					slot--;
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