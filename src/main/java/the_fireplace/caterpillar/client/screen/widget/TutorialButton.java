package the_fireplace.caterpillar.client.screen.widget;

import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.resources.ResourceLocation;

public class TutorialButton extends ImageButton {

    private boolean showTutorial;

    public TutorialButton(boolean showTutorial, int x, int y, int width, int height, int xTexStart, int yTexStart, int yDiffTex, ResourceLocation resourceLocation, OnPress onPress) {
        super(x, y, width, height, xTexStart, yTexStart, yDiffTex, resourceLocation, onPress);

        this.showTutorial = showTutorial;
    }

    public void setShowTutorial(boolean showTutorial) {
        this.showTutorial = showTutorial;
    }

    public boolean showTutorial() {
        return this. showTutorial;
    }
}
