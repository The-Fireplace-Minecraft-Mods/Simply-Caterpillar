package dev.the_fireplace.caterpillar.registry;

import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.the_fireplace.caterpillar.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class CreativeModeTabsRegistry {
    private static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Constants.MOD_ID, Registries.CREATIVE_MODE_TAB);

    public static final Component MAIN_TITLE = Component.translatable("itemGroup." + Constants.MOD_ID + ".main");
    public static final RegistrySupplier<CreativeModeTab> CATERPILLAR_TAB = TABS.register("caterpillar_tab", () -> CreativeTabRegistry.create(MAIN_TITLE, () -> new ItemStack(BlocksRegistry.DRILL_BASE.get())));

    public static void init() {
        TABS.register();
    }
}
