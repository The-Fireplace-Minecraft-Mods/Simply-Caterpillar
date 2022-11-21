package the_fireplace.caterpillar.config;

import net.minecraftforge.common.ForgeConfigSpec;
import the_fireplace.caterpillar.Caterpillar;

/**
 * For configuration settings that change the behaviour of code on the LOGICAL SERVER.
 *
 * This can be moved to an inner class of CaterpillarConfig, but is separate because of personal preference and to keep the code organised
 */
public class ServerConfig {

    final ForgeConfigSpec.BooleanValue firstUse;
    final ForgeConfigSpec.BooleanValue useParticles;
    final ForgeConfigSpec.BooleanValue breakBedrock;
    final ForgeConfigSpec.BooleanValue enableSounds;

    ServerConfig(final ForgeConfigSpec.Builder builder) {
        builder.push("Caterpillar");

        firstUse = builder
                .comment("Show the tutorial if first time use ?")
                .translation(Caterpillar.MOD_ID + ".config.first_use")
                .define("firstUse", true);

        useParticles = builder
                .comment("Use particles for effect ?")
                .translation(Caterpillar.MOD_ID + ".config.use_particles")
                .define("useParticles", true);

        breakBedrock = builder
                .comment("Can the drill break bedrock ?")
                .translation(Caterpillar.MOD_ID + ".config.break_bedrock")
                .define("breakBedrock", false);

        enableSounds = builder
                .comment("Does the drill make noise when it moves ?")
                .translation(Caterpillar.MOD_ID + ".config.enable_sounds")
                .define("enableSounds", true);

        builder.pop();
    }
}
