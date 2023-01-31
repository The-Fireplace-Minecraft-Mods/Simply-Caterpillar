package dev.the_fireplace.caterpillar.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import dev.the_fireplace.caterpillar.Caterpillar;
import dev.the_fireplace.caterpillar.network.packet.client.*;
import dev.the_fireplace.caterpillar.network.packet.server.*;

public final class PacketHandler {

    public static final ResourceLocation ITEM_SYNC = new ResourceLocation(Caterpillar.MOD_ID, "item_sync");
    public static final ResourceLocation INVENTORY_SYNC = new ResourceLocation(Caterpillar.MOD_ID, "inventory_sync");

    public static void registerC2SPackets() {
        ServerPlayNetworking.registerGlobalReceiver(ITEM_SYNC, MinecraftSyncSlotC2SPacket::receive);
    }

    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(INVENTORY_SYNC, CaterpillarSyncInventoryS2CPacket::receive);
    }
}
