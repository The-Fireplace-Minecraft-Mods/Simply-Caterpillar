package dev.the_fireplace.caterpillar.network.packet.server;

import dev.the_fireplace.caterpillar.menu.DrillHeadMenu;
import dev.the_fireplace.caterpillar.menu.util.DrillHeadMenuPart;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class DrillHeadRefreshInventoryS2CPacket {

    private final BlockPos pos;

    private final DrillHeadMenuPart menuPart;

    public DrillHeadRefreshInventoryS2CPacket(BlockPos pos, DrillHeadMenuPart menuPart) {
        this.pos = pos;
        this.menuPart = menuPart;
    }

    public DrillHeadRefreshInventoryS2CPacket(FriendlyByteBuf buf) {
        this.pos = buf.readBlockPos();
        this.menuPart = DrillHeadMenuPart.valueOf(buf.readUtf());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeUtf(menuPart.name());
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            LocalPlayer player = Minecraft.getInstance().player;

            if (player.containerMenu instanceof DrillHeadMenu drillHeadMenu && drillHeadMenu.blockEntity.getBlockPos().equals(pos)) {
                if (menuPart == DrillHeadMenuPart.CONSUMPTION) {
                    int i = 3;
                    int j = (int) ((double) (drillHeadMenu.getConsumptionScrollOffs() * (float) i) + 0.5D);
                    if (j < 0) {
                        j = 0;
                    }
                    int consumptionScrollTo = j;

                    drillHeadMenu.consumptionScrollTo(consumptionScrollTo);
                } else if (menuPart == DrillHeadMenuPart.GATHERED) {
                    int i = 3;
                    int j = (int) ((double) (drillHeadMenu.getGatheredScrollOffs() * (float) i) + 0.5D);
                    if (j < 0) {
                        j = 0;
                    }
                    int gatheredScrollTo = j;

                    drillHeadMenu.gatheredScrollTo(gatheredScrollTo);
                }
            }
        });
        context.setPacketHandled(true);
    }
}
