package dev.the_fireplace.caterpillar.block;

import dev.the_fireplace.caterpillar.block.entity.TransporterBlockEntity;
import dev.the_fireplace.caterpillar.block.util.CaterpillarBlockUtil;
import dev.the_fireplace.caterpillar.init.BlockEntityInit;
import dev.the_fireplace.caterpillar.init.BlockInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Stream;

public class TransporterBlock extends DrillBaseBlock {

    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

    private static final Map<Direction, VoxelShape> SHAPES_UPPER = new EnumMap<>(Direction.class);

    private static final Map<Direction, VoxelShape> SHAPES_LOWER = new EnumMap<>(Direction.class);

    private static final VoxelShape SHAPE_UPPER = Stream.of(
        Block.box(0, 0, 0, 16, 16, 16),
        Block.box(6, 6, 16, 10, 10, 32),
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
        Block.box(3, 3, 3, 13, 11, 13),
        Block.box(6, 8, 1, 10, 16, 2),
        Block.box(5, 7, 2, 11, 13, 3)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();


    public TransporterBlock(Properties properties) {
        super(properties);
        super.registerDefaultState(defaultBlockState().setValue(HALF, DoubleBlockHalf.UPPER));
        super.runCalculation(SHAPES_UPPER, SHAPE_UPPER);
        super.runCalculation(SHAPES_LOWER, SHAPE_LOWER);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            BlockPos basePos = getBasePos(state, pos);
            BlockEntity blockEntity = level.getBlockEntity(basePos);

            if (blockEntity instanceof TransporterBlockEntity transporterBlockEntity) {
                player.openMenu(transporterBlockEntity);

                return InteractionResult.CONSUME;
            } else {
                return InteractionResult.PASS;
            }
        }
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
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (level instanceof ServerLevel) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof TransporterBlockEntity transporterBlockEntity) {
                if (!transporterBlockEntity.hasMinecartChest()) {
                    dropContents(level, pos);
                } else {
                    transporterBlockEntity.releaseMinecartChest();
                }
            }
        }

        super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos blockPos, BlockState blockState, @Nullable BlockEntity blockEntity, ItemStack itemStack) {
        if (blockState.getValue(HALF) == DoubleBlockHalf.LOWER) {
            Containers.dropItemStack(level, blockPos.getX(), blockPos.getY(), blockPos.getZ(), new ItemStack(Items.CHEST_MINECART));
        } else {
            super.playerDestroy(level, player, blockPos, blockState, blockEntity, itemStack);
        }
    }

    @Override
    public void destroy(LevelAccessor level, BlockPos pos, BlockState state) {
        if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
            super.destroy(level, pos, state);
            return;
        }

        BlockEntity blockEntity = level.getBlockEntity(pos.above());
        if (blockEntity instanceof TransporterBlockEntity transporterBlockEntity) {
            Block previousBlock = transporterBlockEntity.getPreviousBlock();

            if (previousBlock == null) {
                return;
            }

            level.setBlock(pos, previousBlock.defaultBlockState(), 3);
        }

        super.destroy(level, pos, state);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();
        Direction direction = context.getHorizontalDirection();

        if (pos.getY() < level.getMaxBuildHeight() - 1) {
            BlockPos caterpillarHeadPos = CaterpillarBlockUtil.getCaterpillarHeadPos(level, pos.relative(direction), direction);

            if (CaterpillarBlockUtil.getConnectedCaterpillarBlockEntities(level, caterpillarHeadPos, new ArrayList<>()).stream().noneMatch(blockEntity -> blockEntity instanceof TransporterBlockEntity)) {
                if (CaterpillarBlockUtil.isConnectedCaterpillarSameDirection(level, pos, direction)) {
                    return super.defaultBlockState().setValue(FACING, direction).setValue(HALF, DoubleBlockHalf.UPPER).setValue(WATERLOGGED, level.getFluidState(pos).getType() == Fluids.WATER);
                }
            } else {
                context.getPlayer().displayClientMessage(Component.translatable("block.simplycaterpillar.blocks.already_connected", BlockInit.TRANSPORTER.getName()), true);
            }
        }

        return null;
    }

    @Override
    public BlockPos getBasePos(BlockState state, BlockPos pos) {
        if (state.getValue(HALF) == DoubleBlockHalf.LOWER) {
            return pos.above();
        }

        return pos;
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
            return BlockEntityInit.TRANSPORTER.create(pos, state);
        }

        return null;
    }
}
