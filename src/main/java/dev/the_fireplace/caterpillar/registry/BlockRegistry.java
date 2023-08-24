package dev.the_fireplace.caterpillar.registry;

import dev.the_fireplace.caterpillar.Caterpillar;
import dev.the_fireplace.caterpillar.block.*;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class BlockRegistry {

    // Blocks
    public static final Block DRILL_BASE = registerBlock("drill_base",
            new DrillBaseBlock(BlockBehaviour.Properties.copy(Blocks.STONE).strength(1.5F, 6.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));

    public static final Block DRILL_HEAD = registerBlock("drill_head",
            new DrillHeadBlock(BlockBehaviour.Properties.copy(Blocks.STONE).noOcclusion().strength(1.5F, 6.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));

    public static final Block DRILL_SEAT = registerBlock("drill_seat",
            new DrillSeatBlock(BlockBehaviour.Properties.copy(Blocks.STONE).strength(1.5F, 6.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));

    public static final Block COLLECTOR = registerBlock("collector",
            new CollectorBlock(BlockBehaviour.Properties.copy(Blocks.STONE).strength(1.5F, 6.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));

    public static final Block REINFORCEMENT = registerBlock("reinforcement",
            new ReinforcementBlock(BlockBehaviour.Properties.copy(Blocks.STONE).strength(1.5F, 6.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));

    public static final Block STORAGE = registerBlock("storage",
            new StorageBlock(BlockBehaviour.Properties.copy(Blocks.STONE).strength(1.5F, 6.0F).sound(SoundType.WOOD).requiresCorrectToolForDrops()));

    public static final Block INCINERATOR = registerBlock("incinerator",
            new IncineratorBlock(BlockBehaviour.Properties.copy(Blocks.STONE).strength(1.5F, 6.0F).sound(SoundType.STONE).lightLevel((blockState) -> 15).requiresCorrectToolForDrops()));

    public static final Block DECORATION = registerBlock("decoration",
            new DecorationBlock(BlockBehaviour.Properties.copy(Blocks.STONE).strength(1.5F, 6.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));

    public static final Block TRANSPORTER = registerBlock("transporter",
            new TransporterBlock(BlockBehaviour.Properties.copy(Blocks.STONE).strength(1.5F, 6.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));


    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(Caterpillar.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block) {
        return Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(Caterpillar.MOD_ID, name), new BlockItem(block, new FabricItemSettings()));
    }

    public static void registerBlocks() {
        Caterpillar.LOGGER.debug("Registering ModBlocks for " + Caterpillar.MOD_ID);
    }
}
