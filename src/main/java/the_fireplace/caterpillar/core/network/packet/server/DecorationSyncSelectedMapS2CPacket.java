package the_fireplace.caterpillar.core.network.packet.server;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.network.NetworkEvent;
import the_fireplace.caterpillar.common.block.entity.DecorationBlockEntity;
import the_fireplace.caterpillar.common.menu.DecorationMenu;

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
            LocalPlayer player = Minecraft.getInstance().player;


            if(level.getBlockEntity(pos) instanceof DecorationBlockEntity blockEntity) {
                blockEntity.setSelectedMap(selectedMap);
                blockEntity.setChanged();

                if (player.containerMenu instanceof DecorationMenu menu) {
                    if (menu.blockEntity instanceof DecorationBlockEntity menuBlockEntity) {
                        menuBlockEntity.setSelectedMap(selectedMap);
                        menuBlockEntity.setChanged();
                    }
                }

                blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
                    for(int row = 0; row < 3; row++) {
                        for(int column = 0; column < 3; column++) {
                            if (row != 1 || column != 1) {
                                int placementSlotId = column + row * 3 + (blockEntity.getSelectedMap() * 9);
                                ItemStack placementStack = handler.getStackInSlot(placementSlotId);
                                Slot decorationSlot = player.containerMenu.getSlot(column + row * 3);
                                decorationSlot.set(placementStack);
                                player.containerMenu.broadcastChanges();
                                System.out.println("placementSlotId: " + placementSlotId);
                                blockEntity.setChanged();
                            }
                        }
                    }
                });
            }
        });
        context.setPacketHandled(true);
    }
}

