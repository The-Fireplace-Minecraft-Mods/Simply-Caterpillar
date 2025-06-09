package dev.the_fireplace.caterpillar.client.screen.widget;

import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.WidgetSprites;

public class TutorialButton extends ImageButton {

//    private final int xTexStart;
//    private final int yTexStart;
//    private final int yDiffTex;

    private boolean showTutorial;

    public TutorialButton(int x, int y, int width, int height, WidgetSprites sprites, OnPress onPress) {
        super(x, y, width, height, sprites, onPress);
    }

    public void toggleTutorial() {
        this.showTutorial = !this.showTutorial;
    }

    public boolean isTutorialShown() {
        return this.showTutorial;
    }
}
