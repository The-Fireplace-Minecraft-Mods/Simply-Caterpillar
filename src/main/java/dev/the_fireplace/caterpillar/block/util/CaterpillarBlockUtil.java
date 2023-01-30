package dev.the_fireplace.caterpillar.block.util;

import dev.the_fireplace.caterpillar.block.DrillBaseBlock;
import dev.the_fireplace.caterpillar.block.entity.DrillBaseBlockEntity;
import dev.the_fireplace.caterpillar.block.entity.DrillHeadBlockEntity;
import dev.the_fireplace.caterpillar.block.entity.StorageBlockEntity;
import dev.the_fireplace.caterpillar.init.BlockInit;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import static dev.the_fireplace.caterpillar.block.DrillBaseBlock.FACING;

public class CaterpillarBlockUtil {
    public static boolean isCaterpillarBlock(Block block) {
        return (block == BlockInit.DRILL_HEAD) ||
                (block == BlockInit.DECORATION) ||
                (block == BlockInit.REINFORCEMENT) ||
                (block == BlockInit.INCINERATOR) ||
                (block == BlockInit.COLLECTOR) ||
                (block == BlockInit.STORAGE) ||
                (block == BlockInit.DRILL_BASE);
    }

    public static BlockPos getCaterpillarHeadPos(World level, BlockPos pos, Direction direction) {
        BlockState state = level.getBlockState(pos);

        if (!CaterpillarBlockUtil.isCaterpillarBlock(state.getBlock())) {
            return pos.offset(direction.getOpposite());
        }

        return getCaterpillarHeadPos(level, pos.offset(direction), direction);
    }

    public static boolean canBreakBlock(Block block) {
        return !block.equals(Blocks.AIR) &&
                !block.equals(Blocks.WATER) &&
                !block.equals(Blocks.LAVA);
    }

    public static List<DrillBaseBlock> getConnectedCaterpillarBlocks(World level, BlockPos pos, List<DrillBaseBlock> caterpillarBlocks) {
        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();

        if (block instanceof DrillBaseBlock) {
            if (!caterpillarBlocks.contains(block)) {
                caterpillarBlocks.add((DrillBaseBlock) block);
            }

            Direction direction = state.get(FACING);

            getConnectedCaterpillarBlocks(level, pos.offset(direction.getOpposite()), caterpillarBlocks);
        }

        return caterpillarBlocks;
    }

    public static List<DrillBaseBlockEntity> getConnectedCaterpillarBlockEntities(World level, BlockPos pos, List<DrillBaseBlockEntity> caterpillarBlockEntities) {
        BlockState blockState = level.getBlockState(pos);

        if (!isCaterpillarBlock(blockState.getBlock())) {
            return caterpillarBlockEntities;
        }

        DrillBaseBlockEntity blockEntity = (DrillBaseBlockEntity) level.getBlockEntity(pos);
        if (caterpillarBlockEntities != null) {
            caterpillarBlockEntities.add(blockEntity);
        }

        if (blockEntity != null) {
            Direction direction = blockState.get(FACING);
            return getConnectedCaterpillarBlockEntities(level, pos.offset(direction.getOpposite()), caterpillarBlockEntities);
        }

        return caterpillarBlockEntities;
    }

    public static boolean isConnectedCaterpillarSameDirection(World level, BlockPos pos, Direction direction) {
        boolean isTheBlockInTheSameDirection = true;
        BlockEntity blockEntity;

        // Block in front
        blockEntity = level.getBlockEntity(pos.offset(direction));
        if (blockEntity != null) {
            isTheBlockInTheSameDirection = isBlockEntitySameDirection(blockEntity, direction);
        }

        // Block in back
        blockEntity = level.getBlockEntity(pos.offset(direction.getOpposite()));
        if (blockEntity != null) {
            isTheBlockInTheSameDirection = isBlockEntitySameDirection(blockEntity, direction);
        }

        // Block in left
        blockEntity = level.getBlockEntity(pos.offset(direction.rotateYClockwise()));
        if (blockEntity != null) {
            isTheBlockInTheSameDirection = isBlockEntitySameDirection(blockEntity, direction);
        }

        // Block in right
        blockEntity = level.getBlockEntity(pos.offset(direction.rotateYCounterclockwise()));
        if (blockEntity != null) {
            isTheBlockInTheSameDirection = isBlockEntitySameDirection(blockEntity, direction);
        }

        return isTheBlockInTheSameDirection;
    }

    private static boolean isBlockEntitySameDirection(BlockEntity blockEntity, Direction direction) {
        if (isCaterpillarBlock(blockEntity.getCachedState().getBlock())) {
            DrillBaseBlockEntity caterpillarBlockEntity = (DrillBaseBlockEntity) blockEntity;

            return caterpillarBlockEntity.getCachedState().get(HorizontalFacingBlock.FACING) == direction;
        }

        return true;
    }

    public static List<DrillBaseBlockEntity> getConnectedDrillHeadAndStorageBlockEntities(World level, BlockPos pos, Direction direction) {
        BlockPos caterpillarHeadPos = CaterpillarBlockUtil.getCaterpillarHeadPos(level, pos, direction);
        List<DrillBaseBlockEntity> caterpillarBlockEntities = CaterpillarBlockUtil.getConnectedCaterpillarBlockEntities(level, caterpillarHeadPos, new ArrayList<>(0));
        DrillHeadBlockEntity drillHeadBlockEntity = CaterpillarBlockUtil.getDrillHeadBlockEntity(caterpillarBlockEntities);
        StorageBlockEntity storageBlockEntity = CaterpillarBlockUtil.getStorageBlockEntity(caterpillarBlockEntities);
        // Because caterpillar is moving, it can have a space between the caterpillar blocks
        if (storageBlockEntity == null) {
            caterpillarBlockEntities = CaterpillarBlockUtil.getConnectedCaterpillarBlockEntities(level, caterpillarBlockEntities.get(caterpillarBlockEntities.size() - 1).getPos().offset(direction, 2), new ArrayList<>(0));
            storageBlockEntity = CaterpillarBlockUtil.getStorageBlockEntity(caterpillarBlockEntities);
        }

        if (drillHeadBlockEntity != null && storageBlockEntity != null) {
            return List.of(drillHeadBlockEntity, storageBlockEntity);
        } else if (drillHeadBlockEntity != null) {
            return List.of(drillHeadBlockEntity);
        }

        return null;
    }

    public static DrillHeadBlockEntity getDrillHeadBlockEntity(List<DrillBaseBlockEntity> caterpillarBlockEntities) {
        return (DrillHeadBlockEntity) caterpillarBlockEntities.stream().filter(blockEntity -> blockEntity instanceof DrillHeadBlockEntity).findFirst().orElse(null);
    }

    public static StorageBlockEntity getStorageBlockEntity(List<DrillBaseBlockEntity> caterpillarBlockEntities) {
        return (StorageBlockEntity) caterpillarBlockEntities.stream().filter(blockEntity -> blockEntity instanceof StorageBlockEntity).findFirst().orElse(null);
    }
}
