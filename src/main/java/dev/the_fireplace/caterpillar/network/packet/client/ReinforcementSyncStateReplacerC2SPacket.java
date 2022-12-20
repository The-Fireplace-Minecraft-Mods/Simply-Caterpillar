package dev.the_fireplace.caterpillar.network.packet.client;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import dev.the_fireplace.caterpillar.block.entity.ReinforcementBlockEntity;
import dev.the_fireplace.caterpillar.network.PacketHandler;
import dev.the_fireplace.caterpillar.network.packet.server.ReinforcementSyncStateReplacerS2CPacket;

import java.util.function.Supplier;

public class ReinforcementSyncStateReplacerC2SPacket {

    private final int replacerIndex;

    private final int replacementIndex;

    private final byte activated;

    private final BlockPos pos;

    public ReinforcementSyncStateReplacerC2SPacket(int replacerIndex, int replacementIndex, byte activated, BlockPos pos) {
        this.replacerIndex = replacerIndex;
        this.replacementIndex = replacementIndex;
        this.activated = activated;
        this.pos = pos;
    }

    public ReinforcementSyncStateReplacerC2SPacket(FriendlyByteBuf buf) {
        this.replacerIndex = buf.readInt();
        this.replacementIndex = buf.readInt();
        this.activated = buf.readByte();
        this.pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(replacerIndex);
        buf.writeInt(replacementIndex);
        buf.writeByte(activated);
        buf.writeBlockPos(pos);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            ServerLevel level = player.getLevel();

            if(level.getBlockEntity(pos) instanceof ReinforcementBlockEntity reinforcementBlockEntity) {
                reinforcementBlockEntity.getReplacers(this.replacerIndex)[this.replacementIndex] = this.activated;
                reinforcementBlockEntity.setChanged();

                PacketHandler.sendToClients(new ReinforcementSyncStateReplacerS2CPacket(this.replacerIndex, this.replacementIndex, this.activated, reinforcementBlockEntity.getBlockPos()));
            }
        });
        context.setPacketHandled(true);
    }
}
