package dev.the_fireplace.caterpillar.client.screen;

import dev.the_fireplace.caterpillar.client.screen.util.ScreenTabs;
import dev.the_fireplace.caterpillar.inventory.TransporterMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class TransporterScreen extends AbstractCaterpillarScreen<TransporterMenu> {
    public TransporterScreen(TransporterMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, ScreenTabs.TRANSPORTER);
    }
}
