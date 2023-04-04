package dev.the_fireplace.caterpillar.event;

import dev.the_fireplace.caterpillar.Caterpillar;
import dev.the_fireplace.caterpillar.client.KeyBinding;
import dev.the_fireplace.caterpillar.client.screen.AbstractCaterpillarScreen;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Caterpillar.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientForgeEvents {
    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (Minecraft.getInstance().screen instanceof AbstractCaterpillarScreen screen) {
            if (event.getKey() == KeyBinding.TOGGLE_TUTORIAL_KEY.getKey().getValue() && event.getAction() == 0) {
                screen.tutorialButton.toggleTutorial();
            }
        }
    }
}
