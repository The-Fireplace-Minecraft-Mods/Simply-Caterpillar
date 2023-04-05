package dev.the_fireplace.caterpillar.network.packet.client;

import dev.the_fireplace.caterpillar.init.ItemInit;
import net.minecraft.nbt.CompoundTag;
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

        List<ItemStack> collection = buf.readCollection(ArrayList::new, FriendlyByteBuf::readItem);
        this.pattern = new ArrayList<>();
        for (int i = 0; i < collection.size(); i++) {
            this.pattern.add(new ItemStackHandler(9));
            this.pattern.get(i).insertItem(0, collection.get(i), false);
        }

        this.title = buf.readUtf();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(slot);
        List<ItemStack> list = new ArrayList<>();
        for (ItemStackHandler itemStackHandler : pattern) {
            list.add(itemStackHandler.getStackInSlot(0));
        }
        buf.writeCollection(list, FriendlyByteBuf::writeItem);
        buf.writeUtf(title);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            Player player = context.getSender();

            if (Inventory.isHotbarSlot(slot)) {
                ItemStack itemStack = player.getInventory().getItem(slot);
                if (itemStack.is(ItemInit.WRITABLE_PATTERN_BOOK.get())) {
                    ItemStack writtenPatternBook = new ItemStack(ItemInit.WRITTEN_PATTERN_BOOK.get());
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
                    for (int i = 0; i < this.pattern.size(); i++) {
                        writtenPatternBook.addTagElement("pattern" + i, this.pattern.get(i).serializeNBT());
                    }

                    player.getInventory().setItem(slot, writtenPatternBook);
                }
            }
        });
        context.setPacketHandled(true);
    }
}
