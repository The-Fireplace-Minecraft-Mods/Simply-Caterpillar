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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import the_fireplace.caterpillar.client.screen.util.ScreenTabs;
import the_fireplace.caterpillar.common.block.entity.DrillHeadBlockEntity;
import the_fireplace.caterpillar.common.menu.CaterpillarMenu;

@OnlyIn(Dist.CLIENT)
public class CaterpillarScreen extends AbstractContainerScreen<CaterpillarMenu> {

    private CaterpillarMenu serverContainer;

    private float scrollOffs;

    private boolean scrolling;


    private static final int SCROLLER_WIDTH = 17;
    private static final int SCROLLER_HEIGHT = 54;

    public CaterpillarScreen(CaterpillarMenu container, Inventory playerInventory, Component title) {
        super(container, playerInventory, title);
    }

    @Override
    protected void init() {
        super.init();
        this.serverContainer = CaterpillarMenu.getServerContainer();
        super.imageWidth = 176;
        super.imageHeight = isReinforcementTab() ? 189 : 166;
    }
    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        super.render(stack, mouseX, mouseY, partialTicks);
        this.renderTooltip(stack, mouseX, mouseY);
        this.renderTabButtons(stack, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(PoseStack stack, int mouseX, int mouseY) {
        Component title = this.serverContainer.getSelectedTab().name;
        super.titleLabelX = (this.imageWidth - this.font.width(title)) / 2;
        super.titleLabelY = isReinforcementTab() || isDrillHeadTab() ? -10 : 6;
        super.inventoryLabelY = super.imageHeight - 94;
        int titleLabelColor = isReinforcementTab() || isDrillHeadTab() ? 0xFFFFFF : 0x404040;

        switch (this.serverContainer.getSelectedTab()) {
            case DECORATION:
            case INCINERATOR:
            case REINFORCEMENT:
                this.font.draw(stack, title, super.titleLabelX, super.titleLabelY, titleLabelColor);
                this.font.draw(stack, super.playerInventoryTitle, super.inventoryLabelX, super.inventoryLabelY, 0x404040);
                break;
            case DRILL_HEAD:
                int consumptionX = 4, consumptionY = 6, gatheredX = 119, gatheredY = 6;

                this.font.draw(stack, DrillHeadBlockEntity.CONSUMPTION, consumptionX, consumptionY, 0x404040);
                this.font.draw(stack, DrillHeadBlockEntity.GATHERED, gatheredX, gatheredY, 0x404040);
            default:
                this.font.draw(stack, title, super.titleLabelX, super.titleLabelY, titleLabelColor);
                this.font.draw(stack, super.playerInventoryTitle, super.inventoryLabelX, super.inventoryLabelY, 0x404040);
                break;
        }
    }

    @Override
    protected void renderBg(PoseStack stack, float partialTicks, int mouseX, int mouseY) {
        super.renderBackground(stack);
        bindTexture();

        super.imageHeight = isReinforcementTab() ? 189 : 166;
        blit(stack, super.leftPos, super.topPos, 0, 0, super.imageWidth, super.imageHeight);

        if(isDecorationTab()) {
            renderScrollBar(stack);
        }
    }
    private void renderScrollBar(PoseStack stack) {
        int i = this.leftPos + 156;
        int j = this.topPos + SCROLLER_WIDTH;
        int k = j + SCROLLER_HEIGHT;
        blit(stack, i, j + (int)((float)(k - j - SCROLLER_WIDTH) * this.scrollOffs), 176, 0, 12, 15);

        int m = 9;
        int n = (int)((double)(scrollOffs * (float)m) + 0.5D);
        this.font.draw(stack, "" + n, this.leftPos + 31, this.topPos + 39, 0x404040);
    }

    private void renderTabButtons(PoseStack stack, int mouseX, int mouseY) {
        clearWidgets();

        int incrementTabPos = 0;

        // TODO: Display items instead of tab names
        // itemRenderer.renderGuiItem(new ItemStack(BlockInit.DECORATION.get().asItem()), this.imageWidth - 50, this.topPos + 56);

        for (ScreenTabs tab: ScreenTabs.values()) {
            if (checkShowTab(tab)) {
                String tabName = tab.name.getString();

                if (tabName.length() > 5) {
                    tabName = tabName.substring(0, 3) + "...";
                }

                if (this.serverContainer.getSelectedTab().equals(tab)) {
                    this.addRenderableWidget(new ImageButton(this.leftPos - 28, this.topPos + 3 + incrementTabPos*20, 31, 20, 176 , 58 + 20, 0, ScreenTabs.DRILL_HEAD.resourceLocation, (onPress) -> {
                        this.serverContainer.setSelectedTab(tab);
                    }));

                    this.font.draw(stack, tabName, this.leftPos - 22, this.topPos + incrementTabPos*20 + 9, ChatFormatting.BLACK.getColor());
                } else {
                    this.addRenderableWidget(new ImageButton(this.leftPos - 31, this.topPos + incrementTabPos*20 + 3 , 31, 20, 176 , 58, 0, ScreenTabs.DRILL_HEAD.resourceLocation, (onPress) -> {
                        this.serverContainer.setSelectedTab(tab);
                    }));

                    this.font.draw(stack, tabName, this.leftPos - 25, this.topPos + incrementTabPos*20 + 9, ChatFormatting.GRAY.getColor());
                }

                if(mouseX >= this.leftPos - 31 && mouseY >= this.topPos + incrementTabPos*20 + 3 && mouseX <= this.leftPos && mouseY <= this.topPos + incrementTabPos*20 + 3 + 20) {
                    this.renderTooltip(stack, tab.name, this.leftPos - 15, this.topPos + incrementTabPos*20 + 21);
                }

                incrementTabPos++;
            }
        }
    }

    private boolean checkShowTab(ScreenTabs tab) {
        switch (tab) {
            case DRILL_HEAD:
                return this.serverContainer.getDrillHead() != null;
            case DECORATION:
                return this.serverContainer.getDecoration() != null;
            case INCINERATOR:
                return this.serverContainer.getIncinerator() != null;
            case REINFORCEMENT:
                return this.serverContainer.getReinforcement() != null;
            default:
                return false;
        }
    }

    private boolean isReinforcementTab() {
        return this.serverContainer.getSelectedTab() == ScreenTabs.REINFORCEMENT;
    }

    private boolean isDrillHeadTab() {
        return this.serverContainer.getSelectedTab() == ScreenTabs.DRILL_HEAD;
    }

    private boolean isDecorationTab() {
        return this.serverContainer.getSelectedTab() == ScreenTabs.DECORATION;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (this.serverContainer.getSelectedTab() == ScreenTabs.DECORATION) {
            if (button == 0) {
                this.scrolling = false;
                return true;
            }
        }

        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.serverContainer.getSelectedTab() == ScreenTabs.DECORATION) {
            if (button == 0) {
                if (this.insideScrollBar(mouseX, mouseY)) {
                    this.scrolling = true;
                    return true;
                }
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    private boolean insideScrollBar(double mouseX, double mouseY) {
        int i = this.leftPos + 156;
        int j = this.topPos + SCROLLER_WIDTH;
        int k = j + SCROLLER_HEIGHT;
        return mouseX >= (double)i && mouseY >= (double)j && mouseX < (double)i + 12 && mouseY < (double)k;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (this.serverContainer.getSelectedTab() == ScreenTabs.DECORATION) {
            if (this.scrolling) {
                int i = this.topPos + SCROLLER_WIDTH;
                int j = i + SCROLLER_HEIGHT;
                this.scrollOffs = ((float)mouseY - (float)i - 7.5F) / ((float)(j - i) - 15.0F);
                this.scrollOffs = Mth.clamp(this.scrollOffs, 0.0F, 1.0F);
                // this.serverContainer.scrollTo(this.scrollOffs);
                return true;
            }

            return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
        }

        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (this.serverContainer.getSelectedTab() == ScreenTabs.DECORATION) {
            int i = 9;
            float f = (float)(delta / (double)i);
            this.scrollOffs = Mth.clamp(this.scrollOffs - f, 0.0F, 1.0F);
            // this.serverContainer.decoration.scrollTo(this.scrollOffs);
            return true;
        }

        return false;
    }
     private void bindTexture() {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, this.serverContainer.getSelectedTab().resourceLocation);
    }
}
