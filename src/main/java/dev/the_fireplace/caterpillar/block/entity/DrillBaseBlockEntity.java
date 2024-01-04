package dev.the_fireplace.caterpillar.block.entity;

import dev.the_fireplace.caterpillar.block.entity.util.ImplementedInventory;
import dev.the_fireplace.caterpillar.block.util.CaterpillarBlockUtil;
import dev.the_fireplace.caterpillar.config.ConfigManager;
import dev.the_fireplace.caterpillar.registry.BlockEntityRegistry;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import java.util.List;

import static dev.the_fireplace.caterpillar.block.DrillBaseBlock.FACING;

public class DrillBaseBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory, ImplementedInventory {

    public NonNullList<ItemStack> inventory;

    protected int timer = 0;

    public DrillBaseBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(BlockEntityRegistry.DRILL_BASE, blockPos, blockState, 0);
    }

    public DrillBaseBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState, int inventorySize) {
        super(blockEntityType, blockPos, blockState);

        this.timer = 0;

        this.inventory = NonNullList.withSize(inventorySize, ItemStack.EMPTY);
    }

    public DrillBaseBlockEntity(BlockEntityType<?> type, BlockPos blockPos, BlockState blockState) {
        super(type, blockPos, blockState);
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return null;
    }

    @Override
    public Component getDisplayName() {
        return Component.empty();
    }

    public void move() {
        Direction direction = this.getBlockState().getValue(FACING);
        BlockPos nextPos = this.getBlockPos().relative(direction);

        this.getLevel().setBlockAndUpdate(nextPos, this.getBlockState());
        this.getLevel().removeBlock(this.getBlockPos(), false);

        if (ConfigManager.enableSounds()) {
            this.getLevel().playSound(null, this.getBlockPos(), SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        ContainerHelper.saveAllItems(compoundTag, this.inventory);

        super.saveAdditional(compoundTag);
    }

    @Override
    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);

        ContainerHelper.loadAllItems(compoundTag, this.inventory);
    }

    @Override
    public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {
        buf.writeBlockPos(this.worldPosition);
    }

    // Prevent the mouse to re-center when the screen changes
    @Override
    public boolean shouldCloseCurrentScreen() {
        return false;
    }

    public void setInventory(NonNullList<ItemStack> inventory) {
        this.inventory = inventory;
    }

    public void dropContents(Level level, BlockPos blockPosWhereToDrop) {
        Containers.dropContents(level, blockPosWhereToDrop, this.inventory);
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
            ItemStack drillHeadItemStack = drillHeadAndStorageBlockEntities.get(0).getItem(i);
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
            ItemStack storageItemStack = drillHeadAndStorageBlockEntities.get(1).getItem(i);
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
            ItemStack drillHeadItemStack = drillHeadAndStorageBlockEntities.get(0).getItem(i);
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
                ItemStack storageItemStack = drillHeadAndStorageBlockEntities.get(1).getItem(i);
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
                ItemStack drillHeadItemStack = drillHeadAndStorageBlockEntities.get(0).getItem(i);
                if (drillHeadItemStack.isEmpty()) {
                    drillHeadAndStorageBlockEntities.get(0).setItem(i, stack.split(stack.getCount()));

                    return stack;
                }
            }
        }

        // Check if storage has empty space
        if (drillHeadAndStorageBlockEntities.size() == 2 && !stack.isEmpty()) {
            for (int i = StorageBlockEntity.GATHERED_SLOT_START; i <= StorageBlockEntity.GATHERED_SLOT_END; i++) {
                ItemStack storageItemStack = drillHeadAndStorageBlockEntities.get(1).getItem(i);
                if (storageItemStack.isEmpty()) {
                    drillHeadAndStorageBlockEntities.get(1).setItem(i, stack.split(stack.getCount()));

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
            ItemStack drillHeadStack = drillHeadAndStorageBlockEntities.get(0).getItem(slotId);
            if (drillHeadStack.getItem().equals(item)) {
                drillHeadAndStorageBlockEntities.get(0).removeItemNoUpdate(slotId);
            }
        }

        if (drillHeadAndStorageBlockEntities.size() == 1) {
            return;
        }

        // Remove item from storage gathered slots
        for (int slotId = StorageBlockEntity.GATHERED_SLOT_START; slotId <= StorageBlockEntity.GATHERED_SLOT_END; slotId++) {
            ItemStack storageStack = drillHeadAndStorageBlockEntities.get(1).getItem(slotId);
            if (storageStack.getItem().equals(item)) {
                drillHeadAndStorageBlockEntities.get(1).removeItemNoUpdate(slotId);
            }
        }
    }
}
