package dev.the_fireplace.caterpillar.block.entity;

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
import dev.the_fireplace.caterpillar.block.entity.util.InventoryBlockEntity;

import java.util.List;

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

    protected boolean takeItem(List<AbstractCaterpillarBlockEntity> blockEntities, Item item, int startIndex, int endIndex) {
        if (item.equals(Items.AIR)) {
            return true;
        }

        for (AbstractCaterpillarBlockEntity blockEntity : blockEntities) {
            for (int i = startIndex; i <= endIndex; i++) {
                ItemStack blockEntityItemStack = blockEntity.getStackInSlot(i);
                if (!blockEntityItemStack.isEmpty() && ItemStack.isSame(blockEntityItemStack, new ItemStack(item))) {
                    blockEntityItemStack.shrink(1);
                    return true;
                }
            }
        }

        return false;
    }

    public ItemStack insertItemStack(List<AbstractCaterpillarBlockEntity> blockEntities, ItemStack stack, int startIndex, int endIndex) {

        // Check if blockEntity has same item in gathered slot
        for (AbstractCaterpillarBlockEntity blockEntity : blockEntities) {
            for (int i = startIndex; i <= endIndex; i++) {
                ItemStack blockEntityItemStack = blockEntity.getStackInSlot(i);
                if (!blockEntityItemStack.isEmpty() && ItemStack.isSameItemSameTags(stack, blockEntityItemStack)) {
                    int j = blockEntityItemStack.getCount() + stack.getCount();
                    int maxSize = Math.min(blockEntityItemStack.getMaxStackSize(), stack.getMaxStackSize());
                    if (j <= maxSize) {
                        stack.setCount(0);
                        blockEntityItemStack.setCount(j);

                        return stack;
                    } else if (blockEntityItemStack.getCount() < maxSize) {
                        stack.shrink(maxSize - blockEntityItemStack.getCount());
                        blockEntityItemStack.setCount(maxSize);
                    }
                }
            }
        }

        // Check if blockEntity has empty space
        for (AbstractCaterpillarBlockEntity blockEntity : blockEntities) {
            if (!stack.isEmpty()) {
                for (int i = startIndex; i <= endIndex; i++) {
                    ItemStack blockEntityItemStack = blockEntity.getStackInSlot(i);
                    if (blockEntityItemStack.isEmpty()) {
                        blockEntity.setStackInSlot(i, stack.split(stack.getCount()));

                        return stack;
                    }
                }
            }
        }

        return stack;
    }

    public void removeItem(List<AbstractCaterpillarBlockEntity> blockEntities, Item item, int startIndex, int endIndex) {
        if (item.equals(Items.AIR)) {
            return;
        }

        for (AbstractCaterpillarBlockEntity blockEntity : blockEntities) {
            for (int slotId = startIndex; slotId <= endIndex; slotId++) {
                ItemStack stack = blockEntity.getStackInSlot(slotId);
                if (stack.getItem().equals(item)) {
                    blockEntity.removeStackInSlot(slotId);
                }
            }
        }
    }
}
