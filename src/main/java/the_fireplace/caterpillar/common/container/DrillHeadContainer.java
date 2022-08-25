package the_fireplace.caterpillar.common.container;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.common.block.entity.DrillHeadBlockEntity;
import the_fireplace.caterpillar.common.container.syncdata.DrillHeadContainerData;
import the_fireplace.caterpillar.core.init.BlockInit;
import the_fireplace.caterpillar.core.init.ContainerInit;

public class DrillHeadContainer extends AbstractContainerMenu {

    public final ContainerLevelAccess containerAccess;

    public final ContainerData data;

    private int slotId;

    public static final int CONTAINER_SIZE = 19;

    // Client Constructor
    public DrillHeadContainer(int id, Inventory playerInventory) {
        this(id, playerInventory, new ItemStackHandler(CONTAINER_SIZE), BlockPos.ZERO, new SimpleContainerData(2));
        System.out.println("Constructing CLIENT DrillHeadContainer: " + id);
    }

    // Server Constructor
    public DrillHeadContainer(int id, Inventory playerInventory, IItemHandler slots, BlockPos pos, ContainerData data) {
        super(ContainerInit.DRILL_HEAD.get(), id);
        this.containerAccess = ContainerLevelAccess.create(playerInventory.player.level, pos);
        this.data = data;
        this.slotId = 0;

        System.out.println("Constructing SERVER DrillHeadContainer: " + id);

        final int slotSizePlus2 = 18, startX = 8, startY = 84, hotbarY = 142, consumptionX = 8, gatheredX = 116, drillHeadY = 17;

        // Drill_head Consumption slots
        for(int row = 0; row < 3; row++) {
            for(int column = 0; column < 3; column++) {
                addSlot(new SlotItemHandler(slots, slotId++, consumptionX + column * slotSizePlus2, drillHeadY + row * slotSizePlus2));
            }
        }

        // Drill_head burner slot
        // addSlot(new CaterpillarBurnerSlot(this, slots, slotId++, 80, 53));


        // Drill_head Gathered slots
        for(int row = 0; row < 3; row++) {
            for(int column = 0; column < 3; column++) {
                addSlot(new SlotItemHandler(slots, slotId++, gatheredX + column * slotSizePlus2, drillHeadY + row * slotSizePlus2));
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
    public ItemStack quickMoveStack(@NotNull Player player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        final Slot slot = getSlot(index);

        if (slot.hasItem()) {
            final ItemStack item = slot.getItem();
            itemStack = item.copy();
            if (isFuel(item)) {
                Caterpillar.LOGGER.debug("DrillHeadItem is fuel" + item);
                if (!moveItemStackTo(item, 9, this.slots.size(), false))
                    return ItemStack.EMPTY;
            } else if (index < CONTAINER_SIZE) {
                if (!moveItemStackTo(item, CONTAINER_SIZE, this.slots.size(), true))
                    return ItemStack.EMPTY;
            } else if (!moveItemStackTo(item, 0, CONTAINER_SIZE, false))
                return ItemStack.EMPTY;
            if (item.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemStack;
    }

    public static boolean isFuel(ItemStack itemStack) {
        return net.minecraftforge.common.ForgeHooks.getBurnTime(itemStack, null) > 0;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(containerAccess, player, BlockInit.DRILL_HEAD.get());
    }

    public static MenuConstructor getServerContainer(DrillHeadBlockEntity drill_head, BlockPos pos) {
        return (id, playerInventory, player) -> new DrillHeadContainer(id, playerInventory, drill_head.inventory, pos, new DrillHeadContainerData(drill_head, 2));
    }

    public boolean isLit() {
        return this.data.get(0) > 0;
    }

    public int getLitProgress() {
        int i = this.data.get(1);
        if (i == 0) {
            i = 200;
        }

        return this.data.get(0) * 13 / i;
    }
}
