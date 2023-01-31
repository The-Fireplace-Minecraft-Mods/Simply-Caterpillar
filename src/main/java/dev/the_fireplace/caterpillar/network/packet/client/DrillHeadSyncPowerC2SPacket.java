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
        boolean powered = buf.readBoolean();
        BlockPos pos = buf.readBlockPos();

        server.execute(() -> {
            ServerLevel level = player.getLevel();

            if (level.getBlockEntity(pos) instanceof DrillHeadBlockEntity drillHeadBlockEntity) {
                drillHeadBlockEntity.setPower(powered);

                drillHeadBlockEntity.sendPowerPacketS2C(drillHeadBlockEntity.isPowered(), drillHeadBlockEntity.getBlockPos());
            }
        });
    }
}
