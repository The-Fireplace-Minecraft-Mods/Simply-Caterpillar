package the_fireplace.caterpillar.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import the_fireplace.caterpillar.common.block.entity.DecorationBlockEntity;
import the_fireplace.caterpillar.common.block.util.DecorationPart;
import the_fireplace.caterpillar.core.init.BlockEntityInit;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class DecorationBlock extends AbstractCaterpillarBlock {

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
        registerDefaultState(defaultBlockState().setValue(DecorationBlock.PART, DecorationPart.BASE));
        runCalculation(DecorationBlock.SHAPES_LEFT, DecorationBlock.SHAPE_LEFT.get());
        runCalculation(DecorationBlock.SHAPES_BASE, DecorationBlock.SHAPE_BASE.get());
        runCalculation(DecorationBlock.SHAPES_RIGHT, DecorationBlock.SHAPE_RIGHT.get());
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            BlockEntity blockEntity = level.getBlockEntity(pos);

            if (blockEntity instanceof DecorationBlockEntity decorationBlockEntity) {
                NetworkHooks.openScreen((ServerPlayer) player, decorationBlockEntity, pos);

                return InteractionResult.CONSUME;
            } else {
                return InteractionResult.PASS;
            }
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(DecorationBlock.PART);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(DecorationBlock.PART)) {
            case LEFT -> DecorationBlock.SHAPES_LEFT.get(state.getValue(FACING));
            case RIGHT -> DecorationBlock.SHAPES_RIGHT.get(state.getValue(FACING));
            default -> DecorationBlock.SHAPES_BASE.get(state.getValue(FACING));
        };
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos blockPos = context.getClickedPos();
        Level level = context.getLevel();
        Direction direction = context.getHorizontalDirection();

        if (blockPos.getY() < level.getMaxBuildHeight() - 1 && level.getBlockState(blockPos.relative(direction.getClockWise())).canBeReplaced(context) && level.getBlockState(blockPos.relative(direction.getCounterClockWise())).canBeReplaced(context)) {
            return super.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(PART, DecorationPart.BASE);
        }

        return null;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, @Nullable LivingEntity livingEntity, @NotNull ItemStack stack) {
        Direction direction = blockState.getValue(FACING);

        level.setBlock(blockPos.relative(direction.getCounterClockWise()), blockState.setValue(DecorationBlock.PART, DecorationPart.LEFT), 3);
        level.setBlock(blockPos.relative(direction.getClockWise()), blockState.setValue(DecorationBlock.PART, DecorationPart.RIGHT), 3);

        super.setPlacedBy(level, blockPos, blockState, livingEntity, stack);
    }

    @Override
    public void playerWillDestroy(@NotNull Level level, @NotNull BlockPos pos, BlockState state, @NotNull Player player) {
        Direction direction = state.getValue(FACING);

        switch (state.getValue(DecorationBlock.PART)) {
            case LEFT -> {
                level.destroyBlock(pos.relative(direction.getClockWise()), false);
                level.destroyBlock(pos.relative(direction.getClockWise(), 2), false);
            }
            case RIGHT -> {
                level.destroyBlock(pos.relative(direction.getCounterClockWise()), false);
                level.destroyBlock(pos.relative(direction.getCounterClockWise(), 2), false);
            }
            default -> {
                level.destroyBlock(pos.relative(direction.getCounterClockWise()), false);
                level.destroyBlock(pos.relative(direction.getClockWise()), false);
            }
        }

        super.playerWillDestroy(level, pos, state, player);
    }

    public BlockPos getBasePos(BlockState state, BlockPos pos) {
        Direction direction = state.getValue(FACING);

        return switch (state.getValue(DecorationBlock.PART)) {
            case LEFT -> pos.relative(direction.getClockWise());
            case RIGHT -> pos.relative(direction.getCounterClockWise());
            default -> pos;
        };
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return BlockEntityInit.DECORATION.get().create(pos, state);
    }
}