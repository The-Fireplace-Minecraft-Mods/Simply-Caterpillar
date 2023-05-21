package dev.the_fireplace.caterpillar.block;

import dev.the_fireplace.caterpillar.block.entity.DrillBaseBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
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
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import dev.the_fireplace.caterpillar.block.entity.StorageBlockEntity;
import dev.the_fireplace.caterpillar.block.util.CaterpillarBlockUtil;
import dev.the_fireplace.caterpillar.block.util.StoragePart;
import dev.the_fireplace.caterpillar.init.BlockEntityInit;
import dev.the_fireplace.caterpillar.init.BlockInit;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Stream;

public class StorageBlock extends DrillBaseBlock {
    public static final EnumProperty<StoragePart> PART = EnumProperty.create("part", StoragePart.class);

    private static final Map<Direction, VoxelShape> SHAPES_LEFT = new EnumMap<>(Direction.class);

    private static final Map<Direction, VoxelShape> SHAPES_BASE = new EnumMap<>(Direction.class);

    private static final Map<Direction, VoxelShape> SHAPES_RIGHT = new EnumMap<>(Direction.class);

    private static final VoxelShape SHAPE_LEFT = Stream.of(
        Block.box(1, 10, 1, 16, 14, 15),
        Block.box(1, 0, 1, 16, 10, 15),
        Block.box(7.5, 7, 0, 9.5, 11, 1)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    private static final VoxelShape SHAPE_BASE = Stream.of(
        Block.box(0, 0, 0, 16, 16, 16),
        Block.box(6, 6, 16, 10, 10, 32)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    private static final VoxelShape SHAPE_RIGHT = Stream.of(
        Block.box(0, 10, 1, 15, 14, 15),
        Block.box(0, 0, 1, 15, 10, 15),
        Block.box(6.5, 7, 0, 8.5, 11, 1)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    public StorageBlock(Properties properties) {
        super(properties);
        super.registerDefaultState(super.defaultBlockState().setValue(StorageBlock.PART, StoragePart.BASE));
        super.runCalculation(StorageBlock.SHAPES_LEFT, StorageBlock.SHAPE_LEFT);
        super.runCalculation(StorageBlock.SHAPES_BASE, StorageBlock.SHAPE_BASE);
        super.runCalculation(StorageBlock.SHAPES_RIGHT, StorageBlock.SHAPE_RIGHT);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(StorageBlock.PART);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return switch (state.getValue(StorageBlock.PART)) {
            case LEFT -> StorageBlock.SHAPES_LEFT.get(state.getValue(FACING));
            case RIGHT -> StorageBlock.SHAPES_RIGHT.get(state.getValue(FACING));
            default -> StorageBlock.SHAPES_BASE.get(state.getValue(FACING));
        };
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();
        Direction direction = context.getHorizontalDirection();
        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());

        if (
            pos.getY() < level.getMaxBuildHeight() - 1 &&
            level.getBlockState(pos.relative(direction.getClockWise())).canBeReplaced(context) &&
            level.getBlockState(pos.relative(direction.getCounterClockWise())).canBeReplaced(context)
        ) {
            BlockPos caterpillarHeadPos = CaterpillarBlockUtil.getCaterpillarHeadPos(level, pos.relative(direction), direction);

            if (CaterpillarBlockUtil.getConnectedCaterpillarBlockEntities(level, caterpillarHeadPos, new ArrayList<>()).stream().noneMatch(blockEntity -> blockEntity instanceof StorageBlockEntity)) {
                if (CaterpillarBlockUtil.isConnectedCaterpillarSameDirection(level, pos, direction)) {
                    return defaultBlockState().setValue(FACING, direction).setValue(StorageBlock.PART, StoragePart.BASE).setValue(DrillHeadBlock.WATERLOGGED, fluidState.getType() == Fluids.WATER);
                }
            } else {
                context.getPlayer().displayClientMessage(Component.translatable("block.simplycaterpillar.blocks.already_connected", BlockInit.STORAGE.get().getName()), true);
            }
        }

        return null;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity livingEntity, @NotNull ItemStack stack) {
        Direction direction = state.getValue(FACING);

        level.setBlockAndUpdate(pos.relative(direction.getCounterClockWise()), state.setValue(StorageBlock.PART, StoragePart.LEFT).setValue(WATERLOGGED, level.getFluidState(pos.relative(direction.getCounterClockWise())).getType() == Fluids.WATER));
        level.setBlockAndUpdate(pos.relative(direction.getClockWise()), state.setValue(StorageBlock.PART, StoragePart.RIGHT).setValue(WATERLOGGED, level.getFluidState(pos.relative(direction.getClockWise())).getType() == Fluids.WATER));

        super.setPlacedBy(level, pos, state, livingEntity, stack);
    }
    private void dropContents(Level level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof DrillBaseBlockEntity caterpillarBlockEntity) {
            if (!level.isClientSide()) {
                caterpillarBlockEntity.drops();
            }
        }
    }

    @Override
    public void playerWillDestroy(@NotNull Level level, @NotNull BlockPos pos, BlockState state, @NotNull Player player) {
        BlockPos basePos = this.getBasePos(state, pos);

        this.dropContents(level, basePos);
        this.destroyStructure(level, basePos, state, player);

        super.playerWillDestroy(level, pos, state, player);
    }

    private void destroyStructure(Level level, BlockPos pos, BlockState state, Player player) {
        Direction direction = state.getValue(FACING);

        level.destroyBlock(pos, !player.isCreative());
        level.destroyBlock(pos.relative(direction.getCounterClockWise()), false);
        level.destroyBlock(pos.relative(direction.getClockWise()), false);
    }

    public BlockPos getBasePos(BlockState state, BlockPos pos) {
        Direction direction = state.getValue(FACING);

        return switch (state.getValue(StorageBlock.PART)) {
            case LEFT -> pos.relative(direction.getClockWise());
            case RIGHT -> pos.relative(direction.getCounterClockWise());
            default -> pos;
        };
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return BlockEntityInit.STORAGE.get().create(pos, state);
    }
}
