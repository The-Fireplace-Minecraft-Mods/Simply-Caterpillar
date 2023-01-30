package dev.the_fireplace.caterpillar.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class CollectorBlock extends AbstractCaterpillarBlock {
    public static final EnumProperty<DoubleBlockHalf> HALF = Properties.DOUBLE_BLOCK_HALF;

    private static final Map<Direction, VoxelShape> SHAPES_UPPER = new EnumMap<>(Direction.class);

    private static final Map<Direction, VoxelShape> SHAPES_LOWER = new EnumMap<>(Direction.class);

    private static final Optional<VoxelShape> SHAPE_UPPER = Stream.of(
            Block.createCuboidShape(6, 0, 0, 10, 6, 16),
            Block.createCuboidShape(0, 0, 0, 6, 16, 16),
            Block.createCuboidShape(10, 0, 0, 16, 16, 16),
            Block.createCuboidShape(6, 10, 0, 10, 16, 16),
            Block.createCuboidShape(6, 6, -15, 10, 10, 0)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR));

    private static final Optional<VoxelShape> SHAPE_LOWER = Stream.of(
            Block.createCuboidShape(0, 0, 12, 16, 4, 16),
            Block.createCuboidShape(0, 4, 12, 4, 12, 16),
            Block.createCuboidShape(0, 12, 12, 16, 16, 16),
            Block.createCuboidShape(3, 3, 5, 13, 13, 11),
            Block.createCuboidShape(6, 6, 1, 10, 16, 5),
            Block.createCuboidShape(0, 0, 11, 16, 16, 12),
            Block.createCuboidShape(12, 4, 12, 16, 12, 16)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR));

    public CollectorBlock(Settings properties) {
        super(properties);
        super.setDefaultState(getDefaultState().with(CollectorBlock.HALF, DoubleBlockHalf.LOWER));
        super.runCalculation(CollectorBlock.SHAPES_UPPER, CollectorBlock.SHAPE_UPPER.get());
        super.runCalculation(CollectorBlock.SHAPES_LOWER, CollectorBlock.SHAPE_LOWER.get());
    }

    @Override
    protected void appendProperties(StateManager.@NotNull Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(CollectorBlock.HALF);
    }

    @Override
    public @NotNull VoxelShape getOutlineShape(BlockState state, @NotNull BlockView level, @NotNull BlockPos pos, @NotNull ShapeContext context) {
        if (state.get(CollectorBlock.HALF) == DoubleBlockHalf.LOWER) {
            return CollectorBlock.SHAPES_LOWER.get(state.get(FACING));
        } else {
            return CollectorBlock.SHAPES_UPPER.get(state.get(FACING));
        }
    }

    @Override
    public void onBreak(@NotNull World level, @NotNull BlockPos pos, BlockState state, @NotNull PlayerEntity player) {
        DoubleBlockHalf half = state.get(CollectorBlock.HALF);

        if (half == DoubleBlockHalf.UPPER) {
            level.breakBlock(pos.down(), false);
        } else {
            level.breakBlock(pos.up(), false);
        }

        super.onBreak(level, pos, state, player);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        BlockPos blockPos = context.getBlockPos();
        World level = context.getWorld();
        Direction direction = context.getPlayerFacing();

        if (
                blockPos.getY() < level.getTopY() - 1 &&
                        level.getBlockState(blockPos.up()).canReplace(context)
        ) {
            return super.getDefaultState().with(FACING, direction.getOpposite()).with(CollectorBlock.HALF, DoubleBlockHalf.LOWER);
        }

        return null;
    }

    @Override
    public BlockPos getBasePos(BlockState state, BlockPos pos) {
        if (state.get(CollectorBlock.HALF) == DoubleBlockHalf.LOWER) {
            return pos.up();
        }
        return pos;
    }

    @Override
    public void onPlaced(World level, BlockPos blockPos, BlockState blockState, @Nullable LivingEntity livingEntity, @NotNull ItemStack stack) {
        level.setBlockState(blockPos.up(), blockState.with(CollectorBlock.HALF, DoubleBlockHalf.UPPER));

        super.onPlaced(level, blockPos, blockState, livingEntity, stack);
    }

    protected void runCalculation(Map<Direction, VoxelShape> shapes, VoxelShape shape) {
        for (Direction direction : Direction.values())
            shapes.put(direction, calculateShapes(direction, shape));
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return null;
    }
}
