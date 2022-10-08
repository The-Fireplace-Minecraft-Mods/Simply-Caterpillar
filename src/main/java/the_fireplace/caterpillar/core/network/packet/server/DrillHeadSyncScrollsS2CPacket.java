package the_fireplace.caterpillar.core.network.packet.server;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import the_fireplace.caterpillar.common.block.entity.DrillHeadBlockEntity;

import java.util.function.Supplier;

public class DrillHeadSyncScrollsS2CPacket {

    private int selectedConsumptionScroll;

    private int selectedGatheredScroll;

    private final BlockPos pos;


    public DrillHeadSyncScrollsS2CPacket(int selectedConsumptionScroll, int selectedGatheredScroll, BlockPos pos) {
        this.selectedConsumptionScroll = selectedConsumptionScroll;
        this.selectedGatheredScroll = selectedGatheredScroll;
        this.pos = pos;
    }

    public DrillHeadSyncScrollsS2CPacket(FriendlyByteBuf buf) {
        this.selectedConsumptionScroll = buf.readInt();
        this.selectedGatheredScroll = buf.readInt();
        this.pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(selectedConsumptionScroll);
        buf.writeInt(selectedGatheredScroll);
        buf.writeBlockPos(pos);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientLevel level = Minecraft.getInstance().level;

            if (level.getBlockEntity(pos) instanceof DrillHeadBlockEntity drillHeadBlockEntity) {
                drillHeadBlockEntity.setSelectedConsumptionScroll(selectedConsumptionScroll);
                drillHeadBlockEntity.setSelectedGatheredScroll(selectedGatheredScroll);
                drillHeadBlockEntity.setChanged();
            }
        });
        context.setPacketHandled(true);
    }
}