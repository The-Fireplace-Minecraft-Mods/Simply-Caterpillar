package dev.the_fireplace.caterpillar.client.screen.widget;

import dev.the_fireplace.caterpillar.Constants;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class PowerButton extends ImageButton {
    private static final WidgetSprites SPRITES = new WidgetSprites(Constants.getId("widget/drill_head/power_on"), Constants.getId("widget/drill_head/power_off"), Constants.getId("widget/drill_head/power_on_highlighted"), Constants.getId("widget/drill_head/power_off_highlighted"));

    public static final MutableComponent POWER_TOOLTIP = Component.translatable("container.simplycaterpillar.drill_head.power");
    public static final MutableComponent POWER_ON_TOOLTIP = Component.translatable("container.simplycaterpillar.drill_head.power.on").withStyle(ChatFormatting.GREEN);
    public static final MutableComponent POWER_OFF_TOOLTIP = Component.translatable("container.simplycaterpillar.drill_head.power.off").withStyle(ChatFormatting.RED);

    public static final int BUTTON_WIDTH = 21;
    public static final int BUTTON_HEIGHT = 18;

    public boolean powered;

    public PowerButton(boolean powered, int x, int y, OnPress onPress) {
        super(x, y, BUTTON_WIDTH, BUTTON_HEIGHT, SPRITES, onPress);

        this.powered = powered;
    }

    @Override
    public boolean isActive() {
        return super.visible && this.powered;
    }

    public void setPowered(boolean powered) {
        this.powered = powered;
    }

    public void renderTooltip(GuiGraphics graphics, Font font, int leftPos, int topPos) {
        graphics.renderTooltip(font, POWER_TOOLTIP.append(this.powered ? POWER_ON_TOOLTIP : POWER_OFF_TOOLTIP), leftPos, topPos);
    }
}
