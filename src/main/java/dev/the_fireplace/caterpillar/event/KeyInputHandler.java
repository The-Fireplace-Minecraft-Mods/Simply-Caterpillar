package dev.the_fireplace.caterpillar.event;

import com.mojang.blaze3d.platform.InputConstants;
import dev.the_fireplace.caterpillar.client.screen.AbstractCaterpillarScreen;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.lwjgl.glfw.GLFW;

public class KeyInputHandler {

    public static final String KEY_CATEGORY_TUTORIAL = "key.simplycaterpillar.category";

    public static final String KEY_TOGGLE_TUTORIAL = "key.simplycaterpillar.toggle_tutorial";

    public static KeyMapping toggleTutorial;

    public static void onKeyPressed(Minecraft minecraft, Screen screen, int int1, int int2, int int4) {
        if (toggleTutorial.matches(int1, int2)) {
            if (screen instanceof AbstractCaterpillarScreen caterpillarScreen) {
                caterpillarScreen.tutorialButton.toggleTutorial();
            }
        }
    }

    public static void register() {
        toggleTutorial = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                KEY_TOGGLE_TUTORIAL,
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_I,
                KEY_CATEGORY_TUTORIAL
        ));
    }
}
