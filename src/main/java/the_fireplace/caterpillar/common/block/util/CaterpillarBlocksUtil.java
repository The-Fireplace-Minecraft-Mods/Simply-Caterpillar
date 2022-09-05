package the_fireplace.caterpillar.common.block.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;
import the_fireplace.caterpillar.common.block.DrillHeadBlock;
import the_fireplace.caterpillar.common.block.entity.AbstractCaterpillarBlockEntity;
import the_fireplace.caterpillar.core.init.BlockInit;

import java.util.List;

import static the_fireplace.caterpillar.common.block.DrillHeadBlock.PART;

public class CaterpillarBlocksUtil {

    public static void moveNextBlock(Level level, BlockPos pos, Direction direction) {
        BlockPos nextBlockPos = pos.relative(direction);
        BlockState nextBlockState = level.getBlockState(nextBlockPos);
        Block nextBlock = nextBlockState.getBlock();

        if (isCaterpillarBlock(nextBlock)) {
            BlockEntity nextBlockEntity = level.getBlockEntity(nextBlockPos);

            if (nextBlockEntity instanceof AbstractCaterpillarBlockEntity) {
                AbstractCaterpillarBlockEntity caterpillarBlockEntity = (AbstractCaterpillarBlockEntity) nextBlockEntity;
                caterpillarBlockEntity.move();
            }

            moveNextBlock(level, nextBlockPos, direction);
        }

        return;
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

    public static BlockPos getDrillHeadPos(Level level, BlockPos pos, Direction direction) {
        BlockState state = level.getBlockState(pos);

        if (!CaterpillarBlocksUtil.isCaterpillarBlock(state.getBlock())) {
            return null;
        }

        if ((state.getBlock() instanceof DrillHeadBlock) && state.getValue(PART).equals(DrillHeadPart.BASE)) {
            return pos;
        }

        return getDrillHeadPos(level, pos.relative(direction.getOpposite()), direction);
    }

    public static boolean canBreakBlock(Block block) {
        return !block.equals(Blocks.AIR) &&
                !block.equals(Blocks.WATER) &&
                !block.equals(Blocks.LAVA) &&
                !block.equals(Fluids.FLOWING_WATER) &&
                !block.equals(Fluids.FLOWING_LAVA);
    }

     public static List<AbstractCaterpillarBlockEntity> getConnectedCaterpillarBlockEntities(Level level, BlockPos pos, @Nullable List<AbstractCaterpillarBlockEntity> caterpillarBlockEntities) {
         System.out.println("stage 2");

         List<AbstractCaterpillarBlockEntity> listCaterpillarBlockEntities = caterpillarBlockEntities;

        BlockState blockState = level.getBlockState(pos);

        if (!isCaterpillarBlock(blockState.getBlock())) {
            System.out.println("finish");
            return caterpillarBlockEntities;
        } else {
            System.out.println("continue");

            BlockEntity blockEntity = level.getBlockEntity(pos);

            if (blockEntity instanceof AbstractCaterpillarBlockEntity caterpillarBlockEntity) {
                System.out.println("stage 3");
                caterpillarBlockEntities.add(caterpillarBlockEntity);
            }

            Direction direction = blockEntity.getBlockState().getValue(HorizontalDirectionalBlock.FACING);

            listCaterpillarBlockEntities = getConnectedCaterpillarBlockEntities(level, pos.relative(direction), caterpillarBlockEntities);
        }

         return listCaterpillarBlockEntities;
     }
}
