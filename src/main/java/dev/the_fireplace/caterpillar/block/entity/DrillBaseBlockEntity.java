package dev.the_fireplace.caterpillar.block.entity;

import dev.the_fireplace.caterpillar.block.entity.util.InventoryBlockEntity;
import dev.the_fireplace.caterpillar.block.util.CaterpillarBlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import dev.the_fireplace.caterpillar.block.DrillBaseBlock;
import dev.the_fireplace.caterpillar.config.CaterpillarConfig;
import dev.the_fireplace.caterpillar.registry.BlockEntityRegistry;

import java.util.List;

import static dev.the_fireplace.caterpillar.block.DrillBaseBlock.FACING;

public class DrillBaseBlockEntity extends InventoryBlockEntity implements MenuProvider {

    public static final int INVENTORY_SIZE = 0;

    public DrillBaseBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.DRILL_BASE.get(), pos, state, INVENTORY_SIZE);
    }

    public DrillBaseBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, int inventorySize) {
        super(type, pos, state, inventorySize);
    }

    @Override
    public Component getDisplayName() {
        return Component.empty();
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return null;
    }

    public void move() {
        Direction direction = this.getBlockState().getValue(DrillBaseBlock.FACING);
        BlockPos nextPos = this.getBlockPos().relative(direction);

        this.getLevel().setBlockAndUpdate(nextPos, this.getBlockState());
        this.getLevel().removeBlock(this.getBlockPos(), false);

        if (CaterpillarConfig.enableSounds) {
            this.getLevel().playSound(null, this.getBlockPos(), SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }

    protected boolean takeItemFromCaterpillarConsumption(Item item) {
        if (item.equals(Items.AIR)) {
            return true;
        }

        Direction direction = this.getBlockState().getValue(FACING);
        List<DrillBaseBlockEntity> drillHeadAndStorageBlockEntities = CaterpillarBlockUtil.getConnectedDrillHeadAndStorageBlockEntities(level, this.getBlockPos(), direction);

        if (drillHeadAndStorageBlockEntities == null || drillHeadAndStorageBlockEntities.size() == 0) {
            return false;
        }

        // Take item from drill head consumption slots
        for (int i = DrillHeadBlockEntity.CONSUMPTION_SLOT_START; i <= DrillHeadBlockEntity.CONSUMPTION_SLOT_END; i++) {
            ItemStack drillHeadItemStack = drillHeadAndStorageBlockEntities.get(0).getStackInSlot(i);
            if (!drillHeadItemStack.isEmpty() && ItemStack.isSameItem(drillHeadItemStack, new ItemStack(item))) {
                drillHeadItemStack.shrink(1);
                return true;
            }
        }

        if (drillHeadAndStorageBlockEntities.size() == 1) {
            return false;
        }

        // Take item from storage consumption slots
        for (int i = StorageBlockEntity.CONSUMPTION_SLOT_START; i <= StorageBlockEntity.CONSUMPTION_SLOT_END; i++) {
            ItemStack storageItemStack = drillHeadAndStorageBlockEntities.get(1).getStackInSlot(i);
            if (!storageItemStack.isEmpty() && ItemStack.isSameItem(storageItemStack, new ItemStack(item))) {
                storageItemStack.shrink(1);
                return true;
            }
        }

        return false;
    }

    protected ItemStack insertItemStackToCaterpillarGathered(ItemStack stack) {
        Direction direction = this.getBlockState().getValue(FACING);
        List<DrillBaseBlockEntity> drillHeadAndStorageBlockEntities = CaterpillarBlockUtil.getConnectedDrillHeadAndStorageBlockEntities(level, this.getBlockPos(), direction);

        if (drillHeadAndStorageBlockEntities == null || drillHeadAndStorageBlockEntities.size() == 0) {
            return stack;
        }

        // Check if drill head has same item in gathered slots
        for (int i = DrillHeadBlockEntity.GATHERED_SLOT_START; i <= DrillHeadBlockEntity.GATHERED_SLOT_END; i++) {
            ItemStack drillHeadItemStack = drillHeadAndStorageBlockEntities.get(0).getStackInSlot(i);
            if (!drillHeadItemStack.isEmpty() && ItemStack.isSameItemSameTags(stack, drillHeadItemStack)) {
                int j = drillHeadItemStack.getCount() + stack.getCount();
                int maxSize = Math.min(drillHeadItemStack.getMaxStackSize(), stack.getMaxStackSize());
                if (j <= maxSize) {
                    stack.setCount(0);
                    drillHeadItemStack.setCount(j);

                    return stack;
                } else if (drillHeadItemStack.getCount() < maxSize) {
                    stack.shrink(maxSize - drillHeadItemStack.getCount());
                    drillHeadItemStack.setCount(maxSize);
                }
            }
        }

        // Check if storage has same item in gathered slots
        if (drillHeadAndStorageBlockEntities.size() == 2) {
            for (int i = StorageBlockEntity.GATHERED_SLOT_START; i <= StorageBlockEntity.GATHERED_SLOT_END; i++) {
                ItemStack storageItemStack = drillHeadAndStorageBlockEntities.get(1).getStackInSlot(i);
                if (!storageItemStack.isEmpty() && ItemStack.isSameItemSameTags(stack, storageItemStack)) {
                    int j = storageItemStack.getCount() + stack.getCount();
                    int maxSize = Math.min(storageItemStack.getMaxStackSize(), stack.getMaxStackSize());
                    if (j <= maxSize) {
                        stack.setCount(0);
                        storageItemStack.setCount(j);

                        return stack;
                    } else if (storageItemStack.getCount() < maxSize) {
                        stack.shrink(maxSize - storageItemStack.getCount());
                        storageItemStack.setCount(maxSize);
                    }
                }
            }
        }

        // Check if drill head has empty space
        if (drillHeadAndStorageBlockEntities.size() >= 1 && !stack.isEmpty()) {
            for (int i = DrillHeadBlockEntity.GATHERED_SLOT_START; i <= DrillHeadBlockEntity.GATHERED_SLOT_END; i++) {
                ItemStack drillHeadItemStack = drillHeadAndStorageBlockEntities.get(0).getStackInSlot(i);
                if (drillHeadItemStack.isEmpty()) {
                    drillHeadAndStorageBlockEntities.get(0).setStackInSlot(i, stack.split(stack.getCount()));

                    return stack;
                }
            }
        }

        // Check if storage has empty space
        if (drillHeadAndStorageBlockEntities.size() == 2 && !stack.isEmpty()) {
            for (int i = StorageBlockEntity.GATHERED_SLOT_START; i <= StorageBlockEntity.GATHERED_SLOT_END; i++) {
                ItemStack storageItemStack = drillHeadAndStorageBlockEntities.get(1).getStackInSlot(i);
                if (storageItemStack.isEmpty()) {
                    drillHeadAndStorageBlockEntities.get(1).setStackInSlot(i, stack.split(stack.getCount()));

                    return stack;
                }
            }
        }

        return stack;
    }

    protected void removeItemFromCaterpillarGathered(Item item) {
        if (item.equals(Items.AIR)) {
            return;
        }

        Direction direction = this.getBlockState().getValue(FACING);
        List<DrillBaseBlockEntity> drillHeadAndStorageBlockEntities = CaterpillarBlockUtil.getConnectedDrillHeadAndStorageBlockEntities(level, this.getBlockPos(), direction);

        if (drillHeadAndStorageBlockEntities == null || drillHeadAndStorageBlockEntities.size() == 0) {
            return;
        }

        // Remove item from drill head gathered slots
        for (int slotId = DrillHeadBlockEntity.GATHERED_SLOT_START; slotId <= DrillHeadBlockEntity.GATHERED_SLOT_END; slotId++) {
            ItemStack drillHeadStack = drillHeadAndStorageBlockEntities.get(0).getStackInSlot(slotId);
            if (drillHeadStack.getItem().equals(item)) {
                drillHeadAndStorageBlockEntities.get(0).removeStackInSlot(slotId);
            }
        }

        if (drillHeadAndStorageBlockEntities.size() == 1) {
            return;
        }

        // Remove item from storage gathered slots
        for (int slotId = StorageBlockEntity.GATHERED_SLOT_START; slotId <= StorageBlockEntity.GATHERED_SLOT_END; slotId++) {
            ItemStack storageStack = drillHeadAndStorageBlockEntities.get(1).getStackInSlot(slotId);
            if (storageStack.getItem().equals(item)) {
                drillHeadAndStorageBlockEntities.get(1).removeStackInSlot(slotId);
            }
        }
    }
}
