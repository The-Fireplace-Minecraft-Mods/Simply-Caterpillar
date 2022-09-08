package the_fireplace.caterpillar.common.block.util;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum StoragePart implements StringRepresentable {

    LEFT("left"),
    BASE("base"),
    RIGHT("right");

    private final String name;

    StoragePart(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }

    public @NotNull String getSerializedName() {
        return this.name;
    }
}
