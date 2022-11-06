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
import the_fireplace.caterpillar.common.block.entity.DrillHeadBlockEntity;
import the_fireplace.caterpillar.common.block.entity.StorageBlockEntity;
import the_fireplace.caterpillar.core.init.BlockInit;

import java.util.List;

public class CaterpillarBlockUtil {

    public static void moveCaterpillar(Level level, BlockPos pos, Direction direction) {
        BlockEntity blockEntity = level.getBlockEntity(pos);

        if (blockEntity instanceof AbstractCaterpillarBlockEntity caterpillarBlockEntity) {
            caterpillarBlockEntity.move();
            CaterpillarBlockUtil.moveCaterpillar(level, pos.relative(direction), direction);
        }
    }

    public static boolean isCaterpillarBlock(Block block) {
        return  (block == BlockInit.DRILL_HEAD.get()) ||
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
        return  !block.equals(Blocks.AIR) &&
                !block.equals(Blocks.WATER) &&
                !block.equals(Blocks.LAVA);
    }

    public static List<AbstractCaterpillarBlockEntity> getConnectedCaterpillarBlockEntities(Level level, BlockPos pos, @Nullable List<AbstractCaterpillarBlockEntity> caterpillarBlockEntities) {
        BlockState blockState = level.getBlockState(pos);

        if (!isCaterpillarBlock(blockState.getBlock())) {
            return caterpillarBlockEntities;
        }

        AbstractCaterpillarBlockEntity blockEntity = (AbstractCaterpillarBlockEntity) level.getBlockEntity(pos);
        if (caterpillarBlockEntities != null) {
            caterpillarBlockEntities.add(blockEntity);
        }

        if (blockEntity != null) {
            Direction direction = blockEntity.getBlockState().getValue(HorizontalDirectionalBlock.FACING);
            return getConnectedCaterpillarBlockEntities(level, pos.relative(direction), caterpillarBlockEntities);
        }

        return caterpillarBlockEntities;
    }

    public static boolean isConnectedCaterpillarSameDirection(Level level, BlockPos pos, Direction direction) {
        boolean isTheBlockInTheSameDirection = true;
        BlockEntity blockEntity;

        // Block in front
        blockEntity = level.getBlockEntity(pos.relative(direction));
        if (blockEntity != null) {
            isTheBlockInTheSameDirection = isBlockEntitySameDirection(blockEntity , direction);
        }

        // Block in back
        blockEntity = level.getBlockEntity(pos.relative(direction.getOpposite()));
        if (blockEntity != null) {
            isTheBlockInTheSameDirection = isBlockEntitySameDirection(blockEntity , direction);
        }

        // Block in left
        blockEntity = level.getBlockEntity(pos.relative(direction.getClockWise()));
        if (blockEntity != null) {
            isTheBlockInTheSameDirection = isBlockEntitySameDirection(blockEntity , direction);
        }

        // Block in right
        blockEntity = level.getBlockEntity(pos.relative(direction.getCounterClockWise()));
        if (blockEntity != null) {
            isTheBlockInTheSameDirection = isBlockEntitySameDirection(blockEntity , direction);
        }

        return isTheBlockInTheSameDirection;
    }

    private static boolean isBlockEntitySameDirection(BlockEntity blockEntity, Direction direction) {
        if (isCaterpillarBlock(blockEntity.getBlockState().getBlock())) {
            AbstractCaterpillarBlockEntity caterpillarBlockEntity = (AbstractCaterpillarBlockEntity) blockEntity;

            return caterpillarBlockEntity.getBlockState().getValue(HorizontalDirectionalBlock.FACING) == direction;
        }

        return true;
    }

    public static DrillHeadBlockEntity getDrillHeadBlockEntity(List<AbstractCaterpillarBlockEntity> caterpillarBlockEntities) {
        return (DrillHeadBlockEntity)caterpillarBlockEntities.stream().filter(blockEntity -> blockEntity instanceof DrillHeadBlockEntity).findFirst().orElse(null);
    }

    public static StorageBlockEntity getStorageBlockEntity(List<AbstractCaterpillarBlockEntity> caterpillarBlockEntities) {
        return (StorageBlockEntity)caterpillarBlockEntities.stream().filter(blockEntity -> blockEntity instanceof StorageBlockEntity).findFirst().orElse(null);
    }
}
