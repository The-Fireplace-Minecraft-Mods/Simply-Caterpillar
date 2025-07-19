package dev.the_fireplace.caterpillar.block.entity;

import dev.the_fireplace.caterpillar.Constants;
import dev.the_fireplace.caterpillar.block.DrillBaseBlock;
import dev.the_fireplace.caterpillar.inventory.DrillHeadMenu;
import dev.the_fireplace.caterpillar.inventory.data.DrillHeadContainerData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.FuelValues;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import dev.the_fireplace.caterpillar.block.DrillHeadBlock;
import dev.the_fireplace.caterpillar.block.util.CaterpillarBlockUtil;
import dev.the_fireplace.caterpillar.registry.BlockEntityTypesRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DrillHeadBlockEntity extends DrillBaseBlockEntity {

    public static final Component TITLE = Component.translatable("container." + Constants.MOD_ID + ".drill_head");
    public static final Component CONSUMPTION_TITLE = Component.translatable("container." + Constants.MOD_ID + ".drill_head.consumption");
    public static final Component GATHERED_TITLE = Component.translatable("container." + Constants.MOD_ID + ".drill_head.gathered");

    public static final int CONSUMPTION_SLOT_START = 1;
    public static final int CONSUMPTION_SLOT_END = 9;
    public static final int FUEL_SLOT = 0;
    public static final int GATHERED_SLOT_START = 10;
    public static final int GATHERED_SLOT_END = 18;

    // 60 ticks equals 3 seconds
    public static final int DRILL_HEAD_MOVEMENT_TICK = 60;
    public static final int DRILL_PARTS_MOVEMENT_TICK = 20;

    public static final int INVENTORY_SIZE = 19;
    public static final int CONTAINER_DATA_SIZE = 4;

    public int litTime;
    public int litDuration;
    public boolean powered;
    public boolean moving;
    private int timer;

    protected final ContainerData dataAccess;

    public DrillHeadBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityTypesRegistry.DRILL_HEAD.get(), pos, state, INVENTORY_SIZE);

        this.dataAccess = new DrillHeadContainerData(this, CONTAINER_DATA_SIZE);

        this.timer = 0;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.litDuration = tag.getShortOr("lit_duration", (short) 0);
        this.litTime = tag.getShortOr("lit_time", (short) 0);
        this.powered = tag.getBooleanOr("powered", false);
        this.moving = tag.getBooleanOr("moving", false);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putShort("lit_duration", (short) this.litDuration);
        tag.putShort("lit_time", (short) this.litTime);
        tag.putBoolean("powered", this.powered);
        tag.putBoolean("moving", this.moving);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, DrillHeadBlockEntity blockEntity) {
        boolean needsUpdate = false;

        if (blockEntity.isPowered() && blockEntity.isLit()) {
            blockEntity.timer++;

            List<DrillBaseBlock> connectedDrillBlocks = CaterpillarBlockUtil.getConnectedCaterpillarBlocks(level, pos);

            blockEntity.litTime -= connectedDrillBlocks.size();

            needsUpdate = true;
        }

        if (blockEntity.isMoving() && blockEntity.timer % DRILL_PARTS_MOVEMENT_TICK == 0) {
            Direction direction = state.getValue(DrillHeadBlock.FACING);

            List<DrillBaseBlockEntity> connectedDrillBaseBlockEntities = CaterpillarBlockUtil.getConnectedCaterpillarBlockEntities(level, pos);
            DrillBaseBlockEntity lastBlockEntity = connectedDrillBaseBlockEntities.get(connectedDrillBaseBlockEntities.size() - 1);
            BlockEntity splitedBlockEntity = level.getBlockEntity(lastBlockEntity.getBlockPos().relative(direction.getOpposite(), 2));

            if (splitedBlockEntity instanceof DrillBaseBlockEntity chainedBlockEntity) {
                chainedBlockEntity.move();
                chainedBlockEntity.act();
            } else {
                blockEntity.setMoving(false);
                blockEntity.timer = 0;
            }

            needsUpdate = true;
        }

        if (blockEntity.isPowered() && blockEntity.isLit() && !blockEntity.isMoving()) {
            if (!state.getValue(DrillHeadBlock.DRILLING)) {
                state = state.setValue(DrillHeadBlock.DRILLING, true);
                DrillHeadBlock.updateDrillingState(level, pos, state);
            }

            if (blockEntity.timer != 0 && blockEntity.timer % DRILL_HEAD_MOVEMENT_TICK == 0) {
                blockEntity.act();

                if (state.getValue(DrillHeadBlock.DRILLING)) {
                    state = state.setValue(DrillHeadBlock.DRILLING, false);
                    DrillHeadBlock.updateDrillingState(level, pos, state);
                }

                if (blockEntity.isPowered()) {
                    blockEntity.move();
                }
            }

            needsUpdate = true;
        }

        ItemStack stack = blockEntity.getItem(DrillHeadBlockEntity.FUEL_SLOT);
        boolean fuelSlotIsEmpty = stack.isEmpty();

        if (blockEntity.isPowered() && blockEntity.getLitTime() <= 0 && !fuelSlotIsEmpty) {
            blockEntity.litTime = blockEntity.getBurnDuration(level.fuelValues(), stack);
            blockEntity.litDuration = blockEntity.litTime;

            if (stack.is(Items.LAVA_BUCKET)) {
                blockEntity.setItem(FUEL_SLOT, new ItemStack(Items.BUCKET));
            }

            stack.shrink(1);

            needsUpdate = true;
        }

        if (blockEntity.isPowered() && !blockEntity.isLit() && fuelSlotIsEmpty) {
            blockEntity.setPowerOff();

            needsUpdate = true;
        }

        if (needsUpdate) {
            setChanged(level, pos, state);
        }
    }

    protected int getBurnDuration(FuelValues fuelValues, ItemStack stack) {
        return fuelValues.burnDuration(stack);
    }

    @Override
    public void move(Level level, BlockState state, BlockPos basePos, BlockPos nextBasePos, Direction direction) {
        this.setMoving(true);

        super.move(level, state, basePos, nextBasePos, direction);

        DrillHeadBlock.removeStructure(level, basePos, state, direction);
        DrillHeadBlock.moveStructure(level, nextBasePos, state, direction);
    }

    @Override
    public void act() {
        this.drill();
    }

    public void drill() {
        Level level = this.getLevel();
        BlockPos pos = this.getBlockPos();
        BlockState state = this.getBlockState();
        Direction direction = state.getValue(DrillHeadBlock.FACING);
        BlockPos destroyPos;

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                destroyPos = switch (direction) {
                    case EAST -> pos.offset(2, i, j);
                    case WEST -> pos.offset(-2, i, j);
                    case SOUTH -> pos.offset(j, i, 2);
                    default -> pos.offset(j, i, -2);
                };

                BlockState blockState = level.getBlockState(destroyPos);
                if (CaterpillarBlockUtil.canBreakBlock(blockState.getBlock()) && CaterpillarBlockUtil.isBlockBreakable(level, destroyPos, blockState)) {
                    level.destroyBlock(destroyPos, true);
                }
            }
        }
    }

    public ItemStack tryInsertItemToGathered(ItemStack stack) {
        Level level = this.getLevel();
        BlockPos pos = this.getBlockPos();
        BlockState state  = this.getBlockState();
        Direction direction = state.getValue(DrillHeadBlock.FACING);

        List<DrillBaseBlockEntity> storagesBlockEntities = CaterpillarBlockUtil.getStorages(level, pos, direction);

        if ( storagesBlockEntities == null || storagesBlockEntities.isEmpty()) return stack;

        // Try to merge the stack into existing slots
        stack = CaterpillarBlockUtil.tryMergeInItem(stack, storagesBlockEntities.getFirst(), DrillHeadBlockEntity.GATHERED_SLOT_START, DrillHeadBlockEntity.GATHERED_SLOT_END);

        if (storagesBlockEntities.size() == 2 && !stack.isEmpty()) {
            stack = CaterpillarBlockUtil.tryMergeInItem(stack, storagesBlockEntities.getLast(), StorageBlockEntity.GATHERED_SLOT_START, StorageBlockEntity.GATHERED_SLOT_END);
        }

        // If the stack is still not empty, try to insert it into empty slots
        if (!stack.isEmpty()) {
            stack = CaterpillarBlockUtil.tryInsertInEmpty(stack, storagesBlockEntities.getFirst(), DrillHeadBlockEntity.GATHERED_SLOT_START, DrillHeadBlockEntity.GATHERED_SLOT_END);
        }

        if (storagesBlockEntities.size() == 2 && !stack.isEmpty()) {
            stack = CaterpillarBlockUtil.tryInsertInEmpty(stack, storagesBlockEntities.getLast(), StorageBlockEntity.GATHERED_SLOT_START, StorageBlockEntity.GATHERED_SLOT_END);
        }

        for (DrillBaseBlockEntity storage : storagesBlockEntities) {
            if (storage instanceof StorageBlockEntity storageEntity) {
                storageEntity.setChanged();
            }
        }

        return stack;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return TITLE;
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new DrillHeadMenu(containerId, playerInventory, this, this.dataAccess);
    }

    public boolean isLit() {
        return this.litTime > 0;
    }

    public int getLitTime() {
        return this.litTime;
    }

    public int getLitDuration() {
        return this.litDuration;
    }

    public int getLitProgress() {
        int i = this.getLitDuration();

        if (i == 0) {
            i = 200;
        }

        return this.getLitTime() * 13 / i;
    }

    public void setLitTime(int litTime) {
        this.litTime = litTime;
        this.setChanged();
    }

    public void setLitDuration(int litDuration) {
        this.litDuration = litDuration;
        this.setChanged();
    }

    public boolean isMoving() {
        return this.moving;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
        this.setChanged();
    }

    public boolean isPowered() {
        return this.powered;
    }

    public void togglePower() {
        if (this.powered) {
            this.setPowerOff();
        } else {
           this.setPowerOn();
        }
    }

    protected void setPowerOff() {
        BlockPos pos = this.getBlockPos();
        BlockState state = this.getBlockState();

        this.powered = false;

        if (state.getValue(DrillHeadBlock.DRILLING)) {
            state = state.setValue(DrillHeadBlock.DRILLING, false);
            DrillHeadBlock.updateDrillingState(level, pos, state);
        }

        this.setChanged();
    }

    protected void setPowerOn() {
        ItemStack fuelStack = this.getItem(FUEL_SLOT);
        boolean isFuelStackEmpty = fuelStack.isEmpty();

        if (!isFuelStackEmpty || this.isLit()) {
            if (!this.isLit()) {
                this.litTime = this.getBurnDuration(this.getLevel().fuelValues(), fuelStack);
                this.litDuration = this.litTime;

                if (fuelStack.is(Items.LAVA_BUCKET)) {
                    this.setItem(FUEL_SLOT, new ItemStack(Items.BUCKET));
                }

                fuelStack.shrink(1);
            }

            this.powered = true;
            this.setChanged();
        }
    }
}
