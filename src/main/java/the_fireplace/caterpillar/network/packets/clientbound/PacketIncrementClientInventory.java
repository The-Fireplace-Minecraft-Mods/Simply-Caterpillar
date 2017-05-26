package the_fireplace.caterpillar.network.packets.clientbound;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.Reference;
import the_fireplace.caterpillar.network.packets.AbstractClientMessageHandler;
import the_fireplace.caterpillar.tileentity.TileEntityDrillHead;

/**
 * @author The_Fireplace
 */
public class PacketIncrementClientInventory implements IMessage {
    public BlockPos remoteCaterpillar;
    public int pageIndex;
    public PacketIncrementClientInventory(){}
    public PacketIncrementClientInventory(BlockPos remoteCaterpillar, int pageIndex)
    {
        this.remoteCaterpillar = remoteCaterpillar;
        this.pageIndex =pageIndex;
    }
    @Override
    public void fromBytes(ByteBuf buf) {
        this.pageIndex = buf.readInt();
        this.remoteCaterpillar = new BlockPos(buf.readInt(), buf.readShort(), buf.readInt());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(pageIndex);
        buf.writeInt(remoteCaterpillar.getX());
        buf.writeShort(remoteCaterpillar.getY());
        buf.writeInt(remoteCaterpillar.getZ());
    }

    public static class Handler extends AbstractClientMessageHandler<PacketIncrementClientInventory> {

        @Override
        public IMessage handleClientMessage(EntityPlayer player, PacketIncrementClientInventory message, MessageContext ctx) {
            TileEntityDrillHead cater = Caterpillar.getCaterpillar(player.world, message.remoteCaterpillar);
            boolean foundcat = cater != null;
            Reference.printDebug("Packets(" + ctx.side.toString() +"): Received, " + cater + " found: " + foundcat);

            if (cater != null)
            {
                cater.pageIndex = message.pageIndex;
                Reference.printDebug("Client: Caterpillar Page Index set to "+cater.pageIndex);
            }

            return null;
        }
    }
}