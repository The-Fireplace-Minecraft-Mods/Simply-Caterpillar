package dev.the_fireplace.caterpillar.client.screen.widget;

import dev.the_fireplace.caterpillar.Constants;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.WidgetSprites;

public class TabButton extends ImageButton {
    private static final WidgetSprites SPRITES = new WidgetSprites(Constants.getId("widget/tab_selected"), Constants.getId("widget/tab"), Constants.getId("widget/tab_selected_highlighted"), Constants.getId("widget/tab_highlighted"));

    public static final int TAB_WIDTH = 31;
    public static final int TAB_HEIGHT = 20;

    public TabButton(int x, int y, boolean active, OnPress onPress) {
        super(x, y, TAB_WIDTH, TAB_HEIGHT, SPRITES, onPress);
        super.active = active;
    }
}
