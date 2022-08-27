package the_fireplace.caterpillar.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.SimpleContainerData;
import the_fireplace.caterpillar.client.screen.util.ScreenTabs;
import the_fireplace.caterpillar.common.block.entity.DrillHeadBlockEntity;
import the_fireplace.caterpillar.common.container.CaterpillarContainer;
import the_fireplace.caterpillar.common.container.DrillHeadContainer;
import the_fireplace.caterpillar.common.container.syncdata.CaterpillarContainerData;

public class CaterpillarScreen extends AbstractContainerScreen<CaterpillarContainer> {

    private float scrollOffs;

    public CaterpillarScreen(CaterpillarContainer container, Inventory playerInventory, Component title) {
        super(container, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = this.menu.getSelectedTab() == ScreenTabs.REINFORCEMENT ? 189 : 166;
        this.leftPos = 0;
        this.topPos = 0;
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        super.render(stack, mouseX, mouseY, partialTicks);
        this.renderTooltip(stack, mouseX, mouseY);
        this.renderTabButtons(stack, mouseX, mouseY);
        this.renderSlots();
    }

    @Override
    protected void renderLabels(PoseStack stack, int mouseX, int mouseY) {
        Component title = this.menu.getSelectedTab().name;
        this.titleLabelX = (this.imageWidth - this.font.width(title)) / 2;
        this.titleLabelY = 6;

        switch (this.menu.getSelectedTab()) {
            case DRILL_HEAD:
                int consumptionX = 112, consumptionY = 6, gatheredX = 11, gatheredY = 6;
                this.titleLabelY = -10;

                this.font.draw(stack, DrillHeadBlockEntity.GATHERED, gatheredX, gatheredY, 0x404040);
                this.font.draw(stack, title, titleLabelX, titleLabelY, 0xFFFFFF);
                this.font.draw(stack, DrillHeadBlockEntity.CONSUMPTION, consumptionX, consumptionY, 0x404040);
                this.font.draw(stack, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 0x404040);
                break;
            case DECORATION:
                this.font.draw(stack, title, this.titleLabelX, this.titleLabelY, 0x404040);
                this.font.draw(stack, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 0x404040);
                break;
            case INCINERATOR:
                this.font.draw(stack, title, this.titleLabelX, this.titleLabelY, 0x404040);
                this.font.draw(stack, this.playerInventoryTitle, (float)this.inventoryLabelX, (float)this.inventoryLabelY, 0x404040);
                break;
            case REINFORCEMENT:
                this.titleLabelY = -10;

                this.font.draw(stack, title, titleLabelX, titleLabelY, 0xFFFFFF);
                break;
            default:
                this.titleLabelY = -10;

                this.font.draw(stack, ScreenTabs.DRILL_HEAD.name, titleLabelX, titleLabelY, 0xFFFFFF);
                break;
        }
    }

    @Override
    protected void renderBg(PoseStack stack, float mouseX, int mouseY, int partialTicks) {
        renderBackground(stack);
        bindTexture();

        this.imageHeight = this.menu.getSelectedTab() == ScreenTabs.REINFORCEMENT ? 189 : 166;
        blit(stack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        if(this.menu.getSelectedTab() == ScreenTabs.DECORATION) {
            renderScrollBar(stack);
        }
    }

    private void renderScrollBar(PoseStack stack) {
        int i = this.leftPos + 156;
        int j = this.topPos + 17;
        int k = j + 54;
        blit(stack, i, j + (int)((float)(k - j - 17) * this.scrollOffs), 176, 0, 12, 15);


        int m = 9;
        int n = (int)((double)(scrollOffs * (float)m) + 0.5D);
        this.font.draw(stack, "" + n, this.leftPos + 31, this.topPos + 39, 0x404040);
    }

    private void renderTabButtons(PoseStack stack, int mouseX, int mouseY) {
        clearWidgets();

        // itemRenderer.renderGuiItem(new ItemStack(BlockInit.DECORATION.get().asItem()), this.imageWidth - 50, this.topPos + 56);

        for (ScreenTabs tab: ScreenTabs.values()) {
            // TODO: checkShowTab doesn't work
            //  if (checkShowTab(tab)) {
            if (true) {
                String tabName = tab.name.getString();

                if (tabName.length() > 5) {
                    tabName = tabName.substring(0, 3) + "...";
                }

                if (this.menu.getSelectedTab().equals(tab)) {
                    this.addRenderableWidget(new ImageButton(this.leftPos - 28, this.topPos + 3 + tab.value*20, 31, 20, 176 , 58 + 20, 0, ScreenTabs.DRILL_HEAD.resourceLocation, (onPress) -> {
                        this.menu.setSelectedTab(tab);
                    }));

                    this.font.draw(stack, tabName, this.leftPos - 22, this.topPos + tab.value*20 + 9, ChatFormatting.BLACK.getColor());
                } else {
                    this.addRenderableWidget(new ImageButton(this.leftPos - 31, this.topPos + tab.value*20 + 3 , 31, 20, 176 , 58, 0, ScreenTabs.DRILL_HEAD.resourceLocation, (onPress) -> {
                        this.menu.setSelectedTab(tab);
                    }));

                    this.font.draw(stack, tabName, this.leftPos - 25, this.topPos + tab.value*20 + 9, ChatFormatting.GRAY.getColor());
                }

                if(mouseX >= this.leftPos - 31 && mouseY >= this.topPos + tab.value*20 + 3 && mouseX <= this.leftPos && mouseY <= this.topPos + tab.value*20 + 3 + 20) {
                    this.renderTooltip(stack, tab.name, this.leftPos - 15, this.topPos + tab.value*20 + 21);
                }
            }
        }
    }

    private boolean checkShowTab(ScreenTabs tab) {
        switch (tab) {
            case DRILL_HEAD:
                System.out.println("DRILL HEAD: " + this.menu.drillHead);
                return this.menu.drillHead != null;
            case DECORATION:
                return this.menu.decoration != null;
            case INCINERATOR:
                return this.menu.incinerator != null;
            case REINFORCEMENT:
                return this.menu.reinforcement != null;
            default:
                return false;
        }
    }

    private void renderSlots() {
        switch (this.menu.getSelectedTab()) {
            case DECORATION:
                // this.menu.placeSlotsDecoration();
                break;
            case INCINERATOR:
                // this.menu.placeSlotsIncinerator();
                break;
            case REINFORCEMENT:
                // this.menu.placeSlotsReinforcement();
                break;
            default:
                // this.menu.placeSlotsDrillHead();
                break;
        }

        this.menu.placeSlotsInventory();
    }

    @Override
    public boolean mouseScrolled(double p_94686_, double p_94687_, double p_94688_) {
        if (this.menu.getSelectedTab() == ScreenTabs.DECORATION) {
            int i = 9;
            float f = (float)(p_94688_ / (double)i);
            this.scrollOffs = Mth.clamp(this.scrollOffs - f, 0.0F, 1.0F);
            // this.container.decoration.scrollTo(this.scrollOffs);
            return true;
        }

        return false;
    }

    private void bindTexture() {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, this.menu.getSelectedTab().resourceLocation);
    }
}
