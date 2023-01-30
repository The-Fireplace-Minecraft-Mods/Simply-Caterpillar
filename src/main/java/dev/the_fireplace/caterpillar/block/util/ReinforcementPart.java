package dev.the_fireplace.caterpillar.block.util;

import net.minecraft.util.StringIdentifiable;
import org.jetbrains.annotations.NotNull;

public enum ReinforcementPart implements StringIdentifiable {

    LEFT("left"),
    BASE("base"),
    RIGHT("right"),
    TOP("top"),
    BOTTOM("bottom");

    private final String name;

    ReinforcementPart(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }

    public @NotNull String asString() {
        return this.name;
    }
}
