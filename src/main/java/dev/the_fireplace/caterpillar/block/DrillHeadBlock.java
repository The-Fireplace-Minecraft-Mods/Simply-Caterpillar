package dev.the_fireplace.caterpillar.block;

import dev.the_fireplace.caterpillar.block.entity.DrillBaseBlockEntity;
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
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import dev.the_fireplace.caterpillar.block.entity.DrillHeadBlockEntity;
import dev.the_fireplace.caterpillar.block.util.CaterpillarBlockUtil;
import dev.the_fireplace.caterpillar.block.util.DrillHeadPart;
import dev.the_fireplace.caterpillar.config.CaterpillarConfig;
import dev.the_fireplace.caterpillar.init.BlockEntityInit;
import dev.the_fireplace.caterpillar.init.BlockInit;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Stream;

public class DrillHeadBlock extends DrillBaseBlock {

    public static final BooleanProperty DRILLING = BooleanProperty.create("drilling");

    public static final EnumProperty<DrillHeadPart> PART = EnumProperty.create("part", DrillHeadPart.class);

    private static final Map<Direction, VoxelShape> SHAPES_BASE = new EnumMap<>(Direction.class);

    private static final Map<Direction, VoxelShape> SHAPES_BLADES = new EnumMap<>(Direction.class);

    private static final VoxelShape SHAPE_BASE = Stream.of(
        Block.box(0, 10, 1, 16, 16, 16),
        Block.box(0, 6, 1, 16, 10, 16),
        Block.box(0, 0, 1, 16, 6, 16),
        Block.box(6, 6, 16, 10, 10, 31),
        Block.box(0, 0, 0.5, 16, 16, 0.5),
        Block.box(0, 0, 0, 16, 16, 1)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    private static final VoxelShape SHAPE_BLADES = Stream.of(
        Block.box(0, 0, 0, 16, 16, 1),
        Block.box(0, 0, 0.5, 16, 16, 0.5)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    public DrillHeadBlock(Properties properties) {
        super(properties);
        super.registerDefaultState(defaultBlockState().setValue(DrillHeadBlock.PART, DrillHeadPart.BLADE_BOTTOM).setValue(DrillHeadBlock.WATERLOGGED, Boolean.FALSE).setValue(DrillHeadBlock.DRILLING, Boolean.FALSE));
        super.runCalculation(DrillHeadBlock.SHAPES_BLADES, DrillHeadBlock.SHAPE_BLADES);
        super.runCalculation(DrillHeadBlock.SHAPES_BASE, DrillHeadBlock.SHAPE_BASE);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(DrillHeadBlock.PART, DrillHeadBlock.DRILLING);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        if (state.getValue(DrillHeadBlock.PART) == DrillHeadPart.BASE) {
            return DrillHeadBlock.SHAPES_BASE.get(state.getValue(FACING));
        }
        return DrillHeadBlock.SHAPES_BLADES.get(state.getValue(FACING));
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
            level.getBlockState(blockPos.above(2)).canBeReplaced(context) &&
            level.getBlockState(blockPos.above(2).relative(direction.getClockWise())).canBeReplaced(context) &&
            level.getBlockState(blockPos.above(2).relative(direction.getCounterClockWise())).canBeReplaced(context)
        ) {
            BlockPos caterpillarHeadPos = CaterpillarBlockUtil.getCaterpillarHeadPos(level, blockPos.above().relative(direction), direction);

            if (CaterpillarBlockUtil.getConnectedCaterpillarBlockEntities(level, caterpillarHeadPos, new ArrayList<>()).stream().noneMatch(blockEntity -> blockEntity instanceof DrillHeadBlockEntity)) {
                if (CaterpillarBlockUtil.isConnectedCaterpillarSameDirection(level, blockPos.above(), direction)) {
                    return defaultBlockState().setValue(FACING, direction).setValue(DrillHeadBlock.PART, DrillHeadPart.BLADE_BOTTOM).setValue(DrillHeadBlock.WATERLOGGED, fluidState.getType() == Fluids.WATER);
                }
            } else {
                context.getPlayer().displayClientMessage(Component.translatable("block.simplycaterpillar.blocks.already_connected", BlockInit.DRILL_HEAD.get().getName()), true);
            }

        }

        return null;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
        return createTickerHelper(type, BlockEntityInit.DRILL_HEAD.get(), DrillHeadBlockEntity::tick);
    }

    @Override
    public void setPlacedBy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @Nullable LivingEntity livingEntity, @NotNull ItemStack stack) {
        buildStructure(level, pos, state);

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
    public void playerWillDestroy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Player player) {
        BlockPos basePos = getBasePos(state, pos);
        this.dropContents(level, basePos);
        this.destroyStructure(level, basePos, state, player);

        super.playerWillDestroy(level, pos, state, player);
    }

    private void destroyStructure(Level level, BlockPos pos, BlockState state, Player player) {
        Direction direction = state.getValue(FACING);

        level.destroyBlock(pos, !player.isCreative());
        level.destroyBlock(pos.relative(direction.getCounterClockWise()), false);
        level.destroyBlock(pos.relative(direction.getClockWise()), false);
        level.destroyBlock(pos.above(), false);
        level.destroyBlock(pos.below(), false);
        level.destroyBlock(pos.above().relative(direction.getCounterClockWise()), false);
        level.destroyBlock(pos.above().relative(direction.getClockWise()), false);
        level.destroyBlock(pos.below().relative(direction.getCounterClockWise()), false);
        level.destroyBlock(pos.below().relative(direction.getClockWise()), false);
    }

    public static void removeStructure(Level level, BlockPos pos, BlockState state) {
        Direction direction = state.getValue(FACING);

        level.removeBlock(pos, false);
        level.removeBlock(pos.relative(direction.getCounterClockWise()), false);
        level.removeBlock(pos.relative(direction.getClockWise()), false);
        level.removeBlock(pos.above(), false);
        level.removeBlock(pos.below(), false);
        level.removeBlock(pos.above().relative(direction.getCounterClockWise()), false);
        level.removeBlock(pos.above().relative(direction.getClockWise()), false);
        level.removeBlock(pos.below().relative(direction.getCounterClockWise()), false);
        level.removeBlock(pos.below().relative(direction.getClockWise()), false);
    }

    public static void buildStructure(Level level, BlockPos pos, BlockState state) {
        Direction direction = state.getValue(FACING);

        level.setBlockAndUpdate(pos.relative(direction.getCounterClockWise()), state.setValue(DrillHeadBlock.PART, DrillHeadPart.BLADE_LEFT_BOTTOM).setValue(DrillHeadBlock.WATERLOGGED, level.getFluidState(pos.relative(direction.getCounterClockWise())).getType() == Fluids.WATER));
        level.setBlockAndUpdate(pos.relative(direction.getClockWise()), state.setValue(DrillHeadBlock.PART, DrillHeadPart.BLADE_RIGHT_BOTTOM).setValue(DrillHeadBlock.WATERLOGGED, level.getFluidState(pos.relative(direction.getClockWise())).getType() == Fluids.WATER));
        level.setBlockAndUpdate(pos.above().relative(direction.getCounterClockWise()), state.setValue(DrillHeadBlock.PART, DrillHeadPart.BLADE_LEFT).setValue(DrillHeadBlock.WATERLOGGED, level.getFluidState(pos.above().relative(direction.getCounterClockWise())).getType() == Fluids.WATER));
        level.setBlockAndUpdate(pos.above().relative(direction.getClockWise()), state.setValue(PART, DrillHeadPart.BLADE_RIGHT).setValue(DrillHeadBlock.WATERLOGGED, level.getFluidState(pos.above().relative(direction.getClockWise())).getType() == Fluids.WATER));
        level.setBlockAndUpdate(pos.above(2), state.setValue(DrillHeadBlock.PART, DrillHeadPart.BLADE_TOP).setValue(DrillHeadBlock.WATERLOGGED, level.getFluidState(pos.above(2)).getType() == Fluids.WATER));
        level.setBlockAndUpdate(pos.above(2).relative(direction.getCounterClockWise()), state.setValue(DrillHeadBlock.PART, DrillHeadPart.BLADE_LEFT_TOP).setValue(DrillHeadBlock.WATERLOGGED, level.getFluidState(pos.above(2).relative(direction.getCounterClockWise())).getType() == Fluids.WATER));
        level.setBlockAndUpdate(pos.above(2).relative(direction.getClockWise()), state.setValue(DrillHeadBlock.PART, DrillHeadPart.BLADE_RIGHT_TOP).setValue(DrillHeadBlock.WATERLOGGED, level.getFluidState(pos.above(2).relative(direction.getClockWise())).getType() == Fluids.WATER));
        level.setBlockAndUpdate(pos.above(), state.setValue(DrillHeadBlock.PART, DrillHeadPart.BASE).setValue(WATERLOGGED, level.getFluidState(pos.above()).getType() == Fluids.WATER));
    }

    public static void moveStructure(Level level, BlockPos pos, BlockState state) {
        Direction direction = state.getValue(FACING);

        level.setBlockAndUpdate(pos.below(), state.setValue(DrillHeadBlock.PART, DrillHeadPart.BLADE_BOTTOM));
        level.setBlockAndUpdate(pos.below().relative(direction.getCounterClockWise()), state.setValue(DrillHeadBlock.PART, DrillHeadPart.BLADE_LEFT_BOTTOM));
        level.setBlockAndUpdate(pos.below().relative(direction.getClockWise()), state.setValue(DrillHeadBlock.PART, DrillHeadPart.BLADE_RIGHT_BOTTOM));
        level.setBlockAndUpdate(pos.relative(direction.getCounterClockWise()), state.setValue(DrillHeadBlock.PART, DrillHeadPart.BLADE_LEFT));
        level.setBlockAndUpdate(pos.relative(direction.getClockWise()), state.setValue(PART, DrillHeadPart.BLADE_RIGHT));
        level.setBlockAndUpdate(pos.above(), state.setValue(DrillHeadBlock.PART, DrillHeadPart.BLADE_TOP));
        level.setBlockAndUpdate(pos.above().relative(direction.getCounterClockWise()), state.setValue(DrillHeadBlock.PART, DrillHeadPart.BLADE_LEFT_TOP));
        level.setBlockAndUpdate(pos.above().relative(direction.getClockWise()), state.setValue(DrillHeadBlock.PART, DrillHeadPart.BLADE_RIGHT_TOP));
    }

    public BlockPos getBasePos(BlockState state, BlockPos pos) {
        Direction direction = state.getValue(FACING);

        return switch (state.getValue(PART)) {
            case BLADE_RIGHT -> pos.relative(direction.getCounterClockWise());
            case BLADE_LEFT -> pos.relative(direction.getClockWise());
            case BLADE_TOP -> pos.below();
            case BLADE_BOTTOM -> pos.above();
            case BLADE_LEFT_TOP -> pos.below().relative(direction.getClockWise());
            case BLADE_LEFT_BOTTOM -> pos.above().relative(direction.getClockWise());
            case BLADE_RIGHT_TOP -> pos.below().relative(direction.getCounterClockWise());
            case BLADE_RIGHT_BOTTOM -> pos.above().relative(direction.getCounterClockWise());
            default -> pos;
        };
    }

    public static void updateDrillingState(Level level, BlockPos pos, BlockState state) {
        Direction direction = state.getValue(FACING);

        level.setBlockAndUpdate(pos.below(), state.setValue(DrillHeadBlock.PART, DrillHeadPart.BLADE_BOTTOM));
        level.setBlockAndUpdate(pos.below().relative(direction.getCounterClockWise()), state.setValue(DrillHeadBlock.PART, DrillHeadPart.BLADE_LEFT_BOTTOM));
        level.setBlockAndUpdate(pos.below().relative(direction.getClockWise()), state.setValue(DrillHeadBlock.PART, DrillHeadPart.BLADE_RIGHT_BOTTOM));
        level.setBlockAndUpdate(pos, state);
        level.setBlockAndUpdate(pos.relative(direction.getCounterClockWise()), state.setValue(DrillHeadBlock.PART, DrillHeadPart.BLADE_LEFT));
        level.setBlockAndUpdate(pos.relative(direction.getClockWise()), state.setValue(PART, DrillHeadPart.BLADE_RIGHT));
        level.setBlockAndUpdate(pos.above(), state.setValue(DrillHeadBlock.PART, DrillHeadPart.BLADE_TOP));
        level.setBlockAndUpdate(pos.above().relative(direction.getCounterClockWise()), state.setValue(DrillHeadBlock.PART, DrillHeadPart.BLADE_LEFT_TOP));
        level.setBlockAndUpdate(pos.above().relative(direction.getClockWise()), state.setValue(DrillHeadBlock.PART, DrillHeadPart.BLADE_RIGHT_TOP));
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.getValue(PART) == DrillHeadPart.BASE &&
            state.getValue(DRILLING) &&
            CaterpillarConfig.useParticles
        ) {
            Direction direction = level.getBlockEntity(pos).getBlockState().getValue(DrillHeadBlock.FACING);
            Direction.Axis direction$axis = direction.getAxis();

            double x = direction$axis == Direction.Axis.X ? pos.getX() + 0.55D : pos.getX();
            double y = pos.getY();
            double z = direction$axis == Direction.Axis.Z ? pos.getZ() + 0.55D : pos.getZ();

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
        return BlockEntityInit.DRILL_HEAD.get().create(pos, state);
    }
}
