package the_fireplace.caterpillar.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;
import the_fireplace.caterpillar.common.block.entity.DrillHeadBlockEntity;
import the_fireplace.caterpillar.common.block.util.CaterpillarBlocksUtil;

import java.util.EnumMap;
import java.util.Map;

public abstract class AbstractCaterpillarBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    protected static final Map<Direction, VoxelShape> SHAPES = new EnumMap<>(Direction.class);

    protected AbstractCaterpillarBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return AbstractCaterpillarBlock.SHAPES.get(state.getValue(FACING));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction direction = context.getHorizontalDirection();

        return defaultBlockState().setValue(FACING, direction.getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            Direction direction = state.getValue(FACING);
            BlockPos basePos = getBasePos(state, pos);
            BlockPos caterpillarHeadPos = CaterpillarBlocksUtil.getCaterpillarHeadPos(level, basePos, direction);

            if (caterpillarHeadPos != null) {
                BlockEntity blockEntity = level.getBlockEntity(caterpillarHeadPos);
                if (blockEntity instanceof DrillHeadBlockEntity drillHeadBlockEntity) {
                    NetworkHooks.openScreen((ServerPlayer) player, drillHeadBlockEntity, caterpillarHeadPos);
                }
            } else {
                player.displayClientMessage(Component.translatable("block.simplycaterpillar.drill_head.not_found"), true);
            }

            return InteractionResult.CONSUME;
        }
    }

    public BlockPos getBasePos(BlockState state, BlockPos pos) {
        return pos;
    }

    protected void runCalculation(Map<Direction, VoxelShape> shapes, VoxelShape shape) {
        for (Direction direction : Direction.values())
            shapes.put(direction, calculateShapes(direction, shape));
    }

    public static VoxelShape calculateShapes(Direction to, VoxelShape shape) {
        final VoxelShape[] buffer = { shape, Shapes.empty() };

        final int times = (to.get2DDataValue() - Direction.NORTH.get2DDataValue() + 4) % 4;
        for (int i = 0; i < times; i++) {
            buffer[0].forAllBoxes((minX, minY, minZ, maxX, maxY,
                                   maxZ) -> buffer[1] = Shapes.or(buffer[1], Shapes.create(1 - maxZ, minY, minX, 1 - minZ, maxY, maxX)));
            buffer[0] = buffer[1];
            buffer[1] = Shapes.empty();
        }

        return buffer[0];
    }

    @Nullable
    @Override
    public abstract BlockEntity newBlockEntity(BlockPos pos, BlockState state);
}
