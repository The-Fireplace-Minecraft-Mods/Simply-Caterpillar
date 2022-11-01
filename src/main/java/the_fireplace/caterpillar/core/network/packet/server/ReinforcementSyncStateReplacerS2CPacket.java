package the_fireplace.caterpillar.core.network.packet.server;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import the_fireplace.caterpillar.common.block.entity.ReinforcementBlockEntity;

import java.util.function.Supplier;

public class ReinforcementSyncStateReplacerS2CPacket {

    private final int replacerIndex;

    private final int replacementIndex;

    private final byte enabled;

    private final BlockPos pos;

    public ReinforcementSyncStateReplacerS2CPacket(int replacerIndex, int replacementIndex, byte enabled, BlockPos pos) {
        this.replacerIndex = replacerIndex;
        this.replacementIndex = replacementIndex;
        this.enabled = enabled;
        this.pos = pos;
    }

    public ReinforcementSyncStateReplacerS2CPacket(FriendlyByteBuf buf) {
        this.replacerIndex = buf.readInt();
        this.replacementIndex = buf.readInt();
        this.enabled = buf.readByte();
        this.pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(replacerIndex);
        buf.writeInt(replacementIndex);
        buf.writeByte(enabled);
        buf.writeBlockPos(pos);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientLevel level = Minecraft.getInstance().level;

            if(level.getBlockEntity(pos) instanceof ReinforcementBlockEntity reinforcementBlockEntity) {
                reinforcementBlockEntity.getReplacers(this.replacerIndex)[this.replacementIndex] = this.enabled;
                reinforcementBlockEntity.setChanged();
            }
        });
        context.setPacketHandled(true);
    }
}
