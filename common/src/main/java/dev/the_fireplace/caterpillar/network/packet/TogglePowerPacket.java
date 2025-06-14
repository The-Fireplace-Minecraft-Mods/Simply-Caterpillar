package dev.the_fireplace.caterpillar.network.packet;

import dev.architectury.networking.NetworkManager;
import dev.the_fireplace.caterpillar.block.entity.DrillHeadBlockEntity;
import dev.the_fireplace.caterpillar.network.NetworkRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class TogglePowerPacket implements CustomPacketPayload {

    private final BlockPos blockPos;

    public static final StreamCodec<RegistryFriendlyByteBuf, TogglePowerPacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public void encode(RegistryFriendlyByteBuf buf, TogglePowerPacket packet) {
            buf.writeBlockPos(packet.blockPos);
        }

        @Override
        public TogglePowerPacket decode(RegistryFriendlyByteBuf buf) {
            return new TogglePowerPacket(buf);
        }
    };

    public TogglePowerPacket(BlockPos blockPos) {
        this.blockPos = blockPos;
    }

    public TogglePowerPacket(RegistryFriendlyByteBuf buf) {
        this(buf.readBlockPos());
    }

    public static void handle(TogglePowerPacket packet, NetworkManager.PacketContext context) {
        Player player = context.getPlayer();

        if (player instanceof ServerPlayer serverPlayer) {
            Level level = serverPlayer.level();

            BlockEntity blockEntity = level.getBlockEntity(packet.blockPos);

            if(blockEntity instanceof DrillHeadBlockEntity drillHeadBlockEntity) {
                drillHeadBlockEntity.togglePower();
            }
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return NetworkRegistry.TOGGLE_POWER_ID;
    }
}
