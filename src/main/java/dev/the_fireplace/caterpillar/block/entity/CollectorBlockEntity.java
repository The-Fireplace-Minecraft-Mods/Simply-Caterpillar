package dev.the_fireplace.caterpillar.block.entity;

import dev.the_fireplace.caterpillar.block.IncineratorBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.AABB;
import dev.the_fireplace.caterpillar.block.CollectorBlock;
import dev.the_fireplace.caterpillar.block.util.CaterpillarBlockUtil;
import dev.the_fireplace.caterpillar.config.CaterpillarConfig;
import dev.the_fireplace.caterpillar.init.BlockEntityInit;

import java.util.List;

public class CollectorBlockEntity extends AbstractCaterpillarBlockEntity {

    public static final int INVENTORY_SIZE = 0;

    public CollectorBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.COLLECTOR.get(), pos, state, INVENTORY_SIZE);
    }

    public void move() {
        BlockPos basePos = this.getBlockPos();
        BlockPos nextPos = basePos.relative(this.getBlockState().getValue(IncineratorBlock.FACING));

        this.getLevel().setBlockAndUpdate(nextPos, this.getBlockState());
        this.getLevel().setBlockAndUpdate(nextPos.below(), this.getBlockState().setValue(CollectorBlock.HALF, DoubleBlockHalf.LOWER));

        if (CaterpillarConfig.enableSounds) {
            this.getLevel().playSound(null, basePos, SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS, 1.0F, 1.0F);
        }

        BlockEntity nextBlockEntity = this.getLevel().getBlockEntity(nextPos);
        if (nextBlockEntity instanceof CollectorBlockEntity nextCollectorBlockEntity) {
            this.getLevel().removeBlock(basePos, false);
            this.getLevel().removeBlock(basePos.below(), false);

            nextCollectorBlockEntity.collect();
        }
    }

    private void collect() {
        for(ItemEntity itemEntity : getItemsAround()) {
            ItemStack remainderStack = super.insertItemStackToCaterpillarGathered(itemEntity.getItem());
            itemEntity.setItem(remainderStack);
        }
    }

    public List<ItemEntity> getItemsAround() {
        return this.getLevel().getEntitiesOfClass(ItemEntity.class, new AABB(this.getBlockPos()).inflate(2)).stream().toList();
    }
}