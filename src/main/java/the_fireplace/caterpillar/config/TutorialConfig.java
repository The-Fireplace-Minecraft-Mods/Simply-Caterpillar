package the_fireplace.caterpillar.config;

import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.ForgeConfigSpec;

public class TutorialConfig {

    public static ForgeConfigSpec.BooleanValue drill_head;
    public static ForgeConfigSpec.BooleanValue coal;
    public static ForgeConfigSpec.BooleanValue power;
    public static ForgeConfigSpec.BooleanValue storage;
    public static ForgeConfigSpec.BooleanValue decoration_selection;
    public static ForgeConfigSpec.BooleanValue decoration_selection_zero;
    public static ForgeConfigSpec.BooleanValue decoration_pattern;
    public static ForgeConfigSpec.BooleanValue reinforcement_1;
    public static ForgeConfigSpec.BooleanValue reinforcement_2;

    public static void init(ForgeConfigSpec.Builder server, ForgeConfigSpec.Builder client) {
        client.comment("Tutorial Config");

        drill_head = client
                .comment(I18n.format("TutorialConfig.drill_head"))
                .define("TutorialConfig.drill_head", true);

        coal = client
                .comment(I18n.format("TutorialConfig.coal"))
                .define("TutorialConfig.coal", true);

        power = client
                .comment(I18n.format("TutorialConfig.power"))
                .define("TutorialConfig.power", true);

        storage = client
                .comment(I18n.format("TutorialConfig.storage"))
                .define("TutorialConfig.storage", true);

        decoration_selection = client
                .comment(I18n.format("TutorialConfig.decoration_selection"))
                .define("TutorialConfig.decoration_selection", true);

        decoration_selection_zero = client
                .comment(I18n.format("TutorialConfig.decoration_selection_zero"))
                .define("TutorialConfig.decoration_selection_zero", true);

        decoration_pattern = client
                .comment(I18n.format("TutorialConfig.decoration_pattern"))
                .define("TutorialConfig.decoration_pattern", true);

        reinforcement_1 = client
                .comment(I18n.format("TutorialConfig.decoration_pattern"))
                .define("TutorialConfig.reinforcement_1", true);

        reinforcement_2 = client
                .comment(I18n.format("TutorialConfig.reinforcement2"))
                .define("TutorialConfig.reinforcement_2", true);
    }
}
