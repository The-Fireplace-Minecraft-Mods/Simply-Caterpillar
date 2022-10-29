package the_fireplace.caterpillar.core.network.packet.server;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import the_fireplace.caterpillar.common.block.entity.AbstractCaterpillarBlockEntity;

import java.util.function.Supplier;

public class CaterpillarSyncSlotS2CPacket {

    private final int slotId;

    private final ItemStack stack;

    private final BlockPos pos;


    public CaterpillarSyncSlotS2CPacket(int slotId, ItemStack stack, BlockPos pos) {
        this.slotId = slotId;
        this.stack = stack;
        this.pos = pos;
    }

    public CaterpillarSyncSlotS2CPacket(FriendlyByteBuf buf) {
        this.slotId = buf.readInt();
        this.stack = buf.readItem();
        this.pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(slotId);
        buf.writeItemStack(stack, true);
        buf.writeBlockPos(pos);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientLevel level =  Minecraft.getInstance().level;

            if(level.getBlockEntity(pos) instanceof AbstractCaterpillarBlockEntity blockEntity) {
                blockEntity.setStackInSlot(slotId, stack);
                blockEntity.setChanged();
                blockEntity.saveWithFullMetadata();
            }
        });
        context.setPacketHandled(true);
    }
}

