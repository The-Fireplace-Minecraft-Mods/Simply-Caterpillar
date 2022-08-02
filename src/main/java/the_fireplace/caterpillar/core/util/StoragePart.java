package the_fireplace.caterpillar.core.util;

import net.minecraft.util.StringRepresentable;

public enum StoragePart implements StringRepresentable {

    LEFT("left"),
    BASE("base"),
    RIGHT("right");

    private final String name;

    private StoragePart(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }

    public String getSerializedName() {
        return this.name;
    }
}
