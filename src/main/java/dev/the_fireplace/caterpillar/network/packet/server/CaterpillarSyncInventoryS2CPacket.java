package dev.the_fireplace.caterpillar.network.packet.server;

import dev.the_fireplace.caterpillar.block.entity.DrillBaseBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public class CaterpillarSyncInventoryS2CPacket {

    private final ItemStackHandler inventory;

    private final BlockPos pos;

    public CaterpillarSyncInventoryS2CPacket(ItemStackHandler inventory, BlockPos pos) {
        this.inventory = inventory;
        this.pos = pos;
    }

    public CaterpillarSyncInventoryS2CPacket(FriendlyByteBuf buf) {
        List<ItemStack> collection = buf.readCollection(ArrayList::new, FriendlyByteBuf::readItem);
        inventory = new ItemStackHandler(collection.size());
        for (int i = 0; i < collection.size(); i++) {
            inventory.insertItem(i, collection.get(i), false);
        }

        this.pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        Collection<ItemStack> list = new ArrayList<>();
        for(int i = 0; i < inventory.getSlots(); i++) {
            list.add(inventory.getStackInSlot(i));
        }

        buf.writeCollection(list, FriendlyByteBuf::writeItem);
        buf.writeBlockPos(pos);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientLevel level = Minecraft.getInstance().level;

            if (level.getBlockEntity(pos) instanceof DrillBaseBlockEntity caterpillarBlockEntity) {
                caterpillarBlockEntity.setInventory(this.inventory);
            }
        });
        context.setPacketHandled(true);
    }
}