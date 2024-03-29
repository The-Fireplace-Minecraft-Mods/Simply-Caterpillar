package dev.the_fireplace.caterpillar.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
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
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import dev.the_fireplace.caterpillar.block.entity.DecorationBlockEntity;
import dev.the_fireplace.caterpillar.block.util.CaterpillarBlockUtil;
import dev.the_fireplace.caterpillar.block.util.DecorationPart;
import dev.the_fireplace.caterpillar.registry.BlockEntityRegistry;
import dev.the_fireplace.caterpillar.registry.BlockRegistry;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Stream;

public class DecorationBlock extends DrillBaseBlock {

    public static final EnumProperty<DecorationPart> PART = EnumProperty.create("part", DecorationPart.class);

    private static final Map<Direction, VoxelShape> SHAPES_LEFT = new EnumMap<>(Direction.class);

    private static final Map<Direction, VoxelShape> SHAPES_BASE = new EnumMap<>(Direction.class);

    private static final Map<Direction, VoxelShape> SHAPES_RIGHT = new EnumMap<>(Direction.class);

    private static final VoxelShape SHAPE_LEFT = Stream.of(
        Block.box(0, 0, 0, 6, 16, 16),
        Block.box(6, 6, 6, 16, 10, 10)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    private static final VoxelShape SHAPE_BASE = Stream.of(
        Block.box(0, 0, 0, 16, 16, 16),
        Block.box(6, 6, 16, 10, 10, 32)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    private static final VoxelShape SHAPE_RIGHT = Stream.of(
        Block.box(0, 6, 6, 10, 10, 10),
        Block.box(10, 0, 0, 16, 16, 16)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    public DecorationBlock(Properties properties) {
        super(properties);
        super.registerDefaultState(defaultBlockState().setValue(DecorationBlock.PART, DecorationPart.BASE));
        super.runCalculation(DecorationBlock.SHAPES_LEFT, DecorationBlock.SHAPE_LEFT);
        super.runCalculation(DecorationBlock.SHAPES_BASE, DecorationBlock.SHAPE_BASE);
        super.runCalculation(DecorationBlock.SHAPES_RIGHT, DecorationBlock.SHAPE_RIGHT);
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            BlockPos basePos = getBasePos(state, pos);
            BlockEntity blockEntity = level.getBlockEntity(basePos);

            if (blockEntity instanceof DecorationBlockEntity decorationBlockEntity) {
                NetworkHooks.openScreen((ServerPlayer) player, decorationBlockEntity, basePos);

                return InteractionResult.CONSUME;
            } else {
                return InteractionResult.PASS;
            }
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(DecorationBlock.PART);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
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
        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());

        if (blockPos.getY() < level.getMaxBuildHeight() - 1 && level.getBlockState(blockPos.relative(direction.getClockWise())).canBeReplaced(context) && level.getBlockState(blockPos.relative(direction.getCounterClockWise())).canBeReplaced(context)) {
            BlockPos caterpillarHeadPos = CaterpillarBlockUtil.getCaterpillarHeadPos(level, blockPos.relative(direction), direction);

            if (CaterpillarBlockUtil.getConnectedCaterpillarBlockEntities(level, caterpillarHeadPos, new ArrayList<>()).stream().noneMatch(blockEntity -> blockEntity instanceof DecorationBlockEntity)) {
                if (CaterpillarBlockUtil.isConnectedCaterpillarSameDirection(level, blockPos, direction)) {
                    return super.defaultBlockState().setValue(FACING, direction).setValue(PART, DecorationPart.BASE).setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
                }
            } else {
                context.getPlayer().displayClientMessage(Component.translatable("block.simplycaterpillar.blocks.already_connected", BlockRegistry.DECORATION.get().getName()), true);
            }
        }

        return null;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity livingEntity, @NotNull ItemStack stack) {
        Direction direction = state.getValue(FACING);

        level.setBlockAndUpdate(pos.relative(direction.getCounterClockWise()), state.setValue(DecorationBlock.PART, DecorationPart.LEFT).setValue(WATERLOGGED, level.getFluidState(pos.relative(direction.getCounterClockWise())).getType() == Fluids.WATER));
        level.setBlockAndUpdate(pos.relative(direction.getClockWise()), state.setValue(DecorationBlock.PART, DecorationPart.RIGHT).setValue(WATERLOGGED, level.getFluidState(pos.relative(direction.getClockWise())).getType() == Fluids.WATER));

        super.setPlacedBy(level, pos, state, livingEntity, stack);
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
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        if (state.getValue(PART) == DecorationPart.BASE) {
            return BlockEntityRegistry.DECORATION.get().create(pos, state);
        }

        return null;
    }
}