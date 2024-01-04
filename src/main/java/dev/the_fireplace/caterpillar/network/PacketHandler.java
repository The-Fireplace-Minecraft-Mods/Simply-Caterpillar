package dev.the_fireplace.caterpillar.network;

import dev.the_fireplace.caterpillar.Caterpillar;
import dev.the_fireplace.caterpillar.network.packet.client.*;
import dev.the_fireplace.caterpillar.network.packet.server.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

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

        CHANNEL.messageBuilder(MinecraftSyncSlotC2SPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .decoder(MinecraftSyncSlotC2SPacket::new)
                .encoder(MinecraftSyncSlotC2SPacket::toBytes)
                .consumerMainThread(MinecraftSyncSlotC2SPacket::handle)
                .add();

        CHANNEL.messageBuilder(CaterpillarSyncInventoryS2CPacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(CaterpillarSyncInventoryS2CPacket::new)
                .encoder(CaterpillarSyncInventoryS2CPacket::toBytes)
                .consumerMainThread(CaterpillarSyncInventoryS2CPacket::handle)
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

        CHANNEL.messageBuilder(CaterpillarSyncCarriedC2SPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .decoder(CaterpillarSyncCarriedC2SPacket::new)
                .encoder(CaterpillarSyncCarriedC2SPacket::toBytes)
                .consumerMainThread(CaterpillarSyncCarriedC2SPacket::handle)
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

        CHANNEL.messageBuilder(DrillHeadSyncMovingS2CPacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(DrillHeadSyncMovingS2CPacket::new)
                .encoder(DrillHeadSyncMovingS2CPacket::toBytes)
                .consumerMainThread(DrillHeadSyncMovingS2CPacket::handle)
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

        CHANNEL.messageBuilder(DrillHeadRefreshInventoryS2CPacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(DrillHeadRefreshInventoryS2CPacket::new)
                .encoder(DrillHeadRefreshInventoryS2CPacket::write)
                .consumerMainThread(DrillHeadRefreshInventoryS2CPacket::handle)
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

        CHANNEL.messageBuilder(DecorationSyncInventoryS2CPacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(DecorationSyncInventoryS2CPacket::new)
                .encoder(DecorationSyncInventoryS2CPacket::toBytes)
                .consumerMainThread(DecorationSyncInventoryS2CPacket::handle)
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

        CHANNEL.messageBuilder(ReinforcementSyncReplacerS2CPacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ReinforcementSyncReplacerS2CPacket::new)
                .encoder(ReinforcementSyncReplacerS2CPacket::toBytes)
                .consumerMainThread(ReinforcementSyncReplacerS2CPacket::handle)
                .add();

        /**
         * TODO: Re-enable when fixed
         *
         *    CHANNEL.messageBuilder(PatternBookEditC2SPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
         *                 .decoder(PatternBookEditC2SPacket::new)
         *                 .encoder(PatternBookEditC2SPacket::toBytes)
         *                 .consumerMainThread(PatternBookEditC2SPacket::handle)
         *                 .add();
         *
         *   CHANNEL.messageBuilder(OpenBookGuiS2CPacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
         *      .decoder(OpenBookGuiS2CPacket::new)
         *      .encoder(OpenBookGuiS2CPacket::toBytes)
         *      .consumerMainThread(OpenBookGuiS2CPacket::handle)
         *      .add();
         */

        Caterpillar.LOGGER.info("Registered {} packets!", index);
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
