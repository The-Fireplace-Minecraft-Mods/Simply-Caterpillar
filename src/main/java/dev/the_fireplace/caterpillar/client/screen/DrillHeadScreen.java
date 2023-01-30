package dev.the_fireplace.caterpillar.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.the_fireplace.caterpillar.Caterpillar;
import dev.the_fireplace.caterpillar.menu.DrillHeadMenu;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class DrillHeadScreen extends HandledScreen<DrillHeadMenu> {

    private static final Identifier TEXTURE = new Identifier(Caterpillar.MOD_ID, "textures/gui/drill_head.png");

    public DrillHeadScreen(DrillHeadMenu menu, PlayerInventory playerInventory, Text title) {
        super(menu, playerInventory, title);
    }

    @Override
    public void render(MatrixStack poseStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, partialTicks);
    }

    @Override
    protected void drawBackground(MatrixStack poseStack, float partialTick, int mouseX, int mouseY) {
        this.bindTexture();

        this.drawTexture(poseStack, this.x, this.y, 0, 0, this.backgroundWidth, this.backgroundHeight);
    }

    private void bindTexture() {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
    }

    @Override
    protected void init() {
        super.init();

        // Center the title
        this.titleX = (this.backgroundWidth - this.textRenderer.getWidth(this.title)) / 2;
    }
}
