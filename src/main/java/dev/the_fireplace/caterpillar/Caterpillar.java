package dev.the_fireplace.caterpillar;

import dev.the_fireplace.caterpillar.config.CaterpillarConfig;
import dev.the_fireplace.caterpillar.registry.*;
import dev.the_fireplace.caterpillar.network.PacketHandler;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Caterpillar implements ModInitializer {

    public static final String MOD_ID = "simplycaterpillar";

    public static CaterpillarConfig config;

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        AutoConfig.register(CaterpillarConfig.class, Toml4jConfigSerializer::new);
        config = AutoConfig.getConfigHolder(CaterpillarConfig.class).getConfig();

        ItemGroupRegistry.registerCreativeModeTab();
        BlockRegistry.registerBlocks();
        BlockEntityRegistry.registerBlockEntities();
        ItemRegistry.registerItems();
        RecipeRegistry.registerRecipes();

        MenuRegistry.registerMenus();

        PacketHandler.registerC2SPackets();
    }
}
