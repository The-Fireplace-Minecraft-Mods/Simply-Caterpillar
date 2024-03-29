package dev.the_fireplace.caterpillar.client.screen.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;

public class TabButton extends ImageButton {

    private final ResourceLocation resourceLocation;

    private final int xTexStart;

    private final int yTexStart;

    private final int xDiffTex;

    private final int yDiffTex;

    private final boolean isSelectedTab;


    public TabButton(boolean isSelectedTab, int x, int y, int width, int height, int xTexStart, int yTexStart, int xDiffTex, int yDiffTex, ResourceLocation resourceLocation, OnPress onPress) {
        super(x, y, width, height, xTexStart, yTexStart, yDiffTex, resourceLocation, onPress);

        this.resourceLocation = resourceLocation;
        this.xTexStart = xTexStart;
        this.yTexStart = yTexStart;
        this.xDiffTex = xDiffTex;
        this.yDiffTex = yDiffTex;
        this.isSelectedTab = isSelectedTab;
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, this.resourceLocation);

        int width = super.width;
        int xOffset = this.xTexStart;
        int yOffset = this.yTexStart;
        if (!this.isSelectedTab) {
            width += this.xDiffTex;
            xOffset += this.xDiffTex;
            yOffset += this.yDiffTex;
        }

        graphics.blit(this.resourceLocation, super.getX(), super.getY(), xOffset, yOffset, width, super.height);
    }

    @Override
    public void onPress() {
        if (!this.isSelectedTab) {
            super.onPress();
        }
    }
}
