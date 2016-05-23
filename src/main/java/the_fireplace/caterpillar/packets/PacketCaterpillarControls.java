package the_fireplace.caterpillar.packets;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.Reference;
import the_fireplace.caterpillar.containers.ContainerCaterpillar;

public class PacketCaterpillarControls implements IMessage{

	public ContainerCaterpillar remoteCaterpillar;
	public PacketCaterpillarControls()
	{

	}
	public PacketCaterpillarControls(ContainerCaterpillar remoteCaterpillar)
	{
		this.remoteCaterpillar = remoteCaterpillar;
	}
	@Override
	public void fromBytes(ByteBuf buf) {

		this.remoteCaterpillar = ContainerCaterpillar.readCaterpiller(ByteBufUtils.readTag(buf));
	}

	@Override
	public void toBytes(ByteBuf buf) {
		if (this.remoteCaterpillar != null)
		{
			ByteBufUtils.writeTag(buf, this.remoteCaterpillar.writeNBTCaterpillar());
		}

	}
	public static class Handler implements IMessageHandler<PacketCaterpillarControls, IMessage>{

		@Override
		public IMessage onMessage(PacketCaterpillarControls message, MessageContext ctx) {
			if (message instanceof PacketCaterpillarControls)
			{
				PacketCaterpillarControls PPCmessage = message;
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
}
