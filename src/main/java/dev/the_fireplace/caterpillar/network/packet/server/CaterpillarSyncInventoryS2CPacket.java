package dev.the_fireplace.caterpillar.network.packet.server;

import dev.the_fireplace.caterpillar.block.entity.DrillBaseBlockEntity;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CaterpillarSyncInventoryS2CPacket {

    public static void receive(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender responseSender) {
        List<ItemStack> collection = buf.readCollection(ArrayList::new, FriendlyByteBuf::readItem);
        NonNullList<ItemStack> inventory = NonNullList.withSize(collection.size(), ItemStack.EMPTY);
        for (int i = 0; i < collection.size(); i++) {
            inventory.add(i, collection.get(i));
        }

        BlockPos pos = buf.readBlockPos();

        if (client.level.getBlockEntity(pos) instanceof DrillBaseBlockEntity caterpillarBlockEntity) {
            caterpillarBlockEntity.setInventory(inventory);
        }
    }
}

