package dev.the_fireplace.caterpillar.block.util;

import dev.the_fireplace.caterpillar.Caterpillar;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.List;

public enum Replacement {
    WATER(0, Component.translatable("gui." + Caterpillar.MOD_ID + ".replacement.water"), List.of(Blocks.WATER)),
    LAVA(1, Component.translatable("gui." + Caterpillar.MOD_ID + ".replacement.lava"), List.of(Blocks.LAVA)),
    FALLING_BLOCKS(2, Component.translatable("gui." + Caterpillar.MOD_ID + ".replacement.falling_blocks"), List.of(Blocks.SAND, Blocks.RED_SAND, Blocks.GRAVEL)),
    AIR(3, Component.translatable("gui." + Caterpillar.MOD_ID + ".replacement.air"), List.of(Blocks.AIR)),
    ALL(4, Component.translatable("gui." + Caterpillar.MOD_ID + ".replacement.all"), List.of(Blocks.BARRIER, Blocks.STRUCTURE_BLOCK, Blocks.BEDROCK, Blocks.END_PORTAL_FRAME));

    public final int INDEX;
    public final MutableComponent NAME;

    public final List<Block> BLOCKS;

    Replacement(int index, MutableComponent name, List<Block> blocks) {
        this.INDEX = index;
        this.NAME = name;
        this.BLOCKS = blocks;
    }
}
