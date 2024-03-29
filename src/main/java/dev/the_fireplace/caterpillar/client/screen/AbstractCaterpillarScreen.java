package dev.the_fireplace.caterpillar.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.the_fireplace.caterpillar.Caterpillar;
import dev.the_fireplace.caterpillar.block.entity.*;
import dev.the_fireplace.caterpillar.block.util.CaterpillarBlockUtil;
import dev.the_fireplace.caterpillar.client.screen.util.ScreenTabs;
import dev.the_fireplace.caterpillar.client.screen.widget.TabButton;
import dev.the_fireplace.caterpillar.client.screen.widget.TutorialButton;
import dev.the_fireplace.caterpillar.menu.AbstractCaterpillarMenu;
import dev.the_fireplace.caterpillar.network.PacketHandler;
import dev.the_fireplace.caterpillar.network.packet.client.CaterpillarSyncSelectedTabC2SPacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.DisplayRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static dev.the_fireplace.caterpillar.block.DrillBaseBlock.FACING;

@OnlyIn(Dist.CLIENT)
public abstract class AbstractCaterpillarScreen<T extends AbstractCaterpillarMenu> extends AbstractContainerScreen<T> {

    public static final int SLOT_SIZE = 18;

    public static final ResourceLocation CATERPILLAR_GUI = new ResourceLocation(Caterpillar.MOD_ID, "textures/gui/caterpillar.png");

    public static final int TAB_WIDTH = 31;

    public static final int TAB_HEIGHT = 20;

    public static final int TAB_X_SELECTED = -28;

    public static final int TAB_X_UNSELECTED = -31;

    public static final int TAB_Y = 3;

    public static final int TAB_BG_X = 0;

    public static final int TAB_BG_Y = 0;
    public static final int TAB_BG_X_OFFSET = -2;
    public static final int TAB_BG_Y_OFFSET = TAB_HEIGHT;

    protected final ScreenTabs SELECTED_TAB;

    final List<TabButton> tabButtons = new ArrayList<>();

    public final int TUTORIAL_WIDTH = 14;

    public final int TUTORIAL_HEIGHT = 18;

    public final int TUTORIAL_X = -11;

    public final int TUTORIAL_Y = -TUTORIAL_HEIGHT - 6;

    public final int TUTORIAL_BG_Y_OFFSET = TUTORIAL_HEIGHT;

    public final int TUTORIAL_BG_X = 0;

    public final int TUTORIAL_BG_Y = 41;

    public TutorialButton tutorialButton;

    public AbstractCaterpillarScreen(T menu, Inventory playerInventory, Component title, ScreenTabs selectedTab) {
        super(menu, playerInventory, title);
        this.SELECTED_TAB = selectedTab;
        super.imageWidth = SELECTED_TAB.IMAGE_WIDTH;
        super.imageHeight = SELECTED_TAB.IMAGE_HEIGHT;
    }

    @Override
    protected void init() {
        super.init();

        super.titleLabelX = (super.imageWidth - super.font.width(title)) / 2;

        this.addTabButtons();
        this.addTutorialButton();
    }

    @Override
    protected void containerTick() {
        super.containerTick();

        this.updateTabButtons();
    }

    protected void updateTabButtons() {
        Direction direction = this.menu.blockEntity.getBlockState().getValue(FACING);

        BlockPos caterpillarHeadPos = CaterpillarBlockUtil.getCaterpillarHeadPos(this.menu.blockEntity.getLevel(), this.menu.blockEntity.getBlockPos(), direction);
        List<DrillBaseBlockEntity> connectedCaterpillarBlockEntities = CaterpillarBlockUtil.getConnectedCaterpillarBlockEntities(this.menu.blockEntity.getLevel(), caterpillarHeadPos, new ArrayList<>());
        this.menu.setConnectedCaterpillarBlockEntities(connectedCaterpillarBlockEntities);

        this.addTabButtons();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(graphics);

        super.render(graphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(graphics, mouseX, mouseY);
        this.renderTooltipTabButtons(graphics, mouseX, mouseY);
        this.renderTutorialTooltip(graphics, mouseX, mouseY);

        this.renderTabItems(graphics);
        this.renderTutorial(graphics);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        this.bindTexture();

        graphics.blit(SELECTED_TAB.TEXTURE, super.leftPos, super.topPos, 0, 0, super.imageWidth, super.imageHeight);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(this.font, this.SELECTED_TAB.TITLE, super.titleLabelX, super.titleLabelY, 4210752, false);
        graphics.drawString(this.font, super.playerInventoryTitle, super.inventoryLabelX, super.inventoryLabelY, 4210752, false);
    }

    private void renderTutorialTooltip(GuiGraphics graphics, int mouseX, int mouseY) {
        if (mouseX > super.leftPos + TUTORIAL_X &&
                mouseX < super.leftPos + TUTORIAL_X + TUTORIAL_WIDTH &&
                mouseY > super.topPos + this.SELECTED_TAB.IMAGE_HEIGHT + TUTORIAL_Y &&
                mouseY < super.topPos + this.SELECTED_TAB.IMAGE_HEIGHT + TUTORIAL_Y + TUTORIAL_HEIGHT
        ) {
            int tooltipX = super.leftPos - 6;
            int tooltipY = super.topPos + this.SELECTED_TAB.IMAGE_HEIGHT - 7;

            List<Component> tooltip = new ArrayList<>();
            MutableComponent tooltipText = Component.translatable(Caterpillar.MOD_ID + ".tutorial." + (this.tutorialButton != null && this.tutorialButton.isTutorialShown() ? "hide" : "show"));
            tooltip.add(tooltipText);
            graphics.renderComponentTooltip(this.font, tooltip, tooltipX, tooltipY);
        }
    }

    protected abstract void renderTutorial(GuiGraphics graphics);

    private void addTutorialButton() {
        this.tutorialButton = new TutorialButton(false, super.leftPos + TUTORIAL_X, super.topPos + SELECTED_TAB.IMAGE_HEIGHT + TUTORIAL_Y, TUTORIAL_WIDTH, TUTORIAL_HEIGHT, TUTORIAL_BG_X, TUTORIAL_BG_Y, TUTORIAL_BG_Y_OFFSET, CATERPILLAR_GUI, (onPress) -> this.tutorialButton.toggleTutorial());

        this.addRenderableWidget(this.tutorialButton);
    }

    void addTabButtons() {
        for (TabButton button : this.tabButtons) {
            button.visible = false;
            super.removeWidget(button);
        }

        int incrementTabPos = 0;
        for (ScreenTabs tab: ScreenTabs.values()) {
            if (tabShouldBeDisplayed(tab)) {
                this.tabButtons.add(new TabButton(this.SELECTED_TAB.equals(tab), super.leftPos + TAB_X_SELECTED, super.topPos + TAB_Y + incrementTabPos*TAB_HEIGHT, TAB_WIDTH, TAB_HEIGHT, TAB_BG_X, TAB_BG_Y, TAB_BG_X_OFFSET, TAB_BG_Y_OFFSET, CATERPILLAR_GUI, (onPress) -> PacketHandler.sendToServer(new CaterpillarSyncSelectedTabC2SPacket(tab, this.menu.blockEntity.getBlockPos()))));
                incrementTabPos++;
            }
        }

        for (TabButton button : this.tabButtons) {
            super.addRenderableWidget(button);
        }
    }

    private void renderTabItems(GuiGraphics graphics) {
        int incrementTabPos = 0;

        // super.itemRenderer.blitOffset = 100.0F;
        for (ScreenTabs tab : ScreenTabs.values()) {
            if (tabShouldBeDisplayed(tab)) {
                if (this.SELECTED_TAB.equals(tab)) {
                    graphics.renderItem(tab.ITEM, this.leftPos - 21, this.topPos + 5 + incrementTabPos * 20);
                } else {
                    graphics.renderItem(tab.ITEM, this.leftPos - 20, this.topPos + 5 + incrementTabPos * 20);
                }

                incrementTabPos++;
            }
        }
        // super.itemRenderer.blitOffset = 0.0F;
    }

    private void renderTooltipTabButtons(GuiGraphics graphics, int mouseX, int mouseY) {
        int incrementTabPos = 0;

        for (ScreenTabs tab: ScreenTabs.values()) {
            if (tabShouldBeDisplayed(tab)) {
                if (mouseX >= this.leftPos - 31 && mouseY >= this.topPos + incrementTabPos * 20 + 3 && mouseX <= this.leftPos && mouseY <= this.topPos + incrementTabPos * 20 + 3 + 20) {
                    graphics.renderTooltip(this.font, tab.TITLE, this.leftPos - 15, this.topPos + incrementTabPos * 20 + 21);
                }

                incrementTabPos++;
            }
        }
    }
    private boolean tabShouldBeDisplayed(ScreenTabs tab) {
        List<DrillBaseBlockEntity> connectedCaterpillarBlockEntities = this.menu.getConnectedCaterpillarBlockEntities();

        if (connectedCaterpillarBlockEntities.isEmpty()) {
            return false;
        }

        switch (tab) {
            case DECORATION -> {
                for (DrillBaseBlockEntity entity : connectedCaterpillarBlockEntities) {
                    if (entity instanceof DecorationBlockEntity) {
                        return true;
                    }
                }
                return false;
            }
            case REINFORCEMENT -> {
                for (DrillBaseBlockEntity entity : connectedCaterpillarBlockEntities) {
                    if (entity instanceof ReinforcementBlockEntity) {
                        return true;
                    }
                }
                return false;
            }
            case INCINERATOR -> {
                for (DrillBaseBlockEntity entity : connectedCaterpillarBlockEntities) {
                    if (entity instanceof IncineratorBlockEntity) {
                        return true;
                    }
                }
                return false;
            }
            case DRILL_HEAD -> {
                for (DrillBaseBlockEntity entity : connectedCaterpillarBlockEntities) {
                    if (entity instanceof DrillHeadBlockEntity) {
                        return true;
                    }
                }
                return false;
            }
            case TRANSPORTER -> {
                for (DrillBaseBlockEntity entity : connectedCaterpillarBlockEntities) {
                    if (entity instanceof TransporterBlockEntity) {
                        return true;
                    }
                }
                return false;
            }
            default -> {
                return false;
            }
        }
    }

    private void bindTexture() {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, SELECTED_TAB.TEXTURE);
    }
}
