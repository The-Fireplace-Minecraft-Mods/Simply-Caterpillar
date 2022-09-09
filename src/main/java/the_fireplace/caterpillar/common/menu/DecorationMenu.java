package the_fireplace.caterpillar.common.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import the_fireplace.caterpillar.common.block.entity.DecorationBlockEntity;
import the_fireplace.caterpillar.common.menu.syncdata.DecorationContainerData;
import the_fireplace.caterpillar.core.init.MenuInit;
import the_fireplace.caterpillar.core.network.PacketHandler;
import the_fireplace.caterpillar.core.network.packet.client.DecorationSyncSelectedMapC2SPacket;

public class DecorationMenu extends AbstractCaterpillarMenu {

    private static final int DECORATION_SLOT_X_START = 62;

    private static final int DECORATION_SLOT_Y_START = 17;

    public DecorationMenu(int id, Inventory playerInventory, FriendlyByteBuf extraData) {
        super(MenuInit.DECORATION.get(), id, playerInventory, extraData, DecorationContainerData.SIZE);
    }

    public DecorationMenu(int id, Inventory playerInventory, DecorationBlockEntity blockEntity, ContainerData data) {
        super(MenuInit.DECORATION.get(), id, playerInventory, blockEntity, data);
    }

    @Override
    protected void addSlots(IItemHandler handler) {
        for(int row = 0; row < 3; row++) {
            for(int column = 0; column < 3; column++) {
                if (row != 1 || column != 1) {
                    super.addSlot(new SlotItemHandler(handler, column + row * 3, DECORATION_SLOT_X_START + column * SLOT_SIZE_PLUS_2, DECORATION_SLOT_Y_START + row * SLOT_SIZE_PLUS_2));
                }
            }
        }

        this.scrollTo(this.getSelectedMap() / 9.0F);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot sourceSlot = slots.get(index);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;
        ItemStack sourceStack = sourceSlot.getItem();

        return sourceStack.copy();
    }

    public int getSelectedMap() {
        if (this.blockEntity instanceof DecorationBlockEntity decorationBlockEntity) {
            return decorationBlockEntity.getSelectedMap();
        }
        return 0;
    }

    public void scrollTo(float scrollOffs) {
        if (this.blockEntity instanceof DecorationBlockEntity decorationBlockEntity) {
            int i = 9;
            int j = (int)((double)(scrollOffs * (float)i) + 0.5D);
            if (j < 0) {
                j = 0;
            }

            decorationBlockEntity.setSelectedMap(j);
            decorationBlockEntity.setChanged();
            this.broadcastChanges();

            decorationBlockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
                for (int row = 0; row < 3; row++) {
                    for (int column = 0; column < 3; column++) {
                        if (row != 1 || column != 1) {
                            int placementSlotId = column + row * 3 + (decorationBlockEntity.getSelectedMap() * 9);
                            ItemStack placementStack = handler.getStackInSlot(placementSlotId);

                            int slotId = BE_INVENTORY_FIRST_SLOT_INDEX + (column + row * 3);
                            if (slotId > 39) {
                                slotId--;
                            };

                            Slot placementSlot =  this.getSlot(slotId);

                            placementSlot.set(placementStack);
                            placementSlot.setChanged();
                        }
                    }
                }
            });

            this.broadcastChanges();
            PacketHandler.sendToServer(new DecorationSyncSelectedMapC2SPacket(decorationBlockEntity.getSelectedMap(), decorationBlockEntity.getBlockPos()));
        }
    }
}
