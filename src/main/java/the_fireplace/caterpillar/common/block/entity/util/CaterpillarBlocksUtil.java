package the_fireplace.caterpillar.common.block.entity.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import the_fireplace.caterpillar.common.block.DrillHeadBlock;
import the_fireplace.caterpillar.common.block.IncineratorBlock;
import the_fireplace.caterpillar.common.block.entity.CollectorBlockEntity;
import the_fireplace.caterpillar.common.block.entity.DecorationBlockEntity;
import the_fireplace.caterpillar.common.block.entity.DrillHeadBlockEntity;
import the_fireplace.caterpillar.common.block.entity.IncineratorBlockEntity;
import the_fireplace.caterpillar.common.block.util.DrillHeadPart;
import the_fireplace.caterpillar.core.init.BlockEntityInit;
import the_fireplace.caterpillar.core.init.BlockInit;

import static the_fireplace.caterpillar.common.block.DrillHeadBlock.PART;

public class CaterpillarBlocksUtil {

    public static void moveNextBlock(Level level, BlockPos pos, Direction direction) {
        /*
        Block block = state.getBlock();
        if (isCaterpillarBlock(block)) {

        }
        */

        BlockPos nextPos = pos.relative(direction);
        BlockEntity nextBlockEntity = level.getBlockEntity(nextPos);

        if (isCaterpillarBlock(nextBlockEntity.getBlockState().getBlock())) {
            if (nextBlockEntity instanceof CollectorBlockEntity collectorBlockEntity) {
                collectorBlockEntity.move(level, nextPos, nextBlockEntity.getBlockState());
            }

            moveNextBlock(level, nextPos, direction);
        }
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
}
