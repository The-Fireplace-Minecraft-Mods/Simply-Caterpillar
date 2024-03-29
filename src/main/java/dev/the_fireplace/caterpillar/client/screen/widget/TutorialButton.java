package dev.the_fireplace.caterpillar.client.screen.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;

public class TutorialButton extends ImageButton {

    private final ResourceLocation resourceLocation;

    private final int xTexStart;

    private final int yTexStart;

    private final int yDiffTex;

    private boolean showTutorial;

    public TutorialButton(boolean showTutorial, int x, int y, int width, int height, int xTexStart, int yTexStart, int yDiffTex, ResourceLocation resourceLocation, OnPress onPress) {
        super(x, y, width, height, xTexStart, yTexStart, yDiffTex, resourceLocation, onPress);

        this.resourceLocation = resourceLocation;
        this.xTexStart = xTexStart;
        this.yTexStart = yTexStart;
        this.yDiffTex = yDiffTex;
        this.showTutorial = showTutorial;
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, this.resourceLocation);

        int yOffset = this.yTexStart;
        if (!this.showTutorial) {
            yOffset += this.yDiffTex;
        }

        graphics.blit(this.resourceLocation, super.getX(), super.getY(), this.xTexStart, yOffset, super.width, super.height);
    }

    public void toggleTutorial() {
        this.showTutorial = !this.showTutorial;
    }

    public boolean isTutorialShown() {
        return this.showTutorial;
    }
}
