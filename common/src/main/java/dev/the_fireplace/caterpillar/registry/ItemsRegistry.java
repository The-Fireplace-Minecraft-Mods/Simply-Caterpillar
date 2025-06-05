package dev.the_fireplace.caterpillar.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.the_fireplace.caterpillar.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

import java.util.function.Function;
import java.util.function.Supplier;

public class ItemsRegistry {
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Constants.MOD_ID, Registries.ITEM);

    public static final RegistrySupplier<Item> DRILL_BASE = registerItem("drill_base", () -> new BlockItem(BlocksRegistry.DRILL_BASE.get(), baseProperties("drill_base").arch$tab(CreativeModeTabsRegistry.CATERPILLAR_TAB)));

    public static void init() {
        ITEMS.register();
    }

    public static RegistrySupplier<Item> registerItem(String name, Supplier<Item> item) {
        return ITEMS.register(Constants.getId(name), item);
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
        return new Item.Properties().setId(ResourceKey.create(Registries.ITEM, Constants.getId(name)));
    }
}
