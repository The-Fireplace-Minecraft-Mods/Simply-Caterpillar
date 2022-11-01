package the_fireplace.caterpillar.core.network.packet.client;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import the_fireplace.caterpillar.common.block.entity.AbstractCaterpillarBlockEntity;

import java.util.function.Supplier;

public class CaterpillarSyncCarriedC2SPacket {
    private final ItemStack stack;

    public CaterpillarSyncCarriedC2SPacket(ItemStack stack) {
        this.stack = stack;
    }

    public CaterpillarSyncCarriedC2SPacket(FriendlyByteBuf buf) {
        this.stack = buf.readItem();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeItemStack(stack, false);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();

            player.containerMenu.setCarried(stack);
        });
        context.setPacketHandled(true);
    }

}
