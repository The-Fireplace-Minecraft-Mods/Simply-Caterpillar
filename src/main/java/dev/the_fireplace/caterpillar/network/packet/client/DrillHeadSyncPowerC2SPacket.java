package dev.the_fireplace.caterpillar.network.packet.client;

import dev.the_fireplace.caterpillar.Caterpillar;
import dev.the_fireplace.caterpillar.block.entity.DrillHeadBlockEntity;
import dev.the_fireplace.caterpillar.network.packet.server.DrillHeadSyncPowerS2CPacket;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

public class DrillHeadSyncPowerC2SPacket {

    public static final ResourceLocation PACKET_ID = new ResourceLocation(Caterpillar.MOD_ID, "drill_head.power_sync_c2s");

    public static void send(Boolean powered, BlockPos pos) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeBoolean(powered);
        buf.writeBlockPos(pos);

        ClientPlayNetworking.send(PACKET_ID, buf);
    }

    public static void receive(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender responseSender) {
        boolean powered = buf.readBoolean();
        BlockPos pos = buf.readBlockPos();

        server.execute(() -> {
            ServerLevel level = player.getLevel();

            if (level.getBlockEntity(pos) instanceof DrillHeadBlockEntity drillHeadBlockEntity) {
                drillHeadBlockEntity.setPower(powered);

                DrillHeadSyncPowerS2CPacket.send(level, drillHeadBlockEntity.isPowered(), drillHeadBlockEntity.getBlockPos());
            }
        });
    }
}
