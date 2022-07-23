package the_fireplace.caterpillar.common.container.syncdata;

import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import the_fireplace.caterpillar.common.block.entity.StorageBlockEntity;

public class StorageContainerData extends SimpleContainerData {

    private final StorageBlockEntity blockEntity;

    public StorageContainerData(StorageBlockEntity blockEntity, int amount) {
        super(amount);
        this.blockEntity = blockEntity;
    }

    @Override
    public int get(int key) {
        return switch (key) {
            case 0 -> this.blockEntity.getItemInSlot(0).getCount();
            default -> throw new UnsupportedOperationException("There is no value corresponding to key: '" + key + "' in: '" + blockEntity + "'");
        };
    }

    @Override
    public void set(int key, int value) {
        switch (key) {
            case 0:
                ItemStack stack = this.blockEntity.getItemInSlot(0);

                if (value > 0 && value < stack.getMaxStackSize()) {
                    stack.setCount(value);
                } else if (value <= 0) {
                    stack = ItemStack.EMPTY;
                    this.blockEntity.inventory.setStackInSlot(0, ItemStack.EMPTY);
                } else if (value > stack.getMaxStackSize()) {
                    stack.setCount(stack.getMaxStackSize());
                }

                this.blockEntity.inventory.setStackInSlot(0, stack);
                break;
            default:
                throw new UnsupportedOperationException("There is no value corresponding to key: '" + key + "' in: '" + blockEntity + "'");
        }
    }
}
