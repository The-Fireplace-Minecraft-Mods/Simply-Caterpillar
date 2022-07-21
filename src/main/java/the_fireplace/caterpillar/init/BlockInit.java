package the_fireplace.caterpillar.init;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import the_fireplace.caterpillar.Caterpillar;

import java.util.function.Supplier;

public class BlockInit {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Caterpillar.MOD_ID);

    // Blocks
    public static final RegistryObject<Block> DRILL_BLADES = register(
            "drill_blades",
            () -> new Block(BlockBehaviour.Properties.of(Material.BARRIER).strength(5.0f, 6.0f).sound(SoundType.STONE).requiresCorrectToolForDrops()),
            new Item.Properties().tab(CreativeModeTabsInit.CREATIVE_MODE_TAB)
    );
    public static final RegistryObject<Block> DRILL_BASE = register(
            "drill_base",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE).strength(5.0f, 6.0f).sound(SoundType.STONE).requiresCorrectToolForDrops()),
            new Item.Properties().tab(CreativeModeTabsInit.CREATIVE_MODE_TAB)
    );
    public static final RegistryObject<Block> DRILL_HEADS = register(
            "drill_heads",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE).strength(5.0f, 6.0f).sound(SoundType.STONE).requiresCorrectToolForDrops()),
            new Item.Properties().tab(CreativeModeTabsInit.CREATIVE_MODE_TAB)
    );
    public static final RegistryObject<Block> REINFORCEMENTS = register(
            "reinforcements",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE).strength(5.0f, 6.0f).sound(SoundType.STONE).requiresCorrectToolForDrops()),
            new Item.Properties().tab(CreativeModeTabsInit.CREATIVE_MODE_TAB)
    );
    public static final RegistryObject<Block> DECORATION = register(
            "decoration",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE).strength(5.0f, 6.0f).sound(SoundType.STONE).requiresCorrectToolForDrops()),
            new Item.Properties().tab(CreativeModeTabsInit.CREATIVE_MODE_TAB)
    );
    public static final RegistryObject<Block> COLLECTOR = register(
            "collector",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE).strength(5.0f, 6.0f).sound(SoundType.STONE).requiresCorrectToolForDrops()),
            new Item.Properties().tab(CreativeModeTabsInit.CREATIVE_MODE_TAB)
    );
    public static final RegistryObject<Block> STORAGE = register(
            "storage",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE).strength(5.0f, 6.0f).sound(SoundType.STONE).requiresCorrectToolForDrops()),
            new Item.Properties().tab(CreativeModeTabsInit.CREATIVE_MODE_TAB)
    );
    public static final RegistryObject<Block> INCINERATOR = register(
            "incinerator",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE).strength(5.0f, 6.0f).sound(SoundType.STONE).requiresCorrectToolForDrops()),
            new Item.Properties().tab(CreativeModeTabsInit.CREATIVE_MODE_TAB)
    );

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> supplier, Item.Properties properties) {
        RegistryObject<T> block = BLOCKS.register(name, supplier);
        ItemInit.ITEMS.register(name, () -> new BlockItem(block.get(), properties));
        return block;
    }
}