package dev.the_fireplace.caterpillar.network.packet.client;

import dev.the_fireplace.caterpillar.Caterpillar;
import dev.the_fireplace.caterpillar.network.PacketHandler;
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
import net.minecraft.world.item.ItemStack;
import dev.the_fireplace.caterpillar.block.entity.DecorationBlockEntity;

public class DecorationSyncSlotC2SPacket {

    public static final ResourceLocation PACKET_ID = new ResourceLocation(Caterpillar.MOD_ID, "decoration.slot_sync_c2s");

    public static void send(int placementSlotId, ItemStack placementStack, BlockPos pos) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeInt(placementSlotId);
        buf.writeItem(placementStack);
        buf.writeBlockPos(pos);

        ClientPlayNetworking.send(PACKET_ID, buf);
    }

    public static void receive(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender responseSender) {
        ServerLevel level = player.getLevel();

        int placementSlotId = buf.readInt();
        ItemStack stack = buf.readItem();
        BlockPos pos = buf.readBlockPos();

        server.execute(() -> {
            if (level.getBlockEntity(pos) instanceof DecorationBlockEntity decorationBlockEntity) {
                decorationBlockEntity.getSelectedPlacementMap().set(placementSlotId, stack);
            }
        });
    }
}
