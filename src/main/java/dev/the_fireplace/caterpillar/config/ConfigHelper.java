package dev.the_fireplace.caterpillar.config;

import net.minecraftforge.fml.config.ModConfig;

/**
 * This bakes the config values to normal fields
 *
 * It can be merged into the main CaterpillarConfig class, but is separate because of personal preference and to keep the code organised
 */
public class ConfigHelper {

    public static void bakeClient(final ModConfig config) {

    }

    public static void bakeServer(final ModConfig config) {
        CaterpillarConfig.firstUse = ConfigHolder.SERVER.firstUse.get();
        CaterpillarConfig.useParticles = ConfigHolder.SERVER.useParticles.get();
        CaterpillarConfig.breakBedrock = ConfigHolder.SERVER.breakBedrock.get();
        CaterpillarConfig.enableSounds = ConfigHolder.SERVER.enableSounds.get();
    }
}
