package the_fireplace.caterpillar.config;

import net.minecraftforge.fml.config.ModConfig;

/**
 * This bakes the config values to normal fields
 *
 * It can be merged into the main CaterpillarConfig class, but is separate because of personal preference and to keep the code organised
 */
public class ConfigHelper {

    public static void bakeClient(final ModConfig config) {
        CaterpillarConfig.drill_head = ConfigHolder.CLIENT.drill_head.get();
        CaterpillarConfig.coal = ConfigHolder.CLIENT.coal.get();
        CaterpillarConfig.power = ConfigHolder.CLIENT.power.get();
        CaterpillarConfig.storage = ConfigHolder.CLIENT.storage.get();
        CaterpillarConfig.decoration_selection = ConfigHolder.CLIENT.decoration_selection.get();
        CaterpillarConfig.decoration_selection_zero = ConfigHolder.CLIENT.decoration_selection_zero.get();
        CaterpillarConfig.decoration_pattern = ConfigHolder.CLIENT.decoration_pattern.get();
        CaterpillarConfig.reinforcement_1 = ConfigHolder.CLIENT.reinforcement_1.get();
        CaterpillarConfig.reinforcement_2 = ConfigHolder.CLIENT.reinforcement_2.get();
    }

    public static void bakeServer(final ModConfig config) {
        CaterpillarConfig.firstUse = ConfigHolder.SERVER.firstUse.get();
        CaterpillarConfig.useParticles = ConfigHolder.SERVER.useParticles.get();
        CaterpillarConfig.breakBedrock = ConfigHolder.SERVER.breakBedrock.get();
        CaterpillarConfig.enableSounds = ConfigHolder.SERVER.enableSounds.get();
    }
}
