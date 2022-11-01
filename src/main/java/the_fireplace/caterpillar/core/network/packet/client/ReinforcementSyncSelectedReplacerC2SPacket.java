package the_fireplace.caterpillar.core.network.packet.client;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import the_fireplace.caterpillar.common.block.entity.ReinforcementBlockEntity;

import java.util.function.Supplier;

public class ReinforcementSyncSelectedReplacerC2SPacket {
    private final int selectedReplacer;
    private final BlockPos pos;

    public ReinforcementSyncSelectedReplacerC2SPacket(int selectedReplacer, BlockPos pos) {
        this.selectedReplacer = selectedReplacer;
        this.pos = pos;
    }

    public ReinforcementSyncSelectedReplacerC2SPacket(FriendlyByteBuf buf) {
        this.selectedReplacer = buf.readInt();
        this.pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(selectedReplacer);
        buf.writeBlockPos(pos);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            ServerLevel level = player.getLevel();

            if(level.getBlockEntity(pos) instanceof ReinforcementBlockEntity reinforcementBlockEntity) {
                reinforcementBlockEntity.setSelectedReplacer(selectedReplacer);
                reinforcementBlockEntity.setChanged();
            }
        });
        context.setPacketHandled(true);
    }
}