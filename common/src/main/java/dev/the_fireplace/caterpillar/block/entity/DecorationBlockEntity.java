package dev.the_fireplace.caterpillar.block.entity;

import dev.the_fireplace.caterpillar.Constants;
import dev.the_fireplace.caterpillar.block.DecorationBlock;
import dev.the_fireplace.caterpillar.block.util.DecorationPart;
import dev.the_fireplace.caterpillar.registry.BlockEntityTypesRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class DecorationBlockEntity extends DrillBaseBlockEntity {

    public static final Component TITLE = Component.translatable(
            "container." + Constants.MOD_ID + ".decoration"
    );

    public DecorationBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityTypesRegistry.DECORATION.get(), pos, state, INVENTORY_SIZE);
    }

    @Override
    public void move(Level level, BlockState state, BlockPos basePos, BlockPos nextBasePos, Direction direction) {
        super.move(level, state, basePos, nextBasePos, direction);
        this.moveMultiblock(level, state, basePos, nextBasePos, direction);
    }

    private void moveMultiblock(Level level, BlockState state, BlockPos basePos, BlockPos nextBasePos, Direction direction) {
        level.setBlockAndUpdate(nextBasePos.relative(direction.getCounterClockWise()), state.setValue(DecorationBlock.PART, DecorationPart.LEFT));
        level.setBlockAndUpdate(nextBasePos.relative(direction.getClockWise()), state.setValue(DecorationBlock.PART, DecorationPart.RIGHT));

        level.removeBlock(basePos.relative(direction.getCounterClockWise()), false);
        level.removeBlock(basePos.relative(direction.getClockWise()), false);
    }
}