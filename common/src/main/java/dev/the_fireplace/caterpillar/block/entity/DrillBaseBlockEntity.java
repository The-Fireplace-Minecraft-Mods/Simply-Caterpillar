package dev.the_fireplace.caterpillar.block.entity;

import dev.architectury.registry.menu.ExtendedMenuProvider;
import dev.the_fireplace.caterpillar.block.DrillBaseBlock;
import dev.the_fireplace.caterpillar.block.entity.util.InventoryBlockEntity;
import dev.the_fireplace.caterpillar.block.util.CaterpillarBlockUtil;
import dev.the_fireplace.caterpillar.registry.BlockEntityTypesRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DrillBaseBlockEntity extends InventoryBlockEntity implements ExtendedMenuProvider {

    public static final int INVENTORY_SIZE = 0;

    public DrillBaseBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityTypesRegistry.DRILL_BASE.get(), pos, state, INVENTORY_SIZE);
    }

    public DrillBaseBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, int inventorySize) {
        super(type, pos, state, inventorySize);
    }

    @Override
    public Component getDisplayName() {
        return Component.empty();
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return null;
    }

    void move() {
        Level level = this.getLevel();
        BlockState state = this.getBlockState();
        Direction direction = state.getValue(DrillBaseBlock.FACING);
        BlockPos basePos = this.getBlockPos();
        BlockPos nextBasePos = basePos.relative(direction);

        this.move(level, state, basePos, nextBasePos, direction);

        level.playSound(null, basePos, SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    public void move(Level level, BlockState state, BlockPos basePos, BlockPos nextBasePos, Direction direction) {
        CompoundTag tag = new CompoundTag();
        this.saveAdditional(tag, level.registryAccess());

        level.removeBlockEntity(basePos);
        this.setRemoved();

        level.setBlockAndUpdate(nextBasePos, state);

        BlockEntity newBlockEntity = level.getBlockEntity(nextBasePos);
        if (newBlockEntity instanceof DrillBaseBlockEntity newDrillBaseBlockEntity) {
            newDrillBaseBlockEntity.loadAdditional(tag, level.registryAccess());
            newDrillBaseBlockEntity.setChanged();
        }

        level.removeBlock(basePos, false);
    }

    public void act() {}

    protected boolean takeItemFromCaterpillarConsumption(Item item) {
        if (item.equals(Items.AIR)) {
            return true;
        }

        Direction direction = this.getBlockState().getValue(DrillBaseBlock.FACING);
        List<? extends DrillBaseBlockEntity> drillHeadAndStorageBlockEntities = CaterpillarBlockUtil.getStorages(level, this.getBlockPos(), direction);

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
        Direction direction = this.getBlockState().getValue(DrillBaseBlock.FACING);
        List<? extends DrillBaseBlockEntity> drillHeadAndStorageBlockEntities = CaterpillarBlockUtil.getStorages(level, this.getBlockPos(), direction);

        if (drillHeadAndStorageBlockEntities == null || drillHeadAndStorageBlockEntities.size() == 0) {
            return stack;
        }

        // Check if drill head has same item in gathered slots
        for (int i = DrillHeadBlockEntity.GATHERED_SLOT_START; i <= DrillHeadBlockEntity.GATHERED_SLOT_END; i++) {
            ItemStack drillHeadItemStack = drillHeadAndStorageBlockEntities.get(0).getItem(i);
            if (!drillHeadItemStack.isEmpty() && ItemStack.isSameItemSameComponents(stack, drillHeadItemStack)) {
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
                if (!storageItemStack.isEmpty() && ItemStack.isSameItemSameComponents(stack, storageItemStack)) {
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

        Direction direction = this.getBlockState().getValue(DrillBaseBlock.FACING);
        List<? extends DrillBaseBlockEntity> drillHeadAndStorageBlockEntities = CaterpillarBlockUtil.getStorages(level, this.getBlockPos(), direction);

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

    @Override
    public void saveExtraData(FriendlyByteBuf buf) {
        buf.writeBlockPos(getBlockPos());
    }
}
