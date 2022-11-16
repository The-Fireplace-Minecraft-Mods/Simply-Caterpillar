package the_fireplace.caterpillar.common.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;
import the_fireplace.caterpillar.common.block.entity.IncineratorBlockEntity;
import the_fireplace.caterpillar.core.init.MenuInit;

import static the_fireplace.caterpillar.common.block.entity.IncineratorBlockEntity.INVENTORY_SIZE;

public class IncineratorMenu extends AbstractCaterpillarMenu {

    private static final int INCINERATOR_SLOT_X_START = 62;

    private static final int INCINERATOR_SLOT_Y_START = 17;

    public IncineratorMenu(int id, Inventory playerInventory, FriendlyByteBuf extraData) {
        super(MenuInit.INCINERATOR.get(), id, playerInventory, extraData, 0, INVENTORY_SIZE);
    }

    public IncineratorMenu(int id, Inventory playerInventory, IncineratorBlockEntity blockEntity, ContainerData data) {
        super(MenuInit.INCINERATOR.get(), id, playerInventory, blockEntity, data, INVENTORY_SIZE);
    }

    @Override
    protected void addSlots(IItemHandler handler) {
        for(int row = 0; row < 3; row++) {
            for(int column = 0; column < 3; column++) {
                super.addSlot(new SlotItemHandler(handler, column + row * 3, INCINERATOR_SLOT_X_START + column * SLOT_SIZE_PLUS_2, INCINERATOR_SLOT_Y_START + row * SLOT_SIZE_PLUS_2));
            }
        }
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        Slot sourceSlot = slots.get(index);

        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();
        copyOfSourceStack.setCount(1);

        // Check if the slot clicked is one of the vanilla container slots
        if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // This is a vanilla container slot so merge the stack into the BE inventory
            if (!itemIsAlreadyInInventory(sourceStack.getItem())) {
                if (!moveItemStackTo(copyOfSourceStack, BE_INVENTORY_FIRST_SLOT_INDEX, BE_INVENTORY_FIRST_SLOT_INDEX + INVENTORY_SIZE, false)) {
                    return ItemStack.EMPTY;
                }
            }
        } else {
            return super.quickMoveStack(player, index);
        }

        return ItemStack.EMPTY;
    }

    public boolean itemIsAlreadyInInventory(Item item) {
        for (int i = 0; i < INVENTORY_SIZE; i++) {
            if (this.blockEntity.getStackInSlot(i).getItem() == item) {
                return true;
            }
        }
        return false;
    }
}
