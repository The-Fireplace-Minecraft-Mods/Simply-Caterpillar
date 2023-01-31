package dev.the_fireplace.caterpillar.network.packet.server;

import dev.the_fireplace.caterpillar.block.entity.ReinforcementBlockEntity;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;

public class ReinforcementSyncReplacerS2CPacket {

    public static void receive(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender responseSender) {
        ClientLevel level = client.level;

        int replacerIndex = buf.readInt();
        byte[] replacer = buf.readByteArray();
        BlockPos pos = buf.readBlockPos();

        if (level.getBlockEntity(pos) instanceof ReinforcementBlockEntity reinforcementBlockEntity) {
            reinforcementBlockEntity.replacers.set(replacerIndex, replacer);
            reinforcementBlockEntity.setChanged();
        }
    }
}
