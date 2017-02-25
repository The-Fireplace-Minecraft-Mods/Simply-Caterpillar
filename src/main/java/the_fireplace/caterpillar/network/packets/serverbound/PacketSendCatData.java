package the_fireplace.caterpillar.network.packets.serverbound;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.Reference;
import the_fireplace.caterpillar.containers.CaterpillarData;
import the_fireplace.caterpillar.network.packets.AbstractServerMessageHandler;

public class PacketSendCatData implements IMessage{

	public CaterpillarData remoteCaterpillar;
	public PacketSendCatData()
	{

	}
	public PacketSendCatData(CaterpillarData remoteCaterpillar)
	{
		this.remoteCaterpillar = remoteCaterpillar;
	}
	@Override
	public void fromBytes(ByteBuf buf) {

		this.remoteCaterpillar = CaterpillarData.readCaterpiller(ByteBufUtils.readTag(buf));
	}

	@Override
	public void toBytes(ByteBuf buf) {
		if (this.remoteCaterpillar != null)
		{
			ByteBufUtils.writeTag(buf, this.remoteCaterpillar.writeNBTCaterpillar());
		}

	}
	public static class Handler extends AbstractServerMessageHandler<PacketSendCatData> {

		@Override
		public IMessage handleServerMessage(EntityPlayer player, PacketSendCatData message, MessageContext ctx) {
			CaterpillarData cater =  message.remoteCaterpillar;
			boolean foundcat  =Caterpillar.instance.doesHaveCaterpillar(cater.name);
			Reference.printDebug("Packets(" + ctx.side.toString() +"): Received, " + cater.name + " found: " + foundcat);

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
			return null;
		}
	}
}
