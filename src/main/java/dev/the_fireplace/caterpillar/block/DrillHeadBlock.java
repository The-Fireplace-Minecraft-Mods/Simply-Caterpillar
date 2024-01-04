package dev.the_fireplace.caterpillar.block;

import dev.the_fireplace.caterpillar.block.entity.DrillHeadBlockEntity;
import dev.the_fireplace.caterpillar.block.util.CaterpillarBlockUtil;
import dev.the_fireplace.caterpillar.block.util.DrillHeadPart;
import dev.the_fireplace.caterpillar.config.ConfigManager;
import dev.the_fireplace.caterpillar.registry.BlockEntityRegistry;
import dev.the_fireplace.caterpillar.registry.BlockRegistry;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class DrillHeadBlock extends DrillBaseBlock {

    public static final BooleanProperty DRILLING = BooleanProperty.create("drilling");

    public static final EnumProperty<DrillHeadPart> PART = EnumProperty.create("part", DrillHeadPart.class);

    private static final Map<Direction, VoxelShape> SHAPES_BASE = new EnumMap<>(Direction.class);

    private static final Map<Direction, VoxelShape> SHAPES_BIT_TOP_LEFT = new EnumMap<>(Direction.class);

    private static final Map<Direction, VoxelShape> SHAPES_BIT_TOP = new EnumMap<>(Direction.class);

    private static final Map<Direction, VoxelShape> SHAPES_BIT_TOP_RIGHT = new EnumMap<>(Direction.class);

    private static final Map<Direction, VoxelShape> SHAPES_BIT_LEFT = new EnumMap<>(Direction.class);

    private static final Map<Direction, VoxelShape> SHAPES_BIT_MIDDLE = new EnumMap<>(Direction.class);

    private static final Map<Direction, VoxelShape> SHAPES_BIT_RIGHT = new EnumMap<>(Direction.class);

    private static final Map<Direction, VoxelShape> SHAPES_BIT_BOTTOM_LEFT = new EnumMap<>(Direction.class);

    private static final Map<Direction, VoxelShape> SHAPES_BIT_BOTTOM = new EnumMap<>(Direction.class);

    private static final Map<Direction, VoxelShape> SHAPES_BIT_BOTTOM_RIGHT = new EnumMap<>(Direction.class);

    private static final VoxelShape SHAPE_BASE = Stream.of(
        Block.box(0, 0, 0, 16, 16, 16),
        Block.box(6, 6, 16, 10, 10, 32)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    private static final VoxelShape SHAPE_BIT_TOP_LEFT = Stream.of(
        Block.box(3, 3, 3, 13, 13, 14),
        Block.box(4, 6, 14, 12, 10, 16),
        Block.box(12, 7, 14, 16, 9, 15),
        Block.box(7, 0, 14, 9, 6, 15)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    private static final VoxelShape SHAPE_BIT_TOP = Stream.of(
        Block.box(0, 7, 14, 4, 9, 15),
        Block.box(4, 6, 14, 12, 10, 16),
        Block.box(12, 7, 14, 16, 9, 15),
        Block.box(7, 0, 14, 9, 6, 15),
        Block.box(3, 3, 3, 13, 13, 14)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    private static final VoxelShape SHAPE_BIT_TOP_RIGHT = Stream.of(
        Block.box(3, 3, 3, 13, 13, 14),
        Block.box(4, 6, 14, 12, 10, 16),
        Block.box(0, 7, 14, 4, 9, 15),
        Block.box(7, 0, 14, 9, 6, 15)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    private static final VoxelShape SHAPE_BIT_LEFT = Stream.of(
        Block.box(4, 6, 14, 12, 10, 16),
        Block.box(7, 10, 14, 9, 16, 15),
        Block.box(12, 7, 14, 16, 9, 15),
        Block.box(7, 0, 14, 9, 6, 15),
        Block.box(3, 3, 3, 13, 13, 14)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    private static final VoxelShape SHAPE_BIT_MIDDLE = Stream.of(
        Block.box(7, 10, 14, 9, 16, 15),
        Block.box(0, 7, 14, 4, 9, 15),
        Block.box(7, 0, 14, 9, 6, 15),
        Block.box(12, 7, 14, 16, 9, 15),
        Block.box(4, 6, 14, 12, 10, 16),
        Block.box(3, 3, 3, 13, 13, 14)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    private static final VoxelShape SHAPE_BIT_RIGHT = Stream.of(
        Block.box(7, 10, 14, 9, 16, 15),
        Block.box(4, 6, 14, 12, 10, 16),
        Block.box(0, 7, 14, 4, 9, 15),
        Block.box(7, 0, 14, 9, 6, 15),
        Block.box(3, 3, 3, 13, 13, 14)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    private static final VoxelShape SHAPE_BIT_BOTTOM_LEFT = Stream.of(
        Block.box(4, 6, 14, 12, 10, 16),
        Block.box(12, 7, 14, 16, 9, 15),
        Block.box(7, 10, 14, 9, 16, 15),
        Block.box(3, 3, 3, 13, 13, 14)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    private static final VoxelShape SHAPE_BIT_BOTTOM = Stream.of(
        Block.box(4, 6, 14, 12, 10, 16),
        Block.box(0, 7, 14, 4, 9, 15),
        Block.box(7, 10, 14, 9, 16, 15),
        Block.box(12, 7, 14, 16, 9, 15),
        Block.box(3, 3, 3, 13, 13, 14)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    private static final VoxelShape SHAPE_BIT_BOTTOM_RIGHT = Stream.of(
        Block.box(0, 7, 14, 4, 9, 15),
        Block.box(4, 6, 14, 12, 10, 16),
        Block.box(7, 10, 14, 9, 16, 15),
        Block.box(3, 3, 3, 13, 13, 14)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    public DrillHeadBlock(Properties properties) {
        super(properties);
        super.registerDefaultState(defaultBlockState().setValue(DrillHeadBlock.PART, DrillHeadPart.BIT_BOTTOM).setValue(DrillHeadBlock.WATERLOGGED, Boolean.FALSE).setValue(DrillHeadBlock.DRILLING, Boolean.FALSE));
        super.runCalculation(DrillHeadBlock.SHAPES_BASE, DrillHeadBlock.SHAPE_BASE);
        super.runCalculation(DrillHeadBlock.SHAPES_BIT_TOP_LEFT, DrillHeadBlock.SHAPE_BIT_TOP_LEFT);
        super.runCalculation(DrillHeadBlock.SHAPES_BIT_TOP, DrillHeadBlock.SHAPE_BIT_TOP);
        super.runCalculation(DrillHeadBlock.SHAPES_BIT_TOP_RIGHT, DrillHeadBlock.SHAPE_BIT_TOP_RIGHT);
        super.runCalculation(DrillHeadBlock.SHAPES_BIT_LEFT, DrillHeadBlock.SHAPE_BIT_LEFT);
        super.runCalculation(DrillHeadBlock.SHAPES_BIT_MIDDLE, DrillHeadBlock.SHAPE_BIT_MIDDLE);
        super.runCalculation(DrillHeadBlock.SHAPES_BIT_RIGHT, DrillHeadBlock.SHAPE_BIT_RIGHT);
        super.runCalculation(DrillHeadBlock.SHAPES_BIT_BOTTOM_LEFT, DrillHeadBlock.SHAPE_BIT_BOTTOM_LEFT);
        super.runCalculation(DrillHeadBlock.SHAPES_BIT_BOTTOM, DrillHeadBlock.SHAPE_BIT_BOTTOM);
        super.runCalculation(DrillHeadBlock.SHAPES_BIT_BOTTOM_RIGHT, DrillHeadBlock.SHAPE_BIT_BOTTOM_RIGHT);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(DrillHeadBlock.PART, DrillHeadBlock.DRILLING);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return switch (state.getValue(DrillHeadBlock.PART)) {
            case BIT_TOP_LEFT -> DrillHeadBlock.SHAPES_BIT_TOP_LEFT.get(state.getValue(FACING));
            case BIT_TOP -> DrillHeadBlock.SHAPES_BIT_TOP.get(state.getValue(FACING));
            case BIT_TOP_RIGHT -> DrillHeadBlock.SHAPES_BIT_TOP_RIGHT.get(state.getValue(FACING));
            case BIT_LEFT -> DrillHeadBlock.SHAPES_BIT_LEFT.get(state.getValue(FACING));
            case BIT_MIDDLE -> DrillHeadBlock.SHAPES_BIT_MIDDLE.get(state.getValue(FACING));
            case BIT_RIGHT -> DrillHeadBlock.SHAPES_BIT_RIGHT.get(state.getValue(FACING));
            case BIT_BOTTOM_LEFT -> DrillHeadBlock.SHAPES_BIT_BOTTOM_LEFT.get(state.getValue(FACING));
            case BIT_BOTTOM -> DrillHeadBlock.SHAPES_BIT_BOTTOM.get(state.getValue(FACING));
            case BIT_BOTTOM_RIGHT -> DrillHeadBlock.SHAPES_BIT_BOTTOM_RIGHT.get(state.getValue(FACING));
            default -> DrillHeadBlock.SHAPES_BASE.get(state.getValue(FACING));
        };
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos blockPos = context.getClickedPos();
        Level level = context.getLevel();
        Direction direction = context.getHorizontalDirection();
        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());

        if (
            blockPos.getY() < level.getMaxBuildHeight() - 1 &&
            level.getBlockState(blockPos.relative(direction.getClockWise())).canBeReplaced(context) &&
            level.getBlockState(blockPos.relative(direction.getCounterClockWise())).canBeReplaced(context) &&
            level.getBlockState(blockPos.above().relative(direction.getClockWise())).canBeReplaced(context) &&
            level.getBlockState(blockPos.above().relative(direction.getCounterClockWise())).canBeReplaced(context) &&
            level.getBlockState(blockPos.above()).canBeReplaced(context) &&
            level.getBlockState(blockPos.above().relative(direction.getOpposite())).canBeReplaced(context) &&
            level.getBlockState(blockPos.above(2)).canBeReplaced(context) &&
            level.getBlockState(blockPos.above(2).relative(direction.getClockWise())).canBeReplaced(context) &&
            level.getBlockState(blockPos.above(2).relative(direction.getCounterClockWise())).canBeReplaced(context)
        ) {
            BlockPos caterpillarHeadPos = CaterpillarBlockUtil.getCaterpillarHeadPos(level, blockPos.relative(direction), direction);

            if (CaterpillarBlockUtil.getConnectedCaterpillarBlockEntities(level, caterpillarHeadPos, new ArrayList<>()).stream().noneMatch(blockEntity -> blockEntity instanceof DrillHeadBlockEntity)) {
                if (CaterpillarBlockUtil.isConnectedCaterpillarSameDirection(level, blockPos.above(), direction)) {
                    return defaultBlockState().setValue(FACING, direction).setValue(DrillHeadBlock.PART, DrillHeadPart.BIT_BOTTOM).setValue(DrillHeadBlock.WATERLOGGED, fluidState.getType() == Fluids.WATER);
                }
            } else {
                context.getPlayer().displayClientMessage(Component.translatable("block.simplycaterpillar.blocks.already_connected", BlockRegistry.DRILL_HEAD.getName()), true);
            }

        }

        return null;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
        return createTickerHelper(type, BlockEntityRegistry.DRILL_HEAD, DrillHeadBlockEntity::tick);
    }

    @Override
    public void setPlacedBy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @Nullable LivingEntity livingEntity, @NotNull ItemStack stack) {
        buildStructure(level, pos, state);
    }

    @Override
    public void playerWillDestroy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Player player) {
        BlockPos basePos = getBasePos(state, pos);

        dropContents(level, basePos);
        this.destroyStructure(level, basePos, state, player);

        super.playerWillDestroy(level, pos, state, player);
    }

    private void destroyStructure(Level level, BlockPos pos, BlockState state, Player player) {
        Direction direction = state.getValue(FACING);

        level.destroyBlock(pos, !player.isCreative());
        level.destroyBlock(pos.relative(direction), false);
        level.destroyBlock(pos.relative(direction).relative(direction.getCounterClockWise()), false);
        level.destroyBlock(pos.relative(direction).relative(direction.getClockWise()), false);
        level.destroyBlock(pos.relative(direction).above(), false);
        level.destroyBlock(pos.relative(direction).below(), false);
        level.destroyBlock(pos.relative(direction).above().relative(direction.getCounterClockWise()), false);
        level.destroyBlock(pos.relative(direction).above().relative(direction.getClockWise()), false);
        level.destroyBlock(pos.relative(direction).below().relative(direction.getCounterClockWise()), false);
        level.destroyBlock(pos.relative(direction).below().relative(direction.getClockWise()), false);
    }

    public static void removeStructure(Level level, BlockPos pos, BlockState state) {
        Direction direction = state.getValue(FACING);

        level.removeBlock(pos, false);
        level.removeBlock(pos.relative(direction).relative(direction.getCounterClockWise()), false);
        level.removeBlock(pos.relative(direction).relative(direction.getClockWise()), false);
        level.removeBlock(pos.relative(direction).above(), false);
        level.removeBlock(pos.relative(direction).below(), false);
        level.removeBlock(pos.relative(direction).above().relative(direction.getCounterClockWise()), false);
        level.removeBlock(pos.relative(direction).above().relative(direction.getClockWise()), false);
        level.removeBlock(pos.relative(direction).below().relative(direction.getCounterClockWise()), false);
        level.removeBlock(pos.relative(direction).below().relative(direction.getClockWise()), false);
    }

    public static void buildStructure(Level level, BlockPos pos, BlockState state) {
        Direction direction = state.getValue(FACING);
        BlockState blockStateInFront = level.getBlockState(pos.relative(direction));
        Block blockInFront = blockStateInFront.getBlock();

        if (!CaterpillarBlockUtil.isCaterpillarBlock(blockInFront)) {
            level.setBlockAndUpdate(pos.relative(direction.getCounterClockWise()), state.setValue(DrillHeadBlock.PART, DrillHeadPart.BIT_BOTTOM_LEFT).setValue(DrillHeadBlock.WATERLOGGED, level.getFluidState(pos.relative(direction.getCounterClockWise())).getType() == Fluids.WATER));
            level.setBlockAndUpdate(pos.relative(direction.getClockWise()), state.setValue(DrillHeadBlock.PART, DrillHeadPart.BIT_BOTTOM_RIGHT).setValue(DrillHeadBlock.WATERLOGGED, level.getFluidState(pos.relative(direction.getClockWise())).getType() == Fluids.WATER));
            level.setBlockAndUpdate(pos, state.setValue(DrillHeadBlock.PART, DrillHeadPart.BIT_BOTTOM).setValue(DrillHeadBlock.WATERLOGGED, level.getFluidState(pos).getType() == Fluids.WATER));
            level.setBlockAndUpdate(pos.above().relative(direction.getCounterClockWise()), state.setValue(DrillHeadBlock.PART, DrillHeadPart.BIT_LEFT).setValue(DrillHeadBlock.WATERLOGGED, level.getFluidState(pos.above().relative(direction.getCounterClockWise())).getType() == Fluids.WATER));
            level.setBlockAndUpdate(pos.above().relative(direction.getClockWise()), state.setValue(PART, DrillHeadPart.BIT_RIGHT).setValue(DrillHeadBlock.WATERLOGGED, level.getFluidState(pos.above().relative(direction.getClockWise())).getType() == Fluids.WATER));
            level.setBlockAndUpdate(pos.above(2), state.setValue(DrillHeadBlock.PART, DrillHeadPart.BIT_TOP).setValue(DrillHeadBlock.WATERLOGGED, level.getFluidState(pos.above(2)).getType() == Fluids.WATER));
            level.setBlockAndUpdate(pos.above(), state.setValue(DrillHeadBlock.PART, DrillHeadPart.BIT_MIDDLE).setValue(DrillHeadBlock.WATERLOGGED, level.getFluidState(pos.above()).getType() == Fluids.WATER));
            level.setBlockAndUpdate(pos.above(2).relative(direction.getCounterClockWise()), state.setValue(DrillHeadBlock.PART, DrillHeadPart.BIT_TOP_LEFT).setValue(DrillHeadBlock.WATERLOGGED, level.getFluidState(pos.above(2).relative(direction.getCounterClockWise())).getType() == Fluids.WATER));
            level.setBlockAndUpdate(pos.above(2).relative(direction.getClockWise()), state.setValue(DrillHeadBlock.PART, DrillHeadPart.BIT_TOP_RIGHT).setValue(DrillHeadBlock.WATERLOGGED, level.getFluidState(pos.above(2).relative(direction.getClockWise())).getType() == Fluids.WATER));
            level.setBlockAndUpdate(pos.above().relative(direction.getOpposite()), state.setValue(DrillHeadBlock.PART, DrillHeadPart.BASE).setValue(WATERLOGGED, level.getFluidState(pos.above()).getType() == Fluids.WATER));
        } else {
            level.setBlockAndUpdate(pos.below().relative(direction.getCounterClockWise()), state.setValue(DrillHeadBlock.PART, DrillHeadPart.BIT_BOTTOM_LEFT).setValue(DrillHeadBlock.WATERLOGGED, level.getFluidState(pos.relative(direction.getCounterClockWise())).getType() == Fluids.WATER));
            level.setBlockAndUpdate(pos.below().relative(direction.getClockWise()), state.setValue(DrillHeadBlock.PART, DrillHeadPart.BIT_BOTTOM_RIGHT).setValue(DrillHeadBlock.WATERLOGGED, level.getFluidState(pos.relative(direction.getClockWise())).getType() == Fluids.WATER));
            level.setBlockAndUpdate(pos.below(), state.setValue(DrillHeadBlock.PART, DrillHeadPart.BIT_BOTTOM).setValue(DrillHeadBlock.WATERLOGGED, level.getFluidState(pos).getType() == Fluids.WATER));
            level.setBlockAndUpdate(pos.relative(direction.getCounterClockWise()), state.setValue(DrillHeadBlock.PART, DrillHeadPart.BIT_LEFT).setValue(DrillHeadBlock.WATERLOGGED, level.getFluidState(pos.above().relative(direction.getCounterClockWise())).getType() == Fluids.WATER));
            level.setBlockAndUpdate(pos.relative(direction.getClockWise()), state.setValue(PART, DrillHeadPart.BIT_RIGHT).setValue(DrillHeadBlock.WATERLOGGED, level.getFluidState(pos.above().relative(direction.getClockWise())).getType() == Fluids.WATER));
            level.setBlockAndUpdate(pos.above(), state.setValue(DrillHeadBlock.PART, DrillHeadPart.BIT_TOP).setValue(DrillHeadBlock.WATERLOGGED, level.getFluidState(pos.above(2)).getType() == Fluids.WATER));
            level.setBlockAndUpdate(pos, state.setValue(DrillHeadBlock.PART, DrillHeadPart.BIT_MIDDLE).setValue(DrillHeadBlock.WATERLOGGED, level.getFluidState(pos.above()).getType() == Fluids.WATER));
            level.setBlockAndUpdate(pos.above().relative(direction.getCounterClockWise()), state.setValue(DrillHeadBlock.PART, DrillHeadPart.BIT_TOP_LEFT).setValue(DrillHeadBlock.WATERLOGGED, level.getFluidState(pos.above(2).relative(direction.getCounterClockWise())).getType() == Fluids.WATER));
            level.setBlockAndUpdate(pos.above().relative(direction.getClockWise()), state.setValue(DrillHeadBlock.PART, DrillHeadPart.BIT_TOP_RIGHT).setValue(DrillHeadBlock.WATERLOGGED, level.getFluidState(pos.above(2).relative(direction.getClockWise())).getType() == Fluids.WATER));
            level.setBlockAndUpdate(pos.relative(direction.getOpposite()), state.setValue(DrillHeadBlock.PART, DrillHeadPart.BASE).setValue(WATERLOGGED, level.getFluidState(pos.above()).getType() == Fluids.WATER));
        }
    }

    public static void moveStructure(Level level, BlockPos pos, BlockState state) {
        Direction direction = state.getValue(FACING);

        level.setBlockAndUpdate(pos.relative(direction).below(), state.setValue(DrillHeadBlock.PART, DrillHeadPart.BIT_BOTTOM));
        level.setBlockAndUpdate(pos.relative(direction).below().relative(direction.getCounterClockWise()), state.setValue(DrillHeadBlock.PART, DrillHeadPart.BIT_BOTTOM_LEFT));
        level.setBlockAndUpdate(pos.relative(direction).below().relative(direction.getClockWise()), state.setValue(DrillHeadBlock.PART, DrillHeadPart.BIT_BOTTOM_RIGHT));
        level.setBlockAndUpdate(pos.relative(direction).relative(direction.getCounterClockWise()), state.setValue(DrillHeadBlock.PART, DrillHeadPart.BIT_LEFT));
        level.setBlockAndUpdate(pos.relative(direction), state.setValue(DrillHeadBlock.PART, DrillHeadPart.BIT_MIDDLE));
        level.setBlockAndUpdate(pos.relative(direction).relative(direction.getClockWise()), state.setValue(PART, DrillHeadPart.BIT_RIGHT));
        level.setBlockAndUpdate(pos.relative(direction).above(), state.setValue(DrillHeadBlock.PART, DrillHeadPart.BIT_TOP));
        level.setBlockAndUpdate(pos.relative(direction).above().relative(direction.getCounterClockWise()), state.setValue(DrillHeadBlock.PART, DrillHeadPart.BIT_TOP_LEFT));
        level.setBlockAndUpdate(pos.relative(direction).above().relative(direction.getClockWise()), state.setValue(DrillHeadBlock.PART, DrillHeadPart.BIT_TOP_RIGHT));
    }

    public BlockPos getBasePos(BlockState state, BlockPos pos) {
        Direction direction = state.getValue(FACING);

        return switch (state.getValue(PART)) {
            case BIT_RIGHT -> pos.relative(direction.getOpposite()).relative(direction.getCounterClockWise());
            case BIT_LEFT -> pos.relative(direction.getOpposite()).relative(direction.getClockWise());
            case BIT_TOP -> pos.relative(direction.getOpposite()).below();
            case BIT_BOTTOM -> pos.relative(direction.getOpposite()).above();
            case BIT_MIDDLE -> pos.relative(direction.getOpposite());
            case BIT_TOP_LEFT -> pos.relative(direction.getOpposite()).below().relative(direction.getClockWise());
            case BIT_BOTTOM_LEFT -> pos.relative(direction.getOpposite()).above().relative(direction.getClockWise());
            case BIT_TOP_RIGHT -> pos.relative(direction.getOpposite()).below().relative(direction.getCounterClockWise());
            case BIT_BOTTOM_RIGHT -> pos.relative(direction.getOpposite()).above().relative(direction.getCounterClockWise());
            default -> pos;
        };
    }

    public static void updateDrillingState(Level level, BlockPos pos, BlockState state) {
        Direction direction = state.getValue(FACING);

        level.setBlockAndUpdate(pos.relative(direction).below(), state.setValue(DrillHeadBlock.PART, DrillHeadPart.BIT_BOTTOM));
        level.setBlockAndUpdate(pos.relative(direction).below().relative(direction.getCounterClockWise()), state.setValue(DrillHeadBlock.PART, DrillHeadPart.BIT_BOTTOM_LEFT));
        level.setBlockAndUpdate(pos.relative(direction).below().relative(direction.getClockWise()), state.setValue(DrillHeadBlock.PART, DrillHeadPart.BIT_BOTTOM_RIGHT));
        level.setBlockAndUpdate(pos, state);
        level.setBlockAndUpdate(pos.relative(direction).relative(direction.getCounterClockWise()), state.setValue(DrillHeadBlock.PART, DrillHeadPart.BIT_LEFT));
        level.setBlockAndUpdate(pos.relative(direction), state.setValue(DrillHeadBlock.PART, DrillHeadPart.BIT_MIDDLE));
        level.setBlockAndUpdate(pos.relative(direction).relative(direction.getClockWise()), state.setValue(PART, DrillHeadPart.BIT_RIGHT));
        level.setBlockAndUpdate(pos.relative(direction).above(), state.setValue(DrillHeadBlock.PART, DrillHeadPart.BIT_TOP));
        level.setBlockAndUpdate(pos.relative(direction).above().relative(direction.getCounterClockWise()), state.setValue(DrillHeadBlock.PART, DrillHeadPart.BIT_TOP_LEFT));
        level.setBlockAndUpdate(pos.relative(direction).above().relative(direction.getClockWise()), state.setValue(DrillHeadBlock.PART, DrillHeadPart.BIT_TOP_RIGHT));
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.getValue(PART) == DrillHeadPart.BIT_MIDDLE &&
            state.getValue(DRILLING) &&
            ConfigManager.useParticles()
        ) {
            Direction direction = state.getValue(DrillHeadBlock.FACING);
            Direction.Axis direction$axis = direction.getAxis();

            double x = direction$axis == Direction.Axis.X ? pos.getX() + 0.44D : pos.getX();
            double y = pos.getY();
            double z = direction$axis == Direction.Axis.Z ? pos.getZ() + 0.44D : pos.getZ();

            for (int i = 0; i < 10; i++) {
                double randomDefault = level.getRandom().nextDouble() * (2.0D + 1.0D) - 1.0D;
                double randomX = direction$axis == Direction.Axis.X ? (double)direction.getStepX() * 0.44D : randomDefault;
                double randomY = level.getRandom().nextDouble() * (2.0D + 1.0D) - 1.0D;
                double randomZ = direction$axis == Direction.Axis.Z ? (double)direction.getStepZ() * 0.44D : randomDefault;

                level.addParticle(ParticleTypes.SMOKE, x + randomX, y + randomY, z + randomZ, 0.0D, 0.0D, 0.0D);
            }
        }

        super.animateTick(state, level, pos, random);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        if (state.getValue(PART) == DrillHeadPart.BASE) {
            return BlockEntityRegistry.DRILL_HEAD.create(pos, state);
        }

        return null;
    }
}
