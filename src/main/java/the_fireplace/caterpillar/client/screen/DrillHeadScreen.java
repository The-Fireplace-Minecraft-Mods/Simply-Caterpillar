package the_fireplace.caterpillar.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.common.block.entity.DrillHeadBlockEntity;
import the_fireplace.caterpillar.common.container.DrillHeadContainer;

public class DrillHeadScreen extends AbstractContainerScreen<DrillHeadContainer> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(Caterpillar.MOD_ID, "textures/gui/caterpillar.png");

    protected Font font;

    private final int consumptionX;

    private final int consumptionY;

    private final int gatheredX;

    private final int gatheredY;

    public DrillHeadScreen(DrillHeadContainer container, Inventory playerInventory, Component title) {
        super(container, playerInventory, title);
        this.font = Minecraft.getInstance().font;
        this.leftPos = 0;
        this.topPos = 0;
        this.imageWidth = 176;
        this.imageHeight = 166;
        this.consumptionX = 112;
        this.consumptionY = 6;
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
        this.titleLabelY = -10;
        this.gatheredX = 11;
        this.gatheredY = 6;
        this.inventoryLabelX = 8;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void renderLabels(PoseStack stack, int mouseX, int mouseY) {
        this.font.draw(stack, DrillHeadBlockEntity.CONSUMPTION, (float)this.consumptionX, (float)this.consumptionY, 0x404040);
        this.font.draw(stack, this.title, (float)this.titleLabelX, (float)this.titleLabelY, 0xFFFFFF);
        this.font.draw(stack, DrillHeadBlockEntity.GATHERED, (float)this.gatheredX, (float)this.consumptionY, 0x404040);
        this.font.draw(stack, this.playerInventoryTitle, (float)this.inventoryLabelX, (float)this.inventoryLabelY, 0x404040);
    }

    @Override
    protected void renderBg(PoseStack stack, float mouseX, int mouseY, int partialTicks) {
        renderBackground(stack);
        bindTexture();
        blit(stack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }

    public static void bindTexture() {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
    }
}