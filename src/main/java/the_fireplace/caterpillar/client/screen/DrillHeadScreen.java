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

    private static final int GATHERED_SCROLLBAR_X = 106;

    private static final int SCROLLBAR_Y = 17;

    private static final int SCROLLBAR_HEIGHT = 52;

    private static final int POWER_BG_WIDTH = 21;

    private static final int POWER_BG_HEIGHT = 18;

    private static final int POWER_BG_X = 176;

    private static final int POWER_BG_Y = 15;

    private static final int BURN_SLOT_BG_X = 81;

    private static final int BURN_SLOT_BG_Y = 36 + 12;

    private static final int BURN_SLOT_BG_WIDTH = 14;

    public PowerButton powerButton;

    private float gatheredScrollOffs;

    private boolean gatheredScrolling;

    public DrillHeadScreen(DrillHeadMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, ScreenTabs.DRILL_HEAD, SCROLLER_BG_X + (menu.canScroll() ? 0 : SCROLLER_WIDTH), SCROLLER_BG_Y, SCROLLER_WIDTH, SCROLLER_HEIGHT, CONSUMPTION_SCROLLBAR_X, SCROLLBAR_Y, SCROLLBAR_HEIGHT);

        this.setConsumptionScrollOffs(0.0F);
        this.setGatheredScrollOffs(0.0F);
    }

    protected void setConsumptionScrollOffs(float scrollOffs) {
        super.setScrollOffs(scrollOffs);
    }

    protected void setGatheredScrollOffs(float scrollOffs) {
        this.gatheredScrollOffs = scrollOffs;
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
        this.powerButton = new PowerButton(this.menu.isPowered(), super.leftPos + (super.imageWidth - SLOT_SIZE) / 2, super.topPos + 16, POWER_BG_WIDTH, POWER_BG_HEIGHT, POWER_BG_X, POWER_BG_Y, POWER_BG_HEIGHT, ScreenTabs.DRILL_HEAD.TEXTURE, (onPress) -> {
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

    @Override
    protected void renderScroller(PoseStack stack) {
        super.renderScroller(stack);

        int gatheredScrollbarX = this.leftPos + GATHERED_SCROLLBAR_X;
        int scrollbarY = this.topPos + SCROLLBAR_Y;
        int scrollbarYEnd = scrollbarY + SCROLLBAR_HEIGHT;

        blit(stack, gatheredScrollbarX, scrollbarY + (int)((float)(scrollbarYEnd - scrollbarY - SCROLLBAR_Y) * this.gatheredScrollOffs), SCROLLER_BG_X + (this.menu.canScroll() ? 0 : SCROLLER_WIDTH), SCROLLER_BG_Y, SCROLLER_WIDTH, SCROLLER_HEIGHT);
    }

    protected void gatheredScrollTo(float scrollOffs) {
        int i = 3;
        int j = (int)((double)(scrollOffs * (float)i) + 0.5D);
        if (j < 0) {
            j = 0;
        }
        int gatheredScrollTo = j;

        this.menu.gatheredScrollTo(gatheredScrollTo);
    }

    protected void consumptionScrollTo(float scrollOffs) {
        int i = 3;
        int j = (int)((double)(scrollOffs * (float)i) + 0.5D);
        if (j < 0) {
            j = 0;
        }
        int consumptionScrollTo = j;

        this.menu.consumptionScrollTo(consumptionScrollTo);
    }

    private boolean insideGatheredScrollArea(double mouseX, double mouseY) {
        int gatheredAreaXStart = this.leftPos + GATHERED_SCROLLBAR_X;
        int gatheredAreaYStart = this.topPos + SCROLLBAR_Y;
        int gatheredAreaXEnd = this.leftPos + DrillHeadMenu.GATHERED_SLOT_X_END;
        int gatheredAreaYEnd = this.topPos + DrillHeadMenu.GATHERED_SLOT_Y_END;

        return mouseX >= (double)gatheredAreaXStart && mouseY >= (double)gatheredAreaYStart && mouseX < (double)gatheredAreaXEnd && mouseY < (double)gatheredAreaYEnd;
    }

    private boolean insideGatheredScrollbar(double mouseX, double mouseY) {
        int i = this.leftPos + GATHERED_SCROLLBAR_X;
        int j = this.topPos + SCROLLBAR_Y;
        int k = j + SCROLLBAR_HEIGHT;

        return mouseX >= (double)i && mouseY >= (double)j && mouseX < (double)i + SCROLLER_WIDTH && mouseY < (double)k;
    }

    private boolean insideConsumptionScrollArea(double mouseX, double mouseY) {
        int consumptionAreaXStart = this.leftPos + DrillHeadMenu.CONSUMPTION_SLOT_X_START;
        int consumptionAreaYStart = this.topPos + DrillHeadMenu.CONSUMPTION_SLOT_Y_START;
        int consumptionAreaXEnd = this.leftPos + CONSUMPTION_SCROLLBAR_X + SCROLLER_WIDTH;
        int consumptionAreaYEnd = this.topPos + SCROLLBAR_Y + SCROLLBAR_HEIGHT;

        return mouseX >= (double)consumptionAreaXStart && mouseY >= (double)consumptionAreaYStart && mouseX < (double)consumptionAreaXEnd && mouseY < (double)consumptionAreaYEnd;
    }

    private boolean insideConsumptionScrollbar(double mouseX, double mouseY) {
        int i = this.leftPos + CONSUMPTION_SCROLLBAR_X;
        int j = this.topPos + SCROLLBAR_Y;
        int k = j + SCROLLBAR_HEIGHT;

        return mouseX >= (double)i && mouseY >= (double)j && mouseX < (double)i + SCROLLER_WIDTH && mouseY < (double)k;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) {
            this.scrolling = false;
            this.gatheredScrolling = false;
        }

        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            if (this.insideGatheredScrollbar(mouseX, mouseY)) {
                this.gatheredScrolling = this.menu.canScroll();
                return true;
            } else if (this.insideConsumptionScrollbar(mouseX, mouseY)) {
                super.scrolling = this.menu.canScroll();
                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (!this.menu.canScroll()) {
            return false;
        }

        if (this.insideGatheredScrollArea(mouseX, mouseY)) {
            int i = 3;
            float f = (float)(delta / (double)i);
            this.gatheredScrollOffs = Mth.clamp(this.gatheredScrollOffs - f, 0.0F, 1.0F);
            this.gatheredScrollTo(this.gatheredScrollOffs);
            return true;
        } else if (this.insideConsumptionScrollArea(mouseX, mouseY)) {
            int i = 3;
            float f = (float)(delta / (double)i);
            super.scrollOffs = Mth.clamp(super.scrollOffs - f, 0.0F, 1.0F);
            this.consumptionScrollTo(super.scrollOffs);
            return true;
        }

        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (super.scrolling) {
            int i = this.topPos + SCROLLBAR_Y;
            int j = i + SCROLLBAR_HEIGHT;
            super.scrollOffs = ((float)mouseY - (float)i - 7.5F) / ((float)(j - i) - 15.0F);
            super.scrollOffs = Mth.clamp(super.scrollOffs, 0.0F, 1.0F);
            this.consumptionScrollTo(super.scrollOffs);
            return true;
        }

        if (this.gatheredScrolling) {
            int i = this.topPos + SCROLLBAR_Y;
            int j = i + SCROLLBAR_HEIGHT;
            this.gatheredScrollOffs = ((float)mouseY - (float)i - 7.5F) / ((float)(j - i) - 15.0F);
            this.gatheredScrollOffs = Mth.clamp(this.gatheredScrollOffs, 0.0F, 1.0F);
            this.gatheredScrollTo( this.gatheredScrollOffs);
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
