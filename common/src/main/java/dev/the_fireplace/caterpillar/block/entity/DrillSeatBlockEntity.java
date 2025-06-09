package dev.the_fireplace.caterpillar.block.entity;

import dev.the_fireplace.caterpillar.registry.BlockEntityTypesRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;


public class DrillSeatBlockEntity extends DrillBaseBlockEntity {

    public static final int INVENTORY_SIZE = 0;
    public DrillSeatBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityTypesRegistry.DRILL_SEAT.get(), pos, state, INVENTORY_SIZE);
    }
}
