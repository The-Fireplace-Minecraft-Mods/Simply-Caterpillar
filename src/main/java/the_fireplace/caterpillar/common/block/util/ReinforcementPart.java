package the_fireplace.caterpillar.common.block.util;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum ReinforcementPart implements StringRepresentable {

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

    public @NotNull String getSerializedName() {
        return this.name;
    }
}