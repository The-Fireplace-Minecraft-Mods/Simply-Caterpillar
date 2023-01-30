package dev.the_fireplace.caterpillar.block;

import dev.the_fireplace.caterpillar.block.util.DecorationPart;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
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

public class DecorationBlock extends AbstractCaterpillarBlock {


    public static final EnumProperty<DecorationPart> PART = EnumProperty.of("part", DecorationPart.class);

    private static final Map<Direction, VoxelShape> SHAPES_LEFT = new EnumMap<>(Direction.class);

    private static final Map<Direction, VoxelShape> SHAPES_BASE = new EnumMap<>(Direction.class);

    private static final Map<Direction, VoxelShape> SHAPES_RIGHT = new EnumMap<>(Direction.class);

    private static final Optional<VoxelShape> SHAPE_LEFT = Stream.of(
            Block.createCuboidShape(0, 0, 0, 6, 16, 16),
            Block.createCuboidShape(6, 6, 6, 16, 10, 10)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR));

    private static final Optional<VoxelShape> SHAPE_BASE = Stream.of(
            Block.createCuboidShape(6, 6, -15, 10, 10, 0),
            Block.createCuboidShape(0, 0, 0, 6, 16, 16),
            Block.createCuboidShape(10, 0, 0, 16, 16, 16),
            Block.createCuboidShape(6, 10, 0, 10, 16, 16),
            Block.createCuboidShape(6, 0, 0, 10, 6, 16)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR));

    private static final Optional<VoxelShape> SHAPE_RIGHT = Stream.of(
            Block.createCuboidShape(0, 6, 6, 10, 10, 10),
            Block.createCuboidShape(10, 0, 0, 16, 16, 16)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR));

    public DecorationBlock(Settings properties) {
        super(properties);
        super.setDefaultState(getDefaultState().with(DecorationBlock.PART, DecorationPart.BASE));
        super.runCalculation(DecorationBlock.SHAPES_LEFT, DecorationBlock.SHAPE_LEFT.get());
        super.runCalculation(DecorationBlock.SHAPES_BASE, DecorationBlock.SHAPE_BASE.get());
        super.runCalculation(DecorationBlock.SHAPES_RIGHT, DecorationBlock.SHAPE_RIGHT.get());
    }

    @Override
    protected void appendProperties(StateManager.@NotNull Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(DecorationBlock.PART);
    }

    @Override
    public @NotNull VoxelShape getOutlineShape(BlockState state, @NotNull BlockView level, @NotNull BlockPos pos, @NotNull ShapeContext context) {
        return switch (state.get(DecorationBlock.PART)) {
            case LEFT -> DecorationBlock.SHAPES_LEFT.get(state.get(FACING));
            case RIGHT -> DecorationBlock.SHAPES_RIGHT.get(state.get(FACING));
            default -> DecorationBlock.SHAPES_BASE.get(state.get(FACING));
        };
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        BlockPos blockPos = context.getBlockPos();
        World level = context.getWorld();
        Direction direction = context.getPlayerFacing();

        return super.getDefaultState().with(FACING, direction.getOpposite()).with(PART, DecorationPart.BASE);
    }

    @Override
    public void onPlaced(World level, BlockPos pos, BlockState state, @Nullable LivingEntity livingEntity, @NotNull ItemStack stack) {
        Direction direction = state.get(FACING);

        level.setBlockState(pos.offset(direction.rotateYCounterclockwise()), state.with(DecorationBlock.PART, DecorationPart.LEFT));
        level.setBlockState(pos.offset(direction.rotateYClockwise()), state.with(DecorationBlock.PART, DecorationPart.RIGHT));

        super.onPlaced(level, pos, state, livingEntity, stack);
    }

    @Override
    public void onBreak(@NotNull World level, @NotNull BlockPos pos, BlockState state, @NotNull PlayerEntity player) {
        Direction direction = state.get(FACING);

        switch (state.get(DecorationBlock.PART)) {
            case LEFT -> {
                level.breakBlock(pos.offset(direction.rotateYClockwise()), false);
                level.breakBlock(pos.offset(direction.rotateYClockwise(), 2), false);
            }
            case RIGHT -> {
                level.breakBlock(pos.offset(direction.rotateYCounterclockwise()), false);
                level.breakBlock(pos.offset(direction.rotateYCounterclockwise(), 2), false);
            }
            default -> {
                level.breakBlock(pos.offset(direction.rotateYCounterclockwise()), false);
                level.breakBlock(pos.offset(direction.rotateYClockwise()), false);
            }
        }

        super.onBreak(level, pos, state, player);
    }

    public BlockPos getBasePos(BlockState state, BlockPos pos) {
        Direction direction = state.get(FACING);

        return switch (state.get(DecorationBlock.PART)) {
            case LEFT -> pos.offset(direction.rotateYClockwise());
            case RIGHT -> pos.offset(direction.rotateYCounterclockwise());
            default -> pos;
        };
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return null;
    }
}
