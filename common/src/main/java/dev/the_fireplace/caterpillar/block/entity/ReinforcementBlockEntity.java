package dev.the_fireplace.caterpillar.block.entity;

import dev.the_fireplace.caterpillar.Constants;
import dev.the_fireplace.caterpillar.block.util.Replacement;
import dev.the_fireplace.caterpillar.registry.BlockEntityTypesRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class ReinforcementBlockEntity extends DrillBaseBlockEntity {

    public static final Component TITLE = Component.translatable("container." + Constants.MOD_ID + ".reinforcement");

    public static final int REINFORCEMENT_SLOT_CEILING_START = 0;

    public static final int REINFORCEMENT_SLOT_CEILING_END = 4;

    public static final int REINFORCEMENT_SLOT_LEFT_START = 5;

    public static final int REINFORCEMENT_SLOT_LEFT_END = 7;

    public static final int REINFORCEMENT_SLOT_RIGHT_START = 8;

    public static final int REINFORCEMENT_SLOT_RIGHT_END = 10;

    public static final int REINFORCEMENT_SLOT_FLOOR_START = 11;

    public static final int REINFORCEMENT_SLOT_FLOOR_END = 15;

    public static final int INVENTORY_SIZE = 16;

    public final List<byte[]> replacers = new ArrayList<>();

    public static final int REPLACER_CEILING = 0;

    public static final int REPLACER_LEFT = 1;

    public static final int REPLACER_RIGHT = 2;

    public static final int REPLACER_FLOOR = 3;

    public ReinforcementBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityTypesRegistry.REINFORCEMENT.get(), pos, state, INVENTORY_SIZE);

        this.setDefaultReinforcementBlocks();
        this.setDefaultReplacers();
    }

    private void setDefaultReinforcementBlocks() {
        for (int i = 0; i < INVENTORY_SIZE; i++) {
            this.setItem(i, new ItemStack(Blocks.COBBLESTONE));
        }
    }

    private void setDefaultReplacers() {
        for (int i = 0; i <= 3; i++) {
            this.replacers.add(new byte[Replacement.values().length]);
        }

        // TODO: refactor this to use data tags fields instead of hardcoded indices

        this.replacers.get(REPLACER_CEILING)[Replacement.WATER.INDEX] = 1;
        this.replacers.get(REPLACER_CEILING)[Replacement.LAVA.INDEX] = 1;
        this.replacers.get(REPLACER_CEILING)[Replacement.FALLING_BLOCKS.INDEX] = 1;
        this.replacers.get(REPLACER_CEILING)[Replacement.AIR.INDEX] = 0;
        this.replacers.get(REPLACER_CEILING)[Replacement.ALL.INDEX] = 0;

        this.replacers.get(REPLACER_LEFT)[Replacement.WATER.INDEX] = 1;
        this.replacers.get(REPLACER_LEFT)[Replacement.LAVA.INDEX] = 1;
        this.replacers.get(REPLACER_LEFT)[Replacement.FALLING_BLOCKS.INDEX] = 0;
        this.replacers.get(REPLACER_LEFT)[Replacement.AIR.INDEX] = 0;
        this.replacers.get(REPLACER_LEFT)[Replacement.ALL.INDEX] = 0;

        this.replacers.get(REPLACER_RIGHT)[Replacement.WATER.INDEX] = 1;
        this.replacers.get(REPLACER_RIGHT)[Replacement.LAVA.INDEX] = 1;
        this.replacers.get(REPLACER_RIGHT)[Replacement.FALLING_BLOCKS.INDEX] = 0;
        this.replacers.get(REPLACER_RIGHT)[Replacement.AIR.INDEX] = 0;
        this.replacers.get(REPLACER_RIGHT)[Replacement.ALL.INDEX] = 0;

        this.replacers.get(REPLACER_FLOOR)[Replacement.WATER.INDEX] = 1;
        this.replacers.get(REPLACER_FLOOR)[Replacement.LAVA.INDEX] = 1;
        this.replacers.get(REPLACER_FLOOR)[Replacement.FALLING_BLOCKS.INDEX] = 0;
        this.replacers.get(REPLACER_FLOOR)[Replacement.AIR.INDEX] = 1;
        this.replacers.get(REPLACER_FLOOR)[Replacement.ALL.INDEX] = 0;
    }

    @Override
    public void move(Level level, BlockState state, BlockPos basePos, BlockPos nextBasePos, Direction direction) {
        super.move(level, state, basePos, nextBasePos, direction);
        this.moveStructure(level, state, basePos, nextBasePos, direction);

        BlockEntity newBlockEntity = level.getBlockEntity(nextBasePos);
        if (newBlockEntity instanceof ReinforcementBlockEntity reinforcement) {
            reinforcement.reinforce();
        }
    }

    private void moveStructure(Level level, BlockState state, BlockPos basePos, BlockPos nextBasePos, Direction direction) {
        level.setBlockAndUpdate(nextBasePos.relative(direction.getCounterClockWise()), level.getBlockState(basePos.relative(direction.getCounterClockWise())));
        level.setBlockAndUpdate(nextBasePos.relative(direction.getClockWise()), level.getBlockState(basePos.relative(direction.getClockWise())));
        level.setBlockAndUpdate(nextBasePos.above(), level.getBlockState(basePos.above()));
        level.setBlockAndUpdate(nextBasePos.below(), level.getBlockState(basePos.below()));

        level.removeBlock(basePos.relative(direction.getCounterClockWise()), false);
        level.removeBlock(basePos.relative(direction.getClockWise()), false);
        level.removeBlock(basePos.above(), false);
        level.removeBlock(basePos.below(), false);
    }

    private void reinforce() {
        // TODO: implement reinforcement logic
    }

    @Override
    public Component getDisplayName() {
        return TITLE;
    }
}
