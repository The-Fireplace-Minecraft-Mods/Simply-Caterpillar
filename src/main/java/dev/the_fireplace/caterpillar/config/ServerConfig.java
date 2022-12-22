package dev.the_fireplace.caterpillar.config;

import net.minecraftforge.common.ForgeConfigSpec;
import dev.the_fireplace.caterpillar.Caterpillar;

public class ServerConfig {

    final ForgeConfigSpec.BooleanValue firstUse;
    final ForgeConfigSpec.BooleanValue useParticles;
    final ForgeConfigSpec.BooleanValue breakUnbreakableBlocks;
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

        breakUnbreakableBlocks = builder
                .comment("Can the drill head break unbreakable blocks, like bedrock, end portal frame, etc ?")
                .translation(Caterpillar.MOD_ID + ".config.break_unbreakable_blocks")
                .define("breakBedrock", false);

        enableSounds = builder
                .comment("Does the drill make noise when it moves ?")
                .translation(Caterpillar.MOD_ID + ".config.enable_sounds")
                .define("enableSounds", true);

        builder.pop();
    }
}
