package dev.the_fireplace.caterpillar.block;

import dev.the_fireplace.caterpillar.entity.SeatEntity;
import dev.the_fireplace.caterpillar.init.BlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class DrillSeatBlock extends AbstractCaterpillarBlock {

    private static final VoxelShape SHAPE = Stream.of(
        Block.box(6, 0, 0, 10, 6, 16),
        Block.box(13, 0, 0, 16, 16, 16),
        Block.box(10, 0, 13, 13, 16, 16),
        Block.box(10, 0, 0, 13, 10, 3),
        Block.box(10, 0, 3, 13, 10, 13),
        Block.box(6, 10, 0, 10, 10, 13),
        Block.box(6, 10, 13, 10, 16, 16),
        Block.box(6, 6, 16, 10, 10, 31),
        Block.box(0, 0, 0, 3, 16, 16),
        Block.box(3, 0, 0, 6, 10, 3),
        Block.box(3, 0, 3, 6, 10, 13),
        Block.box(3, 0, 13, 6, 16, 16)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    public DrillSeatBlock(Properties properties) {
        super(properties);
        super.runCalculation(SHAPES, SHAPE);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        return SeatEntity.create(level, pos, 0.4, player, state.getValue(FACING));
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