package the_fireplace.caterpillar.core.network.packet.server;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import the_fireplace.caterpillar.common.block.entity.DrillHeadBlockEntity;
import the_fireplace.caterpillar.common.menu.DrillHeadMenu;

import java.util.function.Supplier;


public class DrillHeadSyncLitS2CPacket {

    private final int litTime;

    private final int litDuration;

    private final BlockPos pos;

    public DrillHeadSyncLitS2CPacket(int litTime, int litDuration, BlockPos pos) {
        this.litTime = litTime;
        this.litDuration = litDuration;
        this.pos = pos;
    }

    public DrillHeadSyncLitS2CPacket(FriendlyByteBuf buf) {
        this.litTime = buf.readInt();
        this.litDuration = buf.readInt();
        this.pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(litTime);
        buf.writeInt(litDuration);
        buf.writeBlockPos(pos);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientLevel level = Minecraft.getInstance().level;
            LocalPlayer player = Minecraft.getInstance().player;

            if(level.getBlockEntity(pos) instanceof DrillHeadBlockEntity blockEntity) {
                blockEntity.setLitTime(litTime);
                blockEntity.setLitDuration(litDuration);

                if(player.containerMenu instanceof DrillHeadMenu menu && menu.blockEntity.getBlockPos().equals(pos)) {
                    if (menu.blockEntity instanceof DrillHeadBlockEntity menuBlockEntity) {
                        menuBlockEntity.setLitTime(litTime);
                        menuBlockEntity.setLitDuration(litDuration);
                    }
                }
            }
        });
        context.setPacketHandled(true);
    }
}

