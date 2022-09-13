package the_fireplace.caterpillar.core.network.packet.client;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import the_fireplace.caterpillar.common.block.entity.IncineratorBlockEntity;

import java.util.function.Supplier;

public class IncineratorSyncSlotC2SPacket {

    private final int slotId;

    private final ItemStack stack;

    private final BlockPos pos;


    public IncineratorSyncSlotC2SPacket(int slotId, ItemStack stack, BlockPos pos) {
        this.slotId = slotId;
        this.stack = stack;
        this.pos = pos;
    }

    public IncineratorSyncSlotC2SPacket(FriendlyByteBuf buf) {
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
            ServerPlayer player = context.getSender();
            ServerLevel level = player.getLevel();

            if(level.getBlockEntity(pos) instanceof IncineratorBlockEntity incineratorBlockEntity) {
                incineratorBlockEntity.setStackInSlot(slotId, stack);
            }
        });
        context.setPacketHandled(true);
    }
}
