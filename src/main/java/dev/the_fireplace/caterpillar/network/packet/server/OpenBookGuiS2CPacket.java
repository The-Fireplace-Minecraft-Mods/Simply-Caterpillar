package dev.the_fireplace.caterpillar.network.packet.server;

import dev.the_fireplace.caterpillar.Caterpillar;
import dev.the_fireplace.caterpillar.client.screen.PatternBookViewScreen;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class OpenBookGuiS2CPacket {
    public static final ResourceLocation PACKET_ID = new ResourceLocation(Caterpillar.MOD_ID, "book.open");

    public static void send(ItemStack book) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeItem(book);

        ClientPlayNetworking.send(PACKET_ID, buf);
    }

    public static void receive(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender responseSender) {
        ItemStack book = buf.readItem();

        client.execute(() -> {
            client.setScreen(new PatternBookViewScreen(book));
        });
    }
}
