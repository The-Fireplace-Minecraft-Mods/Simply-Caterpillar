package the_fireplace.caterpillar.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import the_fireplace.caterpillar.core.init.BlockEntityInit;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;


public class CollectorBlock extends AbstractCaterpillarBlock {

    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

    private static final Map<Direction, VoxelShape> SHAPES_UPPER = new EnumMap<>(Direction.class);

    private static final Map<Direction, VoxelShape> SHAPES_LOWER = new EnumMap<>(Direction.class);

    private static final Optional<VoxelShape> SHAPE_UPPER = Stream.of(
        Block.box(6, 0, 0, 10, 6, 16),
        Block.box(0, 0, 0, 6, 16, 16),
        Block.box(10, 0, 0, 16, 16, 16),
        Block.box(6, 10, 0, 10, 16, 16),
        Block.box(6, 6, -15, 10, 10, 0)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR));

    private static final Optional<VoxelShape> SHAPE_LOWER = Stream.of(
        Block.box(0, 0, 12, 16, 4, 16),
        Block.box(0, 4, 12, 4, 12, 16),
        Block.box(0, 12, 12, 16, 16, 16),
        Block.box(3, 3, 5, 13, 13, 11),
        Block.box(6, 6, 1, 10, 16, 5),
        Block.box(0, 0, 11, 16, 16, 12),
        Block.box(12, 4, 12, 16, 12, 16)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR));

    public CollectorBlock(Properties properties) {
        super(properties);
        super.registerDefaultState(defaultBlockState().setValue(CollectorBlock.HALF, DoubleBlockHalf.LOWER));
        super.runCalculation(CollectorBlock.SHAPES_UPPER, CollectorBlock.SHAPE_UPPER.get());
        super.runCalculation(CollectorBlock.SHAPES_LOWER, CollectorBlock.SHAPE_LOWER.get());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(CollectorBlock.HALF);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        if (state.getValue(CollectorBlock.HALF) == DoubleBlockHalf.LOWER) {
            return CollectorBlock.SHAPES_LOWER.get(state.getValue(FACING));
        } else {
            return CollectorBlock.SHAPES_UPPER.get(state.getValue(FACING));
        }
    }

    @Override
    public void playerWillDestroy(@NotNull Level level, @NotNull BlockPos pos, BlockState state, @NotNull Player player) {
        DoubleBlockHalf half = state.getValue(CollectorBlock.HALF);

        if (half == DoubleBlockHalf.UPPER) {
            level.destroyBlock(pos.below(), false);
        } else {
            level.destroyBlock(pos.above(), false);
        }

        super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos blockPos = context.getClickedPos();
        Level level = context.getLevel();
        Direction direction = context.getHorizontalDirection();

        if (
            blockPos.getY() < level.getMaxBuildHeight() - 1 &&
            level.getBlockState(blockPos.above()).canBeReplaced(context)
        ) {
            return super.defaultBlockState().setValue(FACING, direction.getOpposite()).setValue(CollectorBlock.HALF, DoubleBlockHalf.LOWER);
        }

        return null;
    }

    @Override
    public BlockPos getBasePos(BlockState state, BlockPos pos) {
        if (state.getValue(CollectorBlock.HALF) == DoubleBlockHalf.LOWER) {
            return pos.above();
        }
        return pos;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, @Nullable LivingEntity livingEntity, @NotNull ItemStack stack) {
        level.setBlock(blockPos.above(), blockState.setValue(CollectorBlock.HALF, DoubleBlockHalf.UPPER), 3);

        super.setPlacedBy(level, blockPos, blockState, livingEntity, stack);
    }

    protected void runCalculation(Map<Direction, VoxelShape> shapes, VoxelShape shape) {
        for (Direction direction : Direction.values())
            shapes.put(direction, calculateShapes(direction, shape));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return BlockEntityInit.COLLECTOR.get().create(pos, state);
    }
}
