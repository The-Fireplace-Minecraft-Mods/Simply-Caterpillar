package the_fireplace.caterpillar.config;

import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.ForgeConfigSpec;

public class CaterpillarConfig {

    public static ForgeConfigSpec.BooleanValue firstUse;
    public static ForgeConfigSpec.BooleanValue useParticles;
    public static ForgeConfigSpec.BooleanValue breakBedrock;
    public static ForgeConfigSpec.BooleanValue enableSounds;

    public static void init(ForgeConfigSpec.Builder server, ForgeConfigSpec.Builder client) {
        server.comment("Caterpillar Config");

        firstUse = server
                .comment(I18n.format("CaterpillarConfig.firstUse"))
                .define("CaterpillarConfig.firstUse", true);

        useParticles = server
                .comment(I18n.format("CaterpillarConfig.useParticles"))
                .define("CaterpillarConfig.useParticles", true);

        breakBedrock = server
                .comment(I18n.format("CaterpillarConfig.breakBedrock"))
                .define("CaterpillarConfig.breakBedrock", true);

        enableSounds = server
                .comment(I18n.format("CaterpillarConfig.enableSounds"))
                .define("CaterpillarConfig.enableSounds", true);
    }
}
