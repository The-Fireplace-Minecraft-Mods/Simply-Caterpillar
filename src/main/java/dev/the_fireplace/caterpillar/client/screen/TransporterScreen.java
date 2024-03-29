package dev.the_fireplace.caterpillar.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.the_fireplace.caterpillar.Caterpillar;
import dev.the_fireplace.caterpillar.client.screen.util.ScreenTabs;
import dev.the_fireplace.caterpillar.menu.TransporterMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;
import java.util.List;

public class TransporterScreen extends AbstractCaterpillarScreen<TransporterMenu> {
    public TransporterScreen(TransporterMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, ScreenTabs.TRANSPORTER);
    }

    @Override
    protected void renderTutorial(GuiGraphics graphics) {
        if (super.tutorialButton != null && super.tutorialButton.isTutorialShown()) {
            if (!this.menu.hasMinecartChest()) {
                this.renderNeedMinecartChestTutorial(graphics);
            }

            this.renderTransporterTutorial(graphics);
            this.renderInventoryIsFull(graphics);
        }
    }

    private void renderTransporterTutorial(GuiGraphics graphics) {
        int tutorialX = super.leftPos - 22;
        int tutorialY = super.topPos - 24;
        List<Component> transporterTutorial = new ArrayList<>();

        MutableComponent tutorialText = Component.translatable(Caterpillar.MOD_ID + ".tutorial.transporter");
        transporterTutorial.add(tutorialText);

        graphics.renderComponentTooltip(this.font, transporterTutorial, tutorialX, tutorialY);
    }

    private void renderInventoryIsFull(GuiGraphics graphics) {
        int tutorialX = super.leftPos - 8;
        int tutorialY = super.topPos + 16;
        List<Component> inventoryIsFullTutorial = new ArrayList<>();

        MutableComponent tutorialText = Component.translatable(Caterpillar.MOD_ID + ".tutorial.transporter.inventory_is_full");
        inventoryIsFullTutorial.add(tutorialText);

        graphics.renderComponentTooltip(this.font, inventoryIsFullTutorial, tutorialX, tutorialY);
    }

    private void renderNeedMinecartChestTutorial(GuiGraphics graphics) {
        int tutorialX = super.leftPos - 8;
        int tutorialY = super.topPos + 46;
        List<Component> needMinecartChestTutorial = new ArrayList<>();

        MutableComponent tutorialText = Component.translatable(Caterpillar.MOD_ID + ".tutorial.transporter.need_minecart_chest");
        needMinecartChestTutorial.add(tutorialText);

        graphics.renderComponentTooltip(this.font, needMinecartChestTutorial, tutorialX, tutorialY);
    }
}
