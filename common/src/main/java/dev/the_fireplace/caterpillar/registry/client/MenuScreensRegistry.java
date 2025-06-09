package dev.the_fireplace.caterpillar.registry.client;

import dev.architectury.registry.menu.MenuRegistry;
import dev.the_fireplace.caterpillar.client.screen.IncineratorScreen;
import dev.the_fireplace.caterpillar.client.screen.TransporterScreen;
import dev.the_fireplace.caterpillar.registry.MenuTypesRegistry;

public class MenuScreensRegistry {
    public static void register() {
        MenuRegistry.registerScreenFactory(MenuTypesRegistry.INCINERATOR.get(), IncineratorScreen::new);
        MenuRegistry.registerScreenFactory(MenuTypesRegistry.TRANSPORTER.get(), TransporterScreen::new);
    }
}
