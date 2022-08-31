package the_fireplace.caterpillar.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
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
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import the_fireplace.caterpillar.common.block.entity.DrillHeadBlockEntity;
import the_fireplace.caterpillar.common.block.util.AbstractCaterpillarBlock;
import the_fireplace.caterpillar.common.block.util.DrillHeadPart;
import the_fireplace.caterpillar.core.init.BlockInit;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class DrillHeadBlock extends AbstractCaterpillarBlock {

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
        Block.box(0, 0, 15.5, 16, 16, 15.5)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR));

    public DrillHeadBlock(Properties properties) {
        super(properties);
        super.registerDefaultState(defaultBlockState().setValue(DrillHeadBlock.PART, DrillHeadPart.BLADE_BOTTOM).setValue(DrillHeadBlock.POWERED, Boolean.valueOf(false)));
        super.runCalculation(DrillHeadBlock.SHAPES_BLADES, DrillHeadBlock.SHAPE_BLADES.get());
        super.runCalculation(DrillHeadBlock.SHAPES_BASE, DrillHeadBlock.SHAPE_BASE.get());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(DrillHeadBlock.PART, DrillHeadBlock.POWERED);
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
            return defaultBlockState().setValue(super.FACING, direction.getOpposite()).setValue(DrillHeadBlock.PART, DrillHeadPart.BLADE_BOTTOM);
        }

        return null;
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos neighbor, boolean p_60514_) {
        if (level.getBlockState(neighbor).getBlock() instanceof DrillHeadLeverBlock) {
            Direction direction = level.getBlockState(neighbor).getValue(super.FACING);

            if (neighbor.relative(direction.getOpposite()).equals(pos)) {
                boolean flag = level.hasNeighborSignal(pos);

                if (!this.defaultBlockState().is(block) && flag != level.getBlockState(pos).getValue(DrillHeadBlock.POWERED)) {
                    level.setBlock(pos, state.setValue(DrillHeadBlock.POWERED, Boolean.valueOf(flag)), 2);
                }
            }
        }
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null : (level0, pos0, state0, blockEntity) -> ((DrillHeadBlockEntity) blockEntity).tick(level0, pos0, state0, (DrillHeadBlockEntity) blockEntity);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity livingEntity, ItemStack stack) {
        buildStructure(level, pos, state);

        super.setPlacedBy(level, pos, state, livingEntity, stack);
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        BlockPos basePos = getBasePos(state, pos);

        if (!player.isCreative()) dropItems(level, basePos);
        destroyStructure(level, basePos, state, player);

        super.playerWillDestroy(level, pos, state, player);
    }

    private void destroyStructure(Level level, BlockPos pos, BlockState state, Player player) {
        Direction direction = state.getValue(super.FACING);

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
        Direction direction = state.getValue(super.FACING);

        level.setBlock(pos.relative(direction.getCounterClockWise()), state.setValue(DrillHeadBlock.PART, DrillHeadPart.BLADE_LEFT_BOTTOM), 3);
        level.setBlock(pos.relative(direction.getClockWise()), state.setValue(DrillHeadBlock.PART, DrillHeadPart.BLADE_RIGHT_BOTTOM), 3);
        level.setBlock(pos.above().relative(direction.getCounterClockWise()), state.setValue(DrillHeadBlock.PART, DrillHeadPart.BLADE_LEFT), 3);
        level.setBlock(pos.above(2), state.setValue(DrillHeadBlock.PART, DrillHeadPart.BLADE_TOP), 3);
        level.setBlock(pos.above(2).relative(direction.getCounterClockWise()), state.setValue(DrillHeadBlock.PART, DrillHeadPart.BLADE_LEFT_TOP), 3);
        level.setBlock(pos.above(2).relative(direction.getClockWise()), state.setValue(DrillHeadBlock.PART, DrillHeadPart.BLADE_RIGHT_TOP), 3);
        level.setBlock(pos.above(), state.setValue(DrillHeadBlock.PART, DrillHeadPart.BASE), 3);
        level.setBlock(pos.above().relative(direction.getClockWise()), BlockInit.DRILL_HEAD_LEVER.get().defaultBlockState().setValue(super.FACING, direction.getClockWise()).setValue(DrillHeadLeverBlock.FACE, AttachFace.WALL).setValue(DrillHeadBlock.POWERED, Boolean.valueOf(false)), 3);
    }

    /*
     Drop items inside the drill head
     */
    private void dropItems(Level level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof DrillHeadBlockEntity drillHeadBlockEntity) {
            for(int i = 0; i < drillHeadBlockEntity.getContainerSize(); ++i) {
                ItemStack itemStack = drillHeadBlockEntity.getItemInSlot(i);
                if (!itemStack.isEmpty()) {
                    ItemEntity itemEntity = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, itemStack);
                    level.addFreshEntity(itemEntity);
                }
            }
        }
    }

    protected BlockPos getBasePos(BlockState state, BlockPos pos) {
        Direction direction = state.getValue(super.FACING);
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
