package the_fireplace.caterpillar.client.screen.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class TabButton extends ImageButton {
    private final ResourceLocation resourceLocation;

    private int xTexStart;

    private int yTexStart;

    private int yDiffTex;

    private boolean selected;


    public TabButton(boolean selected, int x, int y, int width, int height, int xTexStart, int yTexStart, int yDiffTex, ResourceLocation resourceLocation, OnPress onPress) {
        super(x, y, width, height, xTexStart, yTexStart, yDiffTex, resourceLocation, onPress);

        this.resourceLocation = resourceLocation;
        this.xTexStart = xTexStart;
        this.yTexStart = yTexStart;
        this.yDiffTex = yDiffTex;
        this.selected = selected;
    }

    @Override
    public void renderButton(@NotNull PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, this.resourceLocation);

        if (this.selected) {
            this.yTexStart = 0;
        } else {
            this.yTexStart += this.yDiffTex;
        }

        if (this.isHoveredOrFocused()) {
            xTexStart += this.width;
        }

        super.blit(poseStack, super.x, super.y, this.xTexStart, this.yTexStart, super.width, super.height);
    }
}
