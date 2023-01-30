package dev.the_fireplace.caterpillar.block;

import dev.the_fireplace.caterpillar.block.entity.DrillBaseBlockEntity;
import dev.the_fireplace.caterpillar.block.entity.DrillHeadBlockEntity;
import dev.the_fireplace.caterpillar.block.util.DrillHeadPart;
import dev.the_fireplace.caterpillar.init.BlockEntityInit;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class DrillHeadBlock extends AbstractCaterpillarBlock {

    public static final BooleanProperty DRILLING = BooleanProperty.of("drilling");

    public static final EnumProperty<DrillHeadPart> PART = EnumProperty.of("part", DrillHeadPart.class);

    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    private static final Map<Direction, VoxelShape> SHAPES_BASE = new EnumMap<>(Direction.class);

    private static final Map<Direction, VoxelShape> SHAPES_BLADES = new EnumMap<>(Direction.class);

    private static final Optional<VoxelShape> SHAPE_BASE = Stream.of(
            Block.createCuboidShape(0, 10, 0, 16, 16, 15),
            Block.createCuboidShape(0, 6, 0, 16, 10, 15),
            Block.createCuboidShape(0, 0, 0, 16, 6, 15),
            Block.createCuboidShape(6, 6, -15, 10, 10, 0),
            Block.createCuboidShape(0, 0, 16, 16, 16, 16),
            Block.createCuboidShape(0, 0, 16, 16, 16, 16)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR));

    private static final Optional<VoxelShape> SHAPE_BLADES = Stream.of(
            Block.createCuboidShape(0, 0, 15, 16, 16, 16),
            Block.createCuboidShape(0, 0, 15.5, 16, 16, 15.5)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR));

    public DrillHeadBlock(Settings properties) {
        super(properties);
        super.setDefaultState(getDefaultState().with(DrillHeadBlock.PART, DrillHeadPart.BLADE_BOTTOM).with(DrillHeadBlock.WATERLOGGED, Boolean.TRUE).with(DrillHeadBlock.DRILLING, Boolean.FALSE));
        super.runCalculation(DrillHeadBlock.SHAPES_BLADES, DrillHeadBlock.SHAPE_BLADES.get());
        super.runCalculation(DrillHeadBlock.SHAPES_BASE, DrillHeadBlock.SHAPE_BASE.get());
    }

    public static void removeStructure(World level, BlockPos pos, BlockState state) {
        Direction direction = state.get(FACING);

        level.removeBlock(pos, false);
        level.removeBlock(pos.offset(direction.rotateYCounterclockwise()), false);
        level.removeBlock(pos.offset(direction.rotateYClockwise()), false);
        level.removeBlock(pos.up(), false);
        level.removeBlock(pos.down(), false);
        level.removeBlock(pos.up().offset(direction.rotateYCounterclockwise()), false);
        level.removeBlock(pos.up().offset(direction.rotateYClockwise()), false);
        level.removeBlock(pos.down().offset(direction.rotateYCounterclockwise()), false);
        level.removeBlock(pos.down().offset(direction.rotateYClockwise()), false);
    }

    public static void buildStructure(World level, BlockPos pos, BlockState state) {
        Direction direction = state.get(FACING);

        level.setBlockState(pos.offset(direction.rotateYCounterclockwise()), state.with(DrillHeadBlock.PART, DrillHeadPart.BLADE_LEFT_BOTTOM));
        level.setBlockState(pos.offset(direction.rotateYClockwise()), state.with(DrillHeadBlock.PART, DrillHeadPart.BLADE_RIGHT_BOTTOM));
        level.setBlockState(pos.up().offset(direction.rotateYCounterclockwise()), state.with(DrillHeadBlock.PART, DrillHeadPart.BLADE_LEFT));
        level.setBlockState(pos.up().offset(direction.rotateYClockwise()), state.with(PART, DrillHeadPart.BLADE_RIGHT));
        level.setBlockState(pos.up(2), state.with(DrillHeadBlock.PART, DrillHeadPart.BLADE_TOP));
        level.setBlockState(pos.up(2).offset(direction.rotateYCounterclockwise()), state.with(DrillHeadBlock.PART, DrillHeadPart.BLADE_LEFT_TOP));
        level.setBlockState(pos.up(2).offset(direction.rotateYClockwise()), state.with(DrillHeadBlock.PART, DrillHeadPart.BLADE_RIGHT_TOP));
        level.setBlockState(pos.up(), state.with(DrillHeadBlock.PART, DrillHeadPart.BASE));
    }

    public static void moveStructure(World level, BlockPos pos, BlockState state) {
        Direction direction = state.get(FACING);

        level.setBlockState(pos.down(), state.with(DrillHeadBlock.PART, DrillHeadPart.BLADE_BOTTOM));
        level.setBlockState(pos.down().offset(direction.rotateYCounterclockwise()), state.with(DrillHeadBlock.PART, DrillHeadPart.BLADE_LEFT_BOTTOM));
        level.setBlockState(pos.down().offset(direction.rotateYClockwise()), state.with(DrillHeadBlock.PART, DrillHeadPart.BLADE_RIGHT_BOTTOM));
        level.setBlockState(pos.offset(direction.rotateYCounterclockwise()), state.with(DrillHeadBlock.PART, DrillHeadPart.BLADE_LEFT));
        level.setBlockState(pos.offset(direction.rotateYClockwise()), state.with(PART, DrillHeadPart.BLADE_RIGHT));
        level.setBlockState(pos.up(), state.with(DrillHeadBlock.PART, DrillHeadPart.BLADE_TOP));
        level.setBlockState(pos.up().offset(direction.rotateYCounterclockwise()), state.with(DrillHeadBlock.PART, DrillHeadPart.BLADE_LEFT_TOP));
        level.setBlockState(pos.up().offset(direction.rotateYClockwise()), state.with(DrillHeadBlock.PART, DrillHeadPart.BLADE_RIGHT_TOP));
    }

    @Override
    protected void appendProperties(StateManager.@NotNull Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(DrillHeadBlock.PART, DrillHeadBlock.WATERLOGGED, DrillHeadBlock.DRILLING);
    }

    @Override
    public @NotNull VoxelShape getOutlineShape(BlockState state, @NotNull BlockView level, @NotNull BlockPos pos, @NotNull ShapeContext context) {
        if (state.get(DrillHeadBlock.PART) == DrillHeadPart.BASE) {
            return DrillHeadBlock.SHAPES_BASE.get(state.get(FACING));
        }
        return DrillHeadBlock.SHAPES_BLADES.get(state.get(FACING));
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        BlockPos blockPos = context.getBlockPos();
        World level = context.getWorld();
        Direction direction = context.getPlayerFacing();

        return getDefaultState().with(FACING, direction.getOpposite()).with(DrillHeadBlock.PART, DrillHeadPart.BLADE_BOTTOM).with(DrillHeadBlock.WATERLOGGED, context.getWorld().getFluidState(context.getBlockPos()).getFluid() == Fluids.WATER);
    }

    @Override
    public void onPlaced(@NotNull World level, @NotNull BlockPos pos, @NotNull BlockState state, @Nullable LivingEntity livingEntity, @NotNull ItemStack stack) {
        buildStructure(level, pos, state);

        super.onPlaced(level, pos, state, livingEntity, stack);
    }

    private void dropContents(World level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof DrillBaseBlockEntity caterpillarBlockEntity) {
            if (!level.isClient()) {
                // caterpillarBlockEntity.drops();
            }
        }
    }

    private void destroyStructure(World level, BlockPos pos, BlockState state, PlayerEntity player) {
        Direction direction = state.get(FACING);

        level.breakBlock(pos, !player.isCreative());
        level.breakBlock(pos.offset(direction.rotateYCounterclockwise()), false);
        level.breakBlock(pos.offset(direction.rotateYClockwise()), false);
        level.breakBlock(pos.up(), false);
        level.breakBlock(pos.down(), false);
        level.breakBlock(pos.up().offset(direction.rotateYCounterclockwise()), false);
        level.breakBlock(pos.up().offset(direction.rotateYClockwise()), false);
        level.breakBlock(pos.down().offset(direction.rotateYCounterclockwise()), false);
        level.breakBlock(pos.down().offset(direction.rotateYClockwise()), false);
    }

    @Override
    public void onBreak(@NotNull World level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull PlayerEntity player) {
        BlockPos basePos = getBasePos(state, pos);
        this.dropContents(level, basePos);
        this.destroyStructure(level, basePos, state, player);

        super.onBreak(level, pos, state, player);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull World level, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
        return checkType(type, BlockEntityInit.DRILL_HEAD, DrillHeadBlockEntity::tick);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new DrillHeadBlockEntity(pos, state);
    }
}
