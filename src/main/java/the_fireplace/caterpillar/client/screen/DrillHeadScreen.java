package the_fireplace.caterpillar.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.common.block.entity.DecorationBlockEntity;
import the_fireplace.caterpillar.common.block.entity.DrillHeadBlockEntity;
import the_fireplace.caterpillar.common.block.entity.IncineratorBlockEntity;
import the_fireplace.caterpillar.common.block.entity.ReinforcementBlockEntity;
import the_fireplace.caterpillar.common.container.DrillHeadContainer;

import java.util.List;

public class DrillHeadScreen extends AbstractContainerScreen<DrillHeadContainer> {

    public static final ResourceLocation TEXTURE = new ResourceLocation(Caterpillar.MOD_ID, "textures/gui/caterpillar.png");

    protected Font font;

    private final int consumptionX;

    private final int consumptionY;

    private final int gatheredX;

    private final int gatheredY;

    private final int buttonsTextX;

    private final int buttonsTextY;

    private DrillHeadContainer container;

    public DrillHeadScreen(DrillHeadContainer container, Inventory playerInventory, Component title) {
        super(container, playerInventory, title);
        this.container = container;
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
        this.buttonsTextX = this.imageWidth - 40;
        this.buttonsTextY = 10;
        this.inventoryLabelX = 8;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void renderLabels(PoseStack stack, int mouseX, int mouseY) {
        this.font.draw(stack, DrillHeadBlockEntity.CONSUMPTION, (float)this.consumptionX, (float)this.consumptionY, 0x404040);
        this.font.draw(stack, this.title, (float)this.titleLabelX, (float)this.titleLabelY, 0xFFFFFF);
        this.font.draw(stack, DrillHeadBlockEntity.GATHERED, (float)this.gatheredX, (float)this.gatheredY, 0x404040);
        this.font.draw(stack, this.playerInventoryTitle, (float)this.inventoryLabelX, (float)this.inventoryLabelY, 0x404040);
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        super.render(stack, mouseX, mouseY, partialTicks);
        this.renderTabButtons(stack);
    }

    @Override
    protected void renderBg(PoseStack stack, float mouseX, int mouseY, int partialTicks) {
        renderBackground(stack);
        bindTexture();
        blit(stack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        if (this.container.isLit()) {
            int k = this.container.getLitProgress();
            blit(stack, this.leftPos + 81, this.topPos + 36 + 12 - k, 176, 12 - k, 14, k + 1);
        }
    }

    private void renderTabButtons(PoseStack stack) {
        this.addRenderableWidget(new ImageButton(this.leftPos - 28, this.topPos + 3, 31, 20, 176 , 78, 0, TEXTURE, (onPress) -> {
            setTab(0);
        }));

        this.addRenderableWidget(new ImageButton(this.leftPos - 30, this.topPos + 23, 31, 20, 176 , 118, 0, TEXTURE, (onPress) -> {
            setTab(1);
        }));

        this.addRenderableWidget(new ImageButton(this.leftPos - 30, this.topPos + 43, 31, 20, 176 , 118, 0, TEXTURE, (onPress) -> {
            setTab(2);
        }));

        this.addRenderableWidget(new ImageButton(this.leftPos - 30, this.topPos + 63, 31, 20, 176 , 118, 0, TEXTURE, 10, 10, (onPress) -> {
            setTab(2);
        }, (onTooltip) -> {
            renderComponentTooltip(stack, (List<Component>) IncineratorBlockEntity.TITLE, 10, 20);
        }, DrillHeadBlockEntity.TITLE));

        this.font.draw(stack, DrillHeadBlockEntity.TITLE, (float)this.buttonsTextX, (float)this.buttonsTextY, 0xFFFFFF);
        this.font.draw(stack, DecorationBlockEntity.TITLE, (float)this.buttonsTextX, (float)this.buttonsTextY + 20, 0xFFFFFF);
        this.font.draw(stack, ReinforcementBlockEntity.TITLE, (float)this.buttonsTextX, (float)this.buttonsTextY + 40, 0xFFFFFF);
        this.font.draw(stack, IncineratorBlockEntity.TITLE, (float)this.buttonsTextX, (float)this.buttonsTextY + 60, 0xFFFFFF);
    }

    private void setTab(int selectedTab) {
        // BUTTON PRESSED
        System.out.println("BUTTON PRESSED : " + selectedTab);
    }

    public static void bindTexture() {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
    }
}
