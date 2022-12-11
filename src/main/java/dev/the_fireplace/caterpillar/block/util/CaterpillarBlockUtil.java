package dev.the_fireplace.caterpillar.block.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import dev.the_fireplace.caterpillar.block.entity.AbstractCaterpillarBlockEntity;
import dev.the_fireplace.caterpillar.block.entity.DrillHeadBlockEntity;
import dev.the_fireplace.caterpillar.block.entity.StorageBlockEntity;
import dev.the_fireplace.caterpillar.init.BlockInit;

import java.util.ArrayList;
import java.util.List;

public class CaterpillarBlockUtil {

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
            return pos.relative(direction.getOpposite());
        }

        return getCaterpillarHeadPos(level, pos.relative(direction), direction);
    }

    public static boolean canBreakBlock(Block block) {
        return  !block.equals(Blocks.AIR) &&
                !block.equals(Blocks.WATER) &&
                !block.equals(Blocks.LAVA);
    }

    public static List<AbstractCaterpillarBlockEntity> getConnectedCaterpillarBlockEntities(Level level, BlockPos pos, List<AbstractCaterpillarBlockEntity> caterpillarBlockEntities) {
        BlockState blockState = level.getBlockState(pos);

        if (!isCaterpillarBlock(blockState.getBlock())) {
            return caterpillarBlockEntities;
        }

        AbstractCaterpillarBlockEntity blockEntity = (AbstractCaterpillarBlockEntity) level.getBlockEntity(pos);
        if (caterpillarBlockEntities != null) {
            caterpillarBlockEntities.add(blockEntity);
        }

        if (blockEntity != null) {
            Direction direction = blockState.getValue(HorizontalDirectionalBlock.FACING);
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
            AbstractCaterpillarBlockEntity caterpillarBlockEntity = (AbstractCaterpillarBlockEntity) blockEntity;

            return caterpillarBlockEntity.getBlockState().getValue(HorizontalDirectionalBlock.FACING) == direction;
        }

        return true;
    }

    public static List<AbstractCaterpillarBlockEntity> getConnectedDrillHeadAndStorageBlockEntities(Level level, BlockPos pos, Direction direction) {
        BlockPos caterpillarHeadPos = CaterpillarBlockUtil.getCaterpillarHeadPos(level, pos, direction);
        List<AbstractCaterpillarBlockEntity> caterpillarBlockEntities = CaterpillarBlockUtil.getConnectedCaterpillarBlockEntities(level, caterpillarHeadPos, new ArrayList<>(0));
        DrillHeadBlockEntity drillHeadBlockEntity = CaterpillarBlockUtil.getDrillHeadBlockEntity(caterpillarBlockEntities);
        StorageBlockEntity storageBlockEntity = CaterpillarBlockUtil.getStorageBlockEntity(caterpillarBlockEntities);
        // Because caterpillar is moving, it can have a space between the caterpillar blocks
        if (storageBlockEntity == null) {
            caterpillarBlockEntities = CaterpillarBlockUtil.getConnectedCaterpillarBlockEntities(level, caterpillarBlockEntities.get(caterpillarBlockEntities.size() - 1).getBlockPos().relative(direction, 2), new ArrayList<>(0));
            storageBlockEntity = CaterpillarBlockUtil.getStorageBlockEntity(caterpillarBlockEntities);
        }

        if (drillHeadBlockEntity != null && storageBlockEntity != null) {
            return List.of(drillHeadBlockEntity, storageBlockEntity);
        } else if (drillHeadBlockEntity != null) {
            return List.of(drillHeadBlockEntity);
        }

        return null;
    }

    public static DrillHeadBlockEntity getDrillHeadBlockEntity(List<AbstractCaterpillarBlockEntity> caterpillarBlockEntities) {
        return (DrillHeadBlockEntity)caterpillarBlockEntities.stream().filter(blockEntity -> blockEntity instanceof DrillHeadBlockEntity).findFirst().orElse(null);
    }

    public static StorageBlockEntity getStorageBlockEntity(List<AbstractCaterpillarBlockEntity> caterpillarBlockEntities) {
        return (StorageBlockEntity)caterpillarBlockEntities.stream().filter(blockEntity -> blockEntity instanceof StorageBlockEntity).findFirst().orElse(null);
    }
}
