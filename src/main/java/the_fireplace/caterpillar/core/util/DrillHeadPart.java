package the_fireplace.caterpillar.core.util;

import net.minecraft.util.StringRepresentable;

public enum DrillHeadPart implements StringRepresentable {

    BASE("base"),
    BLADE_TOP("blade_top"),
    BLADE_BOTTOM("blade_bottom"),
    BLADE_LEFT("blade_left"),
    BLADE_LEFT_TOP("blade_left_top"),
    BLADE_LEFT_BOTTOM("blade_left_bottom"),
    BLADE_RIGHT_TOP("blade_right_top"),
    BLADE_RIGHT_BOTTOM("blade_right_bottom");

    private final String name;

    DrillHeadPart(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }

    public String getSerializedName() {
        return this.name;
    }
}
