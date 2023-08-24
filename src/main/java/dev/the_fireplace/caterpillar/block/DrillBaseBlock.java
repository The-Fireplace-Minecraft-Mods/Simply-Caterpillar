package dev.the_fireplace.caterpillar.block;

import dev.the_fireplace.caterpillar.block.entity.DrillHeadBlockEntity;
import dev.the_fireplace.caterpillar.block.util.CaterpillarBlockUtil;
import dev.the_fireplace.caterpillar.init.BlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Stream;

public class DrillBaseBlock extends BaseEntityBlock implements EntityBlock, SimpleWaterloggedBlock {

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    protected final Map<Direction, VoxelShape> SHAPES = new EnumMap<>(Direction.class);

    private static final VoxelShape SHAPE = Stream.of(
        Block.box(0, 0, 0, 16, 16, 16),
        Block.box(6, 6, 16, 10, 10, 32)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    public DrillBaseBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, false));
        this.runCalculation(SHAPES, SHAPE);
    }

    public static VoxelShape calculateShapes(Direction to, VoxelShape shape) {
        final VoxelShape[] buffer = {shape, Shapes.empty()};

        final int times = (to.get2DDataValue() - Direction.NORTH.get2DDataValue() + 4) % 4;
        for (int i = 0; i < times; i++) {
            buffer[0].forAllBoxes((minX, minY, minZ, maxX, maxY,
                                   maxZ) -> buffer[1] = Shapes.or(buffer[1], Shapes.create(1 - maxZ, minY, minX, 1 - minZ, maxY, maxX)));
            buffer[0] = buffer[1];
            buffer[1] = Shapes.empty();
        }

        return buffer[0];
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPES.get(state.getValue(FACING));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();
        Direction direction = context.getHorizontalDirection();
        FluidState fluidState = level.getFluidState(pos);

        if (CaterpillarBlockUtil.isConnectedCaterpillarSameDirection(level, pos, direction)) {
            return defaultBlockState().setValue(FACING, direction).setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
        }

        return null;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos currentPos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        return super.updateShape(state, direction, neighborState, level, currentPos, neighborPos);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING, WATERLOGGED);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide()) {
            Direction direction = state.getValue(FACING);
            BlockPos basePos = getBasePos(state, pos);
            BlockPos caterpillarHeadPos = CaterpillarBlockUtil.getCaterpillarHeadPos(level, basePos, direction);

            BlockEntity blockEntity = level.getBlockEntity(caterpillarHeadPos);
            if (blockEntity instanceof DrillHeadBlockEntity drillHeadBlockEntity) {
                player.openMenu(drillHeadBlockEntity);
            } else {
                player.displayClientMessage(Component.translatable("block.simplycaterpillar.drill_head.not_found"), true);
            }
        }

        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    public BlockPos getBasePos(BlockState state, BlockPos pos) {
        return pos;
    }

    public static void dropContents(Level level, BlockPos blockPos) {
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if (blockEntity instanceof DrillHeadBlockEntity caterpillarBlockEntity) {
            caterpillarBlockEntity.dropContents(level, blockPos);
        }
    }

    protected void runCalculation(Map<Direction, VoxelShape> shapes, VoxelShape shape) {
        for (Direction direction : Direction.values())
            shapes.put(direction, calculateShapes(direction, shape));
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return BlockEntityInit.DRILL_BASE.create(pos, state);
    }
}