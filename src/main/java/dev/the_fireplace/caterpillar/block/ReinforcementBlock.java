package dev.the_fireplace.caterpillar.block;

import dev.the_fireplace.caterpillar.block.util.ReinforcementPart;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
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

public class ReinforcementBlock extends AbstractCaterpillarBlock {
    public static final EnumProperty<ReinforcementPart> PART = EnumProperty.of("part", ReinforcementPart.class);

    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    private static final Map<Direction, VoxelShape> SHAPES_LEFT = new EnumMap<>(Direction.class);

    private static final Map<Direction, VoxelShape> SHAPES_BASE = new EnumMap<>(Direction.class);

    private static final Map<Direction, VoxelShape> SHAPES_RIGHT = new EnumMap<>(Direction.class);

    private static final Map<Direction, VoxelShape> SHAPES_TOP = new EnumMap<>(Direction.class);

    private static final Map<Direction, VoxelShape> SHAPES_BOTTOM = new EnumMap<>(Direction.class);

    private static final Optional<VoxelShape> SHAPE_LEFT = Stream.of(
            Block.createCuboidShape(0, 0, 0, 1, 16, 16),
            Block.createCuboidShape(1, 6, 6, 16, 10, 10)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR));

    private static final Optional<VoxelShape> SHAPE_BASE = Stream.of(
            Block.createCuboidShape(6, 6, -15, 10, 10, 0),
            Block.createCuboidShape(0, 0, 0, 6, 16, 16),
            Block.createCuboidShape(10, 0, 0, 16, 16, 16),
            Block.createCuboidShape(6, 10, 0, 10, 16, 16),
            Block.createCuboidShape(6, 0, 0, 10, 6, 16)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR));

    private static final Optional<VoxelShape> SHAPE_RIGHT = Stream.of(
            Block.createCuboidShape(0, 6, 6, 15, 10, 10),
            Block.createCuboidShape(15, 0, 0, 16, 16, 16)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR));

    private static final Optional<VoxelShape> SHAPE_TOP = Stream.of(
            Block.createCuboidShape(6, 0, 6, 10, 15, 10),
            Block.createCuboidShape(0, 15, 0, 16, 16, 16)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR));

    private static final Optional<VoxelShape> SHAPE_BOTTOM = Stream.of(
            Block.createCuboidShape(6, 1, 6, 10, 16, 10),
            Block.createCuboidShape(0, 0, 0, 16, 1, 16)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR));

    public ReinforcementBlock(Settings properties) {
        super(properties);
        super.setDefaultState(super.getDefaultState().with(ReinforcementBlock.PART, ReinforcementPart.BOTTOM).with(ReinforcementBlock.WATERLOGGED, true));
        super.runCalculation(ReinforcementBlock.SHAPES_LEFT, ReinforcementBlock.SHAPE_LEFT.get());
        super.runCalculation(ReinforcementBlock.SHAPES_BASE, ReinforcementBlock.SHAPE_BASE.get());
        super.runCalculation(ReinforcementBlock.SHAPES_RIGHT, ReinforcementBlock.SHAPE_RIGHT.get());
        super.runCalculation(ReinforcementBlock.SHAPES_TOP, ReinforcementBlock.SHAPE_TOP.get());
        super.runCalculation(ReinforcementBlock.SHAPES_BOTTOM, ReinforcementBlock.SHAPE_BOTTOM.get());
    }

    @Override
    protected void appendProperties(StateManager.@NotNull Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(ReinforcementBlock.PART, ReinforcementBlock.WATERLOGGED);
    }

    @Override
    public @NotNull VoxelShape getOutlineShape(BlockState state, @NotNull BlockView level, @NotNull BlockPos pos, @NotNull ShapeContext context) {
        return switch (state.get(ReinforcementBlock.PART)) {
            case LEFT -> ReinforcementBlock.SHAPES_LEFT.get(state.get(FACING));
            case RIGHT -> ReinforcementBlock.SHAPES_RIGHT.get(state.get(FACING));
            case TOP -> ReinforcementBlock.SHAPES_TOP.get(state.get(FACING));
            case BOTTOM -> ReinforcementBlock.SHAPES_BOTTOM.get(state.get(FACING));
            default -> ReinforcementBlock.SHAPES_BASE.get(state.get(FACING));
        };
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        BlockPos blockPos = context.getBlockPos();
        World level = context.getWorld();
        Direction direction = context.getPlayerFacing();

        return getDefaultState().with(FACING, direction.getOpposite()).with(ReinforcementBlock.PART, ReinforcementPart.BOTTOM).with(DrillHeadBlock.WATERLOGGED, context.getWorld().getFluidState(context.getBlockPos()).getFluid() == Fluids.WATER);
    }

    @Override
    public void onPlaced(World level, BlockPos blockPos, BlockState blockState, @Nullable LivingEntity livingEntity, @NotNull ItemStack stack) {
        Direction direction = blockState.get(FACING);

        level.setBlockState(blockPos.up().offset(direction.rotateYCounterclockwise()), blockState.with(PART, ReinforcementPart.LEFT));
        level.setBlockState(blockPos.up().offset(direction.rotateYClockwise()), blockState.with(ReinforcementBlock.PART, ReinforcementPart.RIGHT));
        level.setBlockState(blockPos.up(), blockState.with(ReinforcementBlock.PART, ReinforcementPart.BASE));
        level.setBlockState(blockPos.up(2), blockState.with(ReinforcementBlock.PART, ReinforcementPart.TOP));

        super.onPlaced(level, blockPos, blockState, livingEntity, stack);
    }

    @Override
    public void onBreak(World level, @NotNull BlockPos pos, BlockState state, PlayerEntity player) {
        Direction direction = state.get(FACING);
        BlockPos basePos = getBasePos(state, pos);

        level.breakBlock(basePos, !player.isCreative());
        level.breakBlock(basePos.offset(direction.rotateYCounterclockwise()), false);
        level.breakBlock(basePos.offset(direction.rotateYClockwise()), false);
        level.breakBlock(basePos.up(), false);
        level.breakBlock(basePos.down(), false);

        super.onBreak(level, pos, state, player);
    }

    public BlockPos getBasePos(BlockState state, BlockPos pos) {
        Direction direction = state.get(FACING);
        ReinforcementPart part = state.get(ReinforcementBlock.PART);

        return switch (part) {
            case LEFT -> pos.offset(direction.rotateYClockwise());
            case RIGHT -> pos.offset(direction.rotateYCounterclockwise());
            case TOP -> pos.down();
            case BOTTOM -> pos.up();
            default -> pos;
        };
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return null;
    }
}
