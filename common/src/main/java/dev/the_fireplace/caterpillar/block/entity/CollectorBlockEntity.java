package dev.the_fireplace.caterpillar.block.entity;

import dev.the_fireplace.caterpillar.block.CollectorBlock;
import dev.the_fireplace.caterpillar.block.util.CaterpillarBlockUtil;
import dev.the_fireplace.caterpillar.registry.BlockEntityTypesRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
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
    }

    @Override
    public void act() {
        this.collect();
    }

    private void collect() {
        Level level = this.getLevel();
        BlockPos pos = this.getBlockPos();
        BlockState state = this.getBlockState();
        Direction direction = state.getValue(CollectorBlock.FACING);

        BlockPos caterpillarHeadBlockPos = CaterpillarBlockUtil.getCaterpillarHeadPos(level, pos.relative(direction), direction);
        BlockEntity headBlockEntity = level.getBlockEntity(caterpillarHeadBlockPos);

        if (headBlockEntity instanceof DrillHeadBlockEntity drillHeadBlockEntity) {
            for (ItemEntity itemEntity : this.getItemsAround()) {
                ItemStack remainingStack = drillHeadBlockEntity.tryInsertItemToGathered(itemEntity.getItem());

                if (remainingStack.isEmpty()) {
                    itemEntity.discard();
                } else {
                    itemEntity.setItem(remainingStack);
                }
            }
        }
    }

    public List<ItemEntity> getItemsAround() {
        return this.getLevel().getEntitiesOfClass(ItemEntity.class, new AABB(this.getBlockPos()).inflate(2)).stream().toList();
    }
}