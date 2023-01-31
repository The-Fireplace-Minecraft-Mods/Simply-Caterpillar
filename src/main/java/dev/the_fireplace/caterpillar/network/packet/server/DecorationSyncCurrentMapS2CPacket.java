package dev.the_fireplace.caterpillar.network.packet.server;

import dev.the_fireplace.caterpillar.block.entity.DecorationBlockEntity;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;

public class DecorationSyncCurrentMapS2CPacket {

    public static void receive(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender responseSender) {
        ClientLevel level = client.level;

        int currentMap = buf.readInt();
        BlockPos pos = buf.readBlockPos();

        if (level.getBlockEntity(pos) instanceof DecorationBlockEntity blockEntity) {
            blockEntity.setCurrentMap(currentMap);
        }
    }
}

