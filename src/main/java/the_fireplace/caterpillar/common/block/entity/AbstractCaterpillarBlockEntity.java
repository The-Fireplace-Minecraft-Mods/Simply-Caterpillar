package the_fireplace.caterpillar.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import the_fireplace.caterpillar.common.block.entity.util.InventoryBlockEntity;

public abstract class AbstractCaterpillarBlockEntity extends InventoryBlockEntity implements MenuProvider {

    public AbstractCaterpillarBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, int inventorySize) {
        super(type, pos, state, inventorySize);
    }

    public abstract void move();

    @Override
    public @NotNull Component getDisplayName() {
        return Component.empty();
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, @NotNull Inventory playerInventory, @NotNull Player player) {
        return null;
    }

    protected boolean takeItemFromDrillHead(DrillHeadBlockEntity drillHeadBlockEntity, StorageBlockEntity storageBlockEntity, Item item, int startIndex, int endIndex) {
        if (item.equals(Items.AIR)) {
            return true;
        }

        if (drillHeadBlockEntity != null) {
            for (int i = startIndex; i <= endIndex; i++) {
                ItemStack drillHeadStack = drillHeadBlockEntity.getStackInSlot(i);
                if (!drillHeadStack.isEmpty() && ItemStack.isSame(drillHeadStack, new ItemStack(item))) {
                    drillHeadStack.shrink(1);
                    return true;
                }
            }
        }

        if (storageBlockEntity != null) {
            for (int i = startIndex - 1; i <= endIndex - 1; i++) {
                ItemStack storageStack = storageBlockEntity.getStackInSlot(i);
                if (!storageStack.isEmpty() && ItemStack.isSame(storageStack, new ItemStack(item))) {
                    storageStack.shrink(1);
                    return true;
                }
            }
        }

        return false;
    }

    public ItemStack insertItemStackToDrillHead(DrillHeadBlockEntity drillHeadBlockEntity, StorageBlockEntity storageBlockEntity, ItemStack stack, int startIndex, int endIndex) {
        // Check if drill head has same item in gathered slot
        if (drillHeadBlockEntity != null) {
            for (int i = startIndex; i <= endIndex; i++) {
                ItemStack drillHeadStack = drillHeadBlockEntity.getStackInSlot(i);
                if (!drillHeadStack.isEmpty() && ItemStack.isSameItemSameTags(stack, drillHeadStack)) {
                    int j = drillHeadStack.getCount() + stack.getCount();
                    int maxSize = Math.min(drillHeadStack.getMaxStackSize(), stack.getMaxStackSize());
                    if (j <= maxSize) {
                        stack.setCount(0);
                        drillHeadStack.setCount(j);

                        return stack;
                    } else if (drillHeadStack.getCount() < maxSize) {
                        stack.shrink(maxSize - drillHeadStack.getCount());
                        drillHeadStack.setCount(maxSize);
                    }
                }
            }
        }

        // Check if storage has same item in gathered slot
        if (storageBlockEntity != null) {
            if (!stack.isEmpty()) {
                for (int i = startIndex - 1; i <= endIndex - 1; i++) {
                    ItemStack storageStack = storageBlockEntity.getStackInSlot(i);
                    if (!storageStack.isEmpty() && ItemStack.isSameItemSameTags(stack, storageStack)) {
                        int j = storageStack.getCount() + stack.getCount();
                        int maxSize = Math.min(storageStack.getMaxStackSize(), stack.getMaxStackSize());
                        if (j <= maxSize) {
                            stack.setCount(0);
                            storageStack.setCount(j);

                            return stack;
                        } else if (storageStack.getCount() < maxSize) {
                            stack.shrink(maxSize - storageStack.getCount());
                            storageStack.setCount(maxSize);
                        }
                    }
                }
            }
        }

        // Check if drill head has empty space
        if (drillHeadBlockEntity != null) {
            if (!stack.isEmpty()) {
                for (int i = startIndex; i <= endIndex; i++) {
                    ItemStack drillHeadStack = drillHeadBlockEntity.getStackInSlot(i);
                    if (drillHeadStack.isEmpty()) {
                        drillHeadBlockEntity.setStackInSlot(i, stack.split(stack.getCount()));

                        return stack;
                    }
                }
            }
        }

        // Check if storage has empty space
        if (storageBlockEntity != null) {
            if (!stack.isEmpty()) {
                for (int i = startIndex - 1; i <= endIndex - 1; i++) {
                    ItemStack storageStack = storageBlockEntity.getStackInSlot(i);
                    if (storageStack.isEmpty()) {
                        storageBlockEntity.setStackInSlot(i, stack.split(stack.getCount()));

                        return stack;
                    }
                }
            }
        }

        return stack;
    }
}
