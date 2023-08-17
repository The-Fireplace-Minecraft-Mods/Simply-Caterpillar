package dev.the_fireplace.caterpillar.network.packet.client;

import dev.the_fireplace.caterpillar.registry.ItemRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.network.FilteredText;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static dev.the_fireplace.caterpillar.block.entity.DecorationBlockEntity.INVENTORY_MAX_SLOTS;

public class PatternBookEditC2SPacket {
    private final int slot;

    private final List<ItemStackHandler> pattern;

    private final String title;

    public PatternBookEditC2SPacket(int slot, List<ItemStackHandler> pattern, String title) {
        this.slot = slot;
        this.pattern = pattern;
        this.title = title;
    }

    public PatternBookEditC2SPacket(FriendlyByteBuf buf) {
        this.slot = buf.readInt();

        this.pattern = buf.readCollection(ArrayList::new, buffer -> {
            ItemStackHandler itemStackHandler = new ItemStackHandler(INVENTORY_MAX_SLOTS);
            itemStackHandler.deserializeNBT(buffer.readNbt());
            return itemStackHandler;
        });

        this.title = buf.readUtf();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(slot);

        buf.writeCollection(pattern, (buffer, itemStackHandler) -> buffer.writeNbt(itemStackHandler.serializeNBT()));

        buf.writeUtf(title);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            Player player = context.getSender();

            if (Inventory.isHotbarSlot(slot)) {
                ItemStack itemStack = player.getInventory().getItem(slot);
                if (itemStack.is(ItemRegistry.WRITABLE_PATTERN_BOOK.get())) {
                    ItemStack writtenPatternBook = new ItemStack(ItemRegistry.WRITTEN_PATTERN_BOOK.get());
                    CompoundTag tag = itemStack.getTag();
                    if (tag != null) {
                        writtenPatternBook.setTag(tag.copy());
                    }

                    writtenPatternBook.addTagElement("author", StringTag.valueOf(player.getName().getString()));
                    if (player.isTextFilteringEnabled()) {
                        writtenPatternBook.addTagElement("title", StringTag.valueOf(FilteredText.fullyFiltered(this.title).filteredOrEmpty()));
                    } else {
                        writtenPatternBook.addTagElement("filtered_title", StringTag.valueOf(FilteredText.fullyFiltered(this.title).filteredOrEmpty()));
                        writtenPatternBook.addTagElement("title", StringTag.valueOf(this.title));
                    }

                    ListTag listTag = new ListTag();
                    this.pattern.stream().map(ItemStackHandler::serializeNBT).forEach(listTag::add);
                    writtenPatternBook.addTagElement("pattern", listTag);

                    player.getInventory().setItem(slot, writtenPatternBook);
                }
            }
        });
        context.setPacketHandled(true);
    }
}
