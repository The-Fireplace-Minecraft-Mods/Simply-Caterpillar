package the_fireplace.caterpillar.handlers;


import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.Caterpillar.GuiTabs;
import the_fireplace.caterpillar.Reference;
import the_fireplace.caterpillar.blocks.BlockDrillBase;
import the_fireplace.caterpillar.containers.ContainerCaterpillar;
import the_fireplace.caterpillar.containers.ContainerDrillHead;
import the_fireplace.caterpillar.guis.GuiDrillHead;
import the_fireplace.caterpillar.packets.PacketCaterpillarControls;
import the_fireplace.caterpillar.tileentity.TileEntityDrillComponent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class HandlerGUI implements IGuiHandler
{
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		BlockPos thisPOS = new BlockPos(x, y, z);

		IBlockState thisState =  world.getBlockState(thisPOS);

		if (thisState.getBlock() instanceof BlockDrillBase)
		{
			Caterpillar.instance.getWayMoving(thisState);

			TileEntityDrillComponent TEDrill = (TileEntityDrillComponent) world.getTileEntity(new BlockPos(x, y, z));
			TEDrill.isSelected = true;
			ContainerCaterpillar conCat =  Caterpillar.instance.getSelectedCaterpillar();
			if (conCat != null)
			{
				Reference.printDebug("Client GUI: " + conCat.name);
				return new GuiDrillHead(player,  (TEDrill), conCat);
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
				ContainerCaterpillar conCat = Caterpillar.instance.getContainerCaterpillar(tileEntity.getPos(), world);
				if (conCat != null)
				{
					conCat.tabs.selected = GuiTabs.MAIN;
					TileEntityDrillComponent tileEntityB = (TileEntityDrillComponent) world.getTileEntity(conCat.pos);
					tileEntityB.isSelected = false;
					Caterpillar.network.sendTo(new PacketCaterpillarControls(conCat), (EntityPlayerMP) player);
					Reference.printDebug("Server GUI: " + conCat.name);
					return new ContainerDrillHead(player, tileEntityB, conCat);
				}
			}
		}

		return null;
	}
}
