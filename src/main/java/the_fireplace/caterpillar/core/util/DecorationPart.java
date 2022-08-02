package the_fireplace.caterpillar.core.util;

import net.minecraft.util.StringRepresentable;

public enum DecorationPart implements StringRepresentable {

    LEFT("left"),
    BASE("base"),
    RIGHT("right");

    private final String name;

    private DecorationPart(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }

    public String getSerializedName() {
        return this.name;
    }
}

