package the_fireplace.caterpillar.common.container;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import the_fireplace.caterpillar.common.block.entity.DecorationBlockEntity;
import the_fireplace.caterpillar.common.container.syncdata.DecorationContainerData;
import the_fireplace.caterpillar.core.init.BlockInit;
import the_fireplace.caterpillar.core.init.ContainerInit;

public class DecorationContainer extends AbstractContainerMenu {

    private final ContainerLevelAccess containerAccess;

    public final ContainerData data;
    private int slotId;

    public static final int SLOT_SIZE = 8;

    // Client Constructor
    public DecorationContainer(int id, Inventory playerInventory) {
        this(id, playerInventory, new ItemStackHandler(SLOT_SIZE), BlockPos.ZERO, new SimpleContainerData(1));
    }

    // Server Constructor
    public DecorationContainer(int id, Inventory playerInventory, IItemHandler slots, BlockPos pos, ContainerData data) {
        super(ContainerInit.DECORATION.get(), id);
        this.containerAccess = ContainerLevelAccess.create(playerInventory.player.level, pos);
        this.data = data;
        this.slotId = 0;

        final int slotSizePlus2 = 18, startX = 8, startY = 84, hotbarY = 142, decorationX = 62, decorationY = 17;

        // Decoration slots
        for(int row = 0; row < 3; row++) {
            for(int column = 0; column < 3; column++) {
                if (row != 1 || column != 1) {
                    addSlot(new SlotItemHandler(slots, slotId++, decorationX + column * slotSizePlus2, decorationY + row * slotSizePlus2));
                }
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
        return stillValid(containerAccess, player, BlockInit.DECORATION.get());
    }

    public static MenuConstructor getServerContainer(DecorationBlockEntity decoration, BlockPos pos) {
        return (id, playerInventory, player) -> new DecorationContainer(id, playerInventory, decoration.inventory, pos, new DecorationContainerData(decoration, 1));
    }
}