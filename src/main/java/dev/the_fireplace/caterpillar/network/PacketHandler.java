package dev.the_fireplace.caterpillar.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import dev.the_fireplace.caterpillar.Caterpillar;
import dev.the_fireplace.caterpillar.network.packet.client.*;
import dev.the_fireplace.caterpillar.network.packet.server.*;

public final class PacketHandler {

    // Client to server

    public static final ResourceLocation CATERPILLAR_CARRIED_SYNC = new ResourceLocation(Caterpillar.MOD_ID, "caterpillar.carried_sync");
    public static final ResourceLocation CATERPILLAR_SELECTED_TAB_SYNC = new ResourceLocation(Caterpillar.MOD_ID, "caterpillar.selected_tab_sync");
    public static final ResourceLocation CATERPILLAR_SLOT_SYNC = new ResourceLocation(Caterpillar.MOD_ID, "caterpillar.slot_sync");

    public static final ResourceLocation DECORATION_SELECTED_MAP_SYNC = new ResourceLocation(Caterpillar.MOD_ID, "decoration.selected_map_sync");
    public static final ResourceLocation DECORATION_SLOT_SYNC = new ResourceLocation(Caterpillar.MOD_ID, "decoration.slot_sync");

    public static final ResourceLocation DRILL_HEAD_POWER_SYNC_C2S = new ResourceLocation(Caterpillar.MOD_ID, "drill_head.power_sync_c2s");

    public static final ResourceLocation REINFORCEMENT_STATE_REPLACER_SYNC = new ResourceLocation(Caterpillar.MOD_ID, "reinforcement.state_replacer_sync");

    public static final ResourceLocation SLOT_SYNC = new ResourceLocation(Caterpillar.MOD_ID, "slot_sync");

    // Server to client
    public static final ResourceLocation CATERPILLAR_INVENTORY_SYNC = new ResourceLocation(Caterpillar.MOD_ID, "caterpillar.inventory_sync");

    public static final ResourceLocation DECORATION_CURRENT_MAP_SYNC = new ResourceLocation(Caterpillar.MOD_ID, "decoration.current_map_sync");
    public static final ResourceLocation DECORATION_INVENTORY_SYNC = new ResourceLocation(Caterpillar.MOD_ID, "decoration.inventory_sync");
    // public static final ResourceLocation DECORATION_SELECTED_MAP_SYNC = new ResourceLocation(Caterpillar.MOD_ID, "decoration.selected_map_sync");

    public static final ResourceLocation DRILL_HEAD_LIT_SYNC = new ResourceLocation(Caterpillar.MOD_ID, "drill_head.lit_sync");
    public static final ResourceLocation DRILL_HEAD_MOVING_SYNC = new ResourceLocation(Caterpillar.MOD_ID, "drill_head.moving_sync");
    public static final ResourceLocation DRILL_HEAD_POWER_SYNC_S2C = new ResourceLocation(Caterpillar.MOD_ID, "drill_head.power_sync_s2c");

    public static final ResourceLocation REINFORCEMENT_REPLACER_SYNC = new ResourceLocation(Caterpillar.MOD_ID, "reinforcement.replacer_sync");

    public static void registerC2SPackets() {
        ServerPlayNetworking.registerGlobalReceiver(CATERPILLAR_CARRIED_SYNC, CaterpillarSyncCarriedC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(CATERPILLAR_SELECTED_TAB_SYNC, CaterpillarSyncSelectedTabC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(CATERPILLAR_SLOT_SYNC, CaterpillarSyncSlotC2SPacket::receive);

        ServerPlayNetworking.registerGlobalReceiver(DECORATION_SELECTED_MAP_SYNC, DecorationSyncSelectedMapC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(DECORATION_SLOT_SYNC, DecorationSyncSlotC2SPacket::receive);

        ServerPlayNetworking.registerGlobalReceiver(DRILL_HEAD_POWER_SYNC_C2S, DrillHeadSyncPowerC2SPacket::receive);

        ServerPlayNetworking.registerGlobalReceiver(REINFORCEMENT_STATE_REPLACER_SYNC, ReinforcementSyncStateReplacerC2SPacket::receive);

        ServerPlayNetworking.registerGlobalReceiver(SLOT_SYNC, MinecraftSyncSlotC2SPacket::receive);
    }

    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(CATERPILLAR_INVENTORY_SYNC, CaterpillarSyncInventoryS2CPacket::receive);

        ClientPlayNetworking.registerGlobalReceiver(DECORATION_CURRENT_MAP_SYNC, DecorationSyncCurrentMapS2CPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(DECORATION_INVENTORY_SYNC, DecorationSyncInventoryS2CPacket::receive);
        // ClientPlayNetworking.registerGlobalReceiver(DECORATION_SELECTED_MAP_SYNC, DecorationSyncSelectedMapS2CPacket::receive);

        ClientPlayNetworking.registerGlobalReceiver(DRILL_HEAD_LIT_SYNC, DrillHeadSyncLitS2CPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(DRILL_HEAD_MOVING_SYNC, DrillHeadSyncMovingS2CPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(DRILL_HEAD_POWER_SYNC_S2C, DrillHeadSyncPowerS2CPacket::receive);

        ClientPlayNetworking.registerGlobalReceiver(REINFORCEMENT_REPLACER_SYNC, ReinforcementSyncReplacerS2CPacket::receive);
    }
}
