package the_fireplace.caterpillar.client.guis;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.gui.GuiUtils;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.containers.CaterpillarData;
import the_fireplace.caterpillar.containers.DrillHeadContainer;
import the_fireplace.caterpillar.parts.PartsGuiWidgets;
import the_fireplace.caterpillar.parts.PartsTutorial;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class DrillHeadScreen extends ContainerScreen<DrillHeadContainer> {

    // The inventory contained within the corresponding Dispenser.
    public IInventory dispenserInventory;
    private CaterpillarData caterpillar;
    private HashMap<GUI_TABS, List<PartsGuiWidgets>> widgetsHolder;
    private List<PartsGuiWidgets> selectedWidgets;
    private PartsTutorial tutorial;

    public enum GUI_TABS {
        MAIN((byte)0, I18n.format(Caterpillar.MOD_ID.concat(".gui.main")), false, new ResourceLocation(Caterpillar.MOD_ID, "textures/gui/container/caterpillar.png")),
        DECORATION((byte)1, I18n.format(Caterpillar.MOD_ID + "gui.main"), false, new ResourceLocation(Caterpillar.MOD_ID, "textures/gui/container/decoration.png")),
        REINFORCEMENT((byte)2, I18n.format(Caterpillar.MOD_ID + "gui.main"), false, new ResourceLocation(Caterpillar.MOD_ID, "textures/gui/container/reinforcement.png")),
        INCINERATOR((byte)3, I18n.format(Caterpillar.MOD_ID + "gui.main"), false, new ResourceLocation(Caterpillar.MOD_ID, "textures/gui/container/incinerator.png"));

        public final byte value;
        public final String name;
        public final boolean isCrafting;
        public final ResourceLocation guiTextures;

        GUI_TABS(byte value, String name, boolean isCrafting, ResourceLocation guiTextures) {
            this.value = value;
            this.name = name;
            this.isCrafting = isCrafting;
            this.guiTextures = guiTextures;
        }
    }

    public DrillHeadScreen(final DrillHeadContainer container, final PlayerInventory inventory, final ITextComponent title) {
        super(container, inventory, title);
    }

    /* TODO: Look if needed
    public DrillHeadScreen(final DrillHeadContainer container, final PlayerInventory inventory, IInventory dispenserInventory, CaterpillarData caterpillarData, ITextComponent title) {
        super(container, inventory, title);
        this.guiLeft = 0;
        this.guiTop = 0;
        this.xSize = 175;
        this.ySize = 165;
        this.dispenserInventory = dispenserInventory;
        this.widgetsHolder = new HashMap<>();
        // TODO: setupWidgets
        //this.setupWidgets();
        this.caterpillar = caterpillarData;
        this.caterpillar.tabs.selected = GUI_TABS.MAIN;
        // TODO: sendUpdates
        //this.sendUpdates();
    }
     */

    @Override
    public void render(final MatrixStack matrixStack, final int mouseX, final int mouseY, final float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    private boolean checkShowTabs(GUI_TABS tab) {
        boolean drawTab = true;

        if (tab.equals(GUI_TABS.DECORATION)) {
            if (this.caterpillar.decoration.howclose < 1) {
                drawTab = false;
            }
        }

        if (tab.equals(GUI_TABS.REINFORCEMENT)) {
            if (this.caterpillar.reinforcement.howclose < 1) {
                drawTab = false;
            }
        }

        if (tab.equals(GUI_TABS.INCINERATOR)) {
            if (this.caterpillar.incinerator.howclose < 1) {
                drawTab = false;
            }
        }

        return drawTab;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {
        for (GUI_TABS tab: GUI_TABS.values()) {
            boolean drawTab = this.checkShowTabs(tab);

            if (drawTab) {
                this.drawTabsForeground(tab, matrixStack);
            }
        }

        super.drawGuiContainerForegroundLayer(matrixStack, x, y);

        switch (this.caterpillar.tabs.selected) {
            case MAIN:
                this.drawGuiMainForegroundLayer(matrixStack);
                break;
            case DECORATION:
                this.drawGuiDecorationForegroundLayer(matrixStack);
                break;
            case REINFORCEMENT:
                this.drawGuiReinforcementForegroundLayer(matrixStack);
                break;
            case INCINERATOR:
                this.drawGuiIncineratorForegroundLayer(matrixStack);
                break;
            default:
                this.drawGuiMainForegroundLayer(matrixStack);
                break;
        }

        for (GUI_TABS tab: GUI_TABS.values()) {
            boolean drawTab = this.checkShowTabs(tab);

            if (drawTab) {
                this.drawTabsHover(tab, matrixStack);
            }
        }

        // TODO: draw tutorial
        this.drawTutorialHover();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        switch (this.caterpillar.tabs.selected) {
            case MAIN:
                this.drawGuiMainBackgroundLayer(matrixStack);
                break;
            case DECORATION:
                this.drawGuiDecorationBackgroundLayer(matrixStack);
                break;
            case REINFORCEMENT:
                this.drawGuiReinforcementBackgroundLayer(matrixStack);
                break;
            case INCINERATOR:
                this.drawGuiIncineratorBackgroundLayer(matrixStack);
                break;
            default:
                this.drawGuiMainBackgroundLayer(matrixStack);
                break;
        }

        for (GUI_TABS tab: GUI_TABS.values()) {
            boolean drawTab = this.checkShowTabs(tab);

            if (drawTab) {
                this.drawTabsBackground(tab, matrixStack);
            }
        }
    }

    private void drawGuiIncineratorBackgroundLayer(MatrixStack matrixStack) {
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        getMinecraft().getTextureManager().bindTexture(GUI_TABS.INCINERATOR.guiTextures);
        int x  = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.blit(matrixStack, x, y, 0, 0, this.xSize, this.ySize);
        GuiUtils.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize, 0.f);
    }

    private void drawGuiReinforcementBackgroundLayer(MatrixStack matrixStack) {
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        getMinecraft().getTextureManager().bindTexture(GUI_TABS.REINFORCEMENT.guiTextures);
        int x  = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.blit(matrixStack, x, y, 0, 0, this.xSize, this.ySize);
        GuiUtils.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize, 0.f);
    }

    private void drawGuiDecorationBackgroundLayer(MatrixStack matrixStack) {
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        getMinecraft().getTextureManager().bindTexture(GUI_TABS.DECORATION.guiTextures);
        int x  = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.blit(matrixStack, x, y, 0, 0, this.xSize, this.ySize);
        GuiUtils.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize, 0.f);
    }

    private void drawGuiMainBackgroundLayer(MatrixStack matrixStack) {
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        getMinecraft().getTextureManager().bindTexture(GUI_TABS.MAIN.guiTextures);
        int x  = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.blit(matrixStack, x, y, 0, 0, this.xSize, this.ySize);
        GuiUtils.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize, 0.f);
    }

    private void drawTutorialHover() {
    }

    private void drawTabsHover(GUI_TABS tabHoveredOver, MatrixStack matrixStack) {
        int i = (int)getMinecraft().mouseHelper.getMouseX() * this.width / getMinecraft().currentScreen.width;
        int j = this.height - (int)getMinecraft().mouseHelper.getMouseY() * this.height / getMinecraft().currentScreen.height - 1;
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;

        int XSide = k -31;
        int XWidth = 31;

        int YSide = 1 + 3 + tabHoveredOver.value*20;
        int YHeight = 20;

        if (i > XSide && i < XSide + YHeight) {
            if (j > YSide && j < YSide + YHeight) {
                ArrayList tmoString = new ArrayList<String>();
                tmoString.add(tabHoveredOver.name);
                // TODO: If any tooltip is needed for a tab, add it to tmoString here.
                // GuiUtils.drawHoveringText(matrixStack, tmoString, i - k, j - l);
            }
        }
    }

    public FontRenderer getFontRenderer()
    {
        return this.font;
    }

    private void drawTabsForeground(GUI_TABS tabToDraw, MatrixStack matrixStack) {
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        getMinecraft().getTextureManager().bindTexture(tabToDraw.guiTextures);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.blit(matrixStack, 0, 0, x, y, this.xSize, this.ySize);
        String Caption = tabToDraw.name;

        if (Caption.length() > 5) {
            Caption = Caption.substring(0, 3) + "...";
        }

        if (this.caterpillar.tabs.selected.equals(tabToDraw)) {
            this.font.drawString(matrixStack, Caption, -31 + 5, 3 + tabToDraw.value*20 + 5, Color.BLACK.getRGB());
        } else {
            this.font.drawString(matrixStack, Caption, -31 + 3, 3 + tabToDraw.value*20 + 5, Color.GRAY.getRGB());
        }
    }

    private void drawTabsBackground(GUI_TABS tabToDraw, MatrixStack matrixStack) {
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        getMinecraft().getTextureManager().bindTexture(GUI_TABS.MAIN.guiTextures);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.blit(matrixStack, 0, 0, x, y, this.xSize, this.ySize);

        if (this.caterpillar.tabs.selected.equals(tabToDraw)) {
            GuiUtils.drawTexturedModalRect(matrixStack, x - 31 + 3, y + 3 + tabToDraw.value*20, 176, 58 + 20, 31, 20, 0f);
        } else {
            GuiUtils.drawTexturedModalRect(matrixStack, x - 31, y + 3 + tabToDraw.value*20, 176, 58, 31, 20, 0f);
        }
    }

    private void drawGuiIncineratorForegroundLayer(MatrixStack matrixStack) {
    }

    private void drawGuiReinforcementForegroundLayer(MatrixStack matrixStack) {
    }

    private void drawGuiDecorationForegroundLayer(MatrixStack matrixStack) {
        //this.font.drawString(matrixStack, I18n.format("gui.decoration"), 13 + 18, 10 10 + 20*2, Color.BLUE.getRGB());
    }

    private void drawGuiMainForegroundLayer(MatrixStack matrixStack) {
        this.font.drawString(matrixStack, I18n.format("gui.simplycaterpillar.main.consumption"), 2, -8, Color.WHITE.getRGB());
        this.font.drawString(matrixStack, I18n.format("gui.simplycaterpillar.main.gathered"), 120, -8, Color.WHITE.getRGB());
    }
}
