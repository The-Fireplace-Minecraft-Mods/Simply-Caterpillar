package dev.the_fireplace.caterpillar.block.entity;

import dev.the_fireplace.caterpillar.Constants;
import dev.the_fireplace.caterpillar.block.ReinforcementBlock;
import dev.the_fireplace.caterpillar.block.util.CaterpillarBlockUtil;
import dev.the_fireplace.caterpillar.block.util.Replacement;
import dev.the_fireplace.caterpillar.registry.BlockEntityTypesRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.ByteArrayTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class ReinforcementBlockEntity extends DrillBaseBlockEntity {

    public static final Component TITLE = Component.translatable(
            "container." + Constants.MOD_ID + ".reinforcement"
    );

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
    public Component getDisplayName() {
        return TITLE;
    }
}
