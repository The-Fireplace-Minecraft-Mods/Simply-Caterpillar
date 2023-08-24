package dev.the_fireplace.caterpillar.network.packet.client;

import dev.the_fireplace.caterpillar.Caterpillar;
import dev.the_fireplace.caterpillar.registry.ItemRegistry;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.FilteredText;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import java.util.ArrayList;
import java.util.List;

import static dev.the_fireplace.caterpillar.block.entity.DecorationBlockEntity.INVENTORY_MAX_SLOTS;

public class PatternBookEditC2SPacket {

    public static final ResourceLocation PACKET_ID = new ResourceLocation(Caterpillar.MOD_ID, "pattern_book.sync_book_c2s");

    public static void send(int slot, List<NonNullList<ItemStack>> pattern, String title) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());

        buf.writeInt(slot);

        buf.writeCollection(pattern, (buffer, inventory) -> buffer.writeNbt(ContainerHelper.saveAllItems(new CompoundTag(), inventory)));

        buf.writeUtf(title);

        ClientPlayNetworking.send(PACKET_ID, buf);
    }

    public static void receive(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender responseSender) {
        int slot = buf.readInt();

        List<NonNullList<ItemStack>> pattern = buf.readCollection(ArrayList::new, buffer -> {
            NonNullList<ItemStack> inventory = NonNullList.withSize(INVENTORY_MAX_SLOTS, ItemStack.EMPTY);
            ContainerHelper.loadAllItems(buffer.readNbt(), inventory);
            return inventory;
        });

        String title = buf.readUtf();

        server.execute(() -> {
            if (Inventory.isHotbarSlot(slot)) {
                ItemStack itemStack = player.getInventory().getItem(slot);
                if (itemStack.is(ItemRegistry.WRITABLE_PATTERN_BOOK)) {
                    ItemStack writtenPatternBook = new ItemStack(ItemRegistry.WRITTEN_PATTERN_BOOK);
                    CompoundTag tag = itemStack.getTag();
                    if (tag != null) {
                        writtenPatternBook.setTag(tag.copy());
                    }

                    writtenPatternBook.addTagElement("author", StringTag.valueOf(player.getName().getString()));
                    if (player.isTextFilteringEnabled()) {
                        writtenPatternBook.addTagElement("title", StringTag.valueOf(FilteredText.fullyFiltered(title).filteredOrEmpty()));
                    } else {
                        writtenPatternBook.addTagElement("filtered_title", StringTag.valueOf(FilteredText.fullyFiltered(title).filteredOrEmpty()));
                        writtenPatternBook.addTagElement("title", StringTag.valueOf(title));
                    }

                    ListTag listTag = new ListTag();
                    for (NonNullList<ItemStack> inventory : pattern) {
                        listTag.add(ContainerHelper.saveAllItems(new CompoundTag(), inventory, true));
                    }
                    writtenPatternBook.addTagElement("pattern", listTag);

                    player.getInventory().setItem(slot, writtenPatternBook);
                }
            }
        });
    }
}
