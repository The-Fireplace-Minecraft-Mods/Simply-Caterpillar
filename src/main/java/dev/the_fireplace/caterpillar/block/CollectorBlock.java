package dev.the_fireplace.caterpillar.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
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
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import dev.the_fireplace.caterpillar.block.entity.CollectorBlockEntity;
import dev.the_fireplace.caterpillar.block.util.CaterpillarBlockUtil;
import dev.the_fireplace.caterpillar.registry.BlockEntityRegistry;
import dev.the_fireplace.caterpillar.registry.BlockRegistry;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Stream;


public class CollectorBlock extends DrillBaseBlock {

    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

    private static final Map<Direction, VoxelShape> SHAPES_UPPER = new EnumMap<>(Direction.class);

    private static final Map<Direction, VoxelShape> SHAPES_LOWER = new EnumMap<>(Direction.class);

    private static final VoxelShape SHAPE_UPPER = Stream.of(
        Block.box(0, 0, 0, 16, 16, 16),
        Block.box(6, 6, 16, 10, 10, 32)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    private static final VoxelShape SHAPE_LOWER = Stream.of(
        Block.box(4, 0, 0, 12, 4, 6),
        Block.box(12, 0, 0, 16, 16, 6),
        Block.box(4, 12, 0, 12, 16, 6),
        Block.box(4, 4, 6, 12, 12, 12),
        Block.box(6, 6, 12, 10, 16, 16),
        Block.box(0, 0, 0, 4, 16, 6)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    public CollectorBlock(Properties properties) {
        super(properties);
        super.registerDefaultState(defaultBlockState().setValue(CollectorBlock.HALF, DoubleBlockHalf.LOWER));
        super.runCalculation(CollectorBlock.SHAPES_UPPER, CollectorBlock.SHAPE_UPPER);
        super.runCalculation(CollectorBlock.SHAPES_LOWER, CollectorBlock.SHAPE_LOWER);
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
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();
        Direction direction = context.getHorizontalDirection();

        if (
            pos.getY() < level.getMaxBuildHeight() - 1 &&
            level.getBlockState(pos.above()).canBeReplaced(context)
        ) {
            BlockPos caterpillarHeadPos = CaterpillarBlockUtil.getCaterpillarHeadPos(level, pos.relative(direction), direction);

            if (CaterpillarBlockUtil.getConnectedCaterpillarBlockEntities(level, caterpillarHeadPos, new ArrayList<>()).stream().noneMatch(blockEntity -> blockEntity instanceof CollectorBlockEntity)) {
                if (CaterpillarBlockUtil.isConnectedCaterpillarSameDirection(level, pos.above(), direction)) {
                    return super.defaultBlockState().setValue(FACING, direction).setValue(CollectorBlock.HALF, DoubleBlockHalf.LOWER).setValue(WATERLOGGED, level.getFluidState(pos).getType() == Fluids.WATER);
                }
            } else {
                context.getPlayer().displayClientMessage(Component.translatable("block.simplycaterpillar.blocks.already_connected", BlockRegistry.COLLECTOR.get().getName()), true);
            }
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
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity livingEntity, @NotNull ItemStack stack) {
        Direction direction = state.getValue(FACING);
        BlockState blockStateInFront = level.getBlockState(pos.relative(direction));
        Block blockInFront = blockStateInFront.getBlock();

        if (!CaterpillarBlockUtil.isCaterpillarBlock(blockInFront)) {
            level.setBlockAndUpdate(pos.above(), state.setValue(CollectorBlock.HALF, DoubleBlockHalf.UPPER).setValue(WATERLOGGED, level.getFluidState(pos.above()).getType() == Fluids.WATER));
            level.setBlockAndUpdate(pos, state.setValue(CollectorBlock.HALF, DoubleBlockHalf.LOWER).setValue(WATERLOGGED, level.getFluidState(pos).getType() == Fluids.WATER));
        } else {
            level.setBlockAndUpdate(pos, state.setValue(CollectorBlock.HALF, DoubleBlockHalf.UPPER).setValue(WATERLOGGED, level.getFluidState(pos).getType() == Fluids.WATER));
            level.setBlockAndUpdate(pos.below(), state.setValue(CollectorBlock.HALF, DoubleBlockHalf.LOWER).setValue(WATERLOGGED, level.getFluidState(pos.below()).getType() == Fluids.WATER));
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return BlockEntityRegistry.COLLECTOR.get().create(pos, state);
    }
}
