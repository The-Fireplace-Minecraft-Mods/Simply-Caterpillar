package dev.the_fireplace.caterpillar.registry;

import dev.the_fireplace.caterpillar.Caterpillar;
import dev.the_fireplace.caterpillar.item.WritablePatternBookItem;
import dev.the_fireplace.caterpillar.item.WrittenPatternBookItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

public class ItemRegistry {

    public static final Item WRITABLE_PATTERN_BOOK = registerItem("writable_pattern_book",
            new WritablePatternBookItem(new Item.Properties().stacksTo(1)));

    public static final Item WRITTEN_PATTERN_BOOK = registerItem("written_pattern_book",
            new WrittenPatternBookItem(new Item.Properties().stacksTo(16)));

    private static Item registerItem(String name, Item item) {
        return Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(Caterpillar.MOD_ID, name), item);
    }

    public static void registerItems() {
        Caterpillar.LOGGER.debug("Registering ModItems for " + Caterpillar.MOD_ID);
    }
}
