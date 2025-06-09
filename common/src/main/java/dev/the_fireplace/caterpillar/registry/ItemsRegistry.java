package dev.the_fireplace.caterpillar.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.the_fireplace.caterpillar.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.Function;
import java.util.function.Supplier;

public class ItemsRegistry {
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Constants.MOD_ID, Registries.ITEM);

    public static final RegistrySupplier<Item> DRILL_BASE = registerBlockItem("drill_base", BlocksRegistry.DRILL_BASE);
    public static final RegistrySupplier<Item> DRILL_HEAD = registerBlockItem("drill_head", BlocksRegistry.DRILL_HEAD);
    public static final RegistrySupplier<Item> DRILL_SEAT = registerBlockItem("drill_seat", BlocksRegistry.DRILL_SEAT);
    public static final RegistrySupplier<Item> COLLECTOR = registerBlockItem("collector", BlocksRegistry.COLLECTOR);
    public static final RegistrySupplier<Item> REINFORCEMENT = registerBlockItem("reinforcement", BlocksRegistry.REINFORCEMENT);
    public static final RegistrySupplier<Item> STORAGE = registerBlockItem("storage", BlocksRegistry.STORAGE);
    public static final RegistrySupplier<Item> INCINERATOR = registerBlockItem("incinerator", BlocksRegistry.INCINERATOR);
    public static final RegistrySupplier<Item> DECORATION = registerBlockItem("decoration", BlocksRegistry.DECORATION);
    public static final RegistrySupplier<Item> TRANSPORTER = registerBlockItem("transporter", BlocksRegistry.TRANSPORTER);

    public static void init() {
        ITEMS.register();
    }

    public static RegistrySupplier<Item> registerItem(String name, Supplier<Item> item) {
        return ITEMS.register(Constants.getId(name), item);
    }

    public static RegistrySupplier<Item> registerBlockItem(String name, RegistrySupplier<? extends Block> block) {
        return registerItem(name, () -> new BlockItem(block.get(), baseProperties(name)));
    }

    public RegistrySupplier<Item> registerItem(String name, Item.Properties properties) {
        return registerItem(createItemId(name), Item::new, properties);
    }

    public RegistrySupplier<Item> registerItem(String name, Function<Item.Properties, Item> factory, Item.Properties properties) {
        return registerItem(createItemId(name), factory, properties);
    }

    public RegistrySupplier<Item> registerItem(ResourceKey<Item> key, Function<Item.Properties, Item> factory, Item.Properties properties) {
        Item item = factory.apply(properties.setId(key));

        return ITEMS.register(key.location().getPath(), () -> item);
    }

    public static ResourceKey<Item> createItemId(String name) {
        return ResourceKey.create(Registries.ITEM, Constants.getId(name));
    }

    public static Item.Properties baseProperties(String name) {
        return new Item.Properties().setId(ResourceKey.create(Registries.ITEM, Constants.getId(name))).arch$tab(CreativeModeTabsRegistry.CATERPILLAR_TAB);
    }
}
