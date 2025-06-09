package dev.the_fireplace.caterpillar.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.the_fireplace.caterpillar.Constants;
import dev.the_fireplace.caterpillar.inventory.IncineratorMenu;
import dev.the_fireplace.caterpillar.inventory.TransporterMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.MenuType;

import java.util.function.Supplier;

public class MenuTypesRegistry {
    private static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(Constants.MOD_ID, Registries.MENU);

    public static final RegistrySupplier<MenuType<IncineratorMenu>> INCINERATOR = registerMenuType("incinerator", () -> new MenuType<>(IncineratorMenu::new, FeatureFlagSet.of()));
    public static final RegistrySupplier<MenuType<TransporterMenu>> TRANSPORTER = registerMenuType("transporter", () -> new MenuType<>(TransporterMenu::new, FeatureFlagSet.of()));

    public static void init() {
        MENU_TYPES.register();
    }

    public static <T extends MenuType<?>> RegistrySupplier<T> registerMenuType(String name, Supplier<T> menuType){
        return MENU_TYPES.register(Constants.getId(name), menuType);
    }
}
