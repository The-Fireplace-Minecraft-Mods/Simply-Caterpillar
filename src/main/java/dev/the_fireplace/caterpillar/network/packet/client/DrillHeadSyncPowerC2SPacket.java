package dev.the_fireplace.caterpillar.network.packet.client;

import dev.the_fireplace.caterpillar.block.entity.DrillHeadBlockEntity;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

public class DrillHeadSyncPowerC2SPacket {

    public static void receive(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender responseSender) {
        ServerLevel level = player.getLevel();

        boolean powered = buf.readBoolean();
        BlockPos pos = buf.readBlockPos();

        if (level.getBlockEntity(pos) instanceof DrillHeadBlockEntity drillHeadBlockEntity) {
            drillHeadBlockEntity.setPower(powered);

            // PacketHandler.sendToClients(new DrillHeadSyncPowerS2CPacket(drillHeadBlockEntity.isPowered(), drillHeadBlockEntity.getBlockPos()));
        }
    }
}
