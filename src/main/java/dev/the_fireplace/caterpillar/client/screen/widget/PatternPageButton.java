package dev.the_fireplace.caterpillar.client.screen.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.the_fireplace.caterpillar.client.screen.PatternBookViewScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.PageButton;

public class PatternPageButton extends PageButton {

    private final boolean isForward;

    public PatternPageButton(int x, int y, boolean isForward, OnPress onPress, boolean playTurnSound) {
        super(x, y, isForward, onPress, playTurnSound);

        this.isForward = isForward;
    }

    public void renderWidget(GuiGraphics graphics, int p_99234_, int p_99235_, float p_99236_) {
        RenderSystem.setShaderTexture(0, PatternBookViewScreen.BOOK_TEXTURE);
        int i = 0;
        int j = 192;
        if (this.isHoveredOrFocused()) {
            i += 23;
        }

        if (!this.isForward) {
            j += 13;
        }

        graphics.blit(PatternBookViewScreen.BOOK_TEXTURE, this.getX(), this.getY(), i, j, 23, 13);
    }
}
