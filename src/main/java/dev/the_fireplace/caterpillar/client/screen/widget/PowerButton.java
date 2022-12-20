package dev.the_fireplace.caterpillar.client.screen.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class PowerButton extends ImageButton {

    private final ResourceLocation texture;

    private final int xTexStart;

    private final int yTexStart;

    private final int yDiffTex;

    private boolean powered;

    public PowerButton(boolean powered, int x, int y, int width, int height, int xTexStart, int yTexStart, int yDiffTex, ResourceLocation resourceLocation, OnPress onPress) {
        super(x, y, width, height, xTexStart, yTexStart, yDiffTex, resourceLocation, onPress);

        this.texture = resourceLocation;
        this.xTexStart = xTexStart;
        this.yTexStart = yTexStart;
        this.yDiffTex = yDiffTex;
        this.powered = powered;
    }

    @Override
    public void renderButton(@NotNull PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, texture);

        int yOffset = this.yTexStart;
        if (!this.powered) {
            yOffset += this.yDiffTex;
        }

        RenderSystem.enableDepthTest();
        super.blit(poseStack, this.x + (this.powered ? 0 : -3), this.y, this.xTexStart, yOffset, this.width, this.height);
    }

    public boolean isPowered() {
        return powered;
    }

    public void setPower(boolean powered) {
       this.powered = powered;
    }
}
