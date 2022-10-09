package the_fireplace.caterpillar.common.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import the_fireplace.caterpillar.common.block.entity.DrillHeadBlockEntity;
import the_fireplace.caterpillar.common.menu.slot.CaterpillarFuelSlot;
import the_fireplace.caterpillar.common.menu.slot.SlotWithRestriction;
import the_fireplace.caterpillar.common.menu.syncdata.DrillHeadContainerData;
import the_fireplace.caterpillar.common.menu.util.CaterpillarMenuUtil;
import the_fireplace.caterpillar.core.init.MenuInit;
import the_fireplace.caterpillar.core.network.PacketHandler;
import the_fireplace.caterpillar.core.network.packet.client.DrillHeadSyncPowerC2SPacket;
import the_fireplace.caterpillar.core.network.packet.client.DrillHeadSyncSelectedConsumptionScrollC2SPacket;
import the_fireplace.caterpillar.core.network.packet.client.DrillHeadSyncSelectedGatheredScrollC2SPacket;

import static the_fireplace.caterpillar.common.block.entity.DrillHeadBlockEntity.*;

public class DrillHeadMenu extends AbstractCaterpillarMenu {

    public static final int CONSUMPTION_SLOT_X_START = 8;

    public static final int CONSUMPTION_SLOT_Y_START = 17;

    public static final int CONSUMPTION_SLOT_X_END = 60;

    public static final int CONSUMPTION_SLOT_Y_END = 69;

    public static final int GATHERED_SLOT_X_START = 116;

    public static final int GATHERED_SLOT_Y_START = 17;

    public static final int GATHERED_SLOT_X_END = 168;

    public static final int GATHERED_SLOT_Y_END = 69;

    private static final int FUEL_SLOT_X = 80;

    private static final int FUEL_SLOT_Y = 53;

    public DrillHeadMenu(int id, Inventory playerInventory, FriendlyByteBuf extraData) {
        super(MenuInit.DRILL_HEAD.get(), id, playerInventory, extraData, DrillHeadContainerData.SIZE, DrillHeadBlockEntity.INVENTORY_SIZE);
    }

    public DrillHeadMenu(int id, Inventory playerInventory, DrillHeadBlockEntity entity, DrillHeadContainerData data) {
        super(MenuInit.DRILL_HEAD.get(), id, playerInventory, entity, data, DrillHeadBlockEntity.INVENTORY_SIZE);
    }

    @Override
    protected void addSlots(IItemHandler handler) {
        int slotId = 0;

        // Drill_head Consumption slots
        for(int row = 0; row < 3; row++) {
            for(int column = 0; column < 3; column++) {
                super.addSlot(new SlotItemHandler(handler, slotId++, CONSUMPTION_SLOT_X_START + column * SLOT_SIZE_PLUS_2, CONSUMPTION_SLOT_Y_START + row * SLOT_SIZE_PLUS_2));
            }
        }

        // Drill_head fuel slot
        super.addSlot(new CaterpillarFuelSlot(handler, slotId++, FUEL_SLOT_X, FUEL_SLOT_Y));


        // Drill_head Gathered slots
        for(int row = 0; row < 3; row++) {
            for(int column = 0; column < 3; column++) {
                super.addSlot(new SlotWithRestriction(handler, slotId++, GATHERED_SLOT_X_START + column * SLOT_SIZE_PLUS_2, GATHERED_SLOT_Y_START + row * SLOT_SIZE_PLUS_2));
            }
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        final Slot sourceSlot = this.getSlot(index);

        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // This is a vanilla container slot so merge the stack into the BE inventory
            if (CaterpillarMenuUtil.isFuel(sourceStack)) {
                if (fuelSlotIsEmpty() || this.getSlot(BE_INVENTORY_FIRST_SLOT_INDEX + DrillHeadBlockEntity.FUEl_SLOT).getItem().sameItem(copyOfSourceStack)) {
                    if (!moveItemStackTo(sourceStack, BE_INVENTORY_FIRST_SLOT_INDEX + DrillHeadBlockEntity.FUEl_SLOT, BE_INVENTORY_FIRST_SLOT_INDEX + DrillHeadBlockEntity.FUEl_SLOT + 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    if (!moveItemStackTo(sourceStack, BE_INVENTORY_FIRST_SLOT_INDEX + CONSUMPTION_SLOT_START, BE_INVENTORY_FIRST_SLOT_INDEX + CONSUMPTION_SLOT_END + 1, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else {
                if (!moveItemStackTo(sourceStack, BE_INVENTORY_FIRST_SLOT_INDEX + CONSUMPTION_SLOT_START, BE_INVENTORY_FIRST_SLOT_INDEX + CONSUMPTION_SLOT_END + 1, false)) {
                    return ItemStack.EMPTY;
                }
            }
        } else {
            return super.quickMoveStack(player, index);
        }

        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }

        sourceSlot.onTake(player, sourceStack);
        return copyOfSourceStack;
    }

    public int getLitProgress() {
        if (this.blockEntity instanceof DrillHeadBlockEntity drillHeadBlockEntity) {
            return drillHeadBlockEntity.getLitProgress();
        }

        return 0;
    }

    public boolean isPowered() {
        if (this.blockEntity instanceof DrillHeadBlockEntity drillHeadBlockEntity) {
            return drillHeadBlockEntity.isPowered();
        }

        return false;
    }

    public void setPower(boolean powered) {
        if (powered) {
            this.setPowerOn();
        } else {
            this.setPowerOff();
        }
    }

    public void setPowerOn() {
        if (this.blockEntity instanceof DrillHeadBlockEntity drillHeadBlockEntity) {
            PacketHandler.sendToServer(new DrillHeadSyncPowerC2SPacket(true, drillHeadBlockEntity.getBlockPos()));
        }
    }

    public void setPowerOff() {
        if (this.blockEntity instanceof DrillHeadBlockEntity drillHeadBlockEntity) {
            PacketHandler.sendToServer(new DrillHeadSyncPowerC2SPacket(false, drillHeadBlockEntity.getBlockPos()));
        }
    }

    public boolean fuelSlotIsEmpty() {
        return this.getSlot(BE_INVENTORY_FIRST_SLOT_INDEX + DrillHeadBlockEntity.FUEl_SLOT).getItem().isEmpty();
    }

    public int getSelectedConsumptionScroll() {
        if (this.blockEntity instanceof DrillHeadBlockEntity drillHeadBlockEntity) {
            return drillHeadBlockEntity.getSelectedConsumptionScroll();
        }

        return 0;
    }

    public int getSelectedGatheredScroll() {
        if (this.blockEntity instanceof DrillHeadBlockEntity drillHeadBlockEntity) {
            return drillHeadBlockEntity.getSelectedGatheredScroll();
        }

        return 0;
    }

    public boolean canScroll() {
        return this.slots.size() > 9;
    }

    public void gatheredScrollTo(int gatheredScrollTo) {
        if (this.blockEntity instanceof DrillHeadBlockEntity drillHeadBlockEntity) {
            drillHeadBlockEntity.setSelectedGatheredScroll(gatheredScrollTo);

            PacketHandler.sendToServer(new DrillHeadSyncSelectedGatheredScrollC2SPacket(gatheredScrollTo, drillHeadBlockEntity.getBlockPos()));
        }
    }

    public void consumptionScrollTo(int consumptionScrollTo) {
        if (this.blockEntity instanceof DrillHeadBlockEntity drillHeadBlockEntity) {
            drillHeadBlockEntity.setSelectedGatheredScroll(consumptionScrollTo);

            PacketHandler.sendToServer(new DrillHeadSyncSelectedConsumptionScrollC2SPacket(consumptionScrollTo, drillHeadBlockEntity.getBlockPos()));
        }
    }
}
