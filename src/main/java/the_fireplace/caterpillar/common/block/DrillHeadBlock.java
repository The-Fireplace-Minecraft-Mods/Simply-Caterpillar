package the_fireplace.caterpillar.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import the_fireplace.caterpillar.common.block.entity.DrillHeadBlockEntity;
import the_fireplace.caterpillar.common.block.util.DrillHeadPart;
import the_fireplace.caterpillar.core.init.BlockEntityInit;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class DrillHeadBlock extends AbstractCaterpillarBlock {

    public static final EnumProperty<DrillHeadPart> PART = EnumProperty.create("part", DrillHeadPart.class);

    private static final Map<Direction, VoxelShape> SHAPES_BASE = new EnumMap<>(Direction.class);

    private static final Map<Direction, VoxelShape> SHAPES_BLADES = new EnumMap<>(Direction.class);

    private static final Optional<VoxelShape> SHAPE_BASE = Stream.of(
        Block.box(0, 10, 0, 16, 16, 15),
        Block.box(0, 6, 0, 16, 10, 15),
        Block.box(0, 0, 0, 16, 6, 15),
        Block.box(6, 6, -15, 10, 10, 0),
        Block.box(0, 0, 16, 16, 16, 16),
        Block.box(0, 0, 16, 16, 16, 16)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR));

    private static final Optional<VoxelShape> SHAPE_BLADES = Stream.of(
        Block.box(0, 0, 15, 16, 16, 16),
        Block.box(0, 0, 15.5, 16, 16, 15.5)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR));

    public DrillHeadBlock(Properties properties) {
        super(properties);
        super.registerDefaultState(defaultBlockState().setValue(DrillHeadBlock.PART, DrillHeadPart.BLADE_BOTTOM));
        super.runCalculation(DrillHeadBlock.SHAPES_BLADES, DrillHeadBlock.SHAPE_BLADES.get());
        super.runCalculation(DrillHeadBlock.SHAPES_BASE, DrillHeadBlock.SHAPE_BASE.get());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(DrillHeadBlock.PART);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        switch (state.getValue(DrillHeadBlock.PART)) {
            case BASE:
                return DrillHeadBlock.SHAPES_BASE.get(state.getValue(FACING));
            default:
                return DrillHeadBlock.SHAPES_BLADES.get(state.getValue(FACING));
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos blockPos = context.getClickedPos();
        Level level = context.getLevel();
        Direction direction = context.getHorizontalDirection();

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
            return defaultBlockState().setValue(FACING, direction.getOpposite()).setValue(DrillHeadBlock.PART, DrillHeadPart.BLADE_BOTTOM);
        }

        return null;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, BlockEntityInit.DRILL_HEAD.get(), DrillHeadBlockEntity::tick);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity livingEntity, ItemStack stack) {
        buildStructure(level, pos, state);

        super.setPlacedBy(level, pos, state, livingEntity, stack);
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        BlockPos basePos = getBasePos(state, pos);
        destroyStructure(level, basePos, state, player);

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

    private void buildStructure(Level level, BlockPos pos, BlockState state) {
        Direction direction = state.getValue(FACING);

        level.setBlock(pos.relative(direction.getCounterClockWise()), state.setValue(DrillHeadBlock.PART, DrillHeadPart.BLADE_LEFT_BOTTOM), 3);
        level.setBlock(pos.relative(direction.getClockWise()), state.setValue(DrillHeadBlock.PART, DrillHeadPart.BLADE_RIGHT_BOTTOM), 3);
        level.setBlock(pos.above().relative(direction.getCounterClockWise()), state.setValue(DrillHeadBlock.PART, DrillHeadPart.BLADE_LEFT), 3);
        level.setBlock(pos.above().relative(direction.getClockWise()), state.setValue(PART, DrillHeadPart.BLADE_RIGHT), 3);
        level.setBlock(pos.above(2), state.setValue(DrillHeadBlock.PART, DrillHeadPart.BLADE_TOP), 3);
        level.setBlock(pos.above(2).relative(direction.getCounterClockWise()), state.setValue(DrillHeadBlock.PART, DrillHeadPart.BLADE_LEFT_TOP), 3);
        level.setBlock(pos.above(2).relative(direction.getClockWise()), state.setValue(DrillHeadBlock.PART, DrillHeadPart.BLADE_RIGHT_TOP), 3);
        level.setBlock(pos.above(), state.setValue(DrillHeadBlock.PART, DrillHeadPart.BASE), 3);
    }

    public BlockPos getBasePos(BlockState state, BlockPos pos) {
        Direction direction = state.getValue(FACING);
        BlockPos basePos;

        switch (state.getValue(PART)) {
            case BLADE_LEFT:
                basePos = pos.relative(direction.getClockWise());
                break;
            case BLADE_TOP:
                basePos = pos.below();
                break;
            case BLADE_BOTTOM:
                basePos = pos.above();
                break;
            case BLADE_LEFT_TOP:
                basePos = pos.below().relative(direction.getClockWise());
                break;
            case BLADE_LEFT_BOTTOM:
                basePos = pos.above().relative(direction.getClockWise());
                break;
            case BLADE_RIGHT_TOP:
                basePos = pos.below().relative(direction.getCounterClockWise());
                break;
            case BLADE_RIGHT_BOTTOM:
                basePos = pos.above().relative(direction.getCounterClockWise());
                break;
            default:
                basePos = pos;
                break;
        }

        return basePos;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new DrillHeadBlockEntity(pos, state);
    }
}
