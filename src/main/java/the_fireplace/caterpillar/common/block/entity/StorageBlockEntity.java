package the_fireplace.caterpillar.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.state.BlockState;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.common.block.StorageBlock;
import the_fireplace.caterpillar.common.block.entity.util.InventoryBlockEntity;
import the_fireplace.caterpillar.common.block.util.StoragePart;
import the_fireplace.caterpillar.common.container.StorageContainer;
import the_fireplace.caterpillar.core.init.BlockEntityInit;

import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;

public class StorageBlockEntity extends InventoryBlockEntity {

    public static final Component TITLE = Component.translatable(
            "container." + Caterpillar.MOD_ID + ".storage"
    );

    public StorageBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.STORAGE.get(), pos, state, StorageContainer.SLOT_SIZE);
    }

    public void move() {
        BlockPos nextPos = this.getBlockPos().relative(this.getBlockState().getValue(FACING).getOpposite());

        this.getLevel().setBlock(nextPos, this.getBlockState(), 35);
        this.getLevel().setBlock(nextPos.relative(this.getBlockState().getValue(FACING).getCounterClockWise()), this.getBlockState().setValue(StorageBlock.PART, StoragePart.LEFT), 3);
        this.getLevel().setBlock(nextPos.relative(this.getBlockState().getValue(FACING).getClockWise()), this.getBlockState().setValue(StorageBlock.PART, StoragePart.RIGHT), 3);

        this.getLevel().destroyBlock(this.getBlockPos(), false);
        this.getLevel().destroyBlock(this.getBlockPos().relative(this.getBlockState().getValue(FACING).getCounterClockWise()), false);
        this.getLevel().destroyBlock(this.getBlockPos().relative(this.getBlockState().getValue(FACING).getClockWise()), false);

        this.getLevel().playSound(null, this.getBlockPos(), SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS, 1.0F, 1.0F);
    }
}
