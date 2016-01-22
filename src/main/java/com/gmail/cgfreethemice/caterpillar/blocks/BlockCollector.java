package com.gmail.cgfreethemice.caterpillar.blocks;

import java.util.ArrayList;
import java.util.List;

import com.gmail.cgfreethemice.caterpillar.Caterpillar;
import com.gmail.cgfreethemice.caterpillar.containers.ContainerCaterpillar;

import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockCollector extends BlockDrillBase
{
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

	public BlockCollector()
	{
		super();

		this.DraggBurnTime = 50;

	}
	@Override
	protected void Fired(World worldIn, BlockPos pos, IBlockState state, String catID, int[] movingXZ, int Count)
	{
		List<Entity> ETList = new ArrayList<Entity>();

		for (int i = 0; i < worldIn.loadedEntityList.size(); i++) {
			Entity ETObject = (Entity) worldIn.loadedEntityList.get(i);
			if (ETObject instanceof EntityItem)
			{
				if (ETObject.getPosition().distanceSq(pos.getX(), pos.getY(), pos.getZ()) < 7*7)
				{
					ETList.add(ETObject);
				}
			}
		}

		for (int i = 0; i < ETList.size(); i++) {
			Entity ETObject = ETList.get(i);
			if (ETObject instanceof EntityItem)
			{
				ContainerCaterpillar thisGuy =  Caterpillar.instance.getContainerCaterpillar(pos, state);
				if (thisGuy.addToOutInventory(((EntityItem)ETObject).getEntityItem()) == true)
				{
					worldIn.removeEntity(ETObject);
				}
			}
		}


	}

}