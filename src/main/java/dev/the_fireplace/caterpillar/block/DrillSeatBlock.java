package dev.the_fireplace.caterpillar.block;

import dev.the_fireplace.caterpillar.block.entity.DrillSeatBlockEntity;
import dev.the_fireplace.caterpillar.block.util.CaterpillarBlockUtil;
import dev.the_fireplace.caterpillar.entity.SeatEntity;
import dev.the_fireplace.caterpillar.init.BlockEntityInit;
import dev.the_fireplace.caterpillar.init.BlockInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.stream.Stream;

public class DrillSeatBlock extends DrillBaseBlock {

    private static final VoxelShape SHAPE = Stream.of(
        Block.box(0, 3, 0, 2, 16, 16),
        Block.box(2, 3, 2, 14, 4, 14),
        Block.box(14, 3, 0, 16, 16, 16),
        Block.box(6, 10, 0, 10, 16, 2),
        Block.box(6, 3, 0, 10, 6, 2),
        Block.box(10, 3, 0, 14, 16, 2),
        Block.box(10, 3, 14, 14, 16, 16),
        Block.box(0, 0, 0, 4, 3, 2),
        Block.box(0, 0, 2, 2, 3, 4),
        Block.box(12, 0, 0, 16, 3, 2),
        Block.box(14, 0, 2, 16, 3, 4),
        Block.box(0, 0, 14, 4, 3, 16),
        Block.box(0, 0, 12, 2, 3, 14),
        Block.box(12, 0, 14, 16, 3, 16),
        Block.box(14, 0, 12, 16, 3, 14),
        Block.box(2, 3, 14, 6, 16, 16),
        Block.box(2, 3, 0, 6, 16, 2),
        Block.box(6, 10, 14, 10, 16, 16),
        Block.box(6, 3, 14, 10, 6, 16),
        Block.box(6, 6, 16, 10, 10, 31)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    public DrillSeatBlock(Properties properties) {
        super(properties);
        super.runCalculation(SHAPES, DrillSeatBlock.SHAPE);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        return SeatEntity.create(level, pos, 0.4, player, state.getValue(FACING));
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos blockPos = context.getClickedPos();
        Level level = context.getLevel();
        Direction direction = context.getHorizontalDirection();

        BlockPos caterpillarHeadPos = CaterpillarBlockUtil.getCaterpillarHeadPos(level, blockPos.relative(direction), direction);

        if (CaterpillarBlockUtil.getConnectedCaterpillarBlockEntities(level, caterpillarHeadPos, new ArrayList<>()).stream().noneMatch(blockEntity -> blockEntity instanceof DrillSeatBlockEntity)) {
            if (CaterpillarBlockUtil.isConnectedCaterpillarSameDirection(level, blockPos, direction)) {
                return super.getStateForPlacement(context);
            }
        } else {
            context.getPlayer().displayClientMessage(new TranslatableComponent("block.simplycaterpillar.blocks.already_connected", BlockInit.DRILL_SEAT.get().getName()), true);
        }

        return null;
    }

    @Override
    public BlockPos getBasePos(BlockState state, BlockPos pos) {
        return pos;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return BlockEntityInit.DRILL_SEAT.get().create(pos, state);
    }
}