package the_fireplace.caterpillar.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.common.block.entity.StorageBlockEntity;
import the_fireplace.caterpillar.common.container.StorageContainer;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class StorageBlock extends HorizontalDirectionalBlock implements EntityBlock {

    private static final Map<Direction, VoxelShape> SHAPES = new EnumMap<>(Direction.class);
    private static final Optional<VoxelShape> SHAPE = Stream.of(
        Block.box(16, 10, 1, 31, 14, 15),
        Block.box(0, 0, 0, 6, 16, 16),
        Block.box(6, 6, -15, 10, 10, 0),
        Block.box(10, 0, 0, 16, 16, 16),
        Block.box(6, 10, 0, 10, 16, 16),
        Block.box(6, 0, 0, 10, 6, 16),
        Block.box(22.5, 7, 15, 24.5, 11, 16),
        Block.box(-8.5, 7, 15, -6.5, 11, 16),
        Block.box(-15, 10, 1, 0, 14, 15),
        Block.box(-15, 0, 1, 0, 10, 15),
        Block.box(16, 0, 1, 31, 10, 15)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR));

    public StorageBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH));
        runCalculation(SHAPE.get());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPES.get(state.getValue(FACING));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            this.openContainer(level, pos, player);
            return InteractionResult.CONSUME;
        }
    }

    protected void openContainer(Level level, BlockPos pos, Player player) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof StorageBlockEntity) {
            MenuProvider container = new SimpleMenuProvider(StorageContainer.getServerContainer((StorageBlockEntity) blockEntity, pos), StorageBlockEntity.TITLE);
            player.openMenu(container);
        }
    }

    protected void runCalculation(VoxelShape shape) {
        for (Direction direction : Direction.values())
            SHAPES.put(direction, Caterpillar.calculateShapes(direction, shape));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new StorageBlockEntity(pos, state);
    }
}
