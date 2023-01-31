package dev.the_fireplace.caterpillar.menu.slot;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class FakeSlot extends Slot {

    private ItemStack displayStack;

    public FakeSlot(Container slots, int index, int xPosition, int yPosition) {
        super(slots, index, xPosition, yPosition);
        this.displayStack = slots.getItem(index);
    }

    public void setDisplayStack(ItemStack itemStack) {
        this.displayStack = itemStack;
    }

    @Override
    public @NotNull ItemStack getItem() {
        return displayStack;
    }

    @Override
    public boolean hasItem() {
        return displayStack != null;
    }

    @Override
    public void set(@NotNull ItemStack stack) {
        // NOP
    }

    @Override
    public @NotNull ItemStack remove(int i) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setChanged() {
        // NOP
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }

    @Override
    public boolean mayPickup(Player player) {
        return false;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return false;
    }
}
