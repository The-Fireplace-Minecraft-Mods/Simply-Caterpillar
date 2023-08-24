package dev.the_fireplace.caterpillar.menu.slot;

import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class SlotWithRestriction extends Slot {
    private final Predicate<ItemStack> validator;

    public SlotWithRestriction(Container slots, int index, int xPosition, int yPosition, Predicate<ItemStack> validator) {
        super(slots, index, xPosition, yPosition);
        this.validator = validator;
    }

    public SlotWithRestriction(Container slots, int index, int xPosition, int yPosition) {
        this(slots, index, xPosition, yPosition, stack -> false);
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return this.validator.test(stack);
    }
}
