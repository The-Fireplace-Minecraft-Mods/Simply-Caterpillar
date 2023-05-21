package dev.the_fireplace.caterpillar.init;

import dev.the_fireplace.caterpillar.Caterpillar;
import dev.the_fireplace.caterpillar.block.*;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class BlockInit {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Caterpillar.MOD_ID);

    // Blocks
    public static final RegistryObject<DrillBaseBlock> DRILL_BASE = register(
            "drill_base",
            () -> new DrillBaseBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5F, 6.0F).sound(SoundType.STONE).requiresCorrectToolForDrops())
    );

    public static final RegistryObject<DrillHeadBlock> DRILL_HEAD = register(
            "drill_head",
            () -> new DrillHeadBlock(BlockBehaviour.Properties.of(Material.STONE).noOcclusion().strength(1.5F, 6.0F).sound(SoundType.STONE).requiresCorrectToolForDrops())
    );

    public static final RegistryObject<DrillSeatBlock> DRILL_SEAT = register(
            "drill_seat",
            () -> new DrillSeatBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5F, 6.0F).sound(SoundType.STONE).requiresCorrectToolForDrops())
    );

    public static final RegistryObject<CollectorBlock> COLLECTOR = register(
            "collector",
            () -> new CollectorBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5F, 6.0F).sound(SoundType.STONE).requiresCorrectToolForDrops())
    );

    public static final RegistryObject<ReinforcementBlock> REINFORCEMENT = register(
            "reinforcement",
            () -> new ReinforcementBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5F, 6.0F).sound(SoundType.STONE).requiresCorrectToolForDrops())
    );

    public static final RegistryObject<StorageBlock> STORAGE = register(
            "storage",
            () -> new StorageBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5F, 6.0F).sound(SoundType.STONE).requiresCorrectToolForDrops())
    );

    public static final RegistryObject<IncineratorBlock> INCINERATOR = register(
            "incinerator",
            () -> new IncineratorBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5F, 6.0F).sound(SoundType.STONE).lightLevel((blockState) -> 15).requiresCorrectToolForDrops())
    );

    public static final RegistryObject<DecorationBlock> DECORATION = register(
            "decoration",
            () -> new DecorationBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5F, 6.0F).sound(SoundType.STONE).requiresCorrectToolForDrops())
    );

    public static final RegistryObject<TransporterBlock> TRANSPORTER = register(
            "transporter",
            () -> new TransporterBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5F, 6.0F).sound(SoundType.STONE).requiresCorrectToolForDrops())
    );

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> supplier) {
        RegistryObject<T> block = BLOCKS.register(name, supplier);
        ItemInit.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
        return block;
    }
}