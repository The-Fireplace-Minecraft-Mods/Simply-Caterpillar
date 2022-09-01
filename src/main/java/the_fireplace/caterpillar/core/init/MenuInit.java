package the_fireplace.caterpillar.core.init;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.common.menu.*;

public class MenuInit {

    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Caterpillar.MOD_ID);

    // Containers
    public static final RegistryObject<MenuType<CaterpillarMenu>> CATERPILLAR = MENU_TYPES.register("caterpillar", () -> new MenuType<>(CaterpillarMenu::new));
}
