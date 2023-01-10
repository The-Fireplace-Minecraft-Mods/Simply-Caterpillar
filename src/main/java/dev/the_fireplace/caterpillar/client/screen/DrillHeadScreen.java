package dev.the_fireplace.caterpillar.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.the_fireplace.caterpillar.Caterpillar;
import dev.the_fireplace.caterpillar.block.entity.DrillHeadBlockEntity;
import dev.the_fireplace.caterpillar.block.util.CaterpillarBlockUtil;
import dev.the_fireplace.caterpillar.client.screen.util.ScreenTabs;
import dev.the_fireplace.caterpillar.client.screen.widget.PowerButton;
import dev.the_fireplace.caterpillar.menu.DrillHeadMenu;
import dev.the_fireplace.caterpillar.menu.slot.FakeSlot;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

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

    private PowerButton powerButton;

    public DrillHeadScreen(DrillHeadMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, ScreenTabs.DRILL_HEAD, SCROLLER_BG_X + (menu.canScroll() ? 0 : SCROLLER_WIDTH), SCROLLER_BG_Y, SCROLLER_WIDTH, SCROLLER_HEIGHT, CONSUMPTION_SCROLLBAR_X, SCROLLBAR_Y, SCROLLBAR_HEIGHT);

        this.consumptionScrollTo(0.0F);
        this.gatheredScrollTo(0.0F);
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
    protected void updateTabButtons() {
        if (this.menu.isMoving()) {
            this.menu.setConnectedCaterpillarBlockEntities(CaterpillarBlockUtil.getConnectedCaterpillarBlockEntities(this.menu.blockEntity.getLevel(), this.menu.blockEntity.getBlockPos(), new ArrayList<>()));

            super.addTabButtons();
        }
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

    @Override
    protected void renderTutorial(PoseStack stack) {
        if (super.tutorialButton != null && super.tutorialButton.isTutorialShown()) {
            if (this.menu.canScroll()) {
                this.renderWheelStorageTutorial(stack);
            }

            this.renderFuelSlotTutorial(stack);
            this.renderPowerButtonTutorial(stack);
        }
    }

    private void renderPowerButtonTutorial(PoseStack stack) {
        int tutorialX = super.leftPos + 90;
        int tutorialY = super.topPos + 33;
        List<Component> powerButtonTutorial = new ArrayList<>();

        MutableComponent tutorialText = Component.literal("<").withStyle(ChatFormatting.GREEN).append(" ");
        tutorialText.append(Component.translatable(Caterpillar.MOD_ID + ".tutorial.drill_head.power_button").withStyle(ChatFormatting.WHITE).append(""));
        powerButtonTutorial.add(tutorialText);

        this.renderComponentTooltip(stack, powerButtonTutorial, tutorialX, tutorialY);
    }

    private void renderFuelSlotTutorial(PoseStack stack) {
        int tutorialX = super.leftPos + 90;
        int tutorialY = super.topPos + 69;
        List<Component> fuelSlotTutorial = new ArrayList<>();

        MutableComponent tutorialText = Component.literal("<").withStyle(ChatFormatting.GREEN).append(" ");
        tutorialText.append(Component.translatable(Caterpillar.MOD_ID + ".tutorial.drill_head.fuel_slot").withStyle(ChatFormatting.WHITE).append(""));
        fuelSlotTutorial.add(tutorialText);

        this.renderComponentTooltip(stack, fuelSlotTutorial, tutorialX, tutorialY);
    }

    private void renderWheelStorageTutorial(PoseStack stack) {
        int tutorialX = super.leftPos + 230;
        int tutorialY = super.topPos + 87;
        List<Component> storageWheelTutorial = new ArrayList<>();

        Component tutorialArrow = Component.literal("             /\\        /\\").withStyle(ChatFormatting.GREEN);
        storageWheelTutorial.add(tutorialArrow);

        MutableComponent tutorialText = Component.translatable(Caterpillar.MOD_ID + ".tutorial.drill_head.wheel_storage");
        storageWheelTutorial.add(tutorialText);

        this.renderComponentTooltip(stack, storageWheelTutorial, tutorialX, tutorialY);
    }

    private void addPowerButton() {
        this.powerButton = new PowerButton(this.menu.isPowered(), super.leftPos + (super.imageWidth - SLOT_SIZE) / 2, super.topPos + 16, POWER_BG_WIDTH, POWER_BG_HEIGHT, POWER_BG_X, POWER_BG_Y, POWER_BG_HEIGHT, ScreenTabs.DRILL_HEAD.TEXTURE, (onPress) -> this.menu.setPower(!this.menu.isPowered()));
        this.addRenderableWidget(this.powerButton);
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
        int scrollbarYStart = this.topPos + SCROLLBAR_Y;
        int scrollbarYEnd = scrollbarYStart + super.SCROLLBAR_HEIGHT;

        int consumptionScrollbarX = this.leftPos + CONSUMPTION_SCROLLBAR_X;
        int gatheredScrollbarX = this.leftPos + GATHERED_SCROLLBAR_X;

        blit(stack, consumptionScrollbarX, scrollbarYStart + (int)((float)(scrollbarYEnd - scrollbarYStart - SCROLLBAR_Y) * this.menu.getScrollOffs()), SCROLLER_BG_X + (this.menu.canScroll() ? 0 : SCROLLER_WIDTH), SCROLLER_BG_Y, SCROLLER_WIDTH, SCROLLER_HEIGHT);
        blit(stack, gatheredScrollbarX, scrollbarYStart + (int)((float)(scrollbarYEnd - scrollbarYStart - SCROLLBAR_Y) * this.menu.getGatheredScrollOffs()), SCROLLER_BG_X + (this.menu.canScroll() ? 0 : SCROLLER_WIDTH), SCROLLER_BG_Y, SCROLLER_WIDTH, SCROLLER_HEIGHT);
    }

    @Override
    protected void slotClicked(Slot slot, int slotId, int button, ClickType clickType) {
        if (!this.menu.isDrillHeadSlot(slotId)) {
            super.slotClicked(slot, slotId, button, clickType);
            return;
        }

        if (clickType == ClickType.QUICK_CRAFT) {
            System.out.println("Quick Crafting");
        } else if (clickType == ClickType.PICKUP) {
            ItemStack stack = slot.getItem().copy();
            ItemStack carried = this.menu.getCarried().copy();

            if (carried.isEmpty()) {
                if (button == 0) {
                    carried = stack.copy();
                    stack = ItemStack.EMPTY;
                } else if (button == 1) {
                    carried = stack.split(stack.getCount() / 2 + stack.getCount() % 2);
                    if (stack.isEmpty()) {
                        stack = ItemStack.EMPTY;
                    }
                }
            } else if (this.menu.isConsumptionSlot(slotId)) {
                if (stack.isEmpty()) {
                    stack = carried.copy();

                    if (button == 1) {
                        stack.setCount(1);
                        carried.shrink(1);
                    } else {
                        carried.shrink(stack.getCount());
                    }
                } else {
                    if (stack.sameItem(carried)) {
                        if (button == 0) {
                            if (stack.getCount() + carried.getCount() <= stack.getMaxStackSize()) {
                                stack.grow(carried.getCount());
                                carried = ItemStack.EMPTY;
                            } else {
                                int difference = stack.getMaxStackSize() - stack.getCount();
                                stack.grow(difference);
                                carried.shrink(difference);
                            }
                        } else if (button == 1) {
                            if (stack.getCount() < stack.getMaxStackSize()) {
                                stack.grow(1);
                                carried.shrink(1);
                            }
                        }
                    } else { // SWAP
                        ItemStack temp = stack.copy();
                        stack = carried.copy();
                        carried = temp;
                    }
                }
            }

            this.menu.syncCarried(carried);

            if (slot instanceof FakeSlot fakeSlot) {
                fakeSlot.setDisplayStack(stack);
            }

            this.menu.syncDrillHeadSlot(slotId, stack);
        } else if (clickType == ClickType.QUICK_MOVE) {
            ItemStack quickMoveReturnStack = this.menu.quickMoveStack(this.minecraft.player, slotId);
            this.menu.syncDrillHeadSlot(slotId, slot.remove(quickMoveReturnStack.getCount()));
        } else if (clickType == ClickType.SWAP) {
            System.out.println("Swapping");
        } else if (clickType == ClickType.CLONE && this.minecraft.player.getAbilities().instabuild && this.menu.getCarried().isEmpty() && slotId >= 0) {
            if (slot.hasItem()) {
                ItemStack itemStack = slot.getItem().copy();
                itemStack.setCount(itemStack.getMaxStackSize());
                this.menu.syncCarried(itemStack);
            }
        } else if (clickType == ClickType.THROW && this.menu.getCarried().isEmpty() && slotId >= 0) {
            System.out.println("Throwing");
        } else if (clickType == ClickType.PICKUP_ALL && slotId >= 0) {
            System.out.println("Picking up all");
        }
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

    @Override
    protected void scrollTo(float scrollOffs) {
        this.consumptionScrollTo(scrollOffs);
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
            this.menu.setScrolling(false);
            this.menu.setGatheredScrolling(false);
        }

        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            if (this.insideGatheredScrollbar(mouseX, mouseY)) {
                this.menu.setGatheredScrolling(this.menu.canScroll());
                return true;
            } else if (this.insideConsumptionScrollbar(mouseX, mouseY)) {
                this.menu.setScrolling(this.menu.canScroll());
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
            this.menu.setGatheredScrollOffs(Mth.clamp(this.menu.getGatheredScrollOffs() - f, 0.0F, 1.0F));
            this.gatheredScrollTo(this.menu.getGatheredScrollOffs());
            return true;
        } else if (this.insideConsumptionScrollArea(mouseX, mouseY)) {
            int i = 3;
            float f = (float)(delta / (double)i);
            this.menu.setScrollOffs(Mth.clamp(this.menu.getScrollOffs() - f, 0.0F, 1.0F));
            this.consumptionScrollTo(this.menu.getScrollOffs());
            return true;
        }

        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (this.menu.isScrolling()) {
            int i = this.topPos + SCROLLBAR_Y;
            int j = i + SCROLLBAR_HEIGHT;
            super.menu.setScrollOffs(((float)mouseY - (float)i - 7.5F) / ((float)(j - i) - 15.0F));
            super.menu.setScrollOffs(Mth.clamp(this.menu.getScrollOffs(), 0.0F, 1.0F));
            this.consumptionScrollTo(this.menu.getScrollOffs());
            return true;
        }

        if (this.menu.isGatheredScrolling()) {
            int i = this.topPos + SCROLLBAR_Y;
            int j = i + SCROLLBAR_HEIGHT;
            this.menu.setGatheredScrollOffs(((float)mouseY - (float)i - 7.5F) / ((float)(j - i) - 15.0F));
            this.menu.setGatheredScrollOffs(Mth.clamp(this.menu.getGatheredScrollOffs(), 0.0F, 1.0F));
            this.gatheredScrollTo(this.menu.getGatheredScrollOffs());
            return true;
        }

        /* Disabled dragging of items
        if (getSlotUnderMouse() != null && this.menu.isDrillHeadSlot(getSlotUnderMouse().index)) {
            slotClicked(getSlotUnderMouse(), getSlotUnderMouse().index, button, ClickType.QUICK_CRAFT);
            return true;
        }*/

        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }
}
