package the_fireplace.caterpillar.client.screen;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import the_fireplace.caterpillar.client.screen.util.ScreenTabs;
import the_fireplace.caterpillar.common.menu.IncineratorMenu;

public class IncineratorScreen extends AbstractCaterpillarScreen<IncineratorMenu> {
    public IncineratorScreen(IncineratorMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, ScreenTabs.INCINERATOR);
    }
}
