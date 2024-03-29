package dev.the_fireplace.caterpillar.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.the_fireplace.caterpillar.Caterpillar;
import dev.the_fireplace.caterpillar.block.entity.DecorationBlockEntity;
import dev.the_fireplace.caterpillar.client.screen.util.ScreenTabs;
import dev.the_fireplace.caterpillar.menu.DecorationMenu;
import dev.the_fireplace.caterpillar.network.PacketHandler;
import dev.the_fireplace.caterpillar.network.packet.client.DecorationSyncSlotC2SPacket;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static dev.the_fireplace.caterpillar.block.entity.DecorationBlockEntity.INVENTORY_MAX_SLOTS;
import static dev.the_fireplace.caterpillar.menu.AbstractCaterpillarMenu.BE_INVENTORY_FIRST_SLOT_INDEX;

public class DecorationScreen extends AbstractScrollableScreen<DecorationMenu> {

    private static final int SCROLLER_BG_X = 176;

    private static final int SCROLLER_BG_Y = 0;

    private static final int SCROLLER_WIDTH = 12;

    private static final int SCROLLER_HEIGHT = 15;

    private static final int SCROLLBAR_X = 156;

    private static final int SCROLLBAR_Y = 17;

    private static final int SCROLLBAR_HEIGHT = 54;

    public DecorationScreen(DecorationMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, ScreenTabs.DECORATION, SCROLLER_BG_X, SCROLLER_BG_Y, SCROLLER_WIDTH, SCROLLER_HEIGHT, SCROLLBAR_X, SCROLLBAR_Y, SCROLLBAR_HEIGHT);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);

        this.renderTooltipCurrentMap(graphics, mouseX, mouseY);
        this.renderScrollerText(graphics);
    }

    @Override
    protected void renderTutorial(GuiGraphics graphics) {
        if (super.tutorialButton != null && super.tutorialButton.isTutorialShown()) {
            this.renderDecorationTutorial(graphics);
            this.renderMouseWheelTutorial(graphics);
            this.renderCurrentMapTutorial(graphics);
        }
    }

    private void renderDecorationTutorial(GuiGraphics graphics) {
        int tutorialX = super.leftPos - 32;
        int tutorialY = super.topPos - 16;
        List<Component> decorationTutorial = new ArrayList<>();

        MutableComponent tutorialText = Component.translatable(Caterpillar.MOD_ID + ".tutorial.decoration");
        decorationTutorial.add(tutorialText);

        graphics.renderComponentTooltip(this.font, decorationTutorial, tutorialX, tutorialY);
    }

    private void renderMouseWheelTutorial(GuiGraphics graphics) {
        int tutorialX = super.leftPos + 108;
        int tutorialY = super.topPos + 49;
        List<Component> decorationTutorial = new ArrayList<>();

        Component tutorialArrow = Component.literal("         /\\").withStyle(ChatFormatting.GREEN);
        decorationTutorial.add(tutorialArrow);

        MutableComponent tutorialText = Component.translatable(Caterpillar.MOD_ID + ".tutorial.decoration.mouse_wheel");
        decorationTutorial.add(tutorialText);

        graphics.renderComponentTooltip(this.font, decorationTutorial, tutorialX, tutorialY);
    }

    private void renderCurrentMapTutorial(GuiGraphics graphics) {
        int tutorialX = super.leftPos + 17;
        int tutorialY = super.topPos + 87;
        List<Component> decorationTutorial = new ArrayList<>();

        Component tutorialArrow = Component.literal("/\\").withStyle(ChatFormatting.GREEN);
        decorationTutorial.add(tutorialArrow);

        MutableComponent tutorialText = Component.translatable(Caterpillar.MOD_ID + ".tutorial.decoration.current_map").withStyle(ChatFormatting.WHITE);
        decorationTutorial.add(tutorialText);

        graphics.renderComponentTooltip(this.font, decorationTutorial, tutorialX, tutorialY);
    }

    private void renderScrollerText(GuiGraphics graphics) {
        int colorPlacement = this.menu.getCurrentMap() == this.menu.getSelectedMap() ? ChatFormatting.BLUE.getColor() : 4210752;
        graphics.drawString(this.font, String.valueOf(this.menu.getSelectedMap()), this.leftPos + 31, this.topPos + 39, colorPlacement, false);
    }

    private void renderTooltipCurrentMap(GuiGraphics graphics, int mouseX, int mouseY) {
        if (
                mouseX >= super.leftPos + 25 &&
                mouseY >= super.topPos + 34 &&
                mouseX <= super.leftPos + 25 + SLOT_SIZE &&
                mouseY <= super.topPos + 34 + SLOT_SIZE
        ) {
            if (this.menu.getSelectedMap() == this.menu.getCurrentMap()) {
                Component currentMap = Component.translatable("gui." + Caterpillar.MOD_ID +".decoration.current_map");

                int currentMapTooltipX = -8;
                int currentMapTooltipY = 32;
                graphics.renderTooltip(this.font, currentMap, super.leftPos + currentMapTooltipX, super.topPos + currentMapTooltipY);
            }
        }
    }

    @Override
    protected void slotClicked(@NotNull Slot slot, int slotId, int mouseButton, @NotNull ClickType type) {
        if (!this.isDecorationSlot(slotId)) {
            super.slotClicked(slot, slotId, mouseButton, type);
            return;
        }

        int placementSlotId = slotId - BE_INVENTORY_FIRST_SLOT_INDEX;
        ItemStack placementStack;
        ItemStack carried = this.menu.getCarried().copy();

        if (carried.isEmpty()) {
            placementStack = ItemStack.EMPTY;
        } else {
           placementStack = carried.copy();
           placementStack.setCount(1);
        }

        this.menu.slots.get(BE_INVENTORY_FIRST_SLOT_INDEX + placementSlotId).set(placementStack);
        this.menu.setCarried(carried);

        if (this.menu.blockEntity instanceof DecorationBlockEntity decorationBlockEntity) {
            decorationBlockEntity.getSelectedPlacementMap().setStackInSlot(placementSlotId, placementStack);

            PacketHandler.sendToServer(new DecorationSyncSlotC2SPacket(placementSlotId, placementStack, decorationBlockEntity.getBlockPos()));
        }
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) {
            this.menu.setScrolling(false);
        }

        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            if (this.insideScrollBar(mouseX, mouseY)) {
                this.menu.setScrolling(true);
                return true;
            }
        }

        if (getSlotUnderMouse() != null && this.isDecorationSlot(getSlotUnderMouse().index)) {
            slotClicked(getSlotUnderMouse(), getSlotUnderMouse().index, 0, ClickType.PICKUP);
            return true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    private boolean isDecorationSlot(int slotId) {
        return slotId >= BE_INVENTORY_FIRST_SLOT_INDEX && slotId < BE_INVENTORY_FIRST_SLOT_INDEX + INVENTORY_MAX_SLOTS;
    }

    public boolean insideScrollBar(double mouseX, double mouseY) {
        int i = this.leftPos + 156;
        int j = this.topPos + SCROLLER_WIDTH;
        int k = j + SCROLLER_HEIGHT;
        return mouseX >= (double)i && mouseY >= (double)j && mouseX < (double)i + 12 && mouseY < (double)k;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (this.menu.isScrolling()) {
            int i = this.topPos + SCROLLER_WIDTH;
            int j = i + SCROLLER_HEIGHT;
            this.menu.setScrollOffs(((float)mouseY - (float)i - 7.5F) / ((float)(j - i) - 15.0F));
            this.menu.setScrollOffs(Mth.clamp(this.menu.getScrollOffs(), 0.0F, 1.0F));
            this.scrollTo(this.menu.getScrollOffs());
            return true;
        }

        if (getSlotUnderMouse() != null && this.isDecorationSlot(getSlotUnderMouse().index)) {
           slotClicked(getSlotUnderMouse(), getSlotUnderMouse().index, 0, ClickType.PICKUP);
           return true;
        }

        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        int i = 9;
        float f = (float)(delta / (double)i);
        this.menu.setScrollOffs(Mth.clamp(this.menu.getScrollOffs() - f, 0.0F, 1.0F));
        this.scrollTo(this.menu.getScrollOffs());
        return true;
    }

    protected void scrollTo(float scrollOffs) {
        int i = 9;
        int j = (int)((double)(scrollOffs * (float)i) + 0.5D);
        if (j < 0) {
            j = 0;
        }
        int scrollToMap = j;

        this.menu.scrollTo(scrollToMap);
    }
}
