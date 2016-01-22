package com.gmail.cgfreethemice.caterpillar.handlers;


import com.gmail.cgfreethemice.caterpillar.Caterpillar;
import com.gmail.cgfreethemice.caterpillar.Reference;
import com.gmail.cgfreethemice.caterpillar.Caterpillar.GuiTabs;
import com.gmail.cgfreethemice.caterpillar.blocks.BlockDrillBase;
import com.gmail.cgfreethemice.caterpillar.containers.ContainerCaterpillar;
import com.gmail.cgfreethemice.caterpillar.containers.ContainerDrillHead;
import com.gmail.cgfreethemice.caterpillar.guis.GuiDrillHead;
import com.gmail.cgfreethemice.caterpillar.packets.PacketCaterpillarControls;
import com.gmail.cgfreethemice.caterpillar.tileentity.TileEntityDrillHead;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class HandlerGUI implements IGuiHandler
{
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
		if (tileEntity != null)
		{
			if (ID == Reference.GUI_ENUM.DRILLHEAD.ordinal())
			{

				ContainerCaterpillar conCat = Caterpillar.instance.getContainerCaterpillar(tileEntity.getPos());
				conCat.tabs.selected = GuiTabs.MAIN;
				TileEntityDrillHead tileEntityB = (TileEntityDrillHead) world.getTileEntity(conCat.pos);
				tileEntityB.isSelected = false;
				Caterpillar.network.sendTo(new PacketCaterpillarControls(conCat), (EntityPlayerMP) player);
				Reference.printDebug("Server GUI: " + conCat.name);
				return new ContainerDrillHead(player, tileEntityB, conCat);
			}
		}

		return null;
	}
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{

		BlockPos thisPOS = new BlockPos(x, y, z);

		IBlockState thisState =  Reference.theWorldClient().getBlockState(thisPOS);

		if (thisState.getBlock() instanceof BlockDrillBase)
		{
			int[] movingXZ = Caterpillar.instance.getWayMoving(thisState);
			String myID = Caterpillar.instance.getCatapillarID(movingXZ, thisPOS);

			TileEntityDrillHead TEDrill = (TileEntityDrillHead) world.getTileEntity(new BlockPos(x, y, z));
			TEDrill.isSelected = true;
			ContainerCaterpillar conCat =  Caterpillar.instance.getSelectedCaterpillar();
			Reference.printDebug("Client GUI: " + conCat.name);
			return new GuiDrillHead(player,  (TEDrill), conCat);
		}
		return null;
	}
}
