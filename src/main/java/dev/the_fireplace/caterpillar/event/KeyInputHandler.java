package dev.the_fireplace.caterpillar.event;

import com.mojang.blaze3d.platform.InputConstants;
import dev.the_fireplace.caterpillar.client.screen.AbstractCaterpillarScreen;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class KeyInputHandler {

    public static final String KEY_CATEGORY_TUTORIAL = "key.simplycaterpillar.category";

    public static final String KEY_TOGGLE_TUTORIAL = "key.simplycaterpillar.toggle_tutorial";

    public static KeyMapping toggleTutorial;

    public static void registerKeyInputs() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.screen instanceof AbstractCaterpillarScreen screen) {
                if (toggleTutorial.consumeClick()) {
                    screen.tutorialButton.toggleTutorial();
                }
            }
        });
    }

    public static void register() {
        toggleTutorial = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                KEY_TOGGLE_TUTORIAL,
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_I,
                KEY_CATEGORY_TUTORIAL
        ));

        registerKeyInputs();
    }
}
