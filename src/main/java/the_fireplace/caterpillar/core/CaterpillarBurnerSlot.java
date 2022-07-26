package the_fireplace.caterpillar.core;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import the_fireplace.caterpillar.common.container.DrillHeadContainer;

public class CaterpillarBurnerSlot extends SlotItemHandler {

    private final DrillHeadContainer container;

    public CaterpillarBurnerSlot(DrillHeadContainer container, IItemHandler slots, int index, int xPosition, int yPosition) {
        super(slots, index, xPosition, yPosition);
        this.container = container;
    }

    @Override
    public boolean mayPlace(ItemStack itemStack) {
        return this.container.isFuel(itemStack) || isBucket(itemStack);
    }

    public int getMaxStackSize(ItemStack itemStack) {
        return isBucket(itemStack) ? 1 : super.getMaxStackSize(itemStack);
    }

    public static boolean isBucket(ItemStack itemStack) {
        return itemStack.is(Items.BUCKET);
    }
}
