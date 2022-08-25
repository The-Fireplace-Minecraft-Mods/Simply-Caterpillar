package the_fireplace.caterpillar.common.container;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import the_fireplace.caterpillar.common.block.entity.IncineratorBlockEntity;
import the_fireplace.caterpillar.common.container.syncdata.IncineratorContainerData;
import the_fireplace.caterpillar.core.init.BlockInit;
import the_fireplace.caterpillar.core.init.ContainerInit;

public class IncineratorContainer {
    public static final int SIZE = 9;

    /*
    public ItemStack quickMoveStack(Player player, int index) {
        var retStack = ItemStack.EMPTY;
        final Slot slot = getSlot(index);

        if (slot.hasItem()) {
            final ItemStack item = slot.getItem();
            retStack = item.copy();
            if (index < MAX_SIZE) {
                if (!moveItemStackTo(item, MAX_SIZE, this.slots.size(), true))
                    return ItemStack.EMPTY;
            } else if (!moveItemStackTo(item, 0, MAX_SIZE, false))
                return ItemStack.EMPTY;

            if (item.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return retStack;
    }
    */
}
