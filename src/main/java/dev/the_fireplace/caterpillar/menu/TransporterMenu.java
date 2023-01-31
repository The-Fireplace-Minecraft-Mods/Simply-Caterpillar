package dev.the_fireplace.caterpillar.menu;

import dev.the_fireplace.caterpillar.block.entity.TransporterBlockEntity;
import dev.the_fireplace.caterpillar.init.MenuInit;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.Slot;

import static dev.the_fireplace.caterpillar.block.entity.TransporterBlockEntity.INVENTORY_SIZE;

public class TransporterMenu extends AbstractCaterpillarMenu {

    private static final int TRANSPORTER_SLOT_X_START = 8;

    private static final int TRANSPORTER_SLOT_Y_START = 17;

    public TransporterMenu(int id, Inventory playerInventory, FriendlyByteBuf extraData) {
        super(MenuInit.TRANSPORTER, id, playerInventory, extraData, 0, INVENTORY_SIZE);
    }

    public TransporterMenu(int id, Inventory playerInventory, TransporterBlockEntity blockEntity, ContainerData data) {
        super(MenuInit.TRANSPORTER, id, playerInventory, blockEntity, data, INVENTORY_SIZE);
    }

    @Override
    protected void addSlots(Container inventory) {
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                super.addSlot(new Slot(inventory, column + row * 9, TRANSPORTER_SLOT_X_START + column * SLOT_SIZE_PLUS_2, TRANSPORTER_SLOT_Y_START + row * SLOT_SIZE_PLUS_2));
            }
        }
    }

    public boolean hasMinecartChest() {
        if (this.blockEntity instanceof TransporterBlockEntity transporterBlockEntity) {
            return transporterBlockEntity.hasMinecartChest();
        }

        return false;
    }
}
