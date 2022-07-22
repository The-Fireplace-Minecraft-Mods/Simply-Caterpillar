package the_fireplace.caterpillar.core.init;

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
import the_fireplace.caterpillar.common.block.DrillBaseBlock;
import the_fireplace.caterpillar.common.block.IncineratorBlock;

import java.util.function.Supplier;

public class BlockInit {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Caterpillar.MOD_ID);

    // Blocks
    public static final RegistryObject<DrillBaseBlock> DRILL_BASE = register(
            "drill_base",
            () -> new DrillBaseBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5F, 6.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()),
            new Item.Properties().tab(Caterpillar.CATERPILLAR_CREATIVE_MODE_TAB)
    );

    public static final RegistryObject<Block> DRILL_BLADES = register(
            "drill_blades",
            () -> new Block(BlockBehaviour.Properties.of(Material.BARRIER).strength(1.5F, 6.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()),
            new Item.Properties().tab(Caterpillar.CATERPILLAR_CREATIVE_MODE_TAB)
    );

    public static final RegistryObject<Block> DRILL_HEADS = register(
            "drill_heads",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE).strength(1.5F, 6.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()),
            new Item.Properties().tab(Caterpillar.CATERPILLAR_CREATIVE_MODE_TAB)
    );
    public static final RegistryObject<Block> REINFORCEMENTS = register(
            "reinforcements",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE).strength(1.5F, 6.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()),
            new Item.Properties().tab(Caterpillar.CATERPILLAR_CREATIVE_MODE_TAB)
    );
    public static final RegistryObject<Block> DECORATION = register(
            "decoration",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE).strength(1.5F, 6.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()),
            new Item.Properties().tab(Caterpillar.CATERPILLAR_CREATIVE_MODE_TAB)
    );
    public static final RegistryObject<Block> COLLECTOR = register(
            "collector",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE).strength(1.5F, 6.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()),
            new Item.Properties().tab(Caterpillar.CATERPILLAR_CREATIVE_MODE_TAB)
    );
    public static final RegistryObject<Block> STORAGE = register(
            "storage",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE).strength(1.5F, 6.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()),
            new Item.Properties().tab(Caterpillar.CATERPILLAR_CREATIVE_MODE_TAB)
    );
    public static final RegistryObject<IncineratorBlock> INCINERATOR = register(
            "incinerator",
            () -> new IncineratorBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5F, 6.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()),
            new Item.Properties().tab(Caterpillar.CATERPILLAR_CREATIVE_MODE_TAB)
    );

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> supplier, Item.Properties properties) {
        RegistryObject<T> block = BLOCKS.register(name, supplier);
        ItemInit.ITEMS.register(name, () -> new BlockItem(block.get(), properties));
        return block;
    }
}