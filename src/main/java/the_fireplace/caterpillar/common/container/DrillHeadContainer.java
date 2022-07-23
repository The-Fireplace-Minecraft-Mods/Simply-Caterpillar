package the_fireplace.caterpillar.common.container;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import the_fireplace.caterpillar.common.block.entity.DrillHeadBlockEntity;
import the_fireplace.caterpillar.common.container.syncdata.DrillHeadContainerData;
import the_fireplace.caterpillar.core.init.BlockInit;
import the_fireplace.caterpillar.core.init.ContainerInit;

public class DrillHeadContainer extends AbstractContainerMenu {

    private final ContainerLevelAccess containerAccess;

    public final ContainerData data;

    public static final int SLOT_SIZE = 25;

    // Client Constructor
    public DrillHeadContainer(int id, Inventory playerInventory) {
        this(id, playerInventory, new ItemStackHandler(25), BlockPos.ZERO, new SimpleContainerData(1));
    }

    // Server Constructor
    public DrillHeadContainer(int id, Inventory playerInventory, IItemHandler slots, BlockPos pos, ContainerData data) {
        super(ContainerInit.DRILL_HEAD.get(), id);
        this.containerAccess = ContainerLevelAccess.create(playerInventory.player.level, pos);
        this.data = data;

        final int slotSizePlus2 = 18, startX = 8, startY = 84, hotbarY = 142, consumptionX = 8, gatheredX = 116, drillHeadY = 25;

        // Drill_head Consumption slots
        for(int row = 0; row < 3; row++) {
            for(int column = 0; column < 3; column++) {
                addSlot(new SlotItemHandler(slots, row * 3 + column, consumptionX + column * slotSizePlus2, drillHeadY + row * slotSizePlus2));
            }
        }

        // Drill_head burner slot
        addSlot(new SlotItemHandler(slots, 0, 80, 61));

        // Drill_head Gathered slots
        for(int row = 0; row < 3; row++) {
            for(int column = 0; column < 3; column++) {
                addSlot(new SlotItemHandler(slots, row * 3 + column, gatheredX + column * slotSizePlus2, drillHeadY + row * slotSizePlus2));
            }
        }

        // Player Inventory slots
        for(int row = 0; row < 3; row++) {
            for(int column = 0; column < 9; column++) {
                addSlot(new Slot(playerInventory, row + column * 9 + 9 , startX + column * slotSizePlus2, startY + row * slotSizePlus2));
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
        return stillValid(containerAccess, player, BlockInit.DRILL_HEAD.get());
    }

    public static MenuConstructor getServerContainer(DrillHeadBlockEntity drill_head, BlockPos pos) {
        return (id, playerInventory, player) -> new DrillHeadContainer(id, playerInventory, drill_head.inventory, pos, new DrillHeadContainerData(drill_head, 1));
    }
}
