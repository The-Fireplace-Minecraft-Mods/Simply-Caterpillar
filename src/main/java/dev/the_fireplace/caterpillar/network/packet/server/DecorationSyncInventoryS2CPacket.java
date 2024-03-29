package dev.the_fireplace.caterpillar.network.packet.server;

import dev.the_fireplace.caterpillar.block.entity.DecorationBlockEntity;
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

public class DecorationSyncInventoryS2CPacket {

    private final int placementMapId;

    private final ItemStackHandler inventory;

    private final BlockPos pos;

    public DecorationSyncInventoryS2CPacket(int placementMapId, ItemStackHandler inventory, BlockPos pos) {
        this.placementMapId = placementMapId;
        this.inventory = inventory;
        this.pos = pos;
    }

    public DecorationSyncInventoryS2CPacket(FriendlyByteBuf buf) {
        this.placementMapId = buf.readInt();

        List<ItemStack> collection = buf.readCollection(ArrayList::new, FriendlyByteBuf::readItem);
        inventory = new ItemStackHandler(collection.size());
        for (int i = 0; i < collection.size(); i++) {
            inventory.insertItem(i, collection.get(i), false);
        }

        this.pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.placementMapId);

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

            if (level.getBlockEntity(pos) instanceof DecorationBlockEntity decorationBlockEntity) {
                decorationBlockEntity.setPlacementMap(this.placementMapId, this.inventory);
            }
        });
        context.setPacketHandled(true);
    }
}


