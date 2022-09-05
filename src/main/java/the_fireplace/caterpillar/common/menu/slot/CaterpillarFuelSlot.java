package the_fireplace.caterpillar.common.menu.slot;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import the_fireplace.caterpillar.common.menu.util.CaterpillarMenuUtil;

public class CaterpillarFuelSlot extends SlotItemHandler {

    public CaterpillarFuelSlot(IItemHandler slots, int index, int xPosition, int yPosition) {
        super(slots, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(ItemStack itemStack) {
        return CaterpillarMenuUtil.isFuel(itemStack) || isLavaBucket(itemStack);
    }

    public int getMaxStackSize(ItemStack itemStack) {
        return isLavaBucket(itemStack) ? 1 : super.getMaxStackSize(itemStack);
    }

    public static boolean isLavaBucket(ItemStack itemStack) {
        return itemStack.is(Items.LAVA_BUCKET);
    }
}
