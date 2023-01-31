package dev.the_fireplace.caterpillar.config;

import com.mojang.datafixers.util.Pair;
import dev.the_fireplace.caterpillar.Caterpillar;

public class ConfigHolder {

    public static SimpleConfig CONFIG;
    public static boolean firstUse;
    public static boolean useParticles;
    public static boolean breakUnbreakableBlocks;
    public static boolean enableSounds;
    private static ConfigProvider configs;

    public static void registerConfigs() {
        configs = new ConfigProvider();
        createConfigs();

        CONFIG = SimpleConfig.of(Caterpillar.MOD_ID + "config").provider(configs).request();

        assignConfigs();
    }

    private static void createConfigs() {
        configs.addKeyValuePair(new Pair<>(Caterpillar.MOD_ID + ".config.first_use", true), "Show the tutorial if first time use ?");
        configs.addKeyValuePair(new Pair<>(Caterpillar.MOD_ID + ".config.use_particles", true), "Use particles for effect ?");
        configs.addKeyValuePair(new Pair<>(Caterpillar.MOD_ID + ".config.break_unbreakable_blocks", false), "Can the drill head break unbreakable blocks, like bedrock, end portal frame, etc ?");
        configs.addKeyValuePair(new Pair<>(Caterpillar.MOD_ID + ".config.enable_sounds", true), "Does the drill make noise when it moves ?");
    }

    private static void assignConfigs() {
        firstUse = CONFIG.getOrDefault(Caterpillar.MOD_ID + ".config.first_use", true);
        useParticles = CONFIG.getOrDefault(Caterpillar.MOD_ID + ".config.use_particles", true);
        breakUnbreakableBlocks = CONFIG.getOrDefault(Caterpillar.MOD_ID + ".config.break_unbreakable_blocks", false);
        enableSounds = CONFIG.getOrDefault(Caterpillar.MOD_ID + ".config.enable_sounds", true);
    }
}
