package dev.the_fireplace.caterpillar.menu;

import dev.the_fireplace.caterpillar.block.entity.DrillHeadBlockEntity;
import dev.the_fireplace.caterpillar.block.entity.StorageBlockEntity;
import dev.the_fireplace.caterpillar.init.MenuInit;
import dev.the_fireplace.caterpillar.menu.slot.CaterpillarFuelSlot;
import dev.the_fireplace.caterpillar.menu.slot.FakeSlot;
import dev.the_fireplace.caterpillar.menu.syncdata.DrillHeadContainerData;
import dev.the_fireplace.caterpillar.network.packet.client.CaterpillarSyncCarriedC2SPacket;
import dev.the_fireplace.caterpillar.network.packet.client.CaterpillarSyncSlotC2SPacket;
import dev.the_fireplace.caterpillar.network.packet.client.DrillHeadSyncPowerC2SPacket;
import dev.the_fireplace.caterpillar.network.packet.client.MinecraftSyncSlotC2SPacket;
import dev.the_fireplace.caterpillar.network.packet.server.CaterpillarSyncInventoryS2CPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import static dev.the_fireplace.caterpillar.block.entity.DrillHeadBlockEntity.*;

public class DrillHeadMenu extends AbstractScrollableMenu {

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

    private float gatheredScrollOffs;

    private boolean gatheredScrolling;

    public DrillHeadMenu(int id, Inventory playerInventory, FriendlyByteBuf extraData) {
        super(MenuInit.DRILL_HEAD, id, playerInventory, extraData, DrillHeadContainerData.SIZE, DrillHeadBlockEntity.INVENTORY_SIZE);

        this.consumptionScrollTo(0);
        this.gatheredScrollTo(0);
    }

    public DrillHeadMenu(int id, Inventory playerInventory, DrillHeadBlockEntity entity, DrillHeadContainerData data) {
        super(MenuInit.DRILL_HEAD, id, playerInventory, entity, data, DrillHeadBlockEntity.INVENTORY_SIZE);

        StorageBlockEntity storageBlockEntity = this.getStorageBlockEntity();
        if (storageBlockEntity != null && !storageBlockEntity.getLevel().isClientSide()) {
            CaterpillarSyncInventoryS2CPacket.send((ServerLevel) storageBlockEntity.getLevel(), storageBlockEntity.inventory, storageBlockEntity.getBlockPos());
        }
    }

    @Override
    protected void addSlots(Container inventory) {
        int slotId = 0;

        // Drill_head fuel slot
        super.addSlot(new CaterpillarFuelSlot(inventory, slotId++, FUEL_SLOT_X, FUEL_SLOT_Y));

        // Drill_head Consumption slots
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                super.addSlot(new FakeSlot(inventory, slotId++, CONSUMPTION_SLOT_X_START + column * SLOT_SIZE_PLUS_2, CONSUMPTION_SLOT_Y_START + row * SLOT_SIZE_PLUS_2));
            }
        }

        // Drill_head Gathered slots
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                super.addSlot(new FakeSlot(inventory, slotId++, GATHERED_SLOT_X_START + column * SLOT_SIZE_PLUS_2, GATHERED_SLOT_Y_START + row * SLOT_SIZE_PLUS_2));
            }
        }
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        final Slot sourceSlot = this.getSlot(index);

        if (!sourceSlot.hasItem()) return ItemStack.EMPTY;
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // This is a vanilla container slot so merge the stack into the BE inventory
            if (AbstractCaterpillarMenu.isFuel(sourceStack)) {
                CaterpillarFuelSlot fuelSlot = (CaterpillarFuelSlot) this.getSlot(BE_INVENTORY_FIRST_SLOT_INDEX + DrillHeadBlockEntity.FUEl_SLOT);
                if (fuelSlotIsEmpty() || ItemStack.isSameItemSameTags(fuelSlot.getItem(), copyOfSourceStack) && fuelSlot.getItem().getCount() < fuelSlot.getMaxStackSize()) {
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
            if (this.blockEntity.getLevel().isClientSide()) {
                MinecraftSyncSlotC2SPacket.send(index, ItemStack.EMPTY);
            }
        } else {
            sourceSlot.setChanged();
        }

        sourceSlot.onTake(player, sourceStack);
        return copyOfSourceStack;
    }

    @Override
    public boolean moveItemStackTo(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection) {
        StorageBlockEntity storageBlockEntity = null;
        if (this.isDrillHeadSlot(startIndex) && this.isDrillHeadSlot(endIndex - 1)) {
            storageBlockEntity = this.getStorageBlockEntity();
        }

        boolean flag = false;
        int i = startIndex;
        if (reverseDirection) {
            i = endIndex - 1;
        }

        if (stack.isStackable()) {
            while (!stack.isEmpty()) {
                if (reverseDirection) {
                    if (i < startIndex) {
                        break;
                    }
                } else if (i >= endIndex) {
                    break;
                }

                Slot slot = this.slots.get(i);
                ItemStack itemstack = slot.getItem();
                if (!itemstack.isEmpty() && ItemStack.isSameItemSameTags(stack, itemstack)) {
                    int j = itemstack.getCount() + stack.getCount();
                    int maxSize = Math.min(slot.getMaxStackSize(), stack.getMaxStackSize());
                    if (j <= maxSize) {
                        stack.setCount(0);
                        itemstack.setCount(j);
                        if (slot instanceof FakeSlot) {
                            this.syncDrillHeadSlot(i, itemstack);
                        } else {
                            slot.setChanged();
                            MinecraftSyncSlotC2SPacket.send(i, itemstack);
                        }
                        flag = true;
                    } else if (itemstack.getCount() < maxSize) {
                        stack.shrink(maxSize - itemstack.getCount());
                        itemstack.setCount(maxSize);
                        if (slot instanceof FakeSlot) {
                            this.syncDrillHeadSlot(i, itemstack);
                        } else {
                            slot.setChanged();
                            MinecraftSyncSlotC2SPacket.send(i, itemstack);
                        }
                        flag = true;
                    }
                }

                if (reverseDirection) {
                    --i;
                } else {
                    ++i;
                }
            }
        }

        // Check storage if there is item stack with same item already
        if (storageBlockEntity != null && this.blockEntity.getLevel().isClientSide()) {
            if (!stack.isEmpty()) {
                for (int slotId = StorageBlockEntity.CONSUMPTION_SLOT_START; slotId <= StorageBlockEntity.CONSUMPTION_SLOT_END; slotId++) {
                    ItemStack storageStack = storageBlockEntity.getItem(slotId);
                    if (!storageStack.isEmpty() && ItemStack.isSameItemSameTags(stack, storageStack)) {
                        int j = storageStack.getCount() + stack.getCount();
                        int maxSize = Math.min(storageStack.getMaxStackSize(), stack.getMaxStackSize());
                        if (j <= maxSize) {
                            stack.setCount(0);
                            storageStack.setCount(j);

                            this.setStorageSlot(slotId, storageStack);

                            flag = true;
                        } else if (storageStack.getCount() < maxSize) {
                            stack.shrink(maxSize - storageStack.getCount());
                            storageStack.setCount(maxSize);

                            this.setStorageSlot(slotId, storageStack);

                            flag = true;
                        }
                    }
                }
            }
        }

        if (!stack.isEmpty()) {
            if (reverseDirection) {
                i = endIndex - 1;
            } else {
                i = startIndex;
            }

            while (true) {
                if (reverseDirection) {
                    if (i < startIndex) {
                        break;
                    }
                } else if (i >= endIndex) {
                    break;
                }

                Slot slot1 = this.slots.get(i);
                ItemStack itemstack1 = slot1.getItem();
                if (itemstack1.isEmpty()) {
                    if (stack.getCount() > slot1.getMaxStackSize()) {
                        if (slot1 instanceof FakeSlot fakeSlot) {
                            fakeSlot.setDisplayStack(stack.split(slot1.getMaxStackSize()));
                        } else {
                            slot1.set(stack.split(slot1.getMaxStackSize()));
                        }
                    } else {
                        if (slot1 instanceof FakeSlot fakeSlot) {
                            fakeSlot.setDisplayStack(stack.split(stack.getCount()));
                        } else {
                            slot1.set(stack.split(stack.getCount()));
                        }
                    }

                    if (slot1 instanceof FakeSlot fakeSlot) {
                        this.syncDrillHeadSlot(i, fakeSlot.getItem());
                    } else {
                        slot1.setChanged();
                        MinecraftSyncSlotC2SPacket.send(i, slot1.getItem());
                    }
                    flag = true;
                    break;
                }

                if (reverseDirection) {
                    --i;
                } else {
                    ++i;
                }
            }
        }

        // Check if storage has empty space
        if (storageBlockEntity != null && this.blockEntity.getLevel().isClientSide()) {
            if (!stack.isEmpty()) {
                for (int slotId = StorageBlockEntity.CONSUMPTION_SLOT_START; slotId <= StorageBlockEntity.CONSUMPTION_SLOT_END; slotId++) {
                    ItemStack storageStack = storageBlockEntity.getItem(slotId);
                    if (storageStack.isEmpty()) {
                        if (stack.getCount() > storageStack.getMaxStackSize()) {
                            this.setStorageSlot(slotId, stack.split(storageStack.getMaxStackSize()));
                        } else {
                            this.setStorageSlot(slotId, stack.split(stack.getCount()));
                        }

                        flag = true;
                        break;
                    }
                }
            }
        }

        return flag;
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
            DrillHeadSyncPowerC2SPacket.send(true, drillHeadBlockEntity.getBlockPos());
        }
    }

    public void setPowerOff() {
        if (this.blockEntity instanceof DrillHeadBlockEntity drillHeadBlockEntity) {
            DrillHeadSyncPowerC2SPacket.send(false, drillHeadBlockEntity.getBlockPos());
        }
    }

    public boolean isMoving() {
        if (this.blockEntity instanceof DrillHeadBlockEntity drillHeadBlockEntity) {
            return drillHeadBlockEntity.isMoving();
        }

        return false;
    }

    public boolean fuelSlotIsEmpty() {
        return this.getSlot(BE_INVENTORY_FIRST_SLOT_INDEX + DrillHeadBlockEntity.FUEl_SLOT).getItem().isEmpty();
    }

    public boolean storageIsConnected() {
        return this.getConnectedCaterpillarBlockEntities().stream().anyMatch(blockEntity -> blockEntity instanceof StorageBlockEntity);
    }

    private StorageBlockEntity getStorageBlockEntity() {
        return (StorageBlockEntity) this.getConnectedCaterpillarBlockEntities().stream().filter(blockEntity -> blockEntity instanceof StorageBlockEntity).findFirst().orElse(null);
    }

    public boolean canScroll() {
        return this.storageIsConnected();
    }

    public void gatheredScrollTo(int gatheredScrollTo) {
        if (this.blockEntity instanceof DrillHeadBlockEntity drillHeadBlockEntity) {
            if (gatheredScrollTo >= 0 && gatheredScrollTo < 3) {
                for (int row = 0; row < 3 - gatheredScrollTo; row++) {
                    for (int column = 0; column < 3; column++) {
                        ItemStack stack = drillHeadBlockEntity.getItem(GATHERED_SLOT_START + column + (gatheredScrollTo + row) * 3);
                        Slot slot = super.getSlot(BE_INVENTORY_FIRST_SLOT_INDEX + GATHERED_SLOT_START + column + row * 3);

                        if (slot instanceof FakeSlot fakeSlot) {
                            fakeSlot.setDisplayStack(stack);
                        }
                    }
                }
            }

            if (gatheredScrollTo >= 1 && gatheredScrollTo <= 3) {
                for (int row = 3 - gatheredScrollTo; row < 3; row++) {
                    for (int column = 0; column < 3; column++) {
                        ItemStack stack = this.getStorageBlockEntity().getItem(StorageBlockEntity.GATHERED_SLOT_START + column + (row - (3 - gatheredScrollTo)) * 3);
                        Slot slot = super.getSlot(BE_INVENTORY_FIRST_SLOT_INDEX + GATHERED_SLOT_START + column + row * 3);

                        if (slot instanceof FakeSlot fakeSlot) {
                            fakeSlot.setDisplayStack(stack);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void scrollTo(int scrollTo) {
        this.consumptionScrollTo(scrollTo);
    }

    public void consumptionScrollTo(int consumptionScrollTo) {
        if (this.blockEntity instanceof DrillHeadBlockEntity drillHeadBlockEntity) {
            if (consumptionScrollTo >= 0 && consumptionScrollTo < 3) {
                for (int row = 0; row < 3 - consumptionScrollTo; row++) {
                    for (int column = 0; column < 3; column++) {
                        ItemStack stack = drillHeadBlockEntity.getItem(CONSUMPTION_SLOT_START + column + (consumptionScrollTo + row) * 3);
                        Slot slot = super.getSlot(BE_INVENTORY_FIRST_SLOT_INDEX + CONSUMPTION_SLOT_START + column + row * 3);

                        if (slot instanceof FakeSlot fakeSlot) {
                            fakeSlot.setDisplayStack(stack);
                        }
                    }
                }
            }

            if (consumptionScrollTo >= 1 && consumptionScrollTo <= 3) {
                for (int row = 3 - consumptionScrollTo; row < 3; row++) {
                    for (int column = 0; column < 3; column++) {
                        ItemStack stack = this.getStorageBlockEntity().getItem(column + (row - (3 - consumptionScrollTo)) * 3);
                        Slot slot = super.getSlot(BE_INVENTORY_FIRST_SLOT_INDEX + CONSUMPTION_SLOT_START + column + row * 3);

                        if (slot instanceof FakeSlot fakeSlot) {
                            fakeSlot.setDisplayStack(stack);
                        }
                    }
                }
            }
        }
    }

    public void syncCarried(ItemStack carried) {
        this.setCarried(carried);

        CaterpillarSyncCarriedC2SPacket.send(carried);
    }

    public void syncDrillHeadSlot(int slotId, ItemStack stack) {
        int drillHeadSlotId = slotId - BE_INVENTORY_FIRST_SLOT_INDEX;
        if (this.isConsumptionSlot(slotId)) {
            int i = 3;
            int j = (int) ((double) (this.getConsumptionScrollOffs() * (float) i) + 0.5D);
            if (j < 0) {
                j = 0;
            }
            int consumptionScrollTo = j;

            drillHeadSlotId += consumptionScrollTo * 3;
        } else {
            int i = 3;
            int j = (int) ((double) (this.getGatheredScrollOffs() * (float) i) + 0.5D);
            if (j < 0) {
                j = 0;
            }
            int gatheredScrollTo = j;

            drillHeadSlotId += gatheredScrollTo * 3;
        }

        if (this.isConsumptionSlot(slotId)) {
            if (drillHeadSlotId <= CONSUMPTION_SLOT_END) {
                // Consumption drill head slot
                this.setSlot(drillHeadSlotId, stack);
            } else {
                // Consumption storage slot
                this.setStorageSlot(drillHeadSlotId - CONSUMPTION_SLOT_START - CONSUMPTION_SLOT_END, stack);
            }
        } else {
            if (drillHeadSlotId <= GATHERED_SLOT_END) {
                // Gathered drill head slot
                this.setSlot(drillHeadSlotId, stack);
            } else {
                // Gathered storage slot
                this.setStorageSlot(drillHeadSlotId - CONSUMPTION_SLOT_START - CONSUMPTION_SLOT_END, stack);
            }
        }
    }

    public void setSlot(int slotId, ItemStack stack) {
        if (this.blockEntity instanceof DrillHeadBlockEntity drillHeadBlockEntity) {
            drillHeadBlockEntity.setItem(slotId, stack);
            CaterpillarSyncSlotC2SPacket.send(slotId, stack, drillHeadBlockEntity.getBlockPos());
        }
    }

    public void setStorageSlot(int slotId, ItemStack stack) {
        StorageBlockEntity storageBlockEntity = this.getStorageBlockEntity();

        if (storageBlockEntity != null) {
            storageBlockEntity.setItem(slotId, stack);
            CaterpillarSyncSlotC2SPacket.send(slotId, stack, storageBlockEntity.getBlockPos());
        }
    }

    public boolean isConsumptionSlot(int slotId) {
        return slotId >= BE_INVENTORY_FIRST_SLOT_INDEX + CONSUMPTION_SLOT_START && slotId <= BE_INVENTORY_FIRST_SLOT_INDEX + CONSUMPTION_SLOT_END;
    }

    public boolean isGatheredSlot(int slotId) {
        return slotId >= BE_INVENTORY_FIRST_SLOT_INDEX + GATHERED_SLOT_START && slotId <= BE_INVENTORY_FIRST_SLOT_INDEX + GATHERED_SLOT_END;
    }

    public boolean isDrillHeadSlot(int slotId) {
        return slotId >= BE_INVENTORY_FIRST_SLOT_INDEX + CONSUMPTION_SLOT_START && slotId <= BE_INVENTORY_FIRST_SLOT_INDEX + GATHERED_SLOT_END;
    }

    public float getConsumptionScrollOffs() {
        return super.getScrollOffs();
    }

    public float getGatheredScrollOffs() {
        return this.gatheredScrollOffs;
    }

    public void setConsumptionScrollOffs(float scrollOffs) {
        super.setScrollOffs(scrollOffs);
    }

    public void setGatheredScrollOffs(float scrollOffs) {
        this.gatheredScrollOffs = scrollOffs;
    }

    public boolean isConsumptionScrolling() {
        return super.isScrolling();
    }

    public boolean isGatheredScrolling() {
        return this.gatheredScrolling;
    }

    public void setConsumptionScrolling(boolean scrolling) {
        super.setScrolling(scrolling);
    }

    public void setGatheredScrolling(boolean scrolling) {
        this.gatheredScrolling = scrolling;
    }

    @Override
    public void setCarried(ItemStack stack) {
        super.setCarried(stack);
        super.setRemoteCarried(stack);
    }
}
