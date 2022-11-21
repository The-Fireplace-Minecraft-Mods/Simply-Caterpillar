package the_fireplace.caterpillar.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.TranslatableComponent;
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
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import the_fireplace.caterpillar.common.block.entity.ReinforcementBlockEntity;
import the_fireplace.caterpillar.common.block.util.CaterpillarBlockUtil;
import the_fireplace.caterpillar.common.block.util.ReinforcementPart;
import the_fireplace.caterpillar.core.init.BlockEntityInit;
import the_fireplace.caterpillar.core.init.BlockInit;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class ReinforcementBlock extends AbstractCaterpillarBlock implements SimpleWaterloggedBlock {

    public static final EnumProperty<ReinforcementPart> PART = EnumProperty.create("part", ReinforcementPart.class);

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private static final Map<Direction, VoxelShape> SHAPES_LEFT = new EnumMap<>(Direction.class);

    private static final Map<Direction, VoxelShape> SHAPES_BASE = new EnumMap<>(Direction.class);

    private static final Map<Direction, VoxelShape> SHAPES_RIGHT = new EnumMap<>(Direction.class);

    private static final Map<Direction, VoxelShape> SHAPES_TOP = new EnumMap<>(Direction.class);

    private static final Map<Direction, VoxelShape> SHAPES_BOTTOM = new EnumMap<>(Direction.class);

    private static final Optional<VoxelShape> SHAPE_LEFT = Stream.of(
        Block.box(0, 0, 0, 1, 16, 16),
        Block.box(1, 6, 6, 16, 10, 10)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR));

    private static final Optional<VoxelShape> SHAPE_BASE = Stream.of(
        Block.box(6, 6, -15, 10, 10, 0),
        Block.box(0, 0, 0, 6, 16, 16),
        Block.box(10, 0, 0, 16, 16, 16),
        Block.box(6, 10, 0, 10, 16, 16),
        Block.box(6, 0, 0, 10, 6, 16)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR));

    private static final Optional<VoxelShape> SHAPE_RIGHT = Stream.of(
        Block.box(0, 6, 6, 15, 10, 10),
        Block.box(15, 0, 0, 16, 16, 16)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR));

    private static final Optional<VoxelShape> SHAPE_TOP = Stream.of(
        Block.box(6, 0, 6, 10, 15, 10),
        Block.box(0, 15, 0, 16, 16, 16)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR));

    private static final Optional<VoxelShape> SHAPE_BOTTOM = Stream.of(
        Block.box(6, 1, 6, 10, 16, 10),
        Block.box(0, 0, 0, 16, 1, 16)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR));

    public ReinforcementBlock(Properties properties) {
        super(properties);
        super.registerDefaultState(super.defaultBlockState().setValue(ReinforcementBlock.PART, ReinforcementPart.BOTTOM).setValue(ReinforcementBlock.WATERLOGGED, true));
        super.runCalculation(ReinforcementBlock.SHAPES_LEFT, ReinforcementBlock.SHAPE_LEFT.get());
        super.runCalculation(ReinforcementBlock.SHAPES_BASE, ReinforcementBlock.SHAPE_BASE.get());
        super.runCalculation(ReinforcementBlock.SHAPES_RIGHT, ReinforcementBlock.SHAPE_RIGHT.get());
        super.runCalculation(ReinforcementBlock.SHAPES_TOP, ReinforcementBlock.SHAPE_TOP.get());
        super.runCalculation(ReinforcementBlock.SHAPES_BOTTOM, ReinforcementBlock.SHAPE_BOTTOM.get());
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            BlockPos basePos = getBasePos(state, pos);
            BlockEntity blockEntity = level.getBlockEntity(basePos);

            if (blockEntity instanceof ReinforcementBlockEntity reinforcementBlockEntity) {
                NetworkHooks.openGui((ServerPlayer) player, reinforcementBlockEntity, basePos);

                return InteractionResult.CONSUME;
            } else {
                return InteractionResult.PASS;
            }
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(ReinforcementBlock.PART, ReinforcementBlock.WATERLOGGED);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return switch (state.getValue(ReinforcementBlock.PART)) {
            case LEFT -> ReinforcementBlock.SHAPES_LEFT.get(state.getValue(FACING));
            case RIGHT -> ReinforcementBlock.SHAPES_RIGHT.get(state.getValue(FACING));
            case TOP -> ReinforcementBlock.SHAPES_TOP.get(state.getValue(FACING));
            case BOTTOM -> ReinforcementBlock.SHAPES_BOTTOM.get(state.getValue(FACING));
            default -> ReinforcementBlock.SHAPES_BASE.get(state.getValue(FACING));
        };
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos blockPos = context.getClickedPos();
        Level level = context.getLevel();
        Direction direction = context.getHorizontalDirection();

        if (
            blockPos.getY() < level.getMaxBuildHeight() - 1 &&
            level.getBlockState(blockPos.above().relative(direction.getClockWise())).canBeReplaced(context) &&
            level.getBlockState(blockPos.above().relative(direction.getCounterClockWise())).canBeReplaced(context) &&
            level.getBlockState(blockPos.above()).canBeReplaced(context) &&
            level.getBlockState(blockPos.above(2)).canBeReplaced(context)
        ){
            BlockPos caterpillarHeadPos = CaterpillarBlockUtil.getCaterpillarHeadPos(level, blockPos.above().relative(direction), direction.getOpposite());

            if (CaterpillarBlockUtil.getConnectedCaterpillarBlockEntities(level, caterpillarHeadPos, new ArrayList<>()).stream().noneMatch(blockEntity -> blockEntity instanceof ReinforcementBlockEntity)) {
                if (CaterpillarBlockUtil.isConnectedCaterpillarSameDirection(level, blockPos.above(), direction.getOpposite())) {
                    return defaultBlockState().setValue(FACING, direction.getOpposite()).setValue(ReinforcementBlock.PART, ReinforcementPart.BOTTOM).setValue(DrillHeadBlock.WATERLOGGED, context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER);
                }
            } else {
                context.getPlayer().displayClientMessage(new TranslatableComponent("block.simplycaterpillar.blocks.already_connected", BlockInit.REINFORCEMENT.get().getName()), true);
            }
        }

        return null;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, @Nullable LivingEntity livingEntity, @NotNull ItemStack stack) {
        Direction direction = blockState.getValue(FACING);

        level.setBlockAndUpdate(blockPos.above().relative(direction.getCounterClockWise()), blockState.setValue(PART, ReinforcementPart.LEFT));
        level.setBlockAndUpdate(blockPos.above().relative(direction.getClockWise()), blockState.setValue(ReinforcementBlock.PART, ReinforcementPart.RIGHT));
        level.setBlockAndUpdate(blockPos.above(), blockState.setValue(ReinforcementBlock.PART, ReinforcementPart.BASE));
        level.setBlockAndUpdate(blockPos.above(2), blockState.setValue(ReinforcementBlock.PART, ReinforcementPart.TOP));

        super.setPlacedBy(level, blockPos, blockState, livingEntity, stack);
    }

    @Override
    public void playerWillDestroy(Level level, @NotNull BlockPos pos, BlockState state, Player player) {
        Direction direction = state.getValue(FACING);
        BlockPos basePos = getBasePos(state, pos);

        level.destroyBlock(basePos, !player.isCreative());
        level.destroyBlock(basePos.relative(direction.getCounterClockWise()), false);
        level.destroyBlock(basePos.relative(direction.getClockWise()), false);
        level.destroyBlock(basePos.above(), false);
        level.destroyBlock(basePos.below(), false);

        super.playerWillDestroy(level, pos, state, player);
    }

    public BlockPos getBasePos(BlockState state, BlockPos pos) {
        Direction direction = state.getValue(FACING);
        ReinforcementPart part = state.getValue(ReinforcementBlock.PART);

        return switch (part) {
            case LEFT -> pos.relative(direction.getClockWise());
            case RIGHT -> pos.relative(direction.getCounterClockWise());
            case TOP -> pos.below();
            case BOTTOM -> pos.above();
            default -> pos;
        };
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return BlockEntityInit.REINFORCEMENT.get().create(pos, state);
    }
}
