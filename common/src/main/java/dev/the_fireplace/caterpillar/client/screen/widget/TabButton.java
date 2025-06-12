package dev.the_fireplace.caterpillar.client.screen.widget;

import dev.the_fireplace.caterpillar.Constants;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.world.item.ItemStack;

public class TabButton extends ImageButton {
    private static final WidgetSprites SPRITES = new WidgetSprites(Constants.getId("widget/tab_selected"), Constants.getId("widget/tab"), Constants.getId("widget/tab_selected_highlighted"), Constants.getId("widget/tab_highlighted"));

    public static final int TAB_WIDTH = 31;
    public static final int TAB_HEIGHT = 20;

    private final boolean currentTab;
    public final ItemStack tooltipStack;

    public TabButton(int x, int y, boolean currentTab, OnPress onPress, ItemStack tooltipStack) {
        super(x, y, TAB_WIDTH, TAB_HEIGHT, SPRITES, onPress);
        this.currentTab = currentTab;
        this.tooltipStack = tooltipStack;
    }

    @Override
    public boolean isActive() {
        return super.visible && this.currentTab;
    }

    public void renderTooltip(GuiGraphics graphics, Font font, int leftPos, int topPos) {
        graphics.renderTooltip(font, this.tooltipStack, leftPos, topPos);
    }
}
