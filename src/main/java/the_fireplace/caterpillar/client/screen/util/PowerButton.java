package the_fireplace.caterpillar.client.screen.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;

public class PowerButton extends ImageButton {

    private ResourceLocation texture;

    private int xTexStart;

    private int yTexStart;

    private int yDiffTex;

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
    public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        int uOffset = this.xTexStart;
        int vOffset = this.yTexStart;
        if (!this.powered) {
            vOffset += this.yDiffTex;
        }
        if (this.isHoveredOrFocused()) {
            uOffset += this.width;
        }

        RenderSystem.enableDepthTest();
        super.blit(poseStack, this.x + (this.powered ? 0 : -3), this.y, uOffset, vOffset, this.width, this.height);
    }

    public boolean isPowered() {
        return powered;
    }

    public void setPower(boolean powered) {
       this.powered = powered;
    }
}
