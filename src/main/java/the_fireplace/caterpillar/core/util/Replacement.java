package the_fireplace.caterpillar.core.util;

import net.minecraft.network.chat.Component;

public enum Replacement {
    AIR(0, Component.translatable("replacement.air")),
    WATER(1, Component.translatable("replacement.water")),
    LAVA(2, Component.translatable("replacement.lava")),
    FALLING_BLOCKS(3, Component.translatable("replacement.falling_blocks")),
    ALL(4, Component.translatable("replacement.all"));

    public final int value;
    public final Component name;

    Replacement(int value, Component name) {
        this.value = value;
        this.name = name;
    }
}
