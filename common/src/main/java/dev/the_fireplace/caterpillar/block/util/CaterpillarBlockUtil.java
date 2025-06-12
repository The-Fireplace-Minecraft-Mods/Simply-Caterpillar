package dev.the_fireplace.caterpillar.block.util;

import dev.the_fireplace.caterpillar.block.DrillBaseBlock;
import dev.the_fireplace.caterpillar.block.DrillHeadBlock;
import dev.the_fireplace.caterpillar.block.entity.DrillBaseBlockEntity;
import dev.the_fireplace.caterpillar.block.entity.DrillHeadBlockEntity;
import dev.the_fireplace.caterpillar.block.entity.StorageBlockEntity;
import dev.the_fireplace.caterpillar.registry.BlocksRegistry;
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
import java.util.concurrent.atomic.AtomicReference;

import static dev.the_fireplace.caterpillar.block.DrillBaseBlock.FACING;

public class CaterpillarBlockUtil {

    public static boolean isCaterpillarBlock(Block block) {
        return (block == BlocksRegistry.DRILL_HEAD.get()) ||
                (block == BlocksRegistry.DECORATION.get()) ||
                (block == BlocksRegistry.REINFORCEMENT.get()) ||
                (block == BlocksRegistry.INCINERATOR.get()) ||
                (block == BlocksRegistry.COLLECTOR.get()) ||
                (block == BlocksRegistry.STORAGE.get()) ||
                (block == BlocksRegistry.DRILL_BASE.get()) ||
                (block == BlocksRegistry.DRILL_SEAT.get()) ||
                (block == BlocksRegistry.TRANSPORTER.get());
    }

    public static BlockPos getCaterpillarHeadPos(Level level, BlockPos pos, Direction direction) {
        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();

        boolean isMiddleDrillHead = block == BlocksRegistry.DRILL_HEAD.get()
                && state.getValue(DrillHeadBlock.PART) == DrillHeadPart.BIT_MIDDLE;

        if (!isCaterpillarBlock(block) || isMiddleDrillHead) {
            return pos.relative(direction.getOpposite());
        }

        return getCaterpillarHeadPos(level, pos.relative(direction), direction);
    }

    public static boolean canBreakBlock(Block block) {
        return  !block.equals(Blocks.AIR) &&
                !block.equals(Blocks.WATER) &&
                !block.equals(Blocks.LAVA);
    }

    public static DrillBaseBlock getConnectedCaterpillarBlock(Level level, BlockPos blockPos, Block blockToFind) {
        List<DrillBaseBlock> connectedBlocks = getConnectedCaterpillarBlocks(level, blockPos);

        for (DrillBaseBlock block : connectedBlocks) {
            if (block.equals(blockToFind)) {
                return block;
            }
        }

        return null;
    }

    public static DrillBaseBlockEntity getConnectedCaterpillarBlockEntity(Level level, BlockPos pos, Block blockToFind) {
        List<DrillBaseBlockEntity> connectedEntities = getConnectedCaterpillarBlockEntities(level, pos);

        for (DrillBaseBlockEntity entity : connectedEntities) {
            if (entity.getBlockState().getBlock().equals(blockToFind)) {
                return entity;
            }
        }

        return null;
    }

    public static List<DrillBaseBlock> getConnectedCaterpillarBlocks(Level level, BlockPos pos) {
        List<DrillBaseBlock> result = new ArrayList<>();

        traverseConnectedCaterpillar(level, pos, (blockState, blockEntity) -> {
            if (blockState.getBlock() instanceof DrillBaseBlock drillBlock && !result.contains(drillBlock)) {
                result.add(drillBlock);
            }
        });

        return result;
    }

    public static List<DrillBaseBlockEntity> getConnectedCaterpillarBlockEntities(Level level, BlockPos pos) {
        List<DrillBaseBlockEntity> result = new ArrayList<>();

        traverseConnectedCaterpillar(level, pos, (blockState, blockEntity) -> {
            if (blockEntity instanceof DrillBaseBlockEntity drillEntity) {
                result.add(drillEntity);
            }
        });

        return result;
    }

    private interface CaterpillarVisitor {
        void visit(BlockState state, BlockEntity entity);
    }


    private static void traverseConnectedCaterpillar(Level level, BlockPos pos, CaterpillarVisitor visitor) {
        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();

        if (!(block instanceof DrillBaseBlock)) {
            return;
        }

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof DrillBaseBlockEntity)) {
            return;
        }

        List<BlockPos> visited = new ArrayList<>();
        while (true) {
            if (!visited.contains(pos)) {
                visitor.visit(state, blockEntity);
                visited.add(pos);
            }

            Direction direction = state.getValue(FACING);
            pos = pos.relative(direction.getOpposite());

            state = level.getBlockState(pos);
            block = state.getBlock();

            if (!(block instanceof DrillBaseBlock)) {
                break;
            }

            blockEntity = level.getBlockEntity(pos);
            if (!(blockEntity instanceof DrillBaseBlockEntity)) {
                break;
            }
        }
    }

    public static boolean isConnectedCaterpillarSameDirection(Level level, BlockPos pos, Direction direction) {
        final boolean[] result = { true };

        // Visit all 4 adjacent blocks (front, back, left, right)
        for (Direction dir : new Direction[] {
                direction,
                direction.getOpposite(),
                direction.getClockWise(),
                direction.getCounterClockWise()
        }) {
            BlockPos adjacentPos = pos.relative(dir);
            traverseConnectedCaterpillar(level, adjacentPos, (state, entity) -> {
                if (entity instanceof DrillBaseBlockEntity drillEntity) {
                    Direction facing = drillEntity.getBlockState().getValue(HorizontalDirectionalBlock.FACING);
                    if (facing != direction) {
                        result[0] = false;
                    }
                }
            });
            if (!result[0]) break; // Early exit if mismatch found
        }

        return result[0];
    }

    private static boolean isBlockEntitySameDirection(BlockEntity blockEntity, Direction direction) {
        if (isCaterpillarBlock(blockEntity.getBlockState().getBlock())) {
            DrillBaseBlockEntity caterpillarBlockEntity = (DrillBaseBlockEntity) blockEntity;

            return caterpillarBlockEntity.getBlockState().getValue(HorizontalDirectionalBlock.FACING) == direction;
        }

        return true;
    }

    public static List<? extends DrillBaseBlockEntity> getConnectedDrillHeadAndStorageBlockEntities(Level level, BlockPos pos, Direction direction) {
        BlockPos headPos = getCaterpillarHeadPos(level, pos, direction);

        AtomicReference<DrillHeadBlockEntity> drillHead = new AtomicReference<>();
        AtomicReference<StorageBlockEntity> storage = new AtomicReference<>();

        traverseConnectedCaterpillar(level, headPos, (state, entity) -> {
            if (!(entity instanceof DrillBaseBlockEntity base)) return;
            if (drillHead.get() == null && base instanceof DrillHeadBlockEntity) {
                drillHead.set((DrillHeadBlockEntity) base);
            } else if (storage.get() == null && base instanceof StorageBlockEntity) {
                storage.set((StorageBlockEntity) base);
            }
        });

        // Retry search if storage was not found due to a gap
        if (storage.get() == null) {
            BlockPos retryStart = headPos.relative(direction.getOpposite(), 2);
            traverseConnectedCaterpillar(level, retryStart, (state, entity) -> {
                if (!(entity instanceof DrillBaseBlockEntity base)) return;
                if (storage.get() == null && base instanceof StorageBlockEntity) {
                    storage.set((StorageBlockEntity) base);
                }
            });
        }

        if (drillHead.get() != null && storage.get() != null) {
            return List.of(drillHead.get(), storage.get());
        } else if (drillHead.get() != null) {
            return List.of(drillHead.get());
        }

        return null;
    }

    public static DrillHeadBlockEntity getDrillHeadBlockEntity(List<? extends DrillBaseBlockEntity> caterpillarBlockEntities) {
        return (DrillHeadBlockEntity)caterpillarBlockEntities.stream().filter(blockEntity -> blockEntity instanceof DrillHeadBlockEntity).findFirst().orElse(null);
    }

    public static StorageBlockEntity getStorageBlockEntity(List<? extends DrillBaseBlockEntity> caterpillarBlockEntities) {
        return (StorageBlockEntity)caterpillarBlockEntities.stream().filter(blockEntity -> blockEntity instanceof StorageBlockEntity).findFirst().orElse(null);
    }
}
