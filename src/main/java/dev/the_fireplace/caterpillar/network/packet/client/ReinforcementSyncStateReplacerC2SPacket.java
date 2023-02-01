package dev.the_fireplace.caterpillar.network.packet.client;

import dev.the_fireplace.caterpillar.Caterpillar;
import dev.the_fireplace.caterpillar.block.entity.ReinforcementBlockEntity;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

public class ReinforcementSyncStateReplacerC2SPacket {


    public static final ResourceLocation PACKET_ID = new ResourceLocation(Caterpillar.MOD_ID, "reinforcement.state_replacer_sync_c2s");

    public static void receive(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender responseSender) {
        ServerLevel level = player.getLevel();

        int replacerIndex = buf.readInt();
        int replacementIndex = buf.readInt();
        byte activated = buf.readByte();
        BlockPos pos = buf.readBlockPos();

        server.execute(() -> {
            if (level.getBlockEntity(pos) instanceof ReinforcementBlockEntity reinforcementBlockEntity) {
                reinforcementBlockEntity.getReplacers(replacerIndex)[replacementIndex] = activated;
                reinforcementBlockEntity.setChanged();

                // PacketHandler.sendToClients(new ReinforcementSyncStateReplacerS2CPacket(this.replacerIndex, this.replacementIndex, this.activated, reinforcementBlockEntity.getBlockPos()));
            }
        });
    }
}
