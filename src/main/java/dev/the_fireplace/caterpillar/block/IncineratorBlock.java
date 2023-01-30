package dev.the_fireplace.caterpillar.block;

import dev.the_fireplace.caterpillar.init.BlockInit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Stream;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;

public class IncineratorBlock extends AbstractCaterpillarBlock {

    private static final Optional<VoxelShape> SHAPE = Stream.of(
            Block.createCuboidShape(6, 0, 0, 10, 6, 16),
            Block.createCuboidShape(0, 0, 0, 6, 16, 16),
            Block.createCuboidShape(6, 6, -15, 10, 10, 0),
            Block.createCuboidShape(10, 0, 0, 16, 16, 16),
            Block.createCuboidShape(6, 10, 0, 10, 16, 16)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR));

    public IncineratorBlock(Settings properties) {
        super(properties);
        super.runCalculation(SHAPES, IncineratorBlock.SHAPE.get());
    }

    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext context) {
        BlockPos blockPos = context.getBlockPos();
        World level = context.getWorld();
        Direction direction = context.getPlayerFacing();

        return super.getPlacementState(context);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return null;
    }
}
