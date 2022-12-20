package dev.the_fireplace.caterpillar.network.packet.server;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import dev.the_fireplace.caterpillar.block.entity.DecorationBlockEntity;

import java.util.function.Supplier;

public class DecorationSyncCurrentMapS2CPacket {

    private final int currentMap;

    private final BlockPos pos;


    public DecorationSyncCurrentMapS2CPacket(int currentMap, BlockPos pos) {
        this.currentMap = currentMap;
        this.pos = pos;
    }

    public DecorationSyncCurrentMapS2CPacket(FriendlyByteBuf buf) {
        this.currentMap = buf.readInt();
        this.pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(currentMap);
        buf.writeBlockPos(pos);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientLevel level = Minecraft.getInstance().level;

            if(level.getBlockEntity(pos) instanceof DecorationBlockEntity blockEntity) {
                blockEntity.setCurrentMap(currentMap);
            }
        });
        context.setPacketHandled(true);
    }
}

