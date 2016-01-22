package com.gmail.cgfreethemice.caterpillar.blocks;

import com.gmail.cgfreethemice.caterpillar.Caterpillar;
import com.gmail.cgfreethemice.caterpillar.Reference;
import com.gmail.cgfreethemice.caterpillar.containers.ContainerCaterpillar;

import net.minecraft.block.BlockAir;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.item.ItemBoat;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockStorage extends BlockDrillBase
{
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

	public BlockStorage()
	{
		super();

		this.DraggBurnTime = 75;
	}
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		if (Reference.Loaded == true && !worldIn.isRemote) //!worldIn.isRemote &&
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
				this.changeStorage(myCate,  24);
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
	@Override
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state)
	{
		if (Reference.Loaded == true) //!worldIn.isRemote &&
		{
			if (Caterpillar.instance.doesHaveCaterpillar(pos, state))
			{
				this.changeStorage(pos, state, -24);

				return;
			}

		}
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
	public void changeStorage(BlockPos pos, IBlockState state, int Amount) {
		ContainerCaterpillar myCate = Caterpillar.instance.getContainerCaterpillar(pos, state);
		this.changeStorage(myCate, Amount);
	}

	public void changeStorage(BlockPos pos, int Amount) {
		ContainerCaterpillar myCate = Caterpillar.instance.getContainerCaterpillar(pos);
		this.changeStorage(myCate, Amount);
	}
	public void changeStorage(ContainerCaterpillar myCate, int Amount) {
		
		myCate.changeStorage(Amount);

		ContainerCaterpillar myCateRemote = myCate.clone();

		Caterpillar.instance.putContainerCaterpillar(myCate);

		Caterpillar.instance.saveNBTDrills();

	}


}