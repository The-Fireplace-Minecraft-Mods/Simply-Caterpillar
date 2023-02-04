package dev.the_fireplace.caterpillar;

import dev.the_fireplace.caterpillar.config.CaterpillarConfig;
import dev.the_fireplace.caterpillar.init.BlockEntityInit;
import dev.the_fireplace.caterpillar.init.BlockInit;
import dev.the_fireplace.caterpillar.init.ItemGroupInit;
import dev.the_fireplace.caterpillar.init.MenuInit;
import dev.the_fireplace.caterpillar.network.PacketHandler;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Caterpillar implements ModInitializer {

    public static final String MOD_ID = "simplycaterpillar";

    public static CaterpillarConfig config;

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final CreativeModeTab ITEM_GROUP = FabricItemGroupBuilder.build(
            new ResourceLocation(MOD_ID, "caterpillar"), () -> new ItemStack(BlockInit.DRILL_HEAD.asItem()));

    @Override
    public void onInitialize() {
        AutoConfig.register(CaterpillarConfig.class, Toml4jConfigSerializer::new);
        config = AutoConfig.getConfigHolder(CaterpillarConfig.class).getConfig();

        ItemGroupInit.registerCreativeModeTab();
        BlockInit.registerBlocks();
        BlockEntityInit.registerBlockEntities();

        MenuInit.registerMenus();

        PacketHandler.registerC2SPackets();
    }
}
