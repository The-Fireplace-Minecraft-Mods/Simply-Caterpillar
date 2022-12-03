package dev.the_fireplace.caterpillar.block.entity;

import dev.the_fireplace.caterpillar.init.BlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class DrillBaseBlockEntity extends AbstractCaterpillarBlockEntity {
    public DrillBaseBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntityInit.DRILL_BASE, blockPos, blockState);
    }
}
