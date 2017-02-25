package the_fireplace.caterpillar.network.packets.clientbound;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.Reference;
import the_fireplace.caterpillar.containers.CaterpillarData;
import the_fireplace.caterpillar.network.packets.AbstractClientMessageHandler;

/**
 * @author The_Fireplace
 */
public class PacketIncrementClientInventory implements IMessage {
    public CaterpillarData remoteCaterpillar;
    public PacketIncrementClientInventory(){}
    public PacketIncrementClientInventory(CaterpillarData remoteCaterpillar)
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

    public static class Handler extends AbstractClientMessageHandler<PacketIncrementClientInventory> {

        @Override
        public IMessage handleClientMessage(EntityPlayer player, PacketIncrementClientInventory message, MessageContext ctx) {
            CaterpillarData cater =  message.remoteCaterpillar;
            boolean foundcat  = Caterpillar.instance.doesHaveCaterpillar(cater.name);
            Reference.printDebug("Packets(" + ctx.side.toString() +"): Received, " + cater.name + ", " + foundcat);

            if (Caterpillar.instance.getSelectedCaterpillar() != null)
            {
                if (Caterpillar.instance.getSelectedCaterpillar().myDrillHead != null)
                {
                    Caterpillar.instance.getSelectedCaterpillar().pageIndex=cater.pageIndex;
                }
            }

            return null;
        }
    }
}