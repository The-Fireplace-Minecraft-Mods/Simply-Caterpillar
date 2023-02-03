package dev.the_fireplace.caterpillar.mixin.client.events;

import dev.the_fireplace.caterpillar.event.KeyInputHandler;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
public class MixinKeyboard {

    @Shadow
    @Final
    private Minecraft minecraft;

    @Shadow
    private boolean sendRepeatsToGui;

    @Inject(method = "keyPress", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screens/Screen;wrapScreenError(Ljava/lang/Runnable;Ljava/lang/String;Ljava/lang/String;)V",
            ordinal = 0))

    public void onKey(long long_1, int int_1, int int_2, int int_3, int int_4, CallbackInfo info) {
        if (!info.isCancelled()) {
            if (int_3 != 1 && (int_3 != 2 || !this.sendRepeatsToGui)) {
                if (int_3 == 0) {
                    // onKeyReleased
                }
            } else {
                KeyInputHandler.onKeyPressed(minecraft, minecraft.screen, int_1, int_2, int_4);
            }
        }
    }
}
