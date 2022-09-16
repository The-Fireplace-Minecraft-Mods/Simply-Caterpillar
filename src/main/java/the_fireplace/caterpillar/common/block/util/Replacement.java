package the_fireplace.caterpillar.common.block.util;

import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.Collections;
import java.util.List;

public enum Replacement {
    AIR(0, Component.translatable("replacement.air"), List.of(Blocks.AIR)),
    WATER(1, Component.translatable("replacement.water"), List.of(Blocks.WATER)),
    LAVA(2, Component.translatable("replacement.lava"), List.of(Blocks.LAVA)),
    FALLING_BLOCKS(3, Component.translatable("replacement.falling_blocks"), List.of(Blocks.SAND, Blocks.GRAVEL)),
    ALL(4, Component.translatable("replacement.all"), Collections.emptyList());

    public final int INDEX;
    public final Component NAME;

    public final List<Block> BLOCKS;

    Replacement(int index, Component name, List<Block> blocks) {
        this.INDEX = index;
        this.NAME = name;
        this.BLOCKS = blocks;
    }
}
