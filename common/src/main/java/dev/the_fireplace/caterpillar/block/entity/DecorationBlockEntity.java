package dev.the_fireplace.caterpillar.block.entity;

import dev.the_fireplace.caterpillar.Constants;
import dev.the_fireplace.caterpillar.registry.BlockEntityTypesRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;

public class DecorationBlockEntity extends DrillBaseBlockEntity {

    public static final Component TITLE = Component.translatable(
            "container." + Constants.MOD_ID + ".decoration"
    );

    public DecorationBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityTypesRegistry.DECORATION.get(), pos, state, INVENTORY_SIZE);
    }
}