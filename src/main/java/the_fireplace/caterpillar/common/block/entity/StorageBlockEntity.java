package the_fireplace.caterpillar.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.state.BlockState;
import the_fireplace.caterpillar.common.block.StorageBlock;
import the_fireplace.caterpillar.common.block.util.StoragePart;
import the_fireplace.caterpillar.core.init.BlockEntityInit;

public class StorageBlockEntity extends AbstractCaterpillarBlockEntity {

    public static final int INVENTORY_SIZE = 18;


    public StorageBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.STORAGE.get(), pos, state, INVENTORY_SIZE);
    }

    public void move() {
        BlockPos nextPos = this.getBlockPos().relative(this.getBlockState().getValue(StorageBlock.FACING).getOpposite());

        this.getLevel().setBlockAndUpdate(nextPos, this.getBlockState());
        this.getLevel().setBlockAndUpdate(nextPos.relative(this.getBlockState().getValue(StorageBlock.FACING).getCounterClockWise()), this.getBlockState().setValue(StorageBlock.PART, StoragePart.LEFT));
        this.getLevel().setBlockAndUpdate(nextPos.relative(this.getBlockState().getValue(StorageBlock.FACING).getClockWise()), this.getBlockState().setValue(StorageBlock.PART, StoragePart.RIGHT));

        this.getLevel().removeBlock(this.getBlockPos(), false);
        this.getLevel().removeBlock(this.getBlockPos().relative(this.getBlockState().getValue(StorageBlock.FACING).getCounterClockWise()), false);
        this.getLevel().removeBlock(this.getBlockPos().relative(this.getBlockState().getValue(StorageBlock.FACING).getClockWise()), false);

        this.getLevel().playSound(null, this.getBlockPos(), SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS, 1.0F, 1.0F);
    }
}
