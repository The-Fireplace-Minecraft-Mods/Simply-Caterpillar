package dev.the_fireplace.caterpillar.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.the_fireplace.caterpillar.Caterpillar;
import dev.the_fireplace.caterpillar.menu.DrillHeadMenu;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class DrillHeadScreen extends AbstractContainerScreen<DrillHeadMenu> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(Caterpillar.MOD_ID, "textures/gui/drill_head.png");

    public DrillHeadScreen(DrillHeadMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, partialTicks);
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTick, int mouseX, int mouseY) {
        this.bindTexture();

        this.blit(poseStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
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
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }
}
