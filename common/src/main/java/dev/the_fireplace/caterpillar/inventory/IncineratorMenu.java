package dev.the_fireplace.caterpillar.inventory;

import dev.the_fireplace.caterpillar.block.entity.IncineratorBlockEntity;
import dev.the_fireplace.caterpillar.registry.MenuTypesRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.Slot;

public class IncineratorMenu extends AbstractCaterpillarMenu {

    private static final int INCINERATOR_SLOT_X_START = 62;
    private static final int INCINERATOR_SLOT_Y_START = 17;

    public IncineratorMenu(int containerId, Inventory playerInventory, FriendlyByteBuf extraData) {
        super(MenuTypesRegistry.INCINERATOR.get(), containerId, playerInventory, extraData, 0);
    }

    public IncineratorMenu(int containerId, Inventory playerInventory, IncineratorBlockEntity blockEntity, ContainerData data) {
        super(MenuTypesRegistry.INCINERATOR.get(), containerId, playerInventory, blockEntity, data);
    }

    @Override
    protected void addSlots(Container container) {
        for(int row = 0; row < 3; row++) {
            for(int column = 0; column < 3; column++) {
                this.addSlot(new Slot(container, column + row * 3, INCINERATOR_SLOT_X_START + column * SLOT_SIZE_PLUS_2, INCINERATOR_SLOT_Y_START + row * SLOT_SIZE_PLUS_2));
            }
        }
    }
}
