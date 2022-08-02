package the_fireplace.caterpillar.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.common.block.entity.DecorationBlockEntity;
import the_fireplace.caterpillar.common.container.DecorationContainer;
import the_fireplace.caterpillar.core.util.DecorationPart;
import the_fireplace.caterpillar.core.util.StoragePart;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class DecorationBlock extends HorizontalDirectionalBlock implements EntityBlock {

    public static final EnumProperty<DecorationPart> PART = EnumProperty.create("part", DecorationPart.class);

    private static final Map<Direction, VoxelShape> SHAPES_LEFT = new EnumMap<>(Direction.class);

    private static final Map<Direction, VoxelShape> SHAPES_BASE = new EnumMap<>(Direction.class);

    private static final Map<Direction, VoxelShape> SHAPES_RIGHT = new EnumMap<>(Direction.class);

    private static final Optional<VoxelShape> SHAPE_LEFT = Stream.of(
        Block.box(0, 0, 0, 6, 16, 16),
        Block.box(6, 6, 6, 16, 10, 10)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR));

    private static final Optional<VoxelShape> SHAPE_BASE = Stream.of(
        Block.box(6, 6, -15, 10, 10, 0),
        Block.box(0, 0, 0, 6, 16, 16),
        Block.box(10, 0, 0, 16, 16, 16),
        Block.box(6, 10, 0, 10, 16, 16),
        Block.box(6, 0, 0, 10, 6, 16)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR));

    private static final Optional<VoxelShape> SHAPE_RIGHT = Stream.of(
        Block.box(0, 6, 6, 10, 10, 10),
        Block.box(10, 0, 0, 16, 16, 16)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR));

    public DecorationBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH).setValue(PART, DecorationPart.BASE));
        runCalculation(SHAPES_LEFT, SHAPE_LEFT.get());
        runCalculation(SHAPES_BASE, SHAPE_BASE.get());
        runCalculation(SHAPES_RIGHT, SHAPE_RIGHT.get());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING, PART);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        switch (state.getValue(PART)) {
            case LEFT:
                return SHAPES_LEFT.get(state.getValue(FACING));
            case RIGHT:
                return SHAPES_RIGHT.get(state.getValue(FACING));
            default:
                return SHAPES_BASE.get(state.getValue(FACING));
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos blockPos = context.getClickedPos();
        Level level = context.getLevel();
        Direction direction = context.getNearestLookingDirection();

        if (blockPos.getY() < level.getMaxBuildHeight() - 1 && level.getBlockState(blockPos.relative(direction.getClockWise())).canBeReplaced(context) && level.getBlockState(blockPos.relative(direction.getCounterClockWise())).canBeReplaced(context)) {
            return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(PART, DecorationPart.BASE);
        }

        return null;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            this.openContainer(level, pos, player);
            return InteractionResult.CONSUME;
        }
    }

    @Override
    public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, @Nullable LivingEntity livingEntity, ItemStack stack) {
        Direction direction = blockState.getValue(FACING);

        level.setBlock(blockPos.relative(direction.getCounterClockWise()), blockState.setValue(PART, DecorationPart.LEFT), 3);
        level.setBlock(blockPos.relative(direction.getClockWise()), blockState.setValue(PART, DecorationPart.RIGHT), 3);
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        Direction direction = state.getValue(FACING);
        DecorationPart part = state.getValue(PART);

        switch (part) {
            case LEFT:
                level.destroyBlock(pos.relative(direction.getClockWise()), false);
                level.destroyBlock(pos.relative(direction.getClockWise(), 2), false);
                break;
            case RIGHT:
                level.destroyBlock(pos.relative(direction.getCounterClockWise()), false);
                level.destroyBlock(pos.relative(direction.getCounterClockWise(), 2), false);
                break;
            default:
                level.destroyBlock(pos.relative(direction.getCounterClockWise()), false);
                level.destroyBlock(pos.relative(direction.getClockWise()), false);
                break;
        }
    }

    protected void openContainer(Level level, BlockPos pos, Player player) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof DecorationBlockEntity) {
            MenuProvider container = new SimpleMenuProvider(DecorationContainer.getServerContainer((DecorationBlockEntity) blockEntity, pos), DecorationBlockEntity.TITLE);
            player.openMenu(container);
        }
    }

    protected void runCalculation(Map<Direction, VoxelShape> shapes, VoxelShape shape) {
        for (Direction direction : Direction.values())
            shapes.put(direction, Caterpillar.calculateShapes(direction, shape));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new DecorationBlockEntity(pos, state);
    }
}