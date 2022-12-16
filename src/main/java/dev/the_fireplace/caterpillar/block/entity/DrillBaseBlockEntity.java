package dev.the_fireplace.caterpillar.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.state.BlockState;
import dev.the_fireplace.caterpillar.block.DrillBaseBlock;
import dev.the_fireplace.caterpillar.config.CaterpillarConfig;
import dev.the_fireplace.caterpillar.init.BlockEntityInit;

public class DrillBaseBlockEntity extends AbstractCaterpillarBlockEntity {

    public static final int INVENTORY_SIZE = 0;

    public DrillBaseBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.DRILL_BASE.get(), pos, state, INVENTORY_SIZE);
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
}
