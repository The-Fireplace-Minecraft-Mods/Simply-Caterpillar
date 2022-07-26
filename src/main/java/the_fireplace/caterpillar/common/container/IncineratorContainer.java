package the_fireplace.caterpillar.common.container;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import the_fireplace.caterpillar.common.block.entity.IncineratorBlockEntity;
import the_fireplace.caterpillar.common.container.syncdata.IncineratorContainerData;
import the_fireplace.caterpillar.core.init.BlockInit;
import the_fireplace.caterpillar.core.init.ContainerInit;

public class IncineratorContainer extends AbstractContainerMenu {

    private final ContainerLevelAccess containerAccess;

    public final ContainerData data;

    public static final int SLOT_SIZE = 9;

    // Client Constructor
    public IncineratorContainer(int id, Inventory playerInventory) {
        this(id, playerInventory, new ItemStackHandler(SLOT_SIZE), BlockPos.ZERO, new SimpleContainerData(1));
    }

    // Server Constructor
    public IncineratorContainer(int id, Inventory playerInventory, IItemHandler slots, BlockPos pos, ContainerData data) {
        super(ContainerInit.INCINERATOR.get(), id);
        this.containerAccess = ContainerLevelAccess.create(playerInventory.player.level, pos);
        this.data = data;

        final int slotSizePlus2 = 18, startX = 8, startY = 84, hotbarY = 142, incineratorX = 62, incineratorY = 17;

        // Incinerator slots
        for(int row = 0; row < 3; row++) {
            for(int column = 0; column < 3; column++) {
                addSlot(new SlotItemHandler(slots, column + row * 3, incineratorX + column * slotSizePlus2, incineratorY + row * slotSizePlus2));
            }
        }

        // Player Inventory slots
        for(int row = 0; row < 3; row++) {
            for(int column = 0; column < 9; column++) {
                addSlot(new Slot(playerInventory, column + row * 9 + 9, startX + column * slotSizePlus2, startY + row * slotSizePlus2));
            }
        }

        // Player Hotbar slots
        for(int column = 0; column < 9; column++) {
            addSlot(new Slot(playerInventory, column, startX + column * slotSizePlus2, hotbarY));
        }

        addDataSlots(data);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        var retStack = ItemStack.EMPTY;
        final Slot slot = getSlot(index);

        if (slot.hasItem()) {
            final ItemStack item = slot.getItem();
            retStack = item.copy();
            if (index < SLOT_SIZE) {
                if (!moveItemStackTo(item, SLOT_SIZE, this.slots.size(), true))
                    return ItemStack.EMPTY;
            } else if (!moveItemStackTo(item, 0, SLOT_SIZE, false))
                return ItemStack.EMPTY;

            if (item.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return retStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(containerAccess, player, BlockInit.INCINERATOR.get());
    }

    public static MenuConstructor getServerContainer(IncineratorBlockEntity incinerator, BlockPos pos) {
        return (id, playerInventory, player) -> new IncineratorContainer(id, playerInventory, incinerator.inventory, pos, new IncineratorContainerData(incinerator, 1));
    }
}
