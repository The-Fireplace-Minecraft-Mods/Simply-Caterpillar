package dev.the_fireplace.caterpillar.network.packet.client;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import dev.the_fireplace.caterpillar.block.entity.DecorationBlockEntity;

import java.util.function.Supplier;

public class DecorationSyncSlotC2SPacket {

    private final int placementSlotId;

    private final ItemStack stack;

    private final BlockPos pos;


    public DecorationSyncSlotC2SPacket(int placementSlotId, ItemStack stack, BlockPos pos) {
        this.placementSlotId = placementSlotId;
        this.stack = stack;
        this.pos = pos;
    }

    public DecorationSyncSlotC2SPacket(FriendlyByteBuf buf) {
        this.placementSlotId = buf.readInt();
        this.stack = buf.readItem();
        this.pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(placementSlotId);
        buf.writeItemStack(stack, true);
        buf.writeBlockPos(pos);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            ServerLevel level = player.serverLevel();

            if(level.getBlockEntity(pos) instanceof DecorationBlockEntity decorationBlockEntity) {
                decorationBlockEntity.getSelectedPlacementMap().setStackInSlot(placementSlotId, stack);
            }
        });
        context.setPacketHandled(true);
    }
}
