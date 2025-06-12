package dev.the_fireplace.caterpillar.inventory;

import dev.the_fireplace.caterpillar.block.entity.TransporterBlockEntity;
import dev.the_fireplace.caterpillar.registry.MenuTypesRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.Slot;

public class TransporterMenu extends AbstractCaterpillarMenu {

    private static final int TRANSPORTER_SLOT_X_START = 8;
    private static final int TRANSPORTER_SLOT_Y_START = 17;

    public TransporterMenu(int containerId, Inventory playerInventory, FriendlyByteBuf extraData) {
        super(MenuTypesRegistry.TRANSPORTER.get(), containerId, playerInventory, extraData, 0);
    }

    public TransporterMenu(int containerId, Inventory playerInventory, TransporterBlockEntity blockEntity, ContainerData data) {
        super(MenuTypesRegistry.TRANSPORTER.get(), containerId, playerInventory, blockEntity, data);
    }

    @Override
    protected void addSlots(Container container) {
        for(int row = 0; row < 3; row++) {
            for(int column = 0; column < 9; column++) {
                this.addSlot(new Slot(container, column + row * 9, TRANSPORTER_SLOT_X_START + column * SLOT_SIZE_PLUS_2, TRANSPORTER_SLOT_Y_START + row * SLOT_SIZE_PLUS_2));
            }
        }
    }
}
