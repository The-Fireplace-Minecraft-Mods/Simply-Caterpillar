package dev.the_fireplace.caterpillar.network;

import dev.the_fireplace.caterpillar.network.packet.client.*;
import dev.the_fireplace.caterpillar.network.packet.server.*;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public final class PacketHandler {

    public static void registerC2SPackets() {
        ServerPlayNetworking.registerGlobalReceiver(CaterpillarSyncCarriedC2SPacket.PACKET_ID, CaterpillarSyncCarriedC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(CaterpillarSyncSelectedTabC2SPacket.PACKET_ID, CaterpillarSyncSelectedTabC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(CaterpillarSyncSlotC2SPacket.PACKET_ID, CaterpillarSyncSlotC2SPacket::receive);

        ServerPlayNetworking.registerGlobalReceiver(DecorationSyncSelectedMapC2SPacket.PACKET_ID, DecorationSyncSelectedMapC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(DecorationSyncSlotC2SPacket.PACKET_ID, DecorationSyncSlotC2SPacket::receive);

        ServerPlayNetworking.registerGlobalReceiver(DrillHeadSyncPowerC2SPacket.PACKET_ID, DrillHeadSyncPowerC2SPacket::receive);

        ServerPlayNetworking.registerGlobalReceiver(MinecraftSyncSlotC2SPacket.PACKET_ID, MinecraftSyncSlotC2SPacket::receive);

        ServerPlayNetworking.registerGlobalReceiver(ReinforcementSyncStateReplacerC2SPacket.PACKET_ID, ReinforcementSyncStateReplacerC2SPacket::receive);

        ServerPlayNetworking.registerGlobalReceiver(PatternBookEditC2SPacket.PACKET_ID, PatternBookEditC2SPacket::receive);
    }

    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(CaterpillarSyncInventoryS2CPacket.PACKET_ID, CaterpillarSyncInventoryS2CPacket::receive);

        ClientPlayNetworking.registerGlobalReceiver(DecorationSyncCurrentMapS2CPacket.PACKET_ID, DecorationSyncCurrentMapS2CPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(DecorationSyncInventoryS2CPacket.PACKET_ID, DecorationSyncInventoryS2CPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(DecorationSyncSelectedMapS2CPacket.PACKET_ID, DecorationSyncSelectedMapS2CPacket::receive);

        ClientPlayNetworking.registerGlobalReceiver(DrillHeadSyncLitS2CPacket.PACKET_ID, DrillHeadSyncLitS2CPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(DrillHeadSyncMovingS2CPacket.PACKET_ID, DrillHeadSyncMovingS2CPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(DrillHeadSyncPowerS2CPacket.PACKET_ID, DrillHeadSyncPowerS2CPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(DrillHeadRefreshInventoryS2CPacket.PACKET_ID, DrillHeadRefreshInventoryS2CPacket::receive);

        ClientPlayNetworking.registerGlobalReceiver(ReinforcementSyncReplacerS2CPacket.PACKET_ID, ReinforcementSyncReplacerS2CPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(ReinforcementSyncStateReplacerS2CPacket.PACKET_ID, ReinforcementSyncStateReplacerS2CPacket::receive);

        ClientPlayNetworking.registerGlobalReceiver(OpenBookGuiS2CPacket.PACKET_ID, OpenBookGuiS2CPacket::receive);
    }
}
