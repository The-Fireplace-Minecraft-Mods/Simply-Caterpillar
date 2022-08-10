package the_fireplace.caterpillar.common.block.util;

import net.minecraft.util.StringRepresentable;

public enum ReinforcementPart implements StringRepresentable {

    LEFT("left"),
    BASE("base"),
    RIGHT("right"),
    TOP("top"),
    BOTTOM("bottom");

    private final String name;

    private ReinforcementPart(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }

    public String getSerializedName() {
        return this.name;
    }
}