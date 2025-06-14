package dev.the_fireplace.caterpillar.client.screen.widget;

import dev.the_fireplace.caterpillar.Constants;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.StateSwitchingButton;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class TabButton extends StateSwitchingButton {
    private static final WidgetSprites SPRITES = new WidgetSprites(Constants.getId("widget/caterpillar/tab"), Constants.getId("widget/caterpillar/tab_selected"));

    public static final int TAB_WIDTH = 35;
    public static final int TAB_HEIGHT = 27;

    public final ItemStack icon;

    private static final float ANIMATION_TIME = 15.0F;
    private float animationTime;

    public TabButton(int x, int y, boolean initialState, ItemStack icon) {
        super(x, y, TAB_WIDTH, TAB_HEIGHT, initialState);
        this.icon = icon;
        this.initTextureValues(SPRITES);
    }

    public void startAnimation() {
        this.animationTime = ANIMATION_TIME;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (this.animationTime > 0.0F) {
            float f = 1.0F + 0.1F * (float)Math.sin((double)(this.animationTime / 15.0F * (float)Math.PI));
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate((float)(this.getX() + 8), (float)(this.getY() + 12), 0.0F);
            guiGraphics.pose().scale(1.0F, f, 1.0F);
            guiGraphics.pose().translate((float)(-(this.getX() + 8)), (float)(-(this.getY() + 12)), 0.0F);
        }

        ResourceLocation resourceLocation = this.sprites.get(true, this.isStateTriggered);
        int i = this.getX();
        if (this.isStateTriggered) {
            i -= 2;
        }

        guiGraphics.blitSprite(RenderType::guiTextured, resourceLocation, i, this.getY(), this.width, this.height);
        this.renderIcon(guiGraphics);
        if (this.animationTime > 0.0F) {
            guiGraphics.pose().popPose();
            this.animationTime -= partialTick;
        }
    }

    private void renderIcon(GuiGraphics guiGraphics) {
        int i = this.isStateTriggered ? -2 : 0;
        guiGraphics.renderFakeItem(this.icon, this.getX() + 9 + i, this.getY() + 5);
    }
}
