package the_fireplace.caterpillar.core.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.core.network.packet.client.CaterpillarSetSelectedTabC2SPacket;
import the_fireplace.caterpillar.core.network.packet.client.DecorationSyncSelectedMapC2SPacket;
import the_fireplace.caterpillar.core.network.packet.client.DrillHeadPowerSyncC2SPacket;
import the_fireplace.caterpillar.core.network.packet.server.DecorationSyncSelectedMapS2CPacket;
import the_fireplace.caterpillar.core.network.packet.server.DrillHeadSyncLitS2CPacket;
import the_fireplace.caterpillar.core.network.packet.server.DrillHeadSyncPowerS2CPacket;

public final class PacketHandler {

    private static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Caterpillar.MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void register() {
        int index = 0;
        Caterpillar.LOGGER.info("Registered {} packets!", index);

        CHANNEL.messageBuilder(DrillHeadSyncPowerS2CPacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(DrillHeadSyncPowerS2CPacket::new)
                .encoder(DrillHeadSyncPowerS2CPacket::toBytes)
                .consumerMainThread(DrillHeadSyncPowerS2CPacket::handle)
                .add();

        CHANNEL.messageBuilder(DrillHeadPowerSyncC2SPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .decoder(DrillHeadPowerSyncC2SPacket::new)
                .encoder(DrillHeadPowerSyncC2SPacket::toBytes)
                .consumerMainThread(DrillHeadPowerSyncC2SPacket::handle)
                .add();

        CHANNEL.messageBuilder(DrillHeadSyncLitS2CPacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(DrillHeadSyncLitS2CPacket::new)
                .encoder(DrillHeadSyncLitS2CPacket::toBytes)
                .consumerMainThread(DrillHeadSyncLitS2CPacket::handle)
                .add();

        CHANNEL.messageBuilder(CaterpillarSetSelectedTabC2SPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .decoder(CaterpillarSetSelectedTabC2SPacket::new)
                .encoder(CaterpillarSetSelectedTabC2SPacket::toBytes)
                .consumerMainThread(CaterpillarSetSelectedTabC2SPacket::handle)
                .add();

        CHANNEL.messageBuilder(DecorationSyncSelectedMapC2SPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .decoder(DecorationSyncSelectedMapC2SPacket::new)
                .encoder(DecorationSyncSelectedMapC2SPacket::toBytes)
                .consumerMainThread(DecorationSyncSelectedMapC2SPacket::handle)
                .add();

        CHANNEL.messageBuilder(DecorationSyncSelectedMapS2CPacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(DecorationSyncSelectedMapS2CPacket::new)
                .encoder(DecorationSyncSelectedMapS2CPacket::toBytes)
                .consumerMainThread(DecorationSyncSelectedMapS2CPacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        CHANNEL.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    public static <MSG> void sendToClients(MSG message) {
        CHANNEL.send(PacketDistributor.ALL.noArg(), message);
    }
}
