package dev.the_fireplace.caterpillar;

import dev.the_fireplace.caterpillar.compat.cloth.ClothConfigManager;
import dev.the_fireplace.caterpillar.registry.*;
import dev.the_fireplace.caterpillar.network.PacketHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Caterpillar implements ModInitializer {

    public static final String MOD_ID = "simplycaterpillar";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final List<String> MODS_LOADED = new ArrayList<>();

    @Override
    public void onInitialize() {
        if (FabricLoader.getInstance().isModLoaded("cloth-config")) {
            ClothConfigManager.registerAutoConfig();
        }

        ItemGroupRegistry.registerCreativeModeTab();
        BlockRegistry.registerBlocks();
        BlockEntityRegistry.registerBlockEntities();
        /**
         * TODO: Re-enable when fixed
         *
         * ItemRegistry.registerItems();
         */
        RecipeRegistry.registerRecipes();

        MenuRegistry.registerMenus();

        PacketHandler.registerC2SPackets();
    }
}
