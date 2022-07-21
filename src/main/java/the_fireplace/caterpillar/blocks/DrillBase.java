package the_fireplace.caterpillar.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
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
import net.minecraftforge.network.NetworkHooks;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.containers.CaterpillarContainer;
import the_fireplace.caterpillar.init.BlockEntityInit;
import the_fireplace.caterpillar.tileentity.DrillHeadBlockEntity;

import javax.annotation.Nullable;
import java.util.stream.Stream;

public class DrillBase extends HorizontalDirectionalBlock implements EntityBlock {
    private static final VoxelShape SHAPE = Stream.of(
            Block.box(6, 0, 0, 10, 6, 16),
            Block.box(6, 10, 0, 10, 16, 16),
            Block.box(10, 0, 0, 16, 16, 16),
            Block.box(6, 6, -15, 10, 10, 0),
            Block.box(0, 0, 0, 6, 16, 16)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    public DrillBase(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return BlockEntityInit.DRILL_HEAD.get().create(pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        if(level.isClientSide) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if(blockEntity instanceof DrillHeadBlockEntity) {
                final MenuProvider container = new SimpleMenuProvider(CaterpillarContainer.getServerContainer(blockEntity, pos);
                CaterpillarContainer thisCaterpillar = Caterpillar.instance.getContainerCaterpillar(pos, state);

                NetworkHooks.openScreen((ServerPlayer) player, (DrillHeadBlockEntity) container, pos);
                return InteractionResult.SUCCESS;

            }
        }
        return ActionResultType.FAIL;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
       return SHAPE.get(state.getValue(FACING));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    /**
     * Called from inside the constructor {@link Block#Block(Properties)} to add all the properties to our blockstate
     */
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }
}
