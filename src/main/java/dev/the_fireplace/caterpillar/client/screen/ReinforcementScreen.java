package dev.the_fireplace.caterpillar.client.screen;

import dev.the_fireplace.caterpillar.Caterpillar;
import dev.the_fireplace.caterpillar.block.entity.ReinforcementBlockEntity;
import dev.the_fireplace.caterpillar.block.util.Replacement;
import dev.the_fireplace.caterpillar.client.screen.util.ScreenTabs;
import dev.the_fireplace.caterpillar.menu.ReinforcementMenu;
import dev.the_fireplace.caterpillar.network.packet.client.CaterpillarSyncSlotC2SPacket;
import dev.the_fireplace.caterpillar.network.packet.client.ReinforcementSyncStateReplacerC2SPacket;
import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import static dev.the_fireplace.caterpillar.block.entity.ReinforcementBlockEntity.INVENTORY_SIZE;
import static dev.the_fireplace.caterpillar.menu.AbstractCaterpillarMenu.BE_INVENTORY_FIRST_SLOT_INDEX;

import com.mojang.blaze3d.vertex.PoseStack;

public class ReinforcementScreen extends AbstractScrollableScreen<ReinforcementMenu> {

    private static final int SCROLLER_BG_X = 202;

    private static final int SCROLLER_BG_Y = 0;

    private static final int SCROLLER_WIDTH = 12;

    private static final int SCROLLER_HEIGHT = 15;

    private static final int SCROLLBAR_X = 156;

    private static final int SCROLLBAR_Y = 17;

    private static final int SCROLLBAR_HEIGHT = 90;

    private static final int REPLACER_BTN_BG_X = 7;

    private static final int REPLACER_BTN_BG_Y = 17;

    private static final int REPLACER_BTN_BG_WIDTH = 26;

    private static final int REPLACER_BTN_BG_HEIGHT = 16;

    private final List<ImageButton> replacerButtons = new ArrayList<>();

    public ReinforcementScreen(ReinforcementMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, ScreenTabs.REINFORCEMENT, SCROLLER_BG_X, SCROLLER_BG_Y, SCROLLER_WIDTH, SCROLLER_HEIGHT, SCROLLBAR_X, SCROLLBAR_Y, SCROLLBAR_HEIGHT);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);

        this.renderTextOfCurrentReplacement(graphics);
        this.renderReplacerButtons();
        this.renderTooltipReplacerButtons(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        super.inventoryLabelY = super.imageHeight - 94;

        super.renderLabels(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderTutorial(GuiGraphics graphics) {
        if (super.tutorialButton != null && super.tutorialButton.isTutorialShown()) {
            this.renderReplacerButtonsTutorial(graphics);
            this.renderReplacerBlocksTutorial(graphics);
        }
    }

    private void renderReplacerButtonsTutorial(GuiGraphics graphics) {
        int tutorialX = super.leftPos - 1;
        int tutorialY = super.topPos + 122;
        List<Component> replacerButtonsTutorial = new ArrayList<>();

        Component tutorialArrow = Component.literal(" /\\").withStyle(ChatFormatting.GREEN);
        replacerButtonsTutorial.add(tutorialArrow);

        MutableComponent tutorialText = Component.translatable(Caterpillar.MOD_ID + ".tutorial.reinforcement.replacer_buttons").withStyle(ChatFormatting.WHITE);
        replacerButtonsTutorial.add(tutorialText);

        graphics.renderComponentTooltip(this.font, replacerButtonsTutorial, tutorialX, tutorialY);
    }

    private void renderReplacerBlocksTutorial(GuiGraphics graphics) {
        int tutorialX = super.leftPos + 25;
        int tutorialY = super.topPos - 17;
        List<Component> replacerBlocksTutorial = new ArrayList<>();

        MutableComponent tutorialText = Component.translatable(Caterpillar.MOD_ID + ".tutorial.reinforcement.replacer_blocks");
        replacerBlocksTutorial.add(tutorialText);

        Component tutorialArrow = Component.literal("  \\/").withStyle(ChatFormatting.GREEN);
        replacerBlocksTutorial.add(tutorialArrow);

        graphics.renderComponentTooltip(this.font, replacerBlocksTutorial, tutorialX, tutorialY);
    }

    private void renderTextOfCurrentReplacement(GuiGraphics graphics) {
        int i = 3;
        int j = (int) ((double) (this.menu.getScrollOffs() * (float) i) + 0.5D);
        if (j < 0) {
            j = 0;
        }
        int scrollIndex = j;

        String currentReplacementText = switch (scrollIndex) {
            case 0 -> "ceiling";
            case 1 -> "left_wall";
            case 2 -> "right_wall";
            default -> "floor";
        };

        Component textOfCurrentReplacement = Component.translatable("gui." + Caterpillar.MOD_ID + ".replacement." + currentReplacementText);
        int textX = super.leftPos + (super.imageWidth - super.font.width(textOfCurrentReplacement)) / 2;
        int textY = super.topPos + 58;

        graphics.drawString(this.font, textOfCurrentReplacement, textX, textY, 0x404040, false);
    }

    private void renderReplacerButtons() {
        for (ImageButton button : this.replacerButtons) {
            super.removeWidget(button);
        }
        this.replacerButtons.clear();

        int i = 3;
        int j = (int) ((double) (this.menu.getScrollOffs() * (float) i) + 0.5D);
        if (j < 0) {
            j = 0;
        }
        int scrollIndex = j;

        int replacerIndex = 0;
        for (byte replacer : this.menu.getReplacers(scrollIndex)) {
            int finalReplacerIndex = replacerIndex;
            if (replacer == (byte) 1) {
                this.replacerButtons.add(new ImageButton(super.leftPos + REPLACER_BTN_BG_X, super.topPos + REPLACER_BTN_BG_Y + replacerIndex * (REPLACER_BTN_BG_HEIGHT + 2), REPLACER_BTN_BG_WIDTH, REPLACER_BTN_BG_HEIGHT, 176, replacerIndex * REPLACER_BTN_BG_HEIGHT * 2, 0, ScreenTabs.REINFORCEMENT.TEXTURE, (onPress) -> {
                    ReinforcementSyncStateReplacerC2SPacket.send(scrollIndex, finalReplacerIndex, (byte) 0, this.menu.blockEntity.getBlockPos());
                }));
            } else {
                this.replacerButtons.add(new ImageButton(super.leftPos + REPLACER_BTN_BG_X, super.topPos + REPLACER_BTN_BG_Y + replacerIndex * (REPLACER_BTN_BG_HEIGHT + 2), REPLACER_BTN_BG_WIDTH, REPLACER_BTN_BG_HEIGHT, 176, replacerIndex * REPLACER_BTN_BG_HEIGHT * 2 + REPLACER_BTN_BG_HEIGHT, 0, ScreenTabs.REINFORCEMENT.TEXTURE, (onPress) -> {
                    ReinforcementSyncStateReplacerC2SPacket.send(scrollIndex, finalReplacerIndex, (byte) 1, this.menu.blockEntity.getBlockPos());
                }));
            }

            replacerIndex++;
        }

        for (ImageButton button : this.replacerButtons) {
            this.addRenderableWidget(button);
        }
    }

    private void renderTooltipReplacerButtons(GuiGraphics graphics, int mouseX, int mouseY) {
        int incrementReplacerPos = 0;
        for (Replacement replacement : Replacement.values()) {
            if (mouseX >= super.leftPos + REPLACER_BTN_BG_X && mouseX <= super.leftPos + REPLACER_BTN_BG_X + REPLACER_BTN_BG_WIDTH && mouseY >= super.topPos + REPLACER_BTN_BG_Y + incrementReplacerPos * (REPLACER_BTN_BG_HEIGHT + 2) && mouseY <= super.topPos + REPLACER_BTN_BG_Y + REPLACER_BTN_BG_HEIGHT + incrementReplacerPos * (REPLACER_BTN_BG_HEIGHT + 2)) {
                List<Component> replacerComponents = new ArrayList<>();
                MutableComponent replacerText;
                if (this.menu.getSelectedReplacers()[replacement.INDEX] == 1) {
                    replacerText = Component.translatable("gui." + Caterpillar.MOD_ID + ".replacement.will_replace").append(" ");
                    replacerText.append(replacement.NAME.withStyle(ChatFormatting.GREEN));
                    replacerComponents.add(replacerText);
                } else {
                    replacerText = Component.translatable("gui." + Caterpillar.MOD_ID + ".replacement.wont_replace").append(" ");
                    replacerText.append(replacement.NAME.withStyle(ChatFormatting.RED));
                    replacerComponents.add(replacerText);
                }

                graphics.renderComponentTooltip(this.font, replacerComponents, super.leftPos + REPLACER_BTN_BG_X + 20, super.topPos + REPLACER_BTN_BG_Y + incrementReplacerPos * (REPLACER_BTN_BG_HEIGHT + 2) + 16);
            }

            incrementReplacerPos++;
        }
    }

    @Override
    protected void slotClicked(@NotNull Slot slot, int slotId, int mouseButton, @NotNull ClickType type) {
        if (!this.isReinforcementSlot(slotId)) {
            super.slotClicked(slot, slotId, mouseButton, type);
            return;
        }

        int reinforcementSlotId = slot.getContainerSlot();
        ItemStack reinforcementStack;
        ItemStack carried = this.menu.getCarried().copy();

        if (carried.isEmpty()) {
            reinforcementStack = ItemStack.EMPTY;
        } else {
            reinforcementStack = carried.copy();
            reinforcementStack.setCount(1);
        }

        this.menu.slots.get(slotId).set(reinforcementStack);
        this.menu.setCarried(carried);

        if (this.menu.blockEntity instanceof ReinforcementBlockEntity reinforcementBlockEntity) {
            reinforcementBlockEntity.setItem(reinforcementSlotId, reinforcementStack);
            reinforcementBlockEntity.setChanged();

            CaterpillarSyncSlotC2SPacket.send(reinforcementSlotId, reinforcementStack, reinforcementBlockEntity.getBlockPos());
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

        if (getSlotUnderMouse() != null && this.isReinforcementSlot(getSlotUnderMouse().index)) {
            slotClicked(getSlotUnderMouse(), getSlotUnderMouse().index, 0, ClickType.PICKUP);
            return true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (this.menu.isScrolling()) {
            int i = this.topPos + SCROLLBAR_Y;
            int j = i + SCROLLBAR_HEIGHT;
            this.menu.setScrollOffs(((float) mouseY - (float) i - 7.5F) / ((float) (j - i) - 15.0F));
            this.menu.setScrollOffs(Mth.clamp(this.menu.getScrollOffs(), 0.0F, 1.0F));
            this.scrollTo(this.menu.getScrollOffs());
            return true;
        }

        if (getSlotUnderMouse() != null && this.isReinforcementSlot(getSlotUnderMouse().index)) {
            slotClicked(getSlotUnderMouse(), getSlotUnderMouse().index, 0, ClickType.PICKUP);
            return true;
        }

        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        int i = 3;
        float f = (float) (delta / (double) i);
        this.menu.setScrollOffs(Mth.clamp(this.menu.getScrollOffs() - f, 0.0F, 1.0F));
        this.scrollTo(this.menu.getScrollOffs());
        return true;
    }

    protected void scrollTo(float scrollOffs) {
        int i = 3;
        int j = (int) ((double) (scrollOffs * (float) i) + 0.5D);
        if (j < 0) {
            j = 0;
        }
        int scrollToReplacer = j;

        this.menu.scrollTo(scrollToReplacer);
    }

    private boolean insideScrollBar(double mouseX, double mouseY) {
        int i = this.leftPos + SCROLLBAR_X;
        int j = this.topPos + SCROLLBAR_Y;
        int k = j + SCROLLBAR_HEIGHT;
        return mouseX >= (double) i && mouseY >= (double) j && mouseX < (double) i + SCROLLER_WIDTH && mouseY < (double) k;
    }

    private boolean isReinforcementSlot(int slotId) {
        return slotId >= BE_INVENTORY_FIRST_SLOT_INDEX && slotId < BE_INVENTORY_FIRST_SLOT_INDEX + INVENTORY_SIZE;
    }
}
