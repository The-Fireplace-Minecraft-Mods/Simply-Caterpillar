package dev.the_fireplace.caterpillar.mixin.client.player;

import com.mojang.authlib.GameProfile;
import dev.the_fireplace.caterpillar.client.screen.PatternBookEditScreen;
import dev.the_fireplace.caterpillar.registry.ItemRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public class MixinLocalPlayer extends AbstractClientPlayer {

    @Shadow @Final protected Minecraft minecraft;

    public MixinLocalPlayer(ClientLevel clientLevel, GameProfile gameProfile) {
        super(clientLevel, gameProfile);
    }

    @Inject(method = "openItemGui", at = @At("HEAD"))
    public void openItemGui(ItemStack itemStack, InteractionHand hand, CallbackInfo info) {
        if (itemStack.is(ItemRegistry.WRITABLE_PATTERN_BOOK)) {
            this.minecraft.setScreen(new PatternBookEditScreen(this, itemStack, hand, null));
        }
    }
}
