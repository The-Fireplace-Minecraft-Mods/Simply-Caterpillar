package the_fireplace.caterpillar.network.packets.serverbound;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.Reference;
import the_fireplace.caterpillar.network.packets.AbstractServerMessageHandler;
import the_fireplace.caterpillar.network.packets.clientbound.PacketIncrementClientInventory;
import the_fireplace.caterpillar.tileentity.TileEntityDrillHead;

/**
 * @author The_Fireplace
 */
public class PacketIncrementInventory implements IMessage{

    public BlockPos remoteCaterpillar;
    public int amount;
    public PacketIncrementInventory() {}
    public PacketIncrementInventory(BlockPos remoteCaterpillar, int incrementAmount)
    {
        this.remoteCaterpillar = remoteCaterpillar;
        this.amount = incrementAmount;
    }
    @Override
    public void fromBytes(ByteBuf buf) {
        this.amount = buf.readInt();
        this.remoteCaterpillar = new BlockPos(buf.readInt(), buf.readShort(), buf.readInt());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(amount);
        buf.writeInt(remoteCaterpillar.getX());
        buf.writeShort(remoteCaterpillar.getY());
        buf.writeInt(remoteCaterpillar.getZ());
    }
    public static class Handler extends AbstractServerMessageHandler<PacketIncrementInventory> {

        @Override
        public IMessage handleServerMessage(EntityPlayer player, PacketIncrementInventory message, MessageContext ctx) {
            TileEntityDrillHead cater = Caterpillar.getCaterpillar(player.world, message.remoteCaterpillar);
            boolean foundcat = cater != null;
            Reference.printDebug("Packets(" + ctx.side.toString() +"): Received, " + cater + " found: " + foundcat);

            if (cater != null)
            {
                if(cater.pageIndex + message.amount >= cater.inventoryPages.size())
                    cater.pageIndex = cater.inventoryPages.size()-1;
                else if(cater.pageIndex + message.amount < 0)
                    cater.pageIndex = 0;
                else
                    cater.pageIndex += message.amount;
                Reference.printDebug("Server: Caterpillar Page Index set to "+cater.pageIndex);
                return new PacketIncrementClientInventory(cater.getPos(), cater.pageIndex);//TODO: Ensure that this works fine
            }

            return null;
        }
    }
}