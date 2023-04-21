package dev.the_fireplace.caterpillar.network.packet.server;

import dev.the_fireplace.caterpillar.client.screen.PatternBookViewScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class OpenBookGuiS2CPacket {

    private final ItemStack book;

    public OpenBookGuiS2CPacket(ItemStack book) {
        this.book = book;
    }

    public OpenBookGuiS2CPacket(FriendlyByteBuf buf) {
        this.book = buf.readItem();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeItem(book);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            Minecraft.getInstance().setScreen(new PatternBookViewScreen(book));
        });
        context.setPacketHandled(true);
    }
}
