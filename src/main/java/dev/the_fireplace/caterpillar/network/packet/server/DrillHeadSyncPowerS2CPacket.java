package dev.the_fireplace.caterpillar.network.packet.server;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import dev.the_fireplace.caterpillar.block.entity.DrillHeadBlockEntity;

import java.util.function.Supplier;

public class DrillHeadSyncPowerS2CPacket {

    private final boolean powered;

    private final BlockPos pos;

    public DrillHeadSyncPowerS2CPacket(boolean powered, BlockPos pos) {
        this.powered = powered;
        this.pos = pos;
    }

    public DrillHeadSyncPowerS2CPacket(FriendlyByteBuf buf) {
        this.powered = buf.readBoolean();
        this.pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBoolean(powered);
        buf.writeBlockPos(pos);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientLevel level = Minecraft.getInstance().level;

            if(level.getBlockEntity(pos) instanceof DrillHeadBlockEntity blockEntity) {
                blockEntity.setPower(powered);
            }
        });
        context.setPacketHandled(true);
    }
}
