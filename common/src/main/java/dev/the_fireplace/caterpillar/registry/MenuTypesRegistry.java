package dev.the_fireplace.caterpillar.registry;

import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.the_fireplace.caterpillar.Constants;
import dev.the_fireplace.caterpillar.inventory.DrillHeadMenu;
import dev.the_fireplace.caterpillar.inventory.IncineratorMenu;
import dev.the_fireplace.caterpillar.inventory.TransporterMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;

import java.util.function.Supplier;

public class MenuTypesRegistry {
    private static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(Constants.MOD_ID, Registries.MENU);

    public static final RegistrySupplier<MenuType<DrillHeadMenu>> DRILL_HEAD = registerMenuType("drill_head", () -> MenuRegistry.ofExtended(DrillHeadMenu::new));
    public static final RegistrySupplier<MenuType<IncineratorMenu>> INCINERATOR = registerMenuType("incinerator", () -> MenuRegistry.ofExtended(IncineratorMenu::new));
    public static final RegistrySupplier<MenuType<TransporterMenu>> TRANSPORTER = registerMenuType("transporter", () -> MenuRegistry.ofExtended(TransporterMenu::new));

    public static void init() {
        MENU_TYPES.register();
    }

    public static <T extends MenuType<?>> RegistrySupplier<T> registerMenuType(String name, Supplier<T> menuType){
        return MENU_TYPES.register(Constants.getId(name), menuType);
    }
}
