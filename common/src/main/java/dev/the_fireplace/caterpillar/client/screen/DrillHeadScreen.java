package dev.the_fireplace.caterpillar.client.screen;

import dev.architectury.networking.NetworkManager;
import dev.the_fireplace.caterpillar.block.entity.DrillHeadBlockEntity;
import dev.the_fireplace.caterpillar.client.screen.util.ScreenTabs;
import dev.the_fireplace.caterpillar.client.screen.widget.PowerButton;
import dev.the_fireplace.caterpillar.inventory.DrillHeadMenu;
import dev.the_fireplace.caterpillar.network.packet.TogglePowerPacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

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
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, DrillHeadBlockEntity.CONSUMPTION_TITLE, CONSUMPTION_LABEL_X, CONSUMPTION_LABEL_Y, 4210752, false);
        guiGraphics.drawString(this.font, DrillHeadBlockEntity.GATHERED_TITLE, GATHERED_LABEL_X, GATHERED_LABEL_Y, 4210752, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 4210752, false);
    }

    private void addPowerButton() {
        this.powerButton = new PowerButton(this.leftPos + (this.imageWidth - PowerButton.BUTTON_WIDTH) / 2, this.topPos + POWER_BUTTON_Y, this.menu.isPowered());
        this.addRenderableWidget(this.powerButton);
    }

    private void updatePowerButton() {
        if (this.powerButton != null) {
            this.powerButton.setPowered(this.menu.isPowered());
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);

        if (!this.minecraft.player.isSpectator()) {
            if (this.powerButton.mouseClicked(mouseX, mouseY, button)) {
                NetworkManager.sendToServer(new TogglePowerPacket(this.menu.blockEntity.getBlockPos()));


               return true;
            }

            return false;
        } else {
            return false;
        }
    }
}
