package the_fireplace.caterpillar.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.client.screen.util.ScreenTabs;
import the_fireplace.caterpillar.common.block.entity.ReinforcementBlockEntity;
import the_fireplace.caterpillar.common.block.util.Replacement;
import the_fireplace.caterpillar.common.menu.ReinforcementMenu;
import the_fireplace.caterpillar.core.network.PacketHandler;
import the_fireplace.caterpillar.core.network.packet.client.CaterpillarSyncSlotC2SPacket;
import the_fireplace.caterpillar.core.network.packet.client.ReinforcementSyncStateReplacerC2SPacket;

import java.util.ArrayList;
import java.util.List;

import static the_fireplace.caterpillar.common.block.entity.ReinforcementBlockEntity.INVENTORY_SIZE;
import static the_fireplace.caterpillar.common.menu.AbstractCaterpillarMenu.BE_INVENTORY_FIRST_SLOT_INDEX;

public class ReinforcementScreen extends AbstractCaterpillarScreen<ReinforcementMenu> {

    private float scrollOffs;

    private boolean scrolling;

    private static final int SCROLLER_X = 202;

    private static final int SCROLLER_Y = 0;

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
        super(menu, playerInventory, title, ScreenTabs.REINFORCEMENT);

        this.scrollOffs = this.menu.getSelectedReplacer() / 3.0F;
    }

    @Override
    public void render(@NotNull PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        super.render(stack, mouseX, mouseY, partialTicks);

        this.renderReplacerButtons();
        this.renderTooltipReplacerButtons(stack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(@NotNull PoseStack stack, float partialTick, int mouseX, int mouseY) {
        super.renderBg(stack, partialTick, mouseX, mouseY);

        this.renderScroller(stack);
        this.renderBgSelectedMapSlots(stack);
    }

    @Override
    protected void renderLabels(@NotNull PoseStack stack, int mouseX, int mouseY) {
        super.inventoryLabelY = super.imageHeight - 94;

        super.renderLabels(stack, mouseX, mouseY);
    }

    private void renderReplacerButtons() {
        for (ImageButton button : this.replacerButtons) {
            super.removeWidget(button);
        }
        this.replacerButtons.clear();

        int replacerIndex = 0;
        for (byte replacer : this.menu.getReplacers(this.menu.getSelectedReplacer())) {
            int finalReplacerIndex = replacerIndex;
            if (replacer == (byte) 1) {
                this.replacerButtons.add(new ImageButton(super.leftPos + REPLACER_BTN_BG_X, super.topPos + REPLACER_BTN_BG_Y + replacerIndex * (REPLACER_BTN_BG_HEIGHT + 2), REPLACER_BTN_BG_WIDTH, REPLACER_BTN_BG_HEIGHT, 176 , replacerIndex*REPLACER_BTN_BG_HEIGHT*2, 0, ScreenTabs.REINFORCEMENT.TEXTURE, (onPress) -> PacketHandler.sendToServer(new ReinforcementSyncStateReplacerC2SPacket(this.menu.getSelectedReplacer(), finalReplacerIndex, (byte) 0, this.menu.blockEntity.getBlockPos()))));
            } else {
                this.replacerButtons.add(new ImageButton(super.leftPos + REPLACER_BTN_BG_X, super.topPos + REPLACER_BTN_BG_Y + replacerIndex * (REPLACER_BTN_BG_HEIGHT + 2), REPLACER_BTN_BG_WIDTH, REPLACER_BTN_BG_HEIGHT, 176 , replacerIndex*REPLACER_BTN_BG_HEIGHT*2 + REPLACER_BTN_BG_HEIGHT, 0, ScreenTabs.REINFORCEMENT.TEXTURE, (onPress) -> PacketHandler.sendToServer(new ReinforcementSyncStateReplacerC2SPacket(this.menu.getSelectedReplacer(), finalReplacerIndex, (byte) 1, this.menu.blockEntity.getBlockPos()))));
            }

            replacerIndex++;
        }

        for (ImageButton button : this.replacerButtons) {
            this.addRenderableWidget(button);
        }
    }

    private void renderTooltipReplacerButtons(PoseStack stack, int mouseX, int mouseY) {
        int incrementReplacerPos = 0;
        for (Replacement replacement : Replacement.values()) {
            if (mouseX >= super.leftPos + REPLACER_BTN_BG_X && mouseX <= super.leftPos + REPLACER_BTN_BG_X + REPLACER_BTN_BG_WIDTH && mouseY >= super.topPos + REPLACER_BTN_BG_Y + incrementReplacerPos*(REPLACER_BTN_BG_HEIGHT + 2) && mouseY <= super.topPos + REPLACER_BTN_BG_Y + REPLACER_BTN_BG_HEIGHT + incrementReplacerPos*(REPLACER_BTN_BG_HEIGHT + 2)) {
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

                this.renderComponentTooltip(stack, replacerComponents, super.leftPos + REPLACER_BTN_BG_X + 20,super.topPos + REPLACER_BTN_BG_Y + incrementReplacerPos*(REPLACER_BTN_BG_HEIGHT + 2) + 16);
            }

            incrementReplacerPos++;
        }
    }

    private void renderBgSelectedMapSlots(PoseStack stack) {
        int slotBgX = 226;
        int slotBgY = 0;

        int slotStartX;
        int slotStartY;

        switch (this.menu.getSelectedReplacer()) {
            case ReinforcementBlockEntity.REPLACER_CEILING:
                slotStartX = 44;
                slotStartY = 17;

                for (int x = 0; x < 5; x++) {
                    this.blit(stack, super.leftPos + slotStartX + x * 18, super.topPos + slotStartY, slotBgX, slotBgY, SLOT_SIZE, SLOT_SIZE);
                }
                break;
            case ReinforcementBlockEntity.REPLACER_LEFT:
                slotStartX = 44;
                slotStartY = 35;

                for (int y = 0; y < 3; y++) {
                    this.blit(stack, super.leftPos + slotStartX, super.topPos + slotStartY + y * 18, slotBgX, slotBgY, SLOT_SIZE, SLOT_SIZE);
                }
                break;
            case ReinforcementBlockEntity.REPLACER_RIGHT:
                slotStartX = 116;
                slotStartY = 35;

                for (int y = 0; y < 3; y++) {
                    this.blit(stack, super.leftPos + slotStartX, super.topPos + slotStartY + y * 18, slotBgX, slotBgY, SLOT_SIZE, SLOT_SIZE);
                }
                break;
            case ReinforcementBlockEntity.REPLACER_FLOOR:
                slotStartX = 44;
                slotStartY = 89;

                for (int x = 0; x < 5; x++) {
                    this.blit(stack, super.leftPos + slotStartX + x * 18, super.topPos + slotStartY, slotBgX, slotBgY, SLOT_SIZE, SLOT_SIZE);
                }
                break;
        }
    }

    private void renderScroller(PoseStack stack) {
        int i = this.leftPos + SCROLLBAR_X;
        int j = this.topPos + SCROLLBAR_Y;
        int k = j + SCROLLBAR_HEIGHT;
        blit(stack, i, j + (int)((float)(k - j - SCROLLBAR_Y) * this.scrollOffs), SCROLLER_X, SCROLLER_Y, SCROLLER_WIDTH, SCROLLER_HEIGHT);
    }

    @Override
    protected void slotClicked(@NotNull Slot slot, int slotId, int mouseButton, @NotNull ClickType type) {
        if (!this.isReinforcementSlot(slotId)) {
            super.slotClicked(slot, slotId, mouseButton, type);
            return;
        }

        int reinforcementSlotId = slotId - BE_INVENTORY_FIRST_SLOT_INDEX;
        ItemStack reinforcementStack;
        ItemStack carried = this.menu.getCarried().copy();

        if (carried.isEmpty()) {
            reinforcementStack = ItemStack.EMPTY;
        } else {
            reinforcementStack = carried.copy();
            reinforcementStack.setCount(1);
        }

        this.menu.slots.get(BE_INVENTORY_FIRST_SLOT_INDEX + reinforcementSlotId).set(reinforcementStack);
        this.menu.setCarried(carried);

        if (this.menu.blockEntity instanceof ReinforcementBlockEntity reinforcementBlockEntity) {
            reinforcementBlockEntity.setStackInSlot(reinforcementSlotId, reinforcementStack);

            PacketHandler.sendToServer(new CaterpillarSyncSlotC2SPacket(reinforcementSlotId, reinforcementStack, reinforcementBlockEntity.getBlockPos()));
        }
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) {
            this.scrolling = false;
        }

        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            if (this.insideScrollBar(mouseX, mouseY)) {
                this.scrolling = true;
                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (this.scrolling) {
            int i = this.topPos + SCROLLBAR_Y;
            int j = i + SCROLLBAR_HEIGHT;
            this.scrollOffs = ((float)mouseY - (float)i - 7.5F) / ((float)(j - i) - 15.0F);
            this.scrollOffs = Mth.clamp(this.scrollOffs, 0.0F, 1.0F);
            this.scrollTo(this.scrollOffs);
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
        float f = (float)(delta / (double)i);
        this.scrollOffs = Mth.clamp(this.scrollOffs - f, 0.0F, 1.0F);
        this.scrollTo(scrollOffs);
        return true;
    }

    private void scrollTo(float scrollOffs) {
        int i = 3;
        int j = (int)((double)(scrollOffs * (float)i) + 0.5D);
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
        return mouseX >= (double)i && mouseY >= (double)j && mouseX < (double)i + SCROLLER_WIDTH && mouseY < (double)k;
    }

    private boolean isReinforcementSlot(int slotId) {
        return slotId >= BE_INVENTORY_FIRST_SLOT_INDEX && slotId < BE_INVENTORY_FIRST_SLOT_INDEX + INVENTORY_SIZE;
    }
}
