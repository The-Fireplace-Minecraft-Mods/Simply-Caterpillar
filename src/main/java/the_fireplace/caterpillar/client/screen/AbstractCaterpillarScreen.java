package the_fireplace.caterpillar.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.client.screen.util.ScreenTabs;
import the_fireplace.caterpillar.common.block.entity.*;
import the_fireplace.caterpillar.common.menu.AbstractCaterpillarMenu;
import the_fireplace.caterpillar.core.network.PacketHandler;
import the_fireplace.caterpillar.core.network.packet.client.CaterpillarSyncSelectedTabC2SPacket;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public abstract class AbstractCaterpillarScreen<T extends AbstractCaterpillarMenu> extends AbstractContainerScreen<T> {

    public static final int SLOT_SIZE = 18;

    public static final ResourceLocation GUI_TABS = new ResourceLocation(Caterpillar.MOD_ID, "textures/gui/caterpillar.png");

    public static final int TAB_WIDTH = 31;

    public static final int TAB_HEIGHT = 20;

    public static final int TAB_X_SELECTED = -28;

    public static final int TAB_X_UNSELECTED = -31;

    public static final int TAB_Y = 3;

    public static final int TAB_BG_X_SELECTED = TAB_WIDTH;

    public static final int TAB_BG_X_UNSELECTED = 0;

    public static final int TAB_BG_Y = 0;

    private final ScreenTabs SELECTED_TAB;

    private final List<ImageButton> tabButtons = new ArrayList<>();

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
    public void render(@NotNull PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(stack);

        super.render(stack, mouseX, mouseY, partialTicks);
        this.renderTooltip(stack, mouseX, mouseY);
        this.renderTooltipTabButtons(stack, mouseX, mouseY);

        this.renderTabItems();
    }

    @Override
    protected void renderBg(@NotNull PoseStack stack, float partialTick, int mouseX, int mouseY) {
        this.bindTexture();

        blit(stack, super.leftPos, super.topPos, 0, 0, super.imageWidth, super.imageHeight);
    }

    @Override
    protected void renderLabels(@NotNull PoseStack stack, int mouseX, int mouseY) {
        this.font.draw(stack, this.SELECTED_TAB.TITLE, super.titleLabelX, super.titleLabelY, 0x404040);
        this.font.draw(stack, super.playerInventoryTitle, super.inventoryLabelX, super.inventoryLabelY, 0x404040);
    }

    private void addTutorialButton() {
    }

    private void addTabButtons() {
        for (ImageButton button : this.tabButtons) {
            removeWidget(button);
        }

        int incrementTabPos = 0;
        for (ScreenTabs tab: ScreenTabs.values()) {
            if (tabShouldBeDisplayed(tab)) {
                if (this.SELECTED_TAB.equals(tab)) {
                    this.tabButtons.add(new ImageButton(super.leftPos + TAB_X_SELECTED, super.topPos + TAB_Y + incrementTabPos*TAB_HEIGHT, TAB_WIDTH, TAB_HEIGHT, TAB_BG_X_SELECTED, TAB_BG_Y, 0, GUI_TABS, (onPress) -> {

                    }));
                } else {
                    this.tabButtons.add(new ImageButton(super.leftPos + TAB_X_UNSELECTED, super.topPos + TAB_Y + incrementTabPos*TAB_HEIGHT, TAB_WIDTH, TAB_HEIGHT, TAB_BG_X_UNSELECTED, TAB_BG_Y, 0, GUI_TABS, (onPress) -> PacketHandler.sendToServer(new CaterpillarSyncSelectedTabC2SPacket(tab, this.menu.blockEntity.getBlockPos()))));
                }

                incrementTabPos++;
            }
        }

        for (ImageButton button : this.tabButtons) {
            this.addRenderableWidget(button);
        }
    }

    private void renderTabItems() {
        int incrementTabPos = 0;

        super.itemRenderer.blitOffset = 100.0F;
        for (ScreenTabs tab: ScreenTabs.values()) {
            if (tabShouldBeDisplayed(tab)) {
                if (this.SELECTED_TAB.equals(tab)) {
                    super.itemRenderer.renderAndDecorateItem(tab.ITEM,this.leftPos - 21, this.topPos + 5 + incrementTabPos*20);
                } else {
                    super.itemRenderer.renderAndDecorateItem(tab.ITEM, this.leftPos - 23, this.topPos + 5 + incrementTabPos*20);
                }

                incrementTabPos++;
            }
        }
        super.itemRenderer.blitOffset = 0.0F;
    }

    private void renderTooltipTabButtons(PoseStack stack, int mouseX, int mouseY) {
        int incrementTabPos = 0;

        for (ScreenTabs tab: ScreenTabs.values()) {
            if (tabShouldBeDisplayed(tab)) {
                if (mouseX >= this.leftPos - 31 && mouseY >= this.topPos + incrementTabPos * 20 + 3 && mouseX <= this.leftPos && mouseY <= this.topPos + incrementTabPos * 20 + 3 + 20) {
                    this.renderTooltip(stack, tab.TITLE, this.leftPos - 15, this.topPos + incrementTabPos * 20 + 21);
                }

                incrementTabPos++;
            }
        }
    }
    private boolean tabShouldBeDisplayed(ScreenTabs tab) {
        if (this.menu.getConnectedCaterpillarBlockEntities().isEmpty()) {
            return false;
        }

        switch (tab) {
            case DECORATION -> {
                for (AbstractCaterpillarBlockEntity entity : this.menu.getConnectedCaterpillarBlockEntities()) {
                    if (entity instanceof DecorationBlockEntity) {
                        return true;
                    }
                }
                return false;
            }
            case REINFORCEMENT -> {
                for (AbstractCaterpillarBlockEntity entity : this.menu.getConnectedCaterpillarBlockEntities()) {
                    if (entity instanceof ReinforcementBlockEntity) {
                        return true;
                    }
                }
                return false;
            }
            case INCINERATOR -> {
                for (AbstractCaterpillarBlockEntity entity : this.menu.getConnectedCaterpillarBlockEntities()) {
                    if (entity instanceof IncineratorBlockEntity) {
                        return true;
                    }
                }
                return false;
            }
            case DRILL_HEAD -> {
                for (AbstractCaterpillarBlockEntity entity : this.menu.getConnectedCaterpillarBlockEntities()) {
                    if (entity instanceof DrillHeadBlockEntity) {
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
