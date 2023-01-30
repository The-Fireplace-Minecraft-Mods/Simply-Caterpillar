package dev.the_fireplace.caterpillar.block.util;

import net.minecraft.util.StringIdentifiable;
import org.jetbrains.annotations.NotNull;

public enum DecorationPart implements StringIdentifiable {

    LEFT("left"),
    BASE("base"),
    RIGHT("right");

    private final String name;

    DecorationPart(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }

    public @NotNull String asString() {
        return this.name;
    }
}
