package the_fireplace.caterpillar.common.menu.slot;

import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import the_fireplace.caterpillar.common.menu.CaterpillarMenu;

public class CaterpillarBurnerSlot extends SlotItemHandler {

    public CaterpillarBurnerSlot(IItemHandler slots, int index, int xPosition, int yPosition) {
        super(slots, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(ItemStack itemStack) {
        return CaterpillarMenu.isFuel(itemStack) || isBucket(itemStack);
    }

    public int getMaxStackSize(ItemStack itemStack) {
        return isBucket(itemStack) ? 1 : super.getMaxStackSize(itemStack);
    }

    public static boolean isBucket(ItemStack itemStack) {
        return itemStack.is(Items.BUCKET);
    }
}
