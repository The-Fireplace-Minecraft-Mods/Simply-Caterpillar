package dev.the_fireplace.caterpillar.block;

import dev.the_fireplace.caterpillar.block.util.StoragePart;
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

public class StorageBlock extends AbstractCaterpillarBlock {
    public static final EnumProperty<StoragePart> PART = EnumProperty.of("part", StoragePart.class);

    private static final Map<Direction, VoxelShape> SHAPES_LEFT = new EnumMap<>(Direction.class);

    private static final Map<Direction, VoxelShape> SHAPES_BASE = new EnumMap<>(Direction.class);

    private static final Map<Direction, VoxelShape> SHAPES_RIGHT = new EnumMap<>(Direction.class);

    private static final Optional<VoxelShape> SHAPE_LEFT = Stream.of(
            Block.createCuboidShape(1, 10, 1, 16, 14, 15),
            Block.createCuboidShape(1, 0, 1, 16, 10, 15),
            Block.createCuboidShape(7.5, 7, 15, 9.5, 11, 16)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR));

    private static final Optional<VoxelShape> SHAPE_BASE = Stream.of(
            Block.createCuboidShape(0, 0, 0, 6, 16, 16),
            Block.createCuboidShape(6, 6, -14, 10, 10, 1),
            Block.createCuboidShape(10, 0, 0, 16, 16, 16),
            Block.createCuboidShape(6, 10, 0, 10, 16, 16),
            Block.createCuboidShape(6, 0, 0, 10, 6, 16)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR));

    private static final Optional<VoxelShape> SHAPE_RIGHT = Stream.of(
            Block.createCuboidShape(0, 10, 1, 15, 14, 15),
            Block.createCuboidShape(0, 0, 1, 15, 10, 15),
            Block.createCuboidShape(6.5, 7, 15, 8.5, 11, 16)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR));

    public StorageBlock(Settings properties) {
        super(properties);
        super.setDefaultState(super.getDefaultState().with(StorageBlock.PART, StoragePart.BASE));
        super.runCalculation(StorageBlock.SHAPES_LEFT, StorageBlock.SHAPE_LEFT.get());
        super.runCalculation(StorageBlock.SHAPES_BASE, StorageBlock.SHAPE_BASE.get());
        super.runCalculation(StorageBlock.SHAPES_RIGHT, StorageBlock.SHAPE_RIGHT.get());
    }

    @Override
    protected void appendProperties(StateManager.@NotNull Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(StorageBlock.PART);
    }

    @Override
    public @NotNull VoxelShape getOutlineShape(BlockState state, @NotNull BlockView level, @NotNull BlockPos pos, @NotNull ShapeContext context) {
        return switch (state.get(StorageBlock.PART)) {
            case LEFT -> StorageBlock.SHAPES_LEFT.get(state.get(FACING));
            case RIGHT -> StorageBlock.SHAPES_RIGHT.get(state.get(FACING));
            default -> StorageBlock.SHAPES_BASE.get(state.get(FACING));
        };
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        BlockPos pos = context.getBlockPos();
        World level = context.getWorld();
        Direction direction = context.getPlayerFacing();

        return getDefaultState().with(FACING, direction.getOpposite()).with(StorageBlock.PART, StoragePart.BASE);
    }

    @Override
    public void onPlaced(World level, BlockPos blockPos, BlockState blockState, @Nullable LivingEntity livingEntity, @NotNull ItemStack stack) {
        Direction direction = blockState.get(FACING);

        level.setBlockState(blockPos.offset(direction.rotateYCounterclockwise()), blockState.with(StorageBlock.PART, StoragePart.LEFT));
        level.setBlockState(blockPos.offset(direction.rotateYClockwise()), blockState.with(StorageBlock.PART, StoragePart.RIGHT));

        super.onPlaced(level, blockPos, blockState, livingEntity, stack);
    }

    @Override
    public void onBreak(@NotNull World level, @NotNull BlockPos pos, BlockState state, @NotNull PlayerEntity player) {
        BlockPos basePos = this.getBasePos(state, pos);

        this.destroyStructure(level, basePos, state, player);

        super.onBreak(level, pos, state, player);
    }

    private void destroyStructure(World level, BlockPos pos, BlockState state, PlayerEntity player) {
        Direction direction = state.get(FACING);

        level.breakBlock(pos, !player.isCreative());
        level.breakBlock(pos.offset(direction.rotateYCounterclockwise()), false);
        level.breakBlock(pos.offset(direction.rotateYClockwise()), false);
    }

    public BlockPos getBasePos(BlockState state, BlockPos pos) {
        Direction direction = state.get(FACING);

        return switch (state.get(StorageBlock.PART)) {
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
