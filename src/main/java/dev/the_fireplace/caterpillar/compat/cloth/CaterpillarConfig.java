package dev.the_fireplace.caterpillar.compat.cloth;

import dev.the_fireplace.caterpillar.Caterpillar;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = Caterpillar.MOD_ID)
public class CaterpillarConfig implements ConfigData {

    public boolean firstUse = true;

    public boolean useParticles = true;

    @ConfigEntry.Gui.Tooltip
    public boolean breakUnbreakableBlocks = false;

    public boolean enableSounds = true;
}
