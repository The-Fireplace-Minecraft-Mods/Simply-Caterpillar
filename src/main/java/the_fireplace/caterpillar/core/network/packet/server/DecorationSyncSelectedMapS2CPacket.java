package the_fireplace.caterpillar.core.network.packet.server;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import the_fireplace.caterpillar.common.block.entity.DecorationBlockEntity;

import java.util.function.Supplier;

public class DecorationSyncSelectedMapS2CPacket {

    private final int selectedMap;

    private final BlockPos pos;


    public DecorationSyncSelectedMapS2CPacket(int selectedMap, BlockPos pos) {
        this.selectedMap = selectedMap;
        this.pos = pos;
    }

    public DecorationSyncSelectedMapS2CPacket(FriendlyByteBuf buf) {
        this.selectedMap = buf.readInt();
        this.pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(selectedMap);
        buf.writeBlockPos(pos);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientLevel level = Minecraft.getInstance().level;

            if(level.getBlockEntity(pos) instanceof DecorationBlockEntity blockEntity) {
                blockEntity.setSelectedMap(selectedMap);
                blockEntity.setChanged();
            }
        });
        context.setPacketHandled(true);
    }
}

