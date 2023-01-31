package dev.the_fireplace.caterpillar.network.packet.client;

import dev.the_fireplace.caterpillar.block.entity.DrillBaseBlockEntity;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.item.ItemStack;

public class CaterpillarSyncSlotC2SPacket {

    public static void receive(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender responseSender) {
        ServerLevel level = player.getLevel();

        int slotId = buf.readInt();
        ItemStack stack = buf.readItem();
        BlockPos pos = buf.readBlockPos();

        if (level.getBlockEntity(pos) instanceof DrillBaseBlockEntity blockEntity) {
            blockEntity.setItem(slotId, stack);
        }

    }
}
