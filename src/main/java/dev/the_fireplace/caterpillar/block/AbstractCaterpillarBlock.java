package dev.the_fireplace.caterpillar.block;

import dev.the_fireplace.caterpillar.block.entity.DrillHeadBlockEntity;
import dev.the_fireplace.caterpillar.block.util.CaterpillarBlockUtil;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
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

public abstract class AbstractCaterpillarBlock extends BlockWithEntity implements BlockEntityProvider {
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;

    protected static final Map<Direction, VoxelShape> SHAPES = new EnumMap<>(Direction.class);

    protected AbstractCaterpillarBlock(Settings properties) {
        super(properties);
        setDefaultState(getDefaultState().with(FACING, Direction.NORTH));
    }

    public static VoxelShape calculateShapes(Direction to, VoxelShape shape) {
        final VoxelShape[] buffer = {shape, VoxelShapes.empty()};

        final int times = (to.getHorizontal() - Direction.NORTH.getHorizontal() + 4) % 4;
        for (int i = 0; i < times; i++) {
            buffer[0].forEachBox((minX, minY, minZ, maxX, maxY,
                                  maxZ) -> buffer[1] = VoxelShapes.union(buffer[1], VoxelShapes.cuboidUnchecked(1 - maxZ, minY, minX, 1 - minZ, maxY, maxX)));
            buffer[0] = buffer[1];
            buffer[1] = VoxelShapes.empty();
        }

        return buffer[0];
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView level, BlockPos pos, ShapeContext context) {
        return AbstractCaterpillarBlock.SHAPES.get(state.get(FACING));
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        BlockPos pos = context.getBlockPos();
        World level = context.getWorld();
        Direction direction = context.getPlayerFacing();

        return getDefaultState().with(FACING, direction.getOpposite());
    }

    @Override
    public @NotNull BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public @NotNull BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    protected void appendProperties(StateManager.@NotNull Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACING);
    }

    public BlockPos getBasePos(BlockState state, BlockPos pos) {
        return pos;
    }

    protected void runCalculation(Map<Direction, VoxelShape> shapes, VoxelShape shape) {
        for (Direction direction : Direction.values())
            shapes.put(direction, calculateShapes(direction, shape));
    }

    @Override
    public @NotNull BlockRenderType getRenderType(@NotNull BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public ActionResult onUse(BlockState blockState, World level, BlockPos blockPos, PlayerEntity player, Hand interactionHand, BlockHitResult blockHitResult) {
        if (!level.isClient) {
            Direction direction = blockState.get(FACING);
            BlockPos basePos = getBasePos(blockState, blockPos);
            BlockPos caterpillarHeadPos = CaterpillarBlockUtil.getCaterpillarHeadPos(level, basePos, direction);

            BlockEntity blockEntity = level.getBlockEntity(caterpillarHeadPos);
            if (blockEntity instanceof DrillHeadBlockEntity drillHeadBlockEntity) {
                player.openHandledScreen(drillHeadBlockEntity);
            } else {
                player.sendMessage(Text.translatable("block.simplycaterpillar.drill_head.not_found"), true);
            }
        }

        return ActionResult.SUCCESS;
    }

    @Nullable
    @Override
    public abstract BlockEntity createBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state);

}
