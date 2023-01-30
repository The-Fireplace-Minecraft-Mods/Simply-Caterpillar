package dev.the_fireplace.caterpillar.init;

import dev.the_fireplace.caterpillar.Caterpillar;
import dev.the_fireplace.caterpillar.block.*;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class BlockInit {

    // Blocks
    public static final Block DRILL_BASE = registerBlock("drill_base",
            new DrillBaseBlock(AbstractBlock.Settings.of(Material.STONE).strength(1.5F, 6.0F).sounds(BlockSoundGroup.STONE).requiresTool()), Caterpillar.ITEM_GROUP);

    public static final Block DRILL_HEAD = registerBlock("drill_head",
            new DrillHeadBlock(AbstractBlock.Settings.of(Material.STONE).nonOpaque().strength(1.5F, 6.0F).sounds(BlockSoundGroup.STONE).requiresTool()), Caterpillar.ITEM_GROUP);

    public static final Block COLLECTOR = registerBlock("collector",
            new CollectorBlock(AbstractBlock.Settings.of(Material.STONE).strength(1.5F, 6.0F).sounds(BlockSoundGroup.STONE).requiresTool()), Caterpillar.ITEM_GROUP);

    public static final Block REINFORCEMENT = registerBlock("reinforcement",
            new ReinforcementBlock(AbstractBlock.Settings.of(Material.STONE).strength(1.5F, 6.0F).sounds(BlockSoundGroup.STONE).requiresTool()), Caterpillar.ITEM_GROUP);

    public static final Block STORAGE = registerBlock("storage",
            new StorageBlock(AbstractBlock.Settings.of(Material.STONE).strength(1.5F, 6.0F).sounds(BlockSoundGroup.WOOD).requiresTool()), Caterpillar.ITEM_GROUP);

    public static final Block INCINERATOR = registerBlock("incinerator",
            new IncineratorBlock(AbstractBlock.Settings.of(Material.STONE).strength(1.5F, 6.0F).sounds(BlockSoundGroup.STONE).requiresTool()), Caterpillar.ITEM_GROUP);

    public static final Block DECORATION = registerBlock("decoration",
            new DecorationBlock(AbstractBlock.Settings.of(Material.STONE).strength(1.5F, 6.0F).sounds(BlockSoundGroup.STONE).requiresTool()), Caterpillar.ITEM_GROUP);

    private static Block registerBlockWithoutItem(String name, Block block) {
        return Registry.register(Registry.BLOCK, new Identifier(Caterpillar.MOD_ID, name), block);
    }

    private static Block registerBlock(String name, Block block, ItemGroup tab) {
        registerBlockItem(name, block, tab);
        return Registry.register(Registry.BLOCK, new Identifier(Caterpillar.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block, ItemGroup tab) {
        return Registry.register(Registry.ITEM, new Identifier(Caterpillar.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings().group(tab)));
    }

    public static void registerBlocks() {
        Caterpillar.LOGGER.debug("Registering ModBlocks for " + Caterpillar.MOD_ID);
    }
}
