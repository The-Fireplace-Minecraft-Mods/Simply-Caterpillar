package the_fireplace.caterpillar.common.block.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import the_fireplace.caterpillar.common.block.entity.AbstractCaterpillarBlockEntity;
import the_fireplace.caterpillar.core.init.BlockInit;

import java.util.List;

public class CaterpillarBlockUtil {

    public static void moveNextBlock(Level level, BlockPos pos, Direction direction) {
        BlockPos nextBlockPos = pos.relative(direction);
        BlockState nextBlockState = level.getBlockState(nextBlockPos);
        Block nextBlock = nextBlockState.getBlock();

        if (isCaterpillarBlock(nextBlock)) {
            BlockEntity nextBlockEntity = level.getBlockEntity(nextBlockPos);

            if (nextBlockEntity instanceof AbstractCaterpillarBlockEntity caterpillarBlockEntity) {
                caterpillarBlockEntity.move();
            }

            moveNextBlock(level, nextBlockPos, direction);
        }
    }

    public static boolean isCaterpillarBlock(Block block) {
        return (block == BlockInit.DRILL_HEAD.get()) ||
                (block == BlockInit.DECORATION.get()) ||
                (block == BlockInit.REINFORCEMENT.get()) ||
                (block == BlockInit.INCINERATOR.get()) ||
                (block == BlockInit.COLLECTOR.get()) ||
                (block == BlockInit.STORAGE.get()) ||
                (block == BlockInit.DRILL_BASE.get());
    }

    public static BlockPos getCaterpillarHeadPos(Level level, BlockPos pos, Direction direction) {
        BlockState state = level.getBlockState(pos);

        if (!CaterpillarBlockUtil.isCaterpillarBlock(state.getBlock())) {
            return pos.relative(direction);
        }

        return getCaterpillarHeadPos(level, pos.relative(direction.getOpposite()), direction);
    }

    public static boolean canBreakBlock(Block block) {
        return !block.equals(Blocks.AIR) &&
                !block.equals(Blocks.WATER) &&
                !block.equals(Blocks.LAVA);
    }

    public static List<AbstractCaterpillarBlockEntity> getConnectedCaterpillarBlockEntities(Level level, BlockPos pos, @Nullable List<AbstractCaterpillarBlockEntity> caterpillarBlockEntities) {
        BlockState blockState = level.getBlockState(pos);

        if (!isCaterpillarBlock(blockState.getBlock())) {
            return caterpillarBlockEntities;
        }

        AbstractCaterpillarBlockEntity blockEntity = (AbstractCaterpillarBlockEntity) level.getBlockEntity(pos);
        assert caterpillarBlockEntities != null;
        caterpillarBlockEntities.add(blockEntity);

        assert blockEntity != null;
        Direction direction = blockEntity.getBlockState().getValue(HorizontalDirectionalBlock.FACING);
        return getConnectedCaterpillarBlockEntities(level, pos.relative(direction), caterpillarBlockEntities);
     }
}
