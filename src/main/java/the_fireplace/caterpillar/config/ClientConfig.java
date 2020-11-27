package the_fireplace.caterpillar.config;

import net.minecraftforge.common.ForgeConfigSpec;
import the_fireplace.caterpillar.Caterpillar;

/**
 * For configuration settings that change the behaviour of code on the LOGICAL CLIENT.
 * This can be moved to an inner class of CaterpillarConfig, but is separate because of personal preference and to keep the code organised
 */
public class ClientConfig {

    final ForgeConfigSpec.BooleanValue drill_head;
    final ForgeConfigSpec.BooleanValue coal;
    final ForgeConfigSpec.BooleanValue power;
    final ForgeConfigSpec.BooleanValue storage;
    final ForgeConfigSpec.BooleanValue decoration_selection;
    final ForgeConfigSpec.BooleanValue decoration_selection_zero;
    final ForgeConfigSpec.BooleanValue decoration_pattern;
    final ForgeConfigSpec.BooleanValue reinforcement_1;
    final ForgeConfigSpec.BooleanValue reinforcement_2;

    ClientConfig(final ForgeConfigSpec.Builder builder) {
        builder.push("Tutorial");

        drill_head = builder
                .comment("Does this show the drill head tutorial ?")
                .translation(Caterpillar.MOD_ID + "config.drill_head")
                .define("drill_head", true);

        coal = builder
                .comment("Does this show the coal tutorial ?")
                .translation(Caterpillar.MOD_ID + "config.drill_head")
                .define("coal", true);

        power = builder
                .comment("Does this show the power tutorial ?")
                .translation(Caterpillar.MOD_ID + "config.power")
                .define("power", true);

        storage = builder
                .comment("Does this show the storage tutorial ?")
                .translation(Caterpillar.MOD_ID + "config.storage")
                .define("storage", true);

        decoration_selection = builder
                .comment("Does this show the selection tutorial for the decoration ?")
                .translation(Caterpillar.MOD_ID + "config.decoration_selection")
                .define("decoration_selection", true);

        decoration_selection_zero = builder
                .comment("Does this show the selection zero tutorial for the decoration ?")
                .translation(Caterpillar.MOD_ID + "config.decoration_selection_zero")
                .define("decoration_selection_zero", true);

        decoration_pattern = builder
                .comment("Does this show the patter tutorial for the decoration ?")
                .translation(Caterpillar.MOD_ID + "config.decoration_pattern")
                .define("decoration_pattern", true);

        reinforcement_1 = builder
                .comment("Does this show the reinforcement tutorial 1 ?")
                .translation(Caterpillar.MOD_ID + "config.reinforcement_1")
                .define("reinforcement_1", true);

        reinforcement_2 = builder
                .comment("Does this show the reinforcement tutorial 2 ?")
                .translation(Caterpillar.MOD_ID + "config.reinforcement_2")
                .define("reinforcement_2", true);

        builder.pop();
    }
}
