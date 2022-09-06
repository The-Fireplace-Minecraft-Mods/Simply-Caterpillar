package the_fireplace.caterpillar.core.network.packet.server;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.repository.Pack;
import net.minecraftforge.network.NetworkEvent;
import the_fireplace.caterpillar.common.block.entity.DrillHeadBlockEntity;
import the_fireplace.caterpillar.common.menu.DrillHeadMenu;
import the_fireplace.caterpillar.core.network.PacketHandler;
import the_fireplace.caterpillar.core.network.packet.client.DrillHeadPowerSyncC2SPacket;

import java.util.function.Supplier;

public class DrillHeadPowerSyncS2CPacket {

    private boolean powered;

    private BlockPos pos;

    public DrillHeadPowerSyncS2CPacket(boolean powered, BlockPos pos) {
        this.powered = powered;
        this.pos = pos;
    }

    public DrillHeadPowerSyncS2CPacket(FriendlyByteBuf buf) {
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
            if(Minecraft.getInstance().level.getBlockEntity(pos) instanceof DrillHeadBlockEntity blockEntity) {
                blockEntity.setPower(powered);

                if(Minecraft.getInstance().player.containerMenu instanceof DrillHeadMenu menu && menu.blockEntity.getBlockPos().equals(pos)) {
                    menu.setPower(powered);
                }
            }
        });
        context.setPacketHandled(true);
    }
}
