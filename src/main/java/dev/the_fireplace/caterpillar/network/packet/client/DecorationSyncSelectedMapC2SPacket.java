package dev.the_fireplace.caterpillar.network.packet.client;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import dev.the_fireplace.caterpillar.block.entity.DecorationBlockEntity;

import java.util.function.Supplier;


public class DecorationSyncSelectedMapC2SPacket {

    private final int selectedMap;

    private final BlockPos pos;


    public DecorationSyncSelectedMapC2SPacket(int selectedMap, BlockPos pos) {
        this.selectedMap = selectedMap;
        this.pos = pos;
    }

    public DecorationSyncSelectedMapC2SPacket(FriendlyByteBuf buf) {
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
            ServerPlayer player = context.getSender();
            ServerLevel level = player.getLevel();

            if(level.getBlockEntity(pos) instanceof DecorationBlockEntity blockEntity) {
                blockEntity.setSelectedMap(selectedMap);
            }
        });
        context.setPacketHandled(true);
    }
}
