package dev.the_fireplace.caterpillar.client.screen;

import dev.the_fireplace.caterpillar.client.screen.util.ScreenTabs;
import dev.the_fireplace.caterpillar.inventory.IncineratorMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class IncineratorScreen extends AbstractCaterpillarScreen<IncineratorMenu> {
    public IncineratorScreen(IncineratorMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, ScreenTabs.INCINERATOR);
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }
}
