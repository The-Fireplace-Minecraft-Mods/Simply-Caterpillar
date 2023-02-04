package dev.the_fireplace.caterpillar.config;

import dev.the_fireplace.caterpillar.Caterpillar;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = Caterpillar.MOD_ID)
public class CaterpillarConfig implements ConfigData {

    public static boolean firstUse = true;

    public static boolean useParticles = true;

    @ConfigEntry.Gui.Tooltip
    public static boolean breakUnbreakableBlocks = false;

    public static boolean enableSounds = true;
}
