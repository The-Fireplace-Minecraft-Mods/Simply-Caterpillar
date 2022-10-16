package the_fireplace.caterpillar.common.menu.slot;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class FakeSlot extends SlotWithRestriction {

    private ItemStack displayStack = ItemStack.EMPTY;

    public FakeSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    public void setDisplayStack(ItemStack itemStack) {
        this.displayStack = itemStack;
    }

    @Override
    public ItemStack getItem() {
        return displayStack;
    }

    @Override
    public boolean hasItem() {
        return displayStack != null;
    }

    @Override
    public void set(ItemStack stack) {
        // NOP
    }

    @Override
    public ItemStack remove(int i) {
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
}
