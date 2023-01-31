package dev.the_fireplace.caterpillar.network.packet.server;

import dev.the_fireplace.caterpillar.block.entity.DrillHeadBlockEntity;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;

public class DrillHeadSyncPowerS2CPacket {

    public static void receive(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender responseSender) {
        ClientLevel level = client.level;

        boolean powered = buf.readBoolean();
        BlockPos pos = buf.readBlockPos();

        if (level.getBlockEntity(pos) instanceof DrillHeadBlockEntity blockEntity) {
            blockEntity.setPower(powered);
        }
    }
}
