package dev.the_fireplace.caterpillar.block.util;

import dev.the_fireplace.caterpillar.block.DrillBaseBlock;
import dev.the_fireplace.caterpillar.block.DrillHeadBlock;
import dev.the_fireplace.caterpillar.block.entity.DrillBaseBlockEntity;
import dev.the_fireplace.caterpillar.block.entity.DrillHeadBlockEntity;
import dev.the_fireplace.caterpillar.block.entity.StorageBlockEntity;
import dev.the_fireplace.caterpillar.registry.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

import static dev.the_fireplace.caterpillar.block.DrillBaseBlock.FACING;

public class CaterpillarBlockUtil {

    public static boolean isCaterpillarBlock(Block block) {
        return (block == BlockRegistry.DRILL_HEAD.get()) ||
                (block == BlockRegistry.DECORATION.get()) ||
                (block == BlockRegistry.REINFORCEMENT.get()) ||
                (block == BlockRegistry.INCINERATOR.get()) ||
                (block == BlockRegistry.COLLECTOR.get()) ||
                (block == BlockRegistry.STORAGE.get()) ||
                (block == BlockRegistry.DRILL_BASE.get()) ||
                (block == BlockRegistry.DRILL_SEAT.get()) ||
                (block == BlockRegistry.TRANSPORTER.get());
    }

    public static BlockPos getCaterpillarHeadPos(Level level, BlockPos pos, Direction direction) {
        BlockState state = level.getBlockState(pos);

        if (!isCaterpillarBlock(state.getBlock())) {
            return pos.relative(direction.getOpposite());
        }

        if (state.getBlock() == BlockRegistry.DRILL_HEAD.get() && state.getValue(DrillHeadBlock.PART) == DrillHeadPart.BIT_MIDDLE) {
            return pos.relative(direction.getOpposite());
        }

        return getCaterpillarHeadPos(level, pos.relative(direction), direction);
    }

    public static boolean canBreakBlock(Block block) {
        return  !block.equals(Blocks.AIR) &&
                !block.equals(Blocks.WATER) &&
                !block.equals(Blocks.LAVA);
    }

    public static List<DrillBaseBlock> getConnectedCaterpillarBlocks(Level level, BlockPos pos, List<DrillBaseBlock> caterpillarBlocks) {
        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();

        if (block instanceof DrillBaseBlock) {
            if (!caterpillarBlocks.contains(block)) {
                caterpillarBlocks.add((DrillBaseBlock) block);
            }

            Direction direction = state.getValue(FACING);

            getConnectedCaterpillarBlocks(level, pos.relative(direction.getOpposite()), caterpillarBlocks);
        }

        return caterpillarBlocks;
    }

    public static List<DrillBaseBlockEntity> getConnectedCaterpillarBlockEntities(Level level, BlockPos pos, List<DrillBaseBlockEntity> caterpillarBlockEntities) {
        BlockState blockState = level.getBlockState(pos);

        if (!isCaterpillarBlock(blockState.getBlock())) {
            return caterpillarBlockEntities;
        }

        DrillBaseBlockEntity blockEntity = (DrillBaseBlockEntity) level.getBlockEntity(pos);
        if (caterpillarBlockEntities != null) {
            caterpillarBlockEntities.add(blockEntity);
        }

        if (blockEntity != null) {
            Direction direction = blockState.getValue(FACING);
            return getConnectedCaterpillarBlockEntities(level, pos.relative(direction.getOpposite()), caterpillarBlockEntities);
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
            DrillBaseBlockEntity caterpillarBlockEntity = (DrillBaseBlockEntity) blockEntity;

            return caterpillarBlockEntity.getBlockState().getValue(HorizontalDirectionalBlock.FACING) == direction;
        }

        return true;
    }

    public static List<DrillBaseBlockEntity> getConnectedDrillHeadAndStorageBlockEntities(Level level, BlockPos pos, Direction direction) {
        BlockPos caterpillarHeadPos = getCaterpillarHeadPos(level, pos, direction);
        List<DrillBaseBlockEntity> caterpillarBlockEntities = getConnectedCaterpillarBlockEntities(level, caterpillarHeadPos, new ArrayList<>(0));
        DrillHeadBlockEntity drillHeadBlockEntity = getDrillHeadBlockEntity(caterpillarBlockEntities);
        StorageBlockEntity storageBlockEntity = getStorageBlockEntity(caterpillarBlockEntities);
        // Because caterpillar is moving, it can have a space between the caterpillar blocks
        if (storageBlockEntity == null) {
            caterpillarBlockEntities = getConnectedCaterpillarBlockEntities(level, caterpillarBlockEntities.get(caterpillarBlockEntities.size() - 1).getBlockPos().relative(direction.getOpposite(), 2), new ArrayList<>(0));
            storageBlockEntity = getStorageBlockEntity(caterpillarBlockEntities);
        }

        if (drillHeadBlockEntity != null && storageBlockEntity != null) {
            return List.of(drillHeadBlockEntity, storageBlockEntity);
        } else if (drillHeadBlockEntity != null) {
            return List.of(drillHeadBlockEntity);
        }

        return null;
    }

    public static DrillHeadBlockEntity getDrillHeadBlockEntity(List<DrillBaseBlockEntity> caterpillarBlockEntities) {
        return (DrillHeadBlockEntity)caterpillarBlockEntities.stream().filter(blockEntity -> blockEntity instanceof DrillHeadBlockEntity).findFirst().orElse(null);
    }

    public static StorageBlockEntity getStorageBlockEntity(List<DrillBaseBlockEntity> caterpillarBlockEntities) {
        return (StorageBlockEntity)caterpillarBlockEntities.stream().filter(blockEntity -> blockEntity instanceof StorageBlockEntity).findFirst().orElse(null);
    }
}
