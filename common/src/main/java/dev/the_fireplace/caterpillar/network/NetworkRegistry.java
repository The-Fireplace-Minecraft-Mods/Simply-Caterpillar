package dev.the_fireplace.caterpillar.network;

import dev.architectury.impl.NetworkAggregator;
import dev.architectury.networking.NetworkManager;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import dev.the_fireplace.caterpillar.Constants;
import dev.the_fireplace.caterpillar.network.packet.OpenTabMenuPacket;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.Collections;
import java.util.List;

public class NetworkRegistry {
    public static final CustomPacketPayload.Type<OpenTabMenuPacket> OPEN_MENU_ID = new CustomPacketPayload.Type<>(Constants.getId("open_menu"));

    public static void init() {
        registerC2S(OPEN_MENU_ID, OpenTabMenuPacket.STREAM_CODEC, OpenTabMenuPacket::handle);
    }

    private static <T extends CustomPacketPayload> void registerS2C(CustomPacketPayload.Type<T> packetType, StreamCodec<? super RegistryFriendlyByteBuf, T> codec, NetworkManager.NetworkReceiver<T> receiver) {
        if (Platform.getEnvironment().equals(Env.SERVER)) {
            NetworkAggregator.registerS2CType(packetType, codec, List.of());
        }
        else {
            NetworkAggregator.registerReceiver(NetworkManager.s2c(), packetType, codec, Collections.emptyList(), receiver);
        }
    }

    private static <T extends CustomPacketPayload> void registerC2S(CustomPacketPayload.Type<T> packetType, StreamCodec<? super RegistryFriendlyByteBuf, T> codec, NetworkManager.NetworkReceiver<T> receiver) {
        NetworkAggregator.registerReceiver(NetworkManager.c2s(), packetType, codec, Collections.emptyList(), receiver);
    }
}
