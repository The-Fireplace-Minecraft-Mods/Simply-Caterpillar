package the_fireplace.caterpillar.common.block.entity.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import the_fireplace.caterpillar.common.block.DrillHeadBlock;
import the_fireplace.caterpillar.common.block.entity.*;
import the_fireplace.caterpillar.common.block.util.DrillHeadPart;
import the_fireplace.caterpillar.core.init.BlockInit;

import static the_fireplace.caterpillar.common.block.DrillHeadBlock.PART;

public class CaterpillarBlocksUtil {

    public static void moveNextBlock(Level level, BlockPos pos, Direction direction) {
        BlockPos nextBlockPos = pos.relative(direction);
        BlockState nextBlockState = level.getBlockState(nextBlockPos);
        Block nextBlock = nextBlockState.getBlock();

        if (isCaterpillarBlock(nextBlock)) {
            BlockEntity nextBlockEntity = level.getBlockEntity(nextBlockPos);

            if (nextBlockEntity instanceof DrillBaseBlockEntity drillBaseBlockEntity) {
                drillBaseBlockEntity.move();
            }

            if (nextBlockEntity instanceof CollectorBlockEntity collectorBlockEntity) {
                collectorBlockEntity.move();
            }

            if (nextBlockEntity instanceof IncineratorBlockEntity incineratorBlockEntity) {
                incineratorBlockEntity.move();
            }

            if (nextBlockEntity instanceof StorageBlockEntity storageBlockEntity) {
                storageBlockEntity.move();
            }

            if (nextBlockEntity instanceof ReinforcementBlockEntity reinforcementBlockEntity) {
                reinforcementBlockEntity.move();
            }

            if (nextBlockEntity instanceof DecorationBlockEntity decorationBlockEntity) {
                decorationBlockEntity.move();
            }

            moveNextBlock(level, nextBlockPos, direction);
        }

        return;
    }

    public static boolean isCaterpillarBlock(Block block) {
        if (
                (block == BlockInit.DRILL_HEAD.get()) ||
                (block == BlockInit.DECORATION.get()) ||
                (block == BlockInit.REINFORCEMENT.get()) ||
                (block == BlockInit.INCINERATOR.get()) ||
                (block == BlockInit.COLLECTOR.get()) ||
                (block == BlockInit.STORAGE.get()) ||
                (block == BlockInit.DRILL_BASE.get())
        ) {
            return true;
        }

        return false;
    }

    public static BlockPos getCaterpillarPos(Level level, BlockPos pos, Direction direction) {
        BlockState state = level.getBlockState(pos);

        if (!CaterpillarBlocksUtil.isCaterpillarBlock(state.getBlock())) {
            return null;
        }

        if ((state.getBlock() instanceof DrillHeadBlock) && state.getValue(PART).equals(DrillHeadPart.BASE)) {
            return pos;
        }

        return getCaterpillarPos(level, pos.relative(direction.getOpposite()), direction);
    }

    public static boolean canBreakBlock(Block block) {
        if (
                block.equals(Blocks.AIR)  ||
                block.equals(Blocks.WATER) ||
                block.equals(Blocks.LAVA) ||
                block.equals(Fluids.FLOWING_WATER) ||
                block.equals(Fluids.FLOWING_LAVA)
        ) {
            return false;
        }

        return true;
    }
}
