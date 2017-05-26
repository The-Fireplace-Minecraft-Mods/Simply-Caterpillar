package the_fireplace.caterpillar.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import the_fireplace.caterpillar.Reference;
import the_fireplace.caterpillar.blocks.BlockDrillBase;
import the_fireplace.caterpillar.blocks.BlockDrillHeads;

public class TileEntityDrillPart extends TileEntity implements ITickable
{
	public TileEntityDrillPart()
	{
		Reference.printDebug("Initializing Caterpillar Part");
	}

	@Override
	public void update() {
		IBlockState blockdriller =  this.world.getBlockState(this.pos);

		if (blockdriller.getBlock() instanceof BlockDrillBase || blockdriller.getBlock() instanceof BlockDrillHeads)
		{
			((BlockDrillBase)blockdriller.getBlock()).calculateMovement(this.world, this.pos, this.world.getBlockState(this.pos));
		}
	}
}