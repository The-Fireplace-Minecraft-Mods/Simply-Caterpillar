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
import the_fireplace.caterpillar.network.packets.clientbound.PacketIncrementClientInventory;

/**
 * @author The_Fireplace
 */
public class PacketIncrementInventory implements IMessage{

    public CaterpillarData remoteCaterpillar;
    public int amount;
    public PacketIncrementInventory() {}
    public PacketIncrementInventory(CaterpillarData remoteCaterpillar, int incrementAmount)
    {
        this.remoteCaterpillar = remoteCaterpillar;
        this.amount = incrementAmount;
    }
    @Override
    public void fromBytes(ByteBuf buf) {
        this.amount = buf.readInt();
        this.remoteCaterpillar = CaterpillarData.readCaterpiller(ByteBufUtils.readTag(buf));
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(amount);
        if (this.remoteCaterpillar != null)
        {
            ByteBufUtils.writeTag(buf, this.remoteCaterpillar.writeNBTCaterpillar());
        }
    }
    public static class Handler extends AbstractServerMessageHandler<PacketIncrementInventory> {

        @Override
        public IMessage handleServerMessage(EntityPlayer player, PacketIncrementInventory message, MessageContext ctx) {
            CaterpillarData cater =  message.remoteCaterpillar;
            boolean foundcat  = Caterpillar.instance.doesHaveCaterpillar(cater.name);
            Reference.printDebug("Packets(" + ctx.side.toString() +"): Received, " + cater.name + " found: " + foundcat);

            CaterpillarData serverCaterpillarData = Caterpillar.instance.getContainerCaterpillar(cater.name);
            if (serverCaterpillarData != null)
            {
                if(serverCaterpillarData.pageIndex + message.amount >= serverCaterpillarData.inventoryPages.size())
                    serverCaterpillarData.pageIndex = serverCaterpillarData.inventoryPages.size()-1;
                else if(serverCaterpillarData.pageIndex + message.amount < 0)
                    serverCaterpillarData.pageIndex = 0;
                else
                    serverCaterpillarData.pageIndex += message.amount;
                Reference.printDebug("Server: Caterpillar Page Index set to "+serverCaterpillarData.pageIndex);
            }

            return new PacketIncrementClientInventory(serverCaterpillarData);//TODO: Ensure that this works fine
        }
    }
}