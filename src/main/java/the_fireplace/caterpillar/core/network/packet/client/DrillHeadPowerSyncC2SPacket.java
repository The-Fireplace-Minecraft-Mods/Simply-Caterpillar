package the_fireplace.caterpillar.core.network.packet.client;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import the_fireplace.caterpillar.common.block.entity.DrillHeadBlockEntity;
import the_fireplace.caterpillar.common.menu.DrillHeadMenu;
import the_fireplace.caterpillar.core.network.PacketHandler;
import the_fireplace.caterpillar.core.network.packet.server.DrillHeadSyncPowerS2CPacket;

import java.util.function.Supplier;

public class DrillHeadPowerSyncC2SPacket {

    private final boolean powered;

    private final BlockPos pos;

    public DrillHeadPowerSyncC2SPacket(boolean powered, BlockPos pos) {
        this.powered = powered;
        this.pos = pos;
    }

    public DrillHeadPowerSyncC2SPacket(FriendlyByteBuf buf) {
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
                drillHeadBlockEntity.setChanged();

                if(player.containerMenu instanceof DrillHeadMenu menu && menu.blockEntity.getBlockPos().equals(pos)) {
                    PacketHandler.sendToClients(new DrillHeadSyncPowerS2CPacket(drillHeadBlockEntity.isPowered(), drillHeadBlockEntity.getBlockPos()));
                }
            }
        });
        context.setPacketHandled(true);
    }
}
