package dev.the_fireplace.caterpillar.block.util;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum DrillHeadPart implements StringRepresentable {

    BASE("base"),

    BIT_TOP_LEFT("bit_top_left"),

    BIT_TOP("bit_top"),

    BIT_TOP_RIGHT("bit_top_right"),

    BIT_LEFT("bit_left"),

    BIT_MIDDLE("bit_middle"),

    BIT_RIGHT("bit_right"),

    BIT_BOTTOM_LEFT("bit_bottom_left"),

    BIT_BOTTOM("bit_bottom"),

    BIT_BOTTOM_RIGHT("bit_bottom_right");

    private final String name;

    DrillHeadPart(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }

    public @NotNull String getSerializedName() {
        return this.name;
    }
}
