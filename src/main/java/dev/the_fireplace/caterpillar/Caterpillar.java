package dev.the_fireplace.caterpillar;

import dev.the_fireplace.caterpillar.init.BlockEntityInit;
import dev.the_fireplace.caterpillar.init.BlockInit;
import dev.the_fireplace.caterpillar.init.MenuInit;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Caterpillar implements ModInitializer {

    public static final String MOD_ID = "simplycaterpillar";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.build(
            new Identifier(MOD_ID, "caterpillar"), () -> new ItemStack(BlockInit.DRILL_HEAD.asItem()));

    @Override
    public void onInitialize() {
        BlockInit.registerBlocks();
        BlockEntityInit.registerBlockEntities();
        MenuInit.registerMenus();
    }
}
