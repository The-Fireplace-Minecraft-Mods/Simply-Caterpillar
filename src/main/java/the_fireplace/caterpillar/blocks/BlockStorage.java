package the_fireplace.caterpillar.blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.Reference;
import the_fireplace.caterpillar.tileentity.TileEntityDrillHead;

public class BlockStorage extends BlockDrillBase
{
	public BlockStorage(){
		super();
		this.movementTicks = 50;
	}

	public void changeStorage(TileEntityDrillHead myCate, int amount, World objworld) {
		myCate.changeStorage(amount, objworld);
	}
	@Override
	protected void fired(World worldIn, BlockPos pos, IBlockState state, TileEntityDrillHead thisCat, int[] movingXZ, int Count)
	{
		if (thisCat != null)
		{
			thisCat.storage.storageComponentCount += 1;
		}
	}

	@Override
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state)
	{
		if (!worldIn.isRemote)
		{
			TileEntityDrillHead myCat = Caterpillar.getCaterpillar(worldIn, pos, state.getValue(FACING));
			if (myCat != null)
			{
				myCat.changeStorage(-1, worldIn);
			}
		}
	}
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		if (!worldIn.isRemote)
		{
			BlockPos whereItAt = new BlockPos(pos.getX(), pos.getY(), pos.getZ());
			if (!(worldIn.getBlockState(pos).getBlock() instanceof BlockDrillBase))
			{
				whereItAt = whereItAt.up();
			}

			state = state.withProperty(FACING, placer.getHorizontalFacing().getOpposite());
			TileEntityDrillHead myCate = Caterpillar.getCaterpillar(worldIn, whereItAt, state.getValue(FACING));
			if (myCate != null)
			{
				this.changeStorage(myCate,  1, worldIn);
				return;
			}

			if (worldIn.getBlockState(pos).getBlock() instanceof BlockStorage)
			{
				worldIn.setBlockToAir(pos);
				Reference.dropBlockAsItem(worldIn, pos, pos, state, 0);
			}
			if (worldIn.getBlockState(pos.up()).getBlock() instanceof BlockStorage)
			{
				worldIn.setBlockToAir(pos.up());
				Reference.dropBlockAsItem(worldIn, pos, pos, state, 0);
			}
			//Reference.dropBlockAsItem(worldIn, pos, pos, state, 0);
		}
	}
}