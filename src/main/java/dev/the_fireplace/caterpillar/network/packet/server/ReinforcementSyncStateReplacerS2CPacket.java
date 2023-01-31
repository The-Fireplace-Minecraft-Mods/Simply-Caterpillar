package dev.the_fireplace.caterpillar.network.packet.server;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import dev.the_fireplace.caterpillar.block.entity.ReinforcementBlockEntity;

public class ReinforcementSyncStateReplacerS2CPacket {

    public static void receive(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender responseSender) {
        ClientLevel level = client.level;

        int replacerIndex = buf.readInt();
        int replacementIndex = buf.readInt();
        byte enabled = buf.readByte();
        BlockPos pos = buf.readBlockPos();

        if (level.getBlockEntity(pos) instanceof ReinforcementBlockEntity reinforcementBlockEntity) {
            reinforcementBlockEntity.getReplacers(replacerIndex)[replacementIndex] = enabled;
            reinforcementBlockEntity.setChanged();
        }
    }
}
