package dev.the_fireplace.caterpillar.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import dev.the_fireplace.caterpillar.init.BlockEntityInit;

import java.util.stream.Stream;

public class DrillBaseBlock extends AbstractCaterpillarBlock {
    private static final VoxelShape SHAPE = Stream.of(
        Block.box(6, 0, 0, 10, 6, 16),
        Block.box(10, 0, 0, 16, 16, 16),
        Block.box(0, 0, 0, 6, 16, 16),
        Block.box(6, 10, 0, 10, 16, 16),
        Block.box(6, 6, 16, 10, 10, 31)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    public DrillBaseBlock(Properties properties) {
        super(properties);
        super.runCalculation(SHAPES, SHAPE);
    }

    @Override
    public BlockPos getBasePos(BlockState state, BlockPos pos) {
        return pos;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return BlockEntityInit.DRILL_BASE.get().create(pos, state);
    }
}