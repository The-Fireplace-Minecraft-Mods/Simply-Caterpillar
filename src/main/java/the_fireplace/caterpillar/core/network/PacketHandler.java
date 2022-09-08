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
import the_fireplace.caterpillar.core.network.packet.server.DrillHeadLitSyncS2CPacket;
import the_fireplace.caterpillar.core.network.packet.server.DrillHeadPowerSyncS2CPacket;

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

        CHANNEL.messageBuilder(DrillHeadPowerSyncS2CPacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(DrillHeadPowerSyncS2CPacket::new)
                .encoder(DrillHeadPowerSyncS2CPacket::toBytes)
                .consumerMainThread(DrillHeadPowerSyncS2CPacket::handle)
                .add();

        CHANNEL.messageBuilder(DrillHeadPowerSyncC2SPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .decoder(DrillHeadPowerSyncC2SPacket::new)
                .encoder(DrillHeadPowerSyncC2SPacket::toBytes)
                .consumerMainThread(DrillHeadPowerSyncC2SPacket::handle)
                .add();

        CHANNEL.messageBuilder(DrillHeadLitSyncS2CPacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(DrillHeadLitSyncS2CPacket::new)
                .encoder(DrillHeadLitSyncS2CPacket::toBytes)
                .consumerMainThread(DrillHeadLitSyncS2CPacket::handle)
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
