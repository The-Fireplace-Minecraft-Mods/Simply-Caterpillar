package dev.the_fireplace.caterpillar.block;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.stream.Stream;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

public class DrillBaseBlock extends AbstractCaterpillarBlock {

    private static final Optional<VoxelShape> SHAPE = Stream.of(
            Block.createCuboidShape(6, 0, 0, 10, 6, 16),
            Block.createCuboidShape(0, 0, 0, 6, 16, 16),
            Block.createCuboidShape(6, 6, -15, 10, 10, 0),
            Block.createCuboidShape(10, 0, 0, 16, 16, 16),
            Block.createCuboidShape(6, 10, 0, 10, 16, 16)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR));

    public DrillBaseBlock(Settings properties) {
        super(properties);
        super.runCalculation(SHAPES, SHAPE.get());
    }

    @Override
    public @NotNull BlockRenderType getRenderType(@NotNull BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return null;
    }
}