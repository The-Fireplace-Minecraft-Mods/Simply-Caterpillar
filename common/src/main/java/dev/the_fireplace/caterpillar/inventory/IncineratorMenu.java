package dev.the_fireplace.caterpillar.inventory;

import dev.the_fireplace.caterpillar.block.entity.IncineratorBlockEntity;
import dev.the_fireplace.caterpillar.registry.MenuTypesRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import static dev.the_fireplace.caterpillar.block.entity.IncineratorBlockEntity.INVENTORY_SIZE;

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
                super.addSlot(new Slot(container, column + row * 3, INCINERATOR_SLOT_X_START + column * SLOT_SIZE_PLUS_2, INCINERATOR_SLOT_Y_START + row * SLOT_SIZE_PLUS_2));
            }
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot.hasItem()) {
            ItemStack stackInSlot = slot.getItem();
            itemStack = itemStack.copy();

            if (index < VANILLA_SLOT_END_INDEX && !this.inventoryContainsItem(stackInSlot.getItem())) {
                itemStack.setCount(1);
                if (!this.moveItemStackTo(itemStack, BE_INVENTORY_SLOT_START_INDEX, BE_INVENTORY_SLOT_END_INDEX, false)) return ItemStack.EMPTY;
            }
        }

        return itemStack;
    }

    private boolean inventoryContainsItem(Item item) {
        for (int i = 0; i < INVENTORY_SIZE; i++) {
            ItemStack stack = this.blockEntity.getItem(i);
            if (stack.getItem() == item) {
                return true;
            }
        }
        return false;
    }
}
