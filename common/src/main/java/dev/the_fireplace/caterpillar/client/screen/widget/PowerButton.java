package dev.the_fireplace.caterpillar.client.screen.widget;

import dev.the_fireplace.caterpillar.Constants;
import net.minecraft.client.gui.components.StateSwitchingButton;
import net.minecraft.client.gui.components.WidgetSprites;

public class PowerButton extends StateSwitchingButton {
    private static final WidgetSprites SPRITES = new WidgetSprites(Constants.getId("widget/drill_head/power_on"), Constants.getId("widget/drill_head/power_off"), Constants.getId("widget/drill_head/power_on_highlighted"), Constants.getId("widget/drill_head/power_off_highlighted"));

    public static final int BUTTON_WIDTH = 21;
    public static final int BUTTON_HEIGHT = 18;

    public PowerButton(int x, int y, boolean powered) {
        super(x, y, BUTTON_WIDTH, BUTTON_HEIGHT, powered);
        this.initTextureValues(SPRITES);
    }

    public void setPowered(boolean powered) {
        this.setStateTriggered(powered);
    }
}
