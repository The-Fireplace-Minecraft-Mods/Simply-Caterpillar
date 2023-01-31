package dev.the_fireplace.caterpillar.menu.slot;

import dev.the_fireplace.caterpillar.menu.AbstractCaterpillarMenu;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

public class CaterpillarFuelSlot extends Slot {

    public CaterpillarFuelSlot(Container slots, int index, int xPosition, int yPosition) {
        super(slots, index, xPosition, yPosition);
    }

    public static boolean isLavaBucket(ItemStack itemStack) {
        return itemStack.is(Items.LAVA_BUCKET);
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack itemStack) {
        return AbstractCaterpillarMenu.isFuel(itemStack) || isLavaBucket(itemStack);
    }

    public int getMaxStackSize(@NotNull ItemStack itemStack) {
        return isLavaBucket(itemStack) ? 1 : super.getMaxStackSize(itemStack);
    }
}
