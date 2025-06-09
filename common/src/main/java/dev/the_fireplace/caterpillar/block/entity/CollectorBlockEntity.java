package dev.the_fireplace.caterpillar.block.entity;

import dev.the_fireplace.caterpillar.registry.BlockEntityTypesRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class CollectorBlockEntity extends DrillBaseBlockEntity {

    public static final int INVENTORY_SIZE = 0;

    public CollectorBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityTypesRegistry.COLLECTOR.get(), pos, state, INVENTORY_SIZE);
    }

    public void move() {

    }

    private void collect() {

    }

    public List<ItemEntity> getItemsAround() {
        return this.getLevel().getEntitiesOfClass(ItemEntity.class, new AABB(this.getBlockPos()).inflate(2)).stream().toList();
    }
}