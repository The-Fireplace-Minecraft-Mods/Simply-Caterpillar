package dev.the_fireplace.caterpillar;

import dev.the_fireplace.caterpillar.config.ConfigHolder;
import dev.the_fireplace.caterpillar.entity.SeatEntity;
import dev.the_fireplace.caterpillar.init.BlockEntityInit;
import dev.the_fireplace.caterpillar.init.BlockInit;
import dev.the_fireplace.caterpillar.init.EntityInit;
import dev.the_fireplace.caterpillar.init.MenuInit;
import dev.the_fireplace.caterpillar.network.PacketHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Caterpillar implements ModInitializer {

    public static final String MOD_ID = "simplycaterpillar";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final CreativeModeTab ITEM_GROUP = FabricItemGroupBuilder.build(
            new ResourceLocation(MOD_ID, "caterpillar"), () -> new ItemStack(BlockInit.DRILL_HEAD.asItem()));

    @Override
    public void onInitialize() {
        ConfigHolder.registerConfigs();

        BlockInit.registerBlocks();
        BlockEntityInit.registerBlockEntities();

        MenuInit.registerMenus();

        PacketHandler.registerC2SPackets();
    }
}
