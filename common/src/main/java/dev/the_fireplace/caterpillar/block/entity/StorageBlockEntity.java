package dev.the_fireplace.caterpillar.block.entity;

import dev.the_fireplace.caterpillar.block.StorageBlock;
import dev.the_fireplace.caterpillar.block.util.StoragePart;
import dev.the_fireplace.caterpillar.registry.BlockEntityTypesRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class StorageBlockEntity extends DrillBaseBlockEntity {

    public static final int INVENTORY_SIZE = 18;

    public static final int CONSUMPTION_SLOT_START = 0;

    public static final int CONSUMPTION_SLOT_END = 8;

    public static final int GATHERED_SLOT_START = 9;

    public static final int GATHERED_SLOT_END = 17;

    public StorageBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityTypesRegistry.STORAGE.get(), pos, state, INVENTORY_SIZE);
    }

    @Override
    public void move(Level level, BlockState state, BlockPos basePos, BlockPos nextBasePos, Direction direction) {
        super.move(level, state, basePos, nextBasePos, direction);
        this.moveMultiblock(level, state, basePos, nextBasePos, direction);
    }

    private void moveMultiblock(Level level, BlockState state, BlockPos basePos, BlockPos nextBasePos, Direction direction) {
        level.setBlockAndUpdate(nextBasePos.relative(direction.getCounterClockWise()), state.setValue(StorageBlock.PART, StoragePart.LEFT));
        level.setBlockAndUpdate(nextBasePos.relative(direction.getClockWise()), state.setValue(StorageBlock.PART, StoragePart.RIGHT));

        level.removeBlock(basePos.relative(direction.getCounterClockWise()), false);
        level.removeBlock(basePos.relative(direction.getClockWise()), false);
    }
}
