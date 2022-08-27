package the_fireplace.caterpillar.common.block.util;

import net.minecraft.util.StringRepresentable;

public enum DecorationPart implements StringRepresentable {

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

    public String getSerializedName() {
        return this.name;
    }
}

