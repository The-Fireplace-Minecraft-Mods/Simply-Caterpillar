package dev.the_fireplace.caterpillar.inventory.slot;

import dev.the_fireplace.caterpillar.inventory.DrillHeadMenu;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class DrillHeadFuelSlot extends Slot {
    private final DrillHeadMenu menu;

    public DrillHeadFuelSlot(DrillHeadMenu drillHeadMenu, Container container, int slot, int xPosition, int yPosition) {
        super(container, slot, xPosition, yPosition);
        this.menu = drillHeadMenu;
    }

    public boolean mayPlace(ItemStack stack) {
        return this.menu.isFuel(stack) || isBucket(stack);
    }

    public int getMaxStackSize(ItemStack stack) {
        return isBucket(stack) ? 1 : super.getMaxStackSize(stack);
    }

    public static boolean isBucket(ItemStack stack) {
        return stack.is(Items.BUCKET);
    }
}
