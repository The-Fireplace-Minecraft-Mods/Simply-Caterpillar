package dev.the_fireplace.caterpillar.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import dev.the_fireplace.caterpillar.block.StorageBlock;
import dev.the_fireplace.caterpillar.block.util.StoragePart;
import dev.the_fireplace.caterpillar.config.CaterpillarConfig;
import dev.the_fireplace.caterpillar.registry.BlockEntityRegistry;

public class StorageBlockEntity extends DrillBaseBlockEntity {

    public static final int INVENTORY_SIZE = 18;

    public static final int CONSUMPTION_SLOT_START = 0;

    public static final int CONSUMPTION_SLOT_END = 8;

    public static final int GATHERED_SLOT_START = 9;

    public static final int GATHERED_SLOT_END = 17;

    public StorageBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.STORAGE.get(), pos, state, INVENTORY_SIZE);
    }

    public void move() {
        BlockPos basePos = this.getBlockPos();
        BlockPos nextPos = this.getBlockPos().relative(this.getBlockState().getValue(StorageBlock.FACING));
        BlockEntity blockEntity = this.getLevel().getBlockEntity(basePos);

        CompoundTag oldTag = this.saveWithFullMetadata();
        oldTag.remove("x");
        oldTag.remove("y");
        oldTag.remove("z");

        this.getLevel().setBlockAndUpdate(nextPos, this.getBlockState());
        BlockEntity nextBlockEntity = this.getLevel().getBlockEntity(nextPos);

        if (nextBlockEntity instanceof StorageBlockEntity nextStorageBlockEntity) {
            nextStorageBlockEntity.load(oldTag);

            nextStorageBlockEntity.load(oldTag);
            nextStorageBlockEntity.setChanged();

            this.getLevel().removeBlock(basePos, false);

            this.getLevel().setBlockAndUpdate(nextPos.relative(nextStorageBlockEntity.getBlockState().getValue(StorageBlock.FACING).getCounterClockWise()), nextStorageBlockEntity.getBlockState().setValue(StorageBlock.PART, StoragePart.LEFT));
            this.getLevel().setBlockAndUpdate(nextPos.relative(nextStorageBlockEntity.getBlockState().getValue(StorageBlock.FACING).getClockWise()), nextStorageBlockEntity.getBlockState().setValue(StorageBlock.PART, StoragePart.RIGHT));

            this.getLevel().removeBlock(basePos, false);
            this.getLevel().removeBlock(basePos.relative(blockEntity.getBlockState().getValue(StorageBlock.FACING).getCounterClockWise()), false);
            this.getLevel().removeBlock(basePos.relative(blockEntity.getBlockState().getValue(StorageBlock.FACING).getClockWise()), false);

            if (CaterpillarConfig.enableSounds) {
                this.getLevel().playSound(null, basePos, SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
        }
    }
}
