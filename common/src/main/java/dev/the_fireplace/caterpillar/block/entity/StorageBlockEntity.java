package dev.the_fireplace.caterpillar.block.entity;

import dev.the_fireplace.caterpillar.registry.BlockEntityTypesRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class StorageBlockEntity extends DrillBaseBlockEntity {

    public static final int INVENTORY_SIZE = 18;

    public static final int CONSUMPTION_SLOT_START = 0;

    public static final int CONSUMPTION_SLOT_END = 8;

    public static final int GATHERED_SLOT_START = 9;

    public static final int GATHERED_SLOT_END = 17;

    public StorageBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityTypesRegistry.STORAGE.get(), pos, state, INVENTORY_SIZE);
    }
}
