package dev.the_fireplace.caterpillar.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class KeyBinding {
    public static final String KEY_CATEGORY_TUTORIAL = "key.simplycaterpillar.category";

    public static final String KEY_TOGGLE_TUTORIAL = "key.simplycaterpillar.toggle_tutorial";

    public static final KeyMapping TOGGLE_TUTORIAL_KEY = new KeyMapping(KEY_TOGGLE_TUTORIAL, KeyConflictContext.GUI, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_I, KEY_CATEGORY_TUTORIAL);
}
