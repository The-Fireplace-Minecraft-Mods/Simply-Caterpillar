package the_fireplace.caterpillar.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.client.screen.util.PowerButton;
import the_fireplace.caterpillar.client.screen.util.ScreenTabs;
import the_fireplace.caterpillar.common.block.entity.DrillHeadBlockEntity;
import the_fireplace.caterpillar.common.menu.DrillHeadMenu;

import java.util.ArrayList;
import java.util.List;

import static the_fireplace.caterpillar.common.block.entity.DrillHeadBlockEntity.INVENTORY_SIZE;
import static the_fireplace.caterpillar.common.menu.AbstractCaterpillarMenu.BE_INVENTORY_FIRST_SLOT_INDEX;

@OnlyIn(Dist.CLIENT)
public class DrillHeadScreen extends AbstractScrollableScreen<DrillHeadMenu> {

    private static final int SCROLLER_BG_X = 176;

    private static final int SCROLLER_BG_Y = 52;

    private static final int SCROLLER_WIDTH = 6;

    private static final int SCROLLER_HEIGHT = 17;

    private static final int CONSUMPTION_SCROLLBAR_X = 64;

    private static final int GHATHERED_SCROLLBAR_X = 106;

    private static final int SCROLLBAR_Y = 17;

    private static final int SCROLLBAR_HEIGHT = 52;

    private static final int POWER_BG_WIDTH = 21;

    private static final int POWER_BG_HEIGHT = 18;

    private static final int BURN_SLOT_BG_X = 81;

    private static final int BURN_SLOT_BG_Y = 36 + 12;

    private static final int BURN_SLOT_BG_WIDTH = 14;

    public PowerButton powerButton;

    public DrillHeadScreen(DrillHeadMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, ScreenTabs.DRILL_HEAD, SCROLLER_BG_X, SCROLLER_BG_Y, SCROLLER_WIDTH, SCROLLER_HEIGHT, CONSUMPTION_SCROLLBAR_X, SCROLLBAR_Y, SCROLLBAR_HEIGHT);

        this.setScrollOffs(0.333F);
    }

    @Override
    protected void setScrollOffs(float scrollOffs) {
        super.setScrollOffs(scrollOffs);
    }

    @Override
    protected void init() {
        super.init();

        this.addPowerButton();
        this.addTutorialButton();
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        this.updatePowerButton();
    }

    @Override
    public void render(@NotNull PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        super.render(stack, mouseX, mouseY, partialTicks);

        this.renderTooltipPowerButton(stack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(@NotNull PoseStack stack, float partialTick, int mouseX, int mouseY) {
        super.renderBg(stack, partialTick, mouseX, mouseY);

        if (this.menu.isPowered()) {
            int litProgress = this.menu.getLitProgress();
            blit(stack, super.leftPos + BURN_SLOT_BG_X, super.topPos + BURN_SLOT_BG_Y - litProgress, ScreenTabs.DRILL_HEAD.IMAGE_WIDTH, 12 - litProgress, BURN_SLOT_BG_WIDTH, litProgress + 1);
        }
    }

    @Override
    protected void renderLabels(@NotNull PoseStack stack, int mouseX, int mouseY) {
        int consumptionX = 4, consumptionY = 6, gatheredX = 119, gatheredY = 6;

        this.font.draw(stack, DrillHeadBlockEntity.CONSUMPTION_TITLE, consumptionX, consumptionY, 0x404040);
        this.font.draw(stack, DrillHeadBlockEntity.GATHERED_TITLE, gatheredX, gatheredY, 0x404040);
        this.font.draw(stack, super.playerInventoryTitle, super.inventoryLabelX, super.inventoryLabelY, 0x404040);
    }

    private void addPowerButton() {
        this.powerButton = new PowerButton(this.menu.isPowered(), super.leftPos + (super.imageWidth - SLOT_SIZE) / 2 + (this.menu.isPowered() ? 0 : -3), super.topPos + 16, POWER_BG_WIDTH, POWER_BG_HEIGHT, 176, 15, POWER_BG_HEIGHT, ScreenTabs.DRILL_HEAD.TEXTURE, (onPress) -> {
            this.menu.setPower(!this.menu.isPowered());
        });
        this.addRenderableWidget(powerButton);
    }

    private void renderTooltipPowerButton(PoseStack stack, int mouseX, int mouseY) {
        if (
                mouseX >= super.leftPos + (super.imageWidth - SLOT_SIZE) / 2 - 3 &&
                mouseY >= super.topPos + 16 &&
                mouseX <= super.leftPos + (super.imageWidth - SLOT_SIZE) / 2 - 3 + 21 &&
                mouseY <= super.topPos + 16 + 18
        ) {
            List<Component> powerStatusComponents = new ArrayList<>();
            MutableComponent powerStatusComponent = Component.translatable("gui." + Caterpillar.MOD_ID +".drill_head.power").append(" ");
            if (this.menu.isPowered()) {
                powerStatusComponent.append(Component.translatable("gui." + Caterpillar.MOD_ID +".drill_head.power.on").withStyle(ChatFormatting.GREEN));
                powerStatusComponents.add(powerStatusComponent);
            } else {
                powerStatusComponent.append(Component.translatable("gui." + Caterpillar.MOD_ID +".drill_head.power.off").withStyle(ChatFormatting.RED));
                powerStatusComponents.add(powerStatusComponent);
            }

            int tooltipPadding = 24;
            this.renderComponentTooltip(stack, powerStatusComponents, super.leftPos + (super.imageWidth - super.font.width(powerStatusComponent) - tooltipPadding) / 2, super.topPos - 1);
        }
    }
    private void updatePowerButton() {
        if (this.menu.isPowered() != this.powerButton.isPowered()) {
            this.powerButton.setPower(this.menu.isPowered());
        }
    }

    private void addTutorialButton() {
    }

    @Override
    protected void renderScroller(PoseStack stack) {
        super.renderScroller(stack);

        int gatheredScrollbarX = this.leftPos + GHATHERED_SCROLLBAR_X;
        int scrollbarY = this.topPos + SCROLLBAR_Y;
        int scrollbarYFinish = scrollbarY + SCROLLBAR_HEIGHT;

        blit(stack, gatheredScrollbarX, scrollbarY + (int)((float)(scrollbarYFinish - scrollbarY - SCROLLBAR_Y) * this.scrollOffs), SCROLLER_BG_X, SCROLLER_BG_Y, SCROLLER_WIDTH, SCROLLER_HEIGHT);
    }

    protected void scrollTo(float scrollOffs) {
        int i = 2;
        int j = (int)((double)(scrollOffs * (float)i) + 0.5D);
        if (j < 0) {
            j = 0;
        }
        int scrollToReplacer = j;

        // this.menu.scrollTo(scrollToReplacer);
    }

    private boolean insideScrollBar(double mouseX, double mouseY) {
        return false;
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
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        int i = 2;
        float f = (float)(delta / (double)i);
        this.scrollOffs = Mth.clamp(this.scrollOffs - f, 0.0F, 1.0F);
        this.scrollTo(scrollOffs);
        return true;
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

        if (getSlotUnderMouse() != null && this.isDrillHeadSlot(getSlotUnderMouse().index)) {
            slotClicked(getSlotUnderMouse(), getSlotUnderMouse().index, 0, ClickType.PICKUP);
            return true;
        }


        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    private boolean isDrillHeadSlot(int slotId) {
        return slotId >= BE_INVENTORY_FIRST_SLOT_INDEX && slotId < BE_INVENTORY_FIRST_SLOT_INDEX + INVENTORY_SIZE;
    }
}
