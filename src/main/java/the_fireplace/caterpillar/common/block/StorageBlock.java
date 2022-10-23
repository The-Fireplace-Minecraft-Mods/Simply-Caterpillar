package the_fireplace.caterpillar.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import the_fireplace.caterpillar.common.block.entity.AbstractCaterpillarBlockEntity;
import the_fireplace.caterpillar.common.block.util.StoragePart;
import the_fireplace.caterpillar.core.init.BlockEntityInit;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class StorageBlock extends AbstractCaterpillarBlock {
    public static final EnumProperty<StoragePart> PART = EnumProperty.create("part", StoragePart.class);

    private static final Map<Direction, VoxelShape> SHAPES_LEFT = new EnumMap<>(Direction.class);

    private static final Map<Direction, VoxelShape> SHAPES_BASE = new EnumMap<>(Direction.class);

    private static final Map<Direction, VoxelShape> SHAPES_RIGHT = new EnumMap<>(Direction.class);

    private static final Optional<VoxelShape> SHAPE_LEFT = Stream.of(
        Block.box(1, 10, 1, 16, 14, 15),
        Block.box(1, 0, 1, 16, 10, 15),
        Block.box(7.5, 7, 15, 9.5, 11, 16)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR));

    private static final Optional<VoxelShape> SHAPE_BASE = Stream.of(
        Block.box(0, 0, 0, 6, 16, 16),
        Block.box(6, 6, -14, 10, 10, 1),
        Block.box(10, 0, 0, 16, 16, 16),
        Block.box(6, 10, 0, 10, 16, 16),
        Block.box(6, 0, 0, 10, 6, 16)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR));

    private static final Optional<VoxelShape> SHAPE_RIGHT = Stream.of(
        Block.box(0, 10, 1, 15, 14, 15),
        Block.box(0, 0, 1, 15, 10, 15),
        Block.box(6.5, 7, 15, 8.5, 11, 16)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR));

    public StorageBlock(Properties properties) {
        super(properties);
        super.registerDefaultState(super.defaultBlockState().setValue(StorageBlock.PART, StoragePart.BASE));
        super.runCalculation(StorageBlock.SHAPES_LEFT, StorageBlock.SHAPE_LEFT.get());
        super.runCalculation(StorageBlock.SHAPES_BASE, StorageBlock.SHAPE_BASE.get());
        super.runCalculation(StorageBlock.SHAPES_RIGHT, StorageBlock.SHAPE_RIGHT.get());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(StorageBlock.PART);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return switch (state.getValue(StorageBlock.PART)) {
            case LEFT -> StorageBlock.SHAPES_LEFT.get(state.getValue(FACING));
            case RIGHT -> StorageBlock.SHAPES_RIGHT.get(state.getValue(FACING));
            default -> StorageBlock.SHAPES_BASE.get(state.getValue(FACING));
        };
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();
        Direction direction = context.getHorizontalDirection();

        if (
            pos.getY() < level.getMaxBuildHeight() - 1 &&
            level.getBlockState(pos.relative(direction.getClockWise())).canBeReplaced(context) &&
            level.getBlockState(pos.relative(direction.getCounterClockWise())).canBeReplaced(context)
        ) {
            return defaultBlockState().setValue(FACING, direction.getOpposite()).setValue(StorageBlock.PART, StoragePart.BASE);
        }

        return null;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, @Nullable LivingEntity livingEntity, @NotNull ItemStack stack) {
        Direction direction = blockState.getValue(FACING);

        level.setBlockAndUpdate(blockPos.relative(direction.getCounterClockWise()), blockState.setValue(StorageBlock.PART, StoragePart.LEFT));
        level.setBlockAndUpdate(blockPos.relative(direction.getClockWise()), blockState.setValue(StorageBlock.PART, StoragePart.RIGHT));

        super.setPlacedBy(level, blockPos, blockState, livingEntity, stack);
    }
    private void dropContents(Level level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof AbstractCaterpillarBlockEntity caterpillarBlockEntity) {
            if (!level.isClientSide()) {
                caterpillarBlockEntity.drops();
            }
        }
    }

    @Override
    public void playerWillDestroy(@NotNull Level level, @NotNull BlockPos pos, BlockState state, @NotNull Player player) {
        BlockPos basePos = this.getBasePos(state, pos);

        this.dropContents(level, basePos);
        this.destroyStructure(level, basePos, state, player);

        super.playerWillDestroy(level, pos, state, player);
    }

    private void destroyStructure(Level level, BlockPos pos, BlockState state, Player player) {
        Direction direction = state.getValue(FACING);

        level.destroyBlock(pos, !player.isCreative());
        level.destroyBlock(pos.relative(direction.getCounterClockWise()), false);
        level.destroyBlock(pos.relative(direction.getClockWise()), false);
    }

    public BlockPos getBasePos(BlockState state, BlockPos pos) {
        Direction direction = state.getValue(FACING);

        return switch (state.getValue(StorageBlock.PART)) {
            case LEFT -> pos.relative(direction.getClockWise());
            case RIGHT -> pos.relative(direction.getCounterClockWise());
            default -> pos;
        };
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return BlockEntityInit.STORAGE.get().create(pos, state);
    }
}
