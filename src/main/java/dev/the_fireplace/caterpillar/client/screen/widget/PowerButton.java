package dev.the_fireplace.caterpillar.client.screen.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;

public class PowerButton extends ImageButton {

    private final ResourceLocation resourceLocation;

    private final int xTexStart;

    private final int yTexStart;

    private final int yDiffTex;

    private boolean powered;

    public PowerButton(boolean powered, int x, int y, int width, int height, int xTexStart, int yTexStart, int yDiffTex, ResourceLocation resourceLocation, OnPress onPress) {
        super(x, y, width, height, xTexStart, yTexStart, yDiffTex, resourceLocation, onPress);

        this.resourceLocation = resourceLocation;
        this.xTexStart = xTexStart;
        this.yTexStart = yTexStart;
        this.yDiffTex = yDiffTex;
        this.powered = powered;
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, resourceLocation);

        int yOffset = this.yTexStart;
        if (!this.powered) {
            yOffset += this.yDiffTex;
        }

        RenderSystem.enableDepthTest();
        graphics.blit(resourceLocation, this.getX() + (this.powered ? 0 : -3), this.getY(), this.xTexStart, yOffset, this.width, this.height);
    }

    public boolean isPowered() {
        return powered;
    }

    public void setPower(boolean powered) {
        this.powered = powered;
    }
}
