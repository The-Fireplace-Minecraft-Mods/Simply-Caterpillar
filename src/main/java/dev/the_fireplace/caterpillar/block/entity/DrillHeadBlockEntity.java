package dev.the_fireplace.caterpillar.block.entity;

import dev.the_fireplace.caterpillar.init.BlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class DrillHeadBlockEntity extends AbstractCaterpillarBlockEntity {
    public DrillHeadBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntityInit.DRILL_HEAD, blockPos, blockState);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, DrillHeadBlockEntity blockEntity) {

    }
}
