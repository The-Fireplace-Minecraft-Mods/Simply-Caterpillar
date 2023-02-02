package dev.the_fireplace.caterpillar.network.packet.server;

import dev.the_fireplace.caterpillar.Caterpillar;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import dev.the_fireplace.caterpillar.block.entity.ReinforcementBlockEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class ReinforcementSyncStateReplacerS2CPacket {

    public static final ResourceLocation PACKET_ID = new ResourceLocation(Caterpillar.MOD_ID, "reinforcement.state_replacer_sync_s2c");

    public static void send(ServerLevel level, int replacerIndex, int replacementIndex, byte enabled, BlockPos pos) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeInt(replacerIndex);
        buf.writeInt(replacementIndex);
        buf.writeByte(enabled);
        buf.writeBlockPos(pos);

        for (ServerPlayer player : PlayerLookup.tracking(level, pos)) {
            ServerPlayNetworking.send(player, PACKET_ID, buf);
        }
    }

    public static void receive(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender responseSender) {
        ClientLevel level = client.level;

        int replacerIndex = buf.readInt();
        int replacementIndex = buf.readInt();
        byte enabled = buf.readByte();
        BlockPos pos = buf.readBlockPos();

        client.execute(() -> {
            if (level.getBlockEntity(pos) instanceof ReinforcementBlockEntity reinforcementBlockEntity) {
                reinforcementBlockEntity.getReplacers(replacerIndex)[replacementIndex] = enabled;
                reinforcementBlockEntity.setChanged();
            }
        });
    }
}
