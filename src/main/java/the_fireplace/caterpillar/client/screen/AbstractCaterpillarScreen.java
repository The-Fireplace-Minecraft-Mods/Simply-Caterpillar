package the_fireplace.caterpillar.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import the_fireplace.caterpillar.client.screen.util.ScreenTabs;
import the_fireplace.caterpillar.common.block.entity.*;
import the_fireplace.caterpillar.common.menu.AbstractCaterpillarMenu;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public abstract class AbstractCaterpillarScreen<T extends AbstractCaterpillarMenu> extends AbstractContainerScreen<T> {

    public static final int SLOT_SIZE = 18;

    private final ScreenTabs SELECTED_TAB;

    private List<AbstractCaterpillarBlockEntity> caterpillarBlockEntities;

    public AbstractCaterpillarScreen(T menu, Inventory playerInventory, Component title, ScreenTabs selectedTab) {
        super(menu, playerInventory, title);
        this.SELECTED_TAB = selectedTab;
        super.imageWidth = SELECTED_TAB.IMAGE_WIDTH;
        super.imageHeight = SELECTED_TAB.IMAGE_HEIGHT;
        // super.titleLabelX = (super.imageWidth - super.font.width(this.title)) / 2;

        // this.caterpillarBlockEntities = caterpillarBlockEntities;
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        super.render(stack, mouseX, mouseY, partialTicks);

        this.renderTooltip(stack, mouseX, mouseY);
        this.renderTooltipTabButtons(stack, mouseX, mouseY);
        this.renderBgTabButtons();
    }

    @Override
    protected void renderBg(PoseStack stack, float partialTick, int mouseX, int mouseY) {
        super.renderBackground(stack);
        this.bindTexture();

        blit(stack, super.leftPos, super.topPos, 0, 0, super.imageWidth, super.imageHeight);
    }

    @Override
    protected void renderLabels(PoseStack stack, int mouseX, int mouseY) {
        this.font.draw(stack, this.SELECTED_TAB.TITLE, super.titleLabelX, super.titleLabelY, 0x404040);
        this.font.draw(stack, super.playerInventoryTitle, super.inventoryLabelX, super.inventoryLabelY, 0x404040);
    }

    private void renderBgTabButtons() {
        int incrementTabPos = 0;

        for (ScreenTabs tab: ScreenTabs.values()) {
            if (true) {
            // if (tabShouldBeDisplayed(tab)) {
                if (this.SELECTED_TAB.equals(tab)) {
                    this.addRenderableWidget(new ImageButton(this.leftPos - 28, this.topPos + 3 + incrementTabPos*20, 31, 20, 176 , 58 + 20, 0, ScreenTabs.DRILL_HEAD.TEXTURE, (onPress) -> {
                        // TODO: this.serverContainer.setSelectedTab(tab);
                    }));
                    this.renderFloatingItem(tab.ITEM,this.leftPos - 21, this.topPos + 5 + incrementTabPos*20);
                } else {
                    this.addRenderableWidget(new ImageButton(this.leftPos - 31, this.topPos + 3 + incrementTabPos*20, 31, 20, 176 , 58, 0, ScreenTabs.DRILL_HEAD.TEXTURE, (onPress) -> {
                        // TODO: this.serverContainer.setSelectedTab(tab);
                    }));
                    this.renderFloatingItem(tab.ITEM, this.leftPos - 23, this.topPos + 5 + incrementTabPos*20);
                }

                incrementTabPos++;
            }
        }
    }

    private void renderFgTabButtons(PoseStack stack) {
        int incrementTabPos = 0;

        for (ScreenTabs tab: ScreenTabs.values()) {
            // if (tabShouldBeDisplayed(tab)) {
            if (true) {
                String tabName = tab.TITLE.getString();

                if (tabName.length() > 5) {
                    tabName = tabName.substring(0, 3) + "...";
                }

                if (this.SELECTED_TAB.equals(tab)) {

                    this.font.draw(stack, tabName, this.leftPos - 22, this.topPos + incrementTabPos * 20 + 9, ChatFormatting.BLACK.getColor());

                } else {
                    this.font.draw(stack, tabName, this.leftPos - 25, this.topPos + incrementTabPos * 20 + 9, ChatFormatting.GRAY.getColor());
                }

                incrementTabPos++;
            }
        }
    }

    private void renderTooltipTabButtons(PoseStack stack, int mouseX, int mouseY) {
        int incrementTabPos = 0;

        for (ScreenTabs tab: ScreenTabs.values()) {
            // if (tabShouldBeDisplayed(tab)) {
            if (true) {
                if (mouseX >= this.leftPos - 31 && mouseY >= this.topPos + incrementTabPos * 20 + 3 && mouseX <= this.leftPos && mouseY <= this.topPos + incrementTabPos * 20 + 3 + 20) {
                    this.renderTooltip(stack, tab.TITLE, this.leftPos - 15, this.topPos + incrementTabPos * 20 + 21);
                }

                incrementTabPos++;
            }
        }
    }

    // TODO: Check if the tab should be displayed
    private boolean tabShouldBeDisplayed(ScreenTabs tab) {
        switch (tab) {
            case DECORATION: {
                if (caterpillarBlockEntities.contains(DecorationBlockEntity.class)) {
                    return true;
                }

                return false;
            }
            case REINFORCEMENT: {
                if (caterpillarBlockEntities.contains(ReinforcementBlockEntity.class)) {
                    return true;
                }

                return false;
            }
            case INCINERATOR: {
                if (caterpillarBlockEntities.contains(IncineratorBlockEntity.class)) {
                    return true;
                }

                return false;
            }
            case DRILL_HEAD:
            default: {
                return true;
            }
        }
    }

    private void renderFloatingItem(ItemStack pStack, int x, int y) {
        PoseStack posestack = RenderSystem.getModelViewStack();
        posestack.translate(0.0D, 0.0D, 32.0D);
        RenderSystem.applyModelViewMatrix();
        super.setBlitOffset(200);
        super.itemRenderer.blitOffset = 200.0F;
        var font = net.minecraftforge.client.extensions.common.IClientItemExtensions.of(pStack).getFont(pStack, net.minecraftforge.client.extensions.common.IClientItemExtensions.FontContext.ITEM_COUNT);
        if (font == null) font = this.font;
        super.itemRenderer.renderAndDecorateItem(pStack, x, y);
        super.itemRenderer.renderGuiItemDecorations(font, pStack, x, y -  0);
        super.setBlitOffset(0);
        super.itemRenderer.blitOffset = 0.0F;
    }

    protected void bindTexture() {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, SELECTED_TAB.TEXTURE);
    }
}
