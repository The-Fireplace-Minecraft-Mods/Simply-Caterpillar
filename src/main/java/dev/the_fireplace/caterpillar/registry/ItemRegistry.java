package dev.the_fireplace.caterpillar.registry;

import dev.the_fireplace.caterpillar.Caterpillar;
import dev.the_fireplace.caterpillar.item.WritablePatternBookItem;
import dev.the_fireplace.caterpillar.item.WrittenPatternBookItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegistry {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Caterpillar.MOD_ID);
    public static final RegistryObject<Item> WRITABLE_PATTERN_BOOK = ITEMS.register("writable_pattern_book",
            () -> new WritablePatternBookItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> WRITTEN_PATTERN_BOOK = ITEMS.register("written_pattern_book",
            () -> new WrittenPatternBookItem(new Item.Properties().stacksTo(16)));
}
