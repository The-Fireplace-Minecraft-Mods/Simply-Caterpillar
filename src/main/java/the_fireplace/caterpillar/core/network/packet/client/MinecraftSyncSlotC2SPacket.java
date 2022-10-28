package the_fireplace.caterpillar.core.network.packet.client;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MinecraftSyncSlotC2SPacket {

    private final int slotId;

    private final ItemStack stack;

    public MinecraftSyncSlotC2SPacket(int slotId, ItemStack stack) {
        this.slotId = slotId;
        this.stack = stack;
    }

    public MinecraftSyncSlotC2SPacket(FriendlyByteBuf buf) {
        this.slotId = buf.readInt();
        this.stack = buf.readItem();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(slotId);
        buf.writeItemStack(stack, false);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();

            player.containerMenu.getSlot(slotId).set(stack);
        });
        context.setPacketHandled(true);
    }
}
