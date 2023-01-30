package dev.the_fireplace.caterpillar.block.util;

import net.minecraft.util.StringIdentifiable;

public enum DrillHeadPart implements StringIdentifiable {
    BASE("base"),

    BLADE_TOP("blade_top"),

    BLADE_BOTTOM("blade_bottom"),

    BLADE_LEFT("blade_left"),

    BLADE_LEFT_TOP("blade_left_top"),

    BLADE_LEFT_BOTTOM("blade_left_bottom"),

    BLADE_RIGHT("blade_right"),

    BLADE_RIGHT_TOP("blade_right_top"),

    BLADE_RIGHT_BOTTOM("blade_right_bottom");

    private final String name;

    DrillHeadPart(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }

    @Override
    public String asString() {
        return this.name;
    }
}
