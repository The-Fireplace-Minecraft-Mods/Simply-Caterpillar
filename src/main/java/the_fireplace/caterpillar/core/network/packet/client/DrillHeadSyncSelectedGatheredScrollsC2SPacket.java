package the_fireplace.caterpillar.core.network.packet.client;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import the_fireplace.caterpillar.common.block.entity.DrillHeadBlockEntity;

import java.util.function.Supplier;

public class DrillHeadSyncSelectedGatheredScrollsC2SPacket {
    private final int selectedGatheredScrolls;

    private final BlockPos pos;

    public DrillHeadSyncSelectedGatheredScrollsC2SPacket(int selectedGatheredScrolls, BlockPos pos) {
        this.selectedGatheredScrolls = selectedGatheredScrolls;
        this.pos = pos;
    }

    public DrillHeadSyncSelectedGatheredScrollsC2SPacket(FriendlyByteBuf buf) {
        this.selectedGatheredScrolls = buf.readInt();
        this.pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(selectedGatheredScrolls);
        buf.writeBlockPos(pos);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            ServerLevel level = player.getLevel();

            if(level.getBlockEntity(pos) instanceof DrillHeadBlockEntity drillHeadBlockEntity) {
                drillHeadBlockEntity.setSelectedGatheredScroll(selectedGatheredScrolls);
            }
        });
        context.setPacketHandled(true);
    }
}
