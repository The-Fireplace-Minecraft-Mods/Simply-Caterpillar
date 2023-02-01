package dev.the_fireplace.caterpillar.network.packet.client;

import dev.the_fireplace.caterpillar.Caterpillar;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.item.ItemStack;

public class CaterpillarSyncCarriedC2SPacket {

    public static final ResourceLocation PACKET_ID = new ResourceLocation(Caterpillar.MOD_ID, "caterpillar.carried_sync_c2s");

    public static void send(ItemStack stack) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeItem(stack);

        ClientPlayNetworking.send(PACKET_ID, buf);
    }

    public static void receive(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender responseSender) {
        ItemStack stack = buf.readItem();

        server.execute(() -> {
            player.containerMenu.setCarried(stack);
        });
    }
}
