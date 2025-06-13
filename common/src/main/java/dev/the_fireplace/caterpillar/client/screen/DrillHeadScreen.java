package dev.the_fireplace.caterpillar.client.screen;

import dev.the_fireplace.caterpillar.block.entity.DrillHeadBlockEntity;
import dev.the_fireplace.caterpillar.client.screen.util.ScreenTabs;
import dev.the_fireplace.caterpillar.client.screen.widget.PowerButton;
import dev.the_fireplace.caterpillar.inventory.DrillHeadMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;
import java.util.List;

import static dev.the_fireplace.caterpillar.client.screen.widget.PowerButton.POWER_OFF_TOOLTIP;

public class DrillHeadScreen extends AbstractCaterpillarScreen<DrillHeadMenu> {
    private static final int CONSUMPTION_LABEL_X = 8;
    private static final int CONSUMPTION_LABEL_Y = 6;
    private static final int GATHERED_LABEL_X = 106;
    private static final int GATHERED_LABEL_Y = 6;
    private static final int POWER_BUTTON_Y = 16;

    private PowerButton powerButton;

    public DrillHeadScreen(DrillHeadMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, ScreenTabs.DRILL_HEAD);
    }

    @Override
    protected void init() {
        super.init();

        this.addPowerButton();
    }

    @Override
    protected void containerTick() {
        super.containerTick();

        this.updatePowerButton();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        this.renderTooltipPowerButton(guiGraphics);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, DrillHeadBlockEntity.CONSUMPTION_TITLE, CONSUMPTION_LABEL_X, CONSUMPTION_LABEL_Y, 4210752, false);
        guiGraphics.drawString(this.font, DrillHeadBlockEntity.GATHERED_TITLE, GATHERED_LABEL_X, GATHERED_LABEL_Y, 4210752, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 4210752, false);
    }

    private void renderTooltipPowerButton(GuiGraphics guiGraphics) {
        if (this.powerButton.isHoveredOrFocused()) {
            int tooltipPadding = 24;

            MutableComponent component = PowerButton.POWER_TOOLTIP.append(this.powerButton.powered ? PowerButton.POWER_ON_TOOLTIP : PowerButton.POWER_OFF_TOOLTIP);
            guiGraphics.renderTooltip(this.font, component, this.leftPos + ((this.imageWidth - this.font.width(PowerButton.POWER_TOOLTIP.getVisualOrderText()) - tooltipPadding) / 2), this.topPos - 1);
        }
    }

    private void addPowerButton() {
        this.powerButton = new PowerButton(this.menu.isPowered(), this.leftPos + (this.imageWidth - PowerButton.BUTTON_WIDTH) / 2, this.topPos + POWER_BUTTON_Y, button -> this.menu.togglePower());
        this.addRenderableWidget(this.powerButton);
    }

    private void updatePowerButton() {
        if (this.powerButton != null) {
            this.powerButton.setPowered(this.menu.isPowered());
        }
    }
}
