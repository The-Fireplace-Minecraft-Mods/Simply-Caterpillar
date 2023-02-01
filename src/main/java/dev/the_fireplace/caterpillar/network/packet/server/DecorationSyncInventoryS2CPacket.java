package dev.the_fireplace.caterpillar.network.packet.server;

import dev.the_fireplace.caterpillar.Caterpillar;
import dev.the_fireplace.caterpillar.block.entity.DecorationBlockEntity;
import dev.the_fireplace.caterpillar.network.PacketHandler;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class DecorationSyncInventoryS2CPacket {

    public static final ResourceLocation PACKET_ID = new ResourceLocation(Caterpillar.MOD_ID, "decoration.inventory_sync_s2c");

    public static void send(ServerLevel level, int placementMapId, NonNullList<ItemStack> inventory, BlockPos pos) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeInt(placementMapId);
        buf.writeCollection(inventory, FriendlyByteBuf::writeItem);
        buf.writeBlockPos(pos);

        for (ServerPlayer player : PlayerLookup.tracking(level, pos)) {
            ServerPlayNetworking.send(player, PACKET_ID, buf);
        }
    }

    public static void receive(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender responseSender) {
        ClientLevel level = client.level;

        int placementMapId = buf.readInt();

        List<ItemStack> collection = buf.readCollection(ArrayList::new, FriendlyByteBuf::readItem);
        NonNullList<ItemStack> inventory = NonNullList.withSize(collection.size(), ItemStack.EMPTY);
        for (int i = 0; i < collection.size(); i++) {
            inventory.set(i, collection.get(i));
        }

        BlockPos pos = buf.readBlockPos();

        client.execute(() -> {
            if (level.getBlockEntity(pos) instanceof DecorationBlockEntity decorationBlockEntity) {
                decorationBlockEntity.setPlacementMap(placementMapId, inventory);
            }
        });
    }
}


