package dev.the_fireplace.caterpillar.network.packet;

import dev.architectury.networking.NetworkManager;
import dev.architectury.registry.menu.MenuRegistry;
import dev.the_fireplace.caterpillar.Constants;
import dev.the_fireplace.caterpillar.block.entity.DrillBaseBlockEntity;
import dev.the_fireplace.caterpillar.block.util.CaterpillarBlockUtil;
import dev.the_fireplace.caterpillar.client.screen.util.ScreenTabs;
import dev.the_fireplace.caterpillar.network.NetworkRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

public class OpenTabMenuPacket implements CustomPacketPayload {
    public static final ResourceLocation PACKET_ID = Constants.getId("caterpillar.open_menu_c2s");

    private final ScreenTabs tab;
    private final BlockPos blockPos;

    public static final StreamCodec<RegistryFriendlyByteBuf, OpenTabMenuPacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public void encode(RegistryFriendlyByteBuf buf, OpenTabMenuPacket packet) {
            buf.writeInt(packet.tab.ordinal());
            buf.writeBlockPos(packet.blockPos);
        }

        @Override
        public OpenTabMenuPacket decode(RegistryFriendlyByteBuf buf) {
            return new OpenTabMenuPacket(buf);
        }
    };

    public OpenTabMenuPacket(ScreenTabs tab, BlockPos blockPos) {
        this.tab = tab;
        this.blockPos = blockPos;
    }

    public OpenTabMenuPacket(RegistryFriendlyByteBuf buf) {
        this(ScreenTabs.values()[buf.readInt()], buf.readBlockPos());
    }

    public static void handle(OpenTabMenuPacket packet, NetworkManager.PacketContext context) {
        Player player = context.getPlayer();

        if (player instanceof ServerPlayer serverPlayer) {
            Level level = serverPlayer.level();

            DrillBaseBlockEntity blockEntity = CaterpillarBlockUtil.getConnectedCaterpillarBlockEntity(level, packet.blockPos, packet.tab.BLOCK);

            MenuRegistry.openExtendedMenu(serverPlayer, blockEntity);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return NetworkRegistry.OPEN_MENU_ID;
    }
}
