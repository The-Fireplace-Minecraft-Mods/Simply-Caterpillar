package the_fireplace.caterpillar.core.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.core.network.packet.client.CaterpillarSyncSelectedTabC2SPacket;
import the_fireplace.caterpillar.core.network.packet.client.DecorationSyncSelectedMapC2SPacket;
import the_fireplace.caterpillar.core.network.packet.client.DecorationSyncSlotC2SPacket;
import the_fireplace.caterpillar.core.network.packet.client.DrillHeadSyncPowerC2SPacket;
import the_fireplace.caterpillar.core.network.packet.server.*;

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

        CHANNEL.messageBuilder(DrillHeadSyncPowerC2SPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .decoder(DrillHeadSyncPowerC2SPacket::new)
                .encoder(DrillHeadSyncPowerC2SPacket::toBytes)
                .consumerMainThread(DrillHeadSyncPowerC2SPacket::handle)
                .add();

        CHANNEL.messageBuilder(DrillHeadSyncLitS2CPacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(DrillHeadSyncLitS2CPacket::new)
                .encoder(DrillHeadSyncLitS2CPacket::toBytes)
                .consumerMainThread(DrillHeadSyncLitS2CPacket::handle)
                .add();

        CHANNEL.messageBuilder(CaterpillarSyncSelectedTabC2SPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .decoder(CaterpillarSyncSelectedTabC2SPacket::new)
                .encoder(CaterpillarSyncSelectedTabC2SPacket::toBytes)
                .consumerMainThread(CaterpillarSyncSelectedTabC2SPacket::handle)
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

        CHANNEL.messageBuilder(DecorationSyncSlotC2SPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .decoder(DecorationSyncSlotC2SPacket::new)
                .encoder(DecorationSyncSlotC2SPacket::toBytes)
                .consumerMainThread(DecorationSyncSlotC2SPacket::handle)
                .add();

        CHANNEL.messageBuilder(ItemStackSyncS2CPacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ItemStackSyncS2CPacket::new)
                .encoder(ItemStackSyncS2CPacket::toBytes)
                .consumerMainThread(ItemStackSyncS2CPacket::handle)
                .add();

        CHANNEL.messageBuilder(DecorationItemStackSyncS2CPacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(DecorationItemStackSyncS2CPacket::new)
                .encoder(DecorationItemStackSyncS2CPacket::toBytes)
                .consumerMainThread(DecorationItemStackSyncS2CPacket::handle)
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
