package dev.the_fireplace.caterpillar.network.packet.client;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import dev.the_fireplace.caterpillar.block.entity.DrillHeadBlockEntity;
import dev.the_fireplace.caterpillar.network.PacketHandler;
import dev.the_fireplace.caterpillar.network.packet.server.DrillHeadSyncPowerS2CPacket;

import java.util.function.Supplier;

public class DrillHeadSyncPowerC2SPacket {

    private final boolean powered;

    private final BlockPos pos;

    public DrillHeadSyncPowerC2SPacket(boolean powered, BlockPos pos) {
        this.powered = powered;
        this.pos = pos;
    }

    public DrillHeadSyncPowerC2SPacket(FriendlyByteBuf buf) {
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
            ServerPlayer player = context.getSender();
            ServerLevel level = player.getLevel();

            if(level.getBlockEntity(pos) instanceof DrillHeadBlockEntity drillHeadBlockEntity) {
                drillHeadBlockEntity.setPower(powered);

                PacketHandler.sendToClients(new DrillHeadSyncPowerS2CPacket(drillHeadBlockEntity.isPowered(), drillHeadBlockEntity.getBlockPos()));
            }
        });
        context.setPacketHandled(true);
    }
}
