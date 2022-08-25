package the_fireplace.caterpillar.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.common.container.DecorationContainer;

public class DecorationScreen extends AbstractContainerScreen<DecorationContainer> {

    public static final ResourceLocation TEXTURE = new ResourceLocation(Caterpillar.MOD_ID, "textures/gui/decoration.png");

    protected Font font;

    private float scrollOffs;

    private boolean scrolling;

    public DecorationScreen(DecorationContainer container, Inventory playerInventory, Component title) {
        super(container, playerInventory, title);
        this.font = Minecraft.getInstance().font;
        this.leftPos = 0;
        this.topPos = 0;
        this.imageWidth = 176;
        this.imageHeight = 166;
        this.inventoryLabelX = 8;
        this.inventoryLabelY = this.imageHeight - 94;
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
        this.titleLabelY = 6;
        this.scrollOffs = 0.0F;
    }

    @Override
    protected void renderLabels(PoseStack stack, int mouseX, int mouseY) {
        this.font.draw(stack, this.title, (float)this.titleLabelX, (float)this.titleLabelY, 0x404040);
        this.font.draw(stack, this.playerInventoryTitle, (float)this.inventoryLabelX, (float)this.inventoryLabelY, 0x404040);
    }

    @Override
    protected void renderBg(PoseStack stack, float mouseX, int mouseY, int partialTicks) {
        renderBackground(stack);
        bindTexture();
        blit(stack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        renderScrollBar(stack);
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

    @Override
    public boolean mouseClicked(double p_97748_, double p_97749_, int p_97750_) {
        return super.mouseClicked(p_97748_, p_97749_, p_97750_);
    }

    @Override
    public boolean mouseReleased(double p_97812_, double p_97813_, int p_97814_) {
        return super.mouseReleased(p_97812_, p_97813_, p_97814_);
    }

    @Override
    public boolean mouseDragged(double p_97752_, double p_97753_, int p_97754_, double p_97755_, double p_97756_) {
        return super.mouseDragged(p_97752_, p_97753_, p_97754_, p_97755_, p_97756_);
    }

    @Override
    public boolean mouseScrolled(double p_94686_, double p_94687_, double p_94688_) {
        int i = 9;
        float f = (float)(p_94688_ / (double)i);
        this.scrollOffs = Mth.clamp(this.scrollOffs - f, 0.0F, 1.0F);
        this.menu.scrollTo(this.scrollOffs);
        return true;
    }

    public static void bindTexture() {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
    }
}