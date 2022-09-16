package the_fireplace.caterpillar.common.block.entity.util;

import the_fireplace.caterpillar.common.block.util.Replacement;

public final class Replacer {
    private final Replacement replacement;

    private boolean isActive;

    public Replacer(Replacement replacement, boolean isActive) {
        this.replacement = replacement;
        this.isActive = isActive;
    }

    public Replacement getReplacement() {
        return replacement;
    }

    public boolean isActive() {
        return isActive;
    }
}
