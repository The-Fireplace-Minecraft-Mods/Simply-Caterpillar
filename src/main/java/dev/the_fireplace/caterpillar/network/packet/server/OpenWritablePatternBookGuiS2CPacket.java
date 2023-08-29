package dev.the_fireplace.caterpillar.network.packet.server;

import dev.the_fireplace.caterpillar.Caterpillar;
import dev.the_fireplace.caterpillar.client.screen.PatternBookEditScreen;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static dev.the_fireplace.caterpillar.block.entity.DecorationBlockEntity.INVENTORY_MAX_SLOTS;

public class OpenWritablePatternBookGuiS2CPacket {

    public static final ResourceLocation PACKET_ID = new ResourceLocation(Caterpillar.MOD_ID, "writable_pattern_book.open");

    public static void send(ItemStack book, InteractionHand hand, List<NonNullList<ItemStack>> pattern) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeItem(book);
        buf.writeEnum(hand);
        buf.writeCollection(pattern, (buffer, inventory) -> buffer.writeNbt(ContainerHelper.saveAllItems(new CompoundTag(), inventory)));

        ClientPlayNetworking.send(PACKET_ID, buf);
    }

    public static void receive(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender responseSender) {
        ItemStack book = buf.readItem();
        InteractionHand hand = buf.readEnum(InteractionHand.class);
        List<NonNullList<ItemStack>> pattern = buf.readCollection(ArrayList::new, buffer -> {
            NonNullList<ItemStack> inventory = NonNullList.withSize(INVENTORY_MAX_SLOTS, ItemStack.EMPTY);
            ContainerHelper.loadAllItems(buffer.readNbt(), inventory);
            return inventory;
        });

        Player player = client.player;

        client.execute(() -> {
            client.setScreen(new PatternBookEditScreen(player, book, hand, pattern));
        });
    }
}
