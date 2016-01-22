package com.gmail.cgfreethemice.caterpillar.handlers;

import com.gmail.cgfreethemice.caterpillar.Caterpillar;
import com.gmail.cgfreethemice.caterpillar.Reference;
import com.gmail.cgfreethemice.caterpillar.containers.ContainerCaterpillar;
import com.gmail.cgfreethemice.caterpillar.packets.PacketCaterpillarControls;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class HandlerPackets implements IMessageHandler<IMessage, IMessage>{

	@Override
	public IMessage onMessage(IMessage message, MessageContext ctx) {
		if (message instanceof PacketCaterpillarControls)
		{


			PacketCaterpillarControls PPCmessage = (PacketCaterpillarControls)message;
			ContainerCaterpillar cater =  PPCmessage.remoteCaterpillar;
			boolean foundcat  =Caterpillar.instance.doesHaveCaterpillar(cater.name);
			Reference.printDebug("Packets(" + ctx.side.toString() +"): Received, " + cater.name + ", " + foundcat);
			if (ctx.side.equals(Side.CLIENT))
			{
				if (Caterpillar.instance.getSelectedCaterpillar() != null)
				{
					if (Caterpillar.instance.getSelectedCaterpillar().myDrillHead != null)
					{
						Caterpillar.instance.getSelectedCaterpillar().myDrillHead.updateCaterpillar(cater);
					}
				}
				Caterpillar.instance.setSelectedCaterpillar(cater);

			}
			else
			{
				if (Caterpillar.instance.getContainerCaterpillar(cater.name) != null)
				{
					if (Caterpillar.instance.getContainerCaterpillar(cater.name).myDrillHead != null)
					{
						Caterpillar.instance.getContainerCaterpillar(cater.name).myDrillHead.updateCaterpillar(cater);
					}
				}
				Caterpillar.instance.putContainerCaterpillar(cater.name, cater);
				Caterpillar.instance.saveNBTDrills();
				Reference.printDebug("Server: Saving...");
			}

		}
		return null;
	}


}
