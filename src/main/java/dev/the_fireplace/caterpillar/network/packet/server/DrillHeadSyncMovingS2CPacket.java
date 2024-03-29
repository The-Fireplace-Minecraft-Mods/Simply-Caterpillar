package dev.the_fireplace.caterpillar.network.packet.server;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import dev.the_fireplace.caterpillar.block.entity.DrillHeadBlockEntity;

import java.util.function.Supplier;

public class DrillHeadSyncMovingS2CPacket {

    private final boolean moving;

    private final BlockPos pos;

    public DrillHeadSyncMovingS2CPacket(boolean moving, BlockPos pos) {
        this.moving = moving;
        this.pos = pos;
    }

    public DrillHeadSyncMovingS2CPacket(FriendlyByteBuf buf) {
        this.moving = buf.readBoolean();
        this.pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBoolean(moving);
        buf.writeBlockPos(pos);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientLevel level = Minecraft.getInstance().level;

            if(level.getBlockEntity(pos) instanceof DrillHeadBlockEntity blockEntity) {
                blockEntity.setMoving(moving);
            }
        });
        context.setPacketHandled(true);
    }
}
