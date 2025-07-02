package dev.the_fireplace.caterpillar.block.entity;

import dev.the_fireplace.caterpillar.block.CollectorBlock;
import dev.the_fireplace.caterpillar.registry.BlockEntityTypesRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class CollectorBlockEntity extends DrillBaseBlockEntity {

    public static final int INVENTORY_SIZE = 0;

    public CollectorBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityTypesRegistry.COLLECTOR.get(), pos, state, INVENTORY_SIZE);
    }

    @Override
    public void move(Level level, BlockState state, BlockPos basePos, BlockPos nextBasePos, Direction direction) {
        super.move(level, state, basePos, nextBasePos, direction);
        level.setBlockAndUpdate(nextBasePos.below(), state.setValue(CollectorBlock.HALF, DoubleBlockHalf.LOWER));
        level.removeBlock(basePos.below(), false);

        BlockEntity newBlockEntity = level.getBlockEntity(nextBasePos);
        if (newBlockEntity instanceof CollectorBlockEntity collector) {
            collector.collect();
        }
    }

    private void collect() {
        // TODO: implement item drops collect logic
    }

    public List<ItemEntity> getItemsAround() {
        return this.getLevel().getEntitiesOfClass(ItemEntity.class, new AABB(this.getBlockPos()).inflate(2)).stream().toList();
    }
}