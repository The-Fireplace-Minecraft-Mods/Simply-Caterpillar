package the_fireplace.caterpillar.common.container.util;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import the_fireplace.caterpillar.common.container.CaterpillarContainer;
import the_fireplace.caterpillar.common.container.DrillHeadContainer;

public class CaterpillarBurnerSlot extends SlotItemHandler {

    private final CaterpillarContainer container;

    public CaterpillarBurnerSlot(CaterpillarContainer container, IItemHandler slots, int index, int xPosition, int yPosition) {
        super(slots, index, xPosition, yPosition);
        this.container = container;
    }

    @Override
    public boolean mayPlace(ItemStack itemStack) {
        return DrillHeadContainer.isFuel(itemStack) || isBucket(itemStack);
    }

    public int getMaxStackSize(ItemStack itemStack) {
        return isBucket(itemStack) ? 1 : super.getMaxStackSize(itemStack);
    }

    public static boolean isBucket(ItemStack itemStack) {
        return itemStack.is(Items.BUCKET);
    }
}
