package dev.the_fireplace.caterpillar.block;

import dev.the_fireplace.caterpillar.block.entity.TransporterBlockEntity;
import dev.the_fireplace.caterpillar.block.util.CaterpillarBlockUtil;
import dev.the_fireplace.caterpillar.init.BlockEntityInit;
import dev.the_fireplace.caterpillar.init.BlockInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
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

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Stream;

public class TransporterBlock extends DrillBaseBlock {

    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

    private static final Map<Direction, VoxelShape> SHAPES_UPPER = new EnumMap<>(Direction.class);

    private static final Map<Direction, VoxelShape> SHAPES_LOWER = new EnumMap<>(Direction.class);

    private static final VoxelShape SHAPE_UPPER = Stream.of(
            Block.box(6, 0, 0, 10, 6, 16),
            Block.box(10, 0, 0, 16, 16, 16),
            Block.box(0, 0, 0, 6, 16, 16),
            Block.box(6, 10, 0, 10, 16, 16),
            Block.box(6, 6, 16, 10, 10, 31),
            Block.box(-0.1, -7, 1.5, 2.9, 9, 1.5),
            Block.box(1.5, -7, -0.1, 1.5, 9, 2.9),
            Block.box(14.5, -7, 12.9, 14.5, 9, 15.9),
            Block.box(12.9, -7, 14.5, 15.9, 9, 14.5)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    private static final VoxelShape SHAPE_LOWER = Stream.of(
            Block.box(0, 1, 0, 2, 3, 16),
            Block.box(0, 3, 0, 2, 11, 16),
            Block.box(14, 1, 0, 16, 3, 16),
            Block.box(14, 3, 0, 16, 11, 16),
            Block.box(0, 1, 16, 16, 11, 18),
            Block.box(0, 1, -2, 16, 11, 0),
            Block.box(2, 1, 0, 14, 2, 16),
            Block.box(6.5, 8, 13, 8.5, 12, 14),
            Block.box(3, 11, 3, 13, 15, 13),
            Block.box(3, 3, 3, 13, 11, 13)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    public TransporterBlock(Properties properties) {
        super(properties);
        super.registerDefaultState(defaultBlockState().setValue(HALF, DoubleBlockHalf.UPPER));
        super.runCalculation(SHAPES_UPPER, SHAPE_UPPER);
        super.runCalculation(SHAPES_LOWER, SHAPE_LOWER);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(HALF);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        if (state.getValue(HALF) == DoubleBlockHalf.LOWER) {
            return SHAPES_LOWER.get(state.getValue(FACING));
        }

        return SHAPES_UPPER.get(state.getValue(FACING));
    }

    @Override
    public void playerWillDestroy(@NotNull Level level, @NotNull BlockPos pos, BlockState state, @NotNull Player player) {
        DoubleBlockHalf half = state.getValue(HALF);

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
            BlockPos caterpillarHeadPos = CaterpillarBlockUtil.getCaterpillarHeadPos(level, pos.above().relative(direction), direction);

            if (CaterpillarBlockUtil.getConnectedCaterpillarBlockEntities(level, caterpillarHeadPos, new ArrayList<>()).stream().noneMatch(blockEntity -> blockEntity instanceof TransporterBlockEntity)) {
                if (CaterpillarBlockUtil.isConnectedCaterpillarSameDirection(level, pos.above(), direction)) {
                    return super.defaultBlockState().setValue(FACING, direction).setValue(HALF, DoubleBlockHalf.LOWER).setValue(WATERLOGGED, level.getFluidState(pos).getType() == Fluids.WATER);
                }
            } else {
                context.getPlayer().displayClientMessage(Component.translatable("block.simplycaterpillar.blocks.already_connected", BlockInit.TRANSPORTER.get().getName()), true);
            }
        }

        return null;
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return BlockEntityInit.TRANSPORTER.get().create(pos, state);
    }
}
