package dev.the_fireplace.caterpillar.init;

import dev.the_fireplace.caterpillar.Caterpillar;
import dev.the_fireplace.caterpillar.block.*;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

public class BlockInit {

    // Blocks
    public static final Block DRILL_BASE = registerBlock("drill_base",
            new DrillBaseBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5F, 6.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()), Caterpillar.ITEM_GROUP);

    public static final Block DRILL_HEAD = registerBlock("drill_head",
            new DrillHeadBlock(BlockBehaviour.Properties.of(Material.STONE).noOcclusion().strength(1.5F, 6.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()), Caterpillar.ITEM_GROUP);

    public static final Block COLLECTOR = registerBlock("collector",
            new CollectorBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5F, 6.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()), Caterpillar.ITEM_GROUP);

    public static final Block REINFORCEMENT = registerBlock("reinforcement",
            new ReinforcementBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5F, 6.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()), Caterpillar.ITEM_GROUP);

    public static final Block STORAGE = registerBlock("storage",
            new StorageBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5F, 6.0F).sound(SoundType.WOOD).requiresCorrectToolForDrops()), Caterpillar.ITEM_GROUP);

    public static final Block INCINERATOR = registerBlock("incinerator",
            new IncineratorBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5F, 6.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()), Caterpillar.ITEM_GROUP);

    public static final Block DECORATION = registerBlock("decoration",
            new DecorationBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5F, 6.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()), Caterpillar.ITEM_GROUP);

    private static Block registerBlockWithoutItem(String name, Block block) {
        return Registry.register(Registry.BLOCK, new ResourceLocation(Caterpillar.MOD_ID, name), block);
    }

    private static Block registerBlock(String name, Block block, CreativeModeTab tab) {
        registerBlockItem(name, block, tab);
        return Registry.register(Registry.BLOCK, new ResourceLocation(Caterpillar.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block, CreativeModeTab tab) {
        return Registry.register(Registry.ITEM, new ResourceLocation(Caterpillar.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings().group(tab)));
    }

    public static void registerBlocks() {
        Caterpillar.LOGGER.debug("Registering ModBlocks for " + Caterpillar.MOD_ID);
    }
}
