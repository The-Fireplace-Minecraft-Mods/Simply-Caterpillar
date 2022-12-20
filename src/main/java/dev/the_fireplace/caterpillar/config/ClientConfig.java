package dev.the_fireplace.caterpillar.config;

import net.minecraftforge.common.ForgeConfigSpec;

/**
 * For configuration settings that change the behaviour of code on the LOGICAL CLIENT.
 * This can be moved to an inner class of CaterpillarConfig, but is separate because of personal preference and to keep the code organised
 */
public class ClientConfig {

    ClientConfig(final ForgeConfigSpec.Builder builder) {
        builder.push("Tutorial");

        builder.pop();
    }
}
