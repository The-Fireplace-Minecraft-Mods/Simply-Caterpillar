package dev.the_fireplace.caterpillar.mixin.server.level;

import com.mojang.authlib.GameProfile;
import dev.the_fireplace.caterpillar.Caterpillar;
import dev.the_fireplace.caterpillar.item.WritablePatternBookItem;
import dev.the_fireplace.caterpillar.network.packet.server.OpenWritablePatternBookGuiS2CPacket;
import dev.the_fireplace.caterpillar.registry.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class MixinServerPlayer extends Player {

    @Shadow @Final public ServerPlayerGameMode gameMode;

    @Shadow public abstract ServerLevel serverLevel();

    @Shadow public abstract SectionPos getLastSectionPos();

    public MixinServerPlayer(Level level, BlockPos blockPos, float f, GameProfile gameProfile) {
        super(level, blockPos, f, gameProfile);
    }

    @Override
    public boolean isSpectator() {
        return this.gameMode.getGameModeForPlayer() == GameType.SPECTATOR;
    }

    @Override
    public boolean isCreative() {
        return this.gameMode.getGameModeForPlayer() == GameType.CREATIVE;
    }

    @Inject(method = "openItemGui", at = @At("HEAD"))
    public void openItemGui(ItemStack itemStack, InteractionHand interactionHand, CallbackInfo ci) {
        if (itemStack.is(ItemRegistry.WRITABLE_PATTERN_BOOK)) {
            this.containerMenu.broadcastChanges();

            OpenWritablePatternBookGuiS2CPacket.send(itemStack, interactionHand, null);
        }
    }
}
