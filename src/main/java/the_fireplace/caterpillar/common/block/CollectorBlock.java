package the_fireplace.caterpillar.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.common.block.entity.CollectorBlockEntity;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class CollectorBlock extends HorizontalDirectionalBlock implements EntityBlock {

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
        Block.box(0, 0, 1, 16, 4, 5),
        Block.box(12, 4, 1, 16, 12, 5),
        Block.box(0, 12, 1, 16, 16, 5),
        Block.box(3, 3, 6, 13, 13, 12),
        Block.box(6, 6, 12, 10, 16, 16),
        Block.box(0, 0, 5, 16, 16, 6),
        Block.box(0, 4, 1, 4, 12, 5)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR));

    public CollectorBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH).setValue(HALF, DoubleBlockHalf.LOWER));
        runCalculation(SHAPES_UPPER, SHAPE_UPPER.get());
        runCalculation(SHAPES_LOWER, SHAPE_LOWER.get());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (state.getValue(HALF) == DoubleBlockHalf.LOWER) {
            return SHAPES_LOWER.get(state.getValue(FACING));
        } else {
            return SHAPES_UPPER.get(state.getValue(FACING));
        }
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        DoubleBlockHalf doubleBlockHalf = stateIn.getValue(HALF);

        if (facing.getAxis() == Direction.Axis.Y && doubleBlockHalf == DoubleBlockHalf.LOWER == (facing == Direction.UP)) {
            return facingState.is(this) && facingState.getValue(HALF) != doubleBlockHalf ? stateIn.setValue(FACING, facingState.getValue(FACING)) : Blocks.AIR.defaultBlockState();
        } else {
            return doubleBlockHalf == DoubleBlockHalf.LOWER && facing == Direction.DOWN && !stateIn.canSurvive(worldIn, currentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
        }
    }

    @Override
    public void playerWillDestroy(Level worldIn, BlockPos pos, BlockState state, Player player) {
        if (!worldIn.isClientSide && player.isCreative()) {
            DoubleBlockHalf doubleBlockHalf = state.getValue(HALF);

            if (doubleBlockHalf == DoubleBlockHalf.UPPER) {
                BlockPos belowBlockPos = pos.below();
                BlockState belowBlockState = worldIn.getBlockState(belowBlockPos);

                if (belowBlockState.is(state.getBlock()) && belowBlockState.getValue(HALF) == DoubleBlockHalf.LOWER) {
                    worldIn.setBlock(belowBlockPos, Blocks.AIR.defaultBlockState(), 35);
                    worldIn.levelEvent(player, 2001, belowBlockPos, Block.getId(belowBlockState));
                }
            }
        }

        super.playerWillDestroy(worldIn, pos, state, player);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos blockPos = context.getClickedPos();
        Level level = context.getLevel();

        if (blockPos.getY() < level.getMaxBuildHeight() - 1 && level.getBlockState(blockPos.above()).canBeReplaced(context)) {
            return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(HALF, DoubleBlockHalf.LOWER);
        } else {
            return null;
        }
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null : (level0, pos, state0, blockEntity) -> ((CollectorBlockEntity) blockEntity).tick();
    }

    @Override
    public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, @Nullable LivingEntity livingEntity, ItemStack stack) {
        level.setBlock(blockPos.above(), blockState.setValue(HALF, DoubleBlockHalf.UPPER), 3);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public long getSeed(BlockState state, BlockPos pos) {
        return Mth.getSeed(pos.getX(), pos.below(state.getValue(HALF) == DoubleBlockHalf.LOWER ? 0 : 1).getY(), pos.getZ());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(HALF, FACING);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos blockPos) {
        BlockPos belowBlockpos = blockPos.below();
        BlockState belowBlockState = worldIn.getBlockState(belowBlockpos);
        return state.getValue(HALF) == DoubleBlockHalf.LOWER ? belowBlockState.isFaceSturdy(worldIn, belowBlockpos, Direction.UP) : belowBlockState.is(this);
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState p_60584_) {
        return PushReaction.DESTROY;
    }

    protected void runCalculation(Map<Direction, VoxelShape> shapes, VoxelShape shape) {
        for (Direction direction : Direction.values())
            shapes.put(direction, Caterpillar.calculateShapes(direction, shape));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CollectorBlockEntity(pos, state);
    }
}
