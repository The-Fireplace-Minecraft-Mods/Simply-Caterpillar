package the_fireplace.caterpillar.blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.Reference;
import the_fireplace.caterpillar.containers.ContainerCaterpillar;

public class BlockStorage extends BlockDrillBase
{
	public void changeStorage(BlockPos pos, IBlockState state, int Amount, World objworld) {
		ContainerCaterpillar myCate = Caterpillar.instance.getContainerCaterpillar(pos, state);
		this.changeStorage(myCate, Amount, objworld);
	}

	public void changeStorage(BlockPos pos, int Amount, World objworld) {
		ContainerCaterpillar myCate = Caterpillar.instance.getContainerCaterpillar(pos, objworld);
		this.changeStorage(myCate, Amount, objworld);
	}

	public void changeStorage(ContainerCaterpillar myCate, int Amount, World objworld) {

		myCate.changeStorage(Amount, objworld);

		//ContainerCaterpillar myCateRemote = myCate.clone();

		Caterpillar.instance.putContainerCaterpillar(myCate, objworld);

		Caterpillar.instance.saveNBTDrills();

	}
	@Override
	protected void Fired(World worldIn, BlockPos pos, IBlockState state, String catID, int[] movingXZ, int Count)
	{
		ContainerCaterpillar thisCat = Caterpillar.instance.getContainerCaterpillar(catID);
		if (thisCat != null)
		{
			thisCat.storage.count += 24;
		}


	}

	@Override
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state)
	{
		if (!worldIn.isRemote && Reference.Loaded) //!worldIn.isRemote &&
		{
			if (Caterpillar.instance.doesHaveCaterpillar(pos, state))
			{
				this.changeStorage(pos, state, -24, worldIn);
			}
		}
	}
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		if (Reference.Loaded && !worldIn.isRemote) //!worldIn.isRemote &&
		{
			BlockPos whereItAt = new BlockPos(pos.getX(), pos.getY(), pos.getZ());
			if (!(worldIn.getBlockState(pos).getBlock() instanceof BlockDrillBase))
			{
				whereItAt = whereItAt.add(0, 1, 0);
			}

			state = state.withProperty(FACING, placer.getHorizontalFacing().getOpposite());
			if (Caterpillar.instance.doesHaveCaterpillar(whereItAt, state))
			{
				ContainerCaterpillar myCate = Caterpillar.instance.getContainerCaterpillar(whereItAt, state);
				this.changeStorage(myCate,  24, worldIn);
				return;
			}

			if (worldIn.getBlockState(pos).getBlock() instanceof BlockStorage)
			{
				worldIn.setBlockToAir(pos);
				Reference.dropBlockAsItem(worldIn, pos, pos, state, 0);
			}
			if (worldIn.getBlockState(pos.add(0, 1, 0)).getBlock() instanceof BlockStorage)
			{
				worldIn.setBlockToAir(pos.add(0, 1, 0));
				Reference.dropBlockAsItem(worldIn, pos, pos, state, 0);
			}
			//Reference.dropBlockAsItem(worldIn, pos, pos, state, 0);
		}
	}
}