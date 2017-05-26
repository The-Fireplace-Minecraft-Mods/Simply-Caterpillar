package the_fireplace.caterpillar.blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import the_fireplace.caterpillar.Reference;
import the_fireplace.caterpillar.tileentity.TileEntityDrillHead;

import java.util.ArrayList;
import java.util.List;

public class BlockCollector extends BlockDrillBase
{
	public BlockCollector(){
		super();
		this.movementTicks = 50;
	}

	@Override
	protected void fired(World worldIn, BlockPos pos, IBlockState state, TileEntityDrillHead myCat, int[] movingXZ, int Count)
	{
		List<Entity> entityList = new ArrayList<>();

		for (int i = 0; i < worldIn.loadedEntityList.size(); i++) {
			Entity entity = worldIn.loadedEntityList.get(i);
			if (entity instanceof EntityItem)
			{
				if (entity.getPosition().distanceSq(pos.getX(), pos.getY(), pos.getZ()) < 7*7)
				{
					entityList.add(entity);
				}
			}
		}

		entityList.stream().filter(entity -> entity instanceof EntityItem).forEach(ETObject -> {
			if(myCat != null) {
				if (myCat.addToRightHandSlots(((EntityItem) ETObject).getEntityItem())) {
					worldIn.removeEntity(ETObject);
				}
			}else{
				Reference.printDebug("Error: Null TE");
			}
		});
	}
}