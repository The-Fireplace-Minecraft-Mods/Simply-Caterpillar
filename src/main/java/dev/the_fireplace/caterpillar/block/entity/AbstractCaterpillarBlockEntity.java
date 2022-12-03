package dev.the_fireplace.caterpillar.block.entity;

import dev.the_fireplace.caterpillar.block.entity.util.ImplementedInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class AbstractCaterpillarBlockEntity extends BlockEntity implements ImplementedInventory {
    public AbstractCaterpillarBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }
}
