package the_fireplace.caterpillar.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.common.block.entity.DrillHeadBlockEntity;
import the_fireplace.caterpillar.common.container.DrillHeadContainer;
import the_fireplace.caterpillar.core.util.DrillHeadPart;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static net.minecraft.world.level.block.Blocks.LEVER;
import static net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock.FACE;
import static the_fireplace.caterpillar.core.init.BlockInit.DRILL_HEAD_LEVER;

public class DrillHeadBlock extends HorizontalDirectionalBlock implements EntityBlock {

    // If lever is on, then drill head is powered.
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

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
        Block.box(0, 0, 16, 16, 16, 16)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR));

    public DrillHeadBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH).setValue(PART, DrillHeadPart.BLADE_BOTTOM).setValue(POWERED, false));
        runCalculation(SHAPES_BLADES, SHAPE_BLADES.get());
        runCalculation(SHAPES_BASE, SHAPE_BASE.get());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING, PART, POWERED);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        switch (state.getValue(PART)) {
            case BASE:
                return SHAPES_BASE.get(state.getValue(FACING));
            default:
                return SHAPES_BLADES.get(state.getValue(FACING));
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos blockPos = context.getClickedPos();
        Level level = context.getLevel();
        Direction direction = context.getNearestLookingDirection();

        if (blockPos.getY() < level.getMaxBuildHeight() - 1 && level.getBlockState(blockPos.relative(direction.getClockWise())).canBeReplaced(context) && level.getBlockState(blockPos.relative(direction.getCounterClockWise())).canBeReplaced(context) && level.getBlockState(blockPos.above().relative(direction.getClockWise())).canBeReplaced(context) && level.getBlockState(blockPos.above().relative(direction.getCounterClockWise())).canBeReplaced(context) && level.getBlockState(blockPos.above()).canBeReplaced(context) && level.getBlockState(blockPos.above(2)).canBeReplaced(context) && level.getBlockState(blockPos.above(2).relative(direction.getClockWise())).canBeReplaced(context) && level.getBlockState(blockPos.above(2).relative(direction.getCounterClockWise())).canBeReplaced(context)) {
            return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(PART, DrillHeadPart.BLADE_BOTTOM);
        }

        return null;
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos neighbor, boolean p_60514_) {
        Direction direction = level.getBlockState(neighbor).getValue(FACING);

        if (neighbor.relative(direction.getOpposite()).equals(pos)) {
            boolean flag = level.hasNeighborSignal(pos);

            if (!this.defaultBlockState().is(block) && flag != level.getBlockState(pos.below()).getValue(POWERED)) {
                System.out.println("Signal changed : " + flag);
                System.out.println("TARGET:" + pos.below());
                level.getBlockState(pos.below()).setValue(POWERED, true);
            }
        }
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null : (level0, pos, state0, blockEntity) -> ((DrillHeadBlockEntity) blockEntity).tick();
    }

    @Override
    public @NotNull InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            this.openContainer(level, pos, player);
            return InteractionResult.CONSUME;
        }
    }

    @Override
    public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, @Nullable LivingEntity livingEntity, ItemStack stack) {
        Direction direction = blockState.getValue(FACING);

        level.setBlock(blockPos.relative(direction.getCounterClockWise()), blockState.setValue(PART, DrillHeadPart.BLADE_LEFT_BOTTOM), 3);
        level.setBlock(blockPos.relative(direction.getClockWise()), blockState.setValue(PART, DrillHeadPart.BLADE_RIGHT_BOTTOM), 3);
        level.setBlock(blockPos.above().relative(direction.getCounterClockWise()), blockState.setValue(PART, DrillHeadPart.BLADE_LEFT), 3);
        level.setBlock(blockPos.above(2), blockState.setValue(PART, DrillHeadPart.BLADE_TOP), 3);
        level.setBlock(blockPos.above(2).relative(direction.getCounterClockWise()), blockState.setValue(PART, DrillHeadPart.BLADE_LEFT_TOP), 3);
        level.setBlock(blockPos.above(2).relative(direction.getClockWise()), blockState.setValue(PART, DrillHeadPart.BLADE_RIGHT_TOP), 3);
        level.setBlock(blockPos.above(), blockState.setValue(PART, DrillHeadPart.BASE), 3);
        level.setBlock(blockPos.above().relative(direction.getClockWise()), DRILL_HEAD_LEVER.get().defaultBlockState().setValue(FACING, direction.getClockWise()).setValue(FACE, AttachFace.WALL).setValue(POWERED, Boolean.valueOf(false)), 3);
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        Direction direction = state.getValue(FACING);
        DrillHeadPart part = state.getValue(PART);
        BlockPos basePos;

        switch (part) {
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

        level.destroyBlock(basePos, player.isCreative() ? false : true);
        level.destroyBlock(basePos.relative(direction.getCounterClockWise()), false);
        level.destroyBlock(basePos.relative(direction.getClockWise()), false);
        level.destroyBlock(basePos.above(), false);
        level.destroyBlock(basePos.below(), false);
        level.destroyBlock(basePos.above().relative(direction.getCounterClockWise()), false);
        level.destroyBlock(basePos.above().relative(direction.getClockWise()), false);
        level.destroyBlock(basePos.below().relative(direction.getCounterClockWise()), false);
        level.destroyBlock(basePos.below().relative(direction.getClockWise()), false);

        // level.removeBlockEntity(basePos);
    }

    protected void openContainer(Level level, BlockPos pos, Player player) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof DrillHeadBlockEntity) {
            MenuProvider container = new SimpleMenuProvider(DrillHeadContainer.getServerContainer((DrillHeadBlockEntity) blockEntity, pos), DrillHeadBlockEntity.TITLE);
            player.openMenu(container);
        }
    }

    protected void runCalculation(Map<Direction, VoxelShape> shapes, VoxelShape shape) {
        for (Direction direction : Direction.values())
            shapes.put(direction, Caterpillar.calculateShapes(direction, shape));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new DrillHeadBlockEntity(pos, state);
    }
}
