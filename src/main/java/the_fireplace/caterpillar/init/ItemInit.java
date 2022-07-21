package the_fireplace.caterpillar.init;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import the_fireplace.caterpillar.Caterpillar;

public class ItemInit {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Caterpillar.MOD_ID);

    public static final RegistryObject<Item> DRILL_BASE_ITEM = ITEMS.register("drill_base", () -> new Item(new Item.Properties().tab(CreativeModeTabsInit.CREATIVE_MODE_TAB)));
}
