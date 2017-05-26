package the_fireplace.caterpillar.network;


import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.Caterpillar.GuiTabs;
import the_fireplace.caterpillar.Reference;
import the_fireplace.caterpillar.blocks.BlockDrillBase;
import the_fireplace.caterpillar.containers.ContainerDrillHead;
import the_fireplace.caterpillar.guis.GuiDrillHead;
import the_fireplace.caterpillar.tileentity.TileEntityDrillHead;

public class GUIHandler implements IGuiHandler
{
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		BlockPos thisPOS = new BlockPos(x, y, z);

		IBlockState thisState =  world.getBlockState(thisPOS);

		if (thisState.getBlock() instanceof BlockDrillBase)
		{
			Caterpillar.instance.getWayMoving(thisState);

			TileEntityDrillHead te = (TileEntityDrillHead) world.getTileEntity(new BlockPos(x, y, z));
			te.isSelected = true;
			if (te != null)
			{
				Reference.printDebug("Client GUI: " + te.getName());
				return new GuiDrillHead(player, te);
			}else{
				Reference.printDebug("Error: TE not found on the Client.");
			}
		}
		return null;
	}
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
		if (tileEntity != null)
		{
			if (ID == 0)
			{
				if (tileEntity instanceof TileEntityDrillHead)
				{
					TileEntityDrillHead tileEntityB = (TileEntityDrillHead)tileEntity;
					tileEntityB.tabs.selected = GuiTabs.MAIN;
					tileEntityB.isSelected = false;
					//PacketDispatcher.sendTo(new PacketSendCatData(conCat), (EntityPlayerMP) player);
					//TODO: See what should happen here
					Reference.printDebug("Server GUI: " + tileEntityB.getName());
					return new ContainerDrillHead(player, tileEntityB);
				}
			}
		}

		return null;
	}
}
