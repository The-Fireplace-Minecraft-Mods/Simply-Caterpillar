package the_fireplace.caterpillar.common.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
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
        super(MenuInit.DECORATION.get(), id, playerInventory, extraData, DecorationContainerData.SIZE, DecorationBlockEntity.INVENTORY_SIZE);
    }

    public DecorationMenu(int id, Inventory playerInventory, DecorationBlockEntity blockEntity, ContainerData data) {
        super(MenuInit.DECORATION.get(), id, playerInventory, blockEntity, data, DecorationBlockEntity.INVENTORY_SIZE);
    }

    @Override
    protected void addSlots(IItemHandler handler) {
        int slotId = 0;

        for(int row = 0; row < 3; row++) {
            for(int column = 0; column < 3; column++) {
                if (row != 1 || column != 1) {
                    super.addSlot(new SlotItemHandler(handler, slotId++, DECORATION_SLOT_X_START + column * SLOT_SIZE_PLUS_2, DECORATION_SLOT_Y_START + row * SLOT_SIZE_PLUS_2));
                }
            }
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    public int getSelectedMap() {
        if (this.blockEntity instanceof DecorationBlockEntity decorationBlockEntity) {
            return decorationBlockEntity.getSelectedMap();
        }
        return 0;
    }

    public int getCurrentMap() {
        if (this.blockEntity instanceof DecorationBlockEntity decorationBlockEntity) {
            return decorationBlockEntity.getCurrentMap();
        }
        return 0;
    }

    public void scrollTo(int selectedMap) {
        if (this.blockEntity instanceof DecorationBlockEntity decorationBlockEntity) {
            decorationBlockEntity.setSelectedMap(selectedMap);

            for (int i = 0; i < DecorationBlockEntity.INVENTORY_MAX_SLOTS; i++) {
                this.slots.get(BE_INVENTORY_FIRST_SLOT_INDEX + i).set(decorationBlockEntity.getSelectedPlacementMap().getStackInSlot(i));
            }

            PacketHandler.sendToServer(new DecorationSyncSelectedMapC2SPacket(decorationBlockEntity.getSelectedMap(), decorationBlockEntity.getBlockPos()));
        }
    }
}
