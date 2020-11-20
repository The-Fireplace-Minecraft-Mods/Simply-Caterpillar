package the_fireplace.caterpillar.inits;

import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.blocks.BlockItemBase;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Caterpillar.MOD_ID);

    // Block Items
    public static final RegistryObject<Item> DRILL_BASE_ITEM = ITEMS.register("drill_base",
            () -> new BlockItemBase(ModBlocks.DRILL_BASE.get()));

    public static final RegistryObject<Item> REINFORCEMENTS_ITEM = ITEMS.register("reinforcements",
            () -> new BlockItemBase(ModBlocks.REINFORCEMENTS.get()));

    public static final RegistryObject<Item> DECORATION_ITEM = ITEMS.register("decoration",
            () -> new BlockItemBase(ModBlocks.DECORATION.get()));

    public static final RegistryObject<Item> COLLECTOR_ITEM = ITEMS.register("collector",
            () -> new BlockItemBase(ModBlocks.COLLECTOR.get()));

    public static final RegistryObject<Item> STORAGE_ITEM = ITEMS.register("storage",
            () -> new BlockItemBase(ModBlocks.STORAGE.get()));

    public static final RegistryObject<Item> INCINERATOR_ITEM = ITEMS.register("incinerator",
            () -> new BlockItemBase(ModBlocks.INCINERATOR.get()));

    //public static final RegistryObject<Item> DRILL_BLADES_ITEM = ITEMS.register("drill_blades",
         // () -> new BlockItemBase(ModBlocks.DRILL_BLADES.get()));
}
