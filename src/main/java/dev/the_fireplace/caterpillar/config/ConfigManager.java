package dev.the_fireplace.caterpillar.config;

import dev.the_fireplace.caterpillar.Caterpillar;
import dev.the_fireplace.caterpillar.compat.cloth.ClothConfigManager;

public class ConfigManager {

    protected static boolean clothPresent = false;

    public static boolean clothPresent() {
        boolean cloth = Caterpillar.MODS_LOADED.contains("cloth");
        if(cloth != clothPresent) {
            clothPresent = cloth;
        }

        return clothPresent;
    }

    public static boolean firstUse() {
        if(clothPresent()) {
            return ClothConfigManager.getConfig().firstUse;
        }

        return true;
    }

    public static boolean useParticles() {
        if(clothPresent()) {
            return ClothConfigManager.getConfig().useParticles;
        }

        return true;
    }

    public static boolean breakUnbreakableBlocks() {
        if(clothPresent()) {
            return ClothConfigManager.getConfig().breakUnbreakableBlocks;
        }

        return false;
    }

    public static boolean enableSounds() {
        if(clothPresent()) {
            return ClothConfigManager.getConfig().enableSounds;
        }

        return true;
    }
}
