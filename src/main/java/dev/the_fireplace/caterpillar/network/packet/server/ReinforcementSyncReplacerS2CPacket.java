package dev.the_fireplace.caterpillar.network.packet.server;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import dev.the_fireplace.caterpillar.block.entity.ReinforcementBlockEntity;

import java.util.function.Supplier;

public class ReinforcementSyncReplacerS2CPacket {

    private final int replacerIndex;

    private final byte[] replacer;

    private final BlockPos pos;


    public ReinforcementSyncReplacerS2CPacket(int replacerIndex, byte[] replacer, BlockPos pos) {
        this.replacerIndex = replacerIndex;
        this.replacer = replacer;
        this.pos = pos;
    }

    public ReinforcementSyncReplacerS2CPacket(FriendlyByteBuf buf) {
        this.replacerIndex = buf.readInt();
        this.replacer = buf.readByteArray();
        this.pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.replacerIndex);
        buf.writeByteArray(replacer);
        buf.writeBlockPos(pos);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientLevel level = Minecraft.getInstance().level;

            if (level.getBlockEntity(pos) instanceof ReinforcementBlockEntity reinforcementBlockEntity) {
                reinforcementBlockEntity.replacers.set(replacerIndex, replacer);
                reinforcementBlockEntity.setChanged();
            }
        });
        context.setPacketHandled(true);
    }
}
