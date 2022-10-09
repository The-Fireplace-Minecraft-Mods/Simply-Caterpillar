package the_fireplace.caterpillar.core.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.core.network.packet.client.*;
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

        CHANNEL.messageBuilder(ItemStackSyncS2CPacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ItemStackSyncS2CPacket::new)
                .encoder(ItemStackSyncS2CPacket::toBytes)
                .consumerMainThread(ItemStackSyncS2CPacket::handle)
                .add();

        CHANNEL.messageBuilder(CaterpillarSyncSlotC2SPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .decoder(CaterpillarSyncSlotC2SPacket::new)
                .encoder(CaterpillarSyncSlotC2SPacket::toBytes)
                .consumerMainThread(CaterpillarSyncSlotC2SPacket::handle)
                .add();

        CHANNEL.messageBuilder(CaterpillarSyncSlotC2SPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .decoder(CaterpillarSyncSlotC2SPacket::new)
                .encoder(CaterpillarSyncSlotC2SPacket::toBytes)
                .consumerMainThread(CaterpillarSyncSlotC2SPacket::handle)
                .add();

        CHANNEL.messageBuilder(CaterpillarSyncSelectedTabC2SPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .decoder(CaterpillarSyncSelectedTabC2SPacket::new)
                .encoder(CaterpillarSyncSelectedTabC2SPacket::toBytes)
                .consumerMainThread(CaterpillarSyncSelectedTabC2SPacket::handle)
                .add();

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

        CHANNEL.messageBuilder(DrillHeadSyncScrollsS2CPacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(DrillHeadSyncScrollsS2CPacket::new)
                .encoder(DrillHeadSyncScrollsS2CPacket::toBytes)
                .consumerMainThread(DrillHeadSyncScrollsS2CPacket::handle)
                .add();

        CHANNEL.messageBuilder(DrillHeadSyncSelectedGatheredScrollC2SPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .decoder(DrillHeadSyncSelectedGatheredScrollC2SPacket::new)
                .encoder(DrillHeadSyncSelectedGatheredScrollC2SPacket::toBytes)
                .consumerMainThread(DrillHeadSyncSelectedGatheredScrollC2SPacket::handle)
                .add();

        CHANNEL.messageBuilder(DrillHeadSyncSelectedConsumptionScrollC2SPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .decoder(DrillHeadSyncSelectedConsumptionScrollC2SPacket::new)
                .encoder(DrillHeadSyncSelectedConsumptionScrollC2SPacket::toBytes)
                .consumerMainThread(DrillHeadSyncSelectedConsumptionScrollC2SPacket::handle)
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

        CHANNEL.messageBuilder(DecorationSyncCurrentMapS2CPacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(DecorationSyncCurrentMapS2CPacket::new)
                .encoder(DecorationSyncCurrentMapS2CPacket::toBytes)
                .consumerMainThread(DecorationSyncCurrentMapS2CPacket::handle)
                .add();

        CHANNEL.messageBuilder(DecorationSyncSlotC2SPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .decoder(DecorationSyncSlotC2SPacket::new)
                .encoder(DecorationSyncSlotC2SPacket::toBytes)
                .consumerMainThread(DecorationSyncSlotC2SPacket::handle)
                .add();

        CHANNEL.messageBuilder(DecorationItemStackSyncS2CPacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(DecorationItemStackSyncS2CPacket::new)
                .encoder(DecorationItemStackSyncS2CPacket::toBytes)
                .consumerMainThread(DecorationItemStackSyncS2CPacket::handle)
                .add();

        CHANNEL.messageBuilder(ReinforcementSyncStateReplacerC2SPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .decoder(ReinforcementSyncStateReplacerC2SPacket::new)
                .encoder(ReinforcementSyncStateReplacerC2SPacket::toBytes)
                .consumerMainThread(ReinforcementSyncStateReplacerC2SPacket::handle)
                .add();

        CHANNEL.messageBuilder(ReinforcementSyncStateReplacerS2CPacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ReinforcementSyncStateReplacerS2CPacket::new)
                .encoder(ReinforcementSyncStateReplacerS2CPacket::toBytes)
                .consumerMainThread(ReinforcementSyncStateReplacerS2CPacket::handle)
                .add();

        CHANNEL.messageBuilder(ReinforcementSyncSelectedReplacerC2SPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .decoder(ReinforcementSyncSelectedReplacerC2SPacket::new)
                .encoder(ReinforcementSyncSelectedReplacerC2SPacket::toBytes)
                .consumerMainThread(ReinforcementSyncSelectedReplacerC2SPacket::handle)
                .add();

        CHANNEL.messageBuilder(ReinforcementSyncSelectedReplacerS2CPacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ReinforcementSyncSelectedReplacerS2CPacket::new)
                .encoder(ReinforcementSyncSelectedReplacerS2CPacket::toBytes)
                .consumerMainThread(ReinforcementSyncSelectedReplacerS2CPacket::handle)
                .add();

        CHANNEL.messageBuilder(ReinforcementSyncReplacerS2CPacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ReinforcementSyncReplacerS2CPacket::new)
                .encoder(ReinforcementSyncReplacerS2CPacket::toBytes)
                .consumerMainThread(ReinforcementSyncReplacerS2CPacket::handle)
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
