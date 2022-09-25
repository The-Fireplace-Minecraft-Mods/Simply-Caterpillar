package the_fireplace.caterpillar.core.network.packet.server;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import the_fireplace.caterpillar.common.block.entity.ReinforcementBlockEntity;

import java.util.function.Supplier;

public class ReinforcementSyncSelectedReplacerS2CPacket {

    private final int selectedReplacer;

    private final BlockPos pos;


    public ReinforcementSyncSelectedReplacerS2CPacket(int selectedReplacer, BlockPos pos) {
        this.selectedReplacer = selectedReplacer;
        this.pos = pos;
    }

    public ReinforcementSyncSelectedReplacerS2CPacket(FriendlyByteBuf buf) {
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
            ClientLevel level = Minecraft.getInstance().level;

            if(level.getBlockEntity(pos) instanceof ReinforcementBlockEntity reinforcementBlockEntity) {
                reinforcementBlockEntity.setSelectedReplacer(selectedReplacer);
                reinforcementBlockEntity.setChanged();
            }
        });
        context.setPacketHandled(true);
    }
}
