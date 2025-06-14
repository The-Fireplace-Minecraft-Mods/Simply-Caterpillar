package dev.the_fireplace.caterpillar.inventory;

import dev.the_fireplace.caterpillar.block.entity.DrillBaseBlockEntity;
import dev.the_fireplace.caterpillar.block.entity.DrillHeadBlockEntity;
import dev.the_fireplace.caterpillar.inventory.slot.DrillHeadFuelSlot;
import dev.the_fireplace.caterpillar.registry.MenuTypesRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class DrillHeadMenu extends AbstractCaterpillarMenu {
    private static final int FUEL_SLOT_X = 80;
    private static final int FUEL_SLOT_Y = 53;
    private static final int CONSUMPTION_SLOT_X_START = 8;
    private static final int CONSUMPTION_SLOT_Y_START = 17;
    private static final int CONSUMPTION_SLOT_X_END = 60;
    private static final int CONSUMPTION_SLOT_Y_END = 69;
    private static final int GATHERED_SLOT_X_START = 106;
    private static final int GATHERED_SLOT_Y_START = 17;
    private static final int GATHERED_SLOT_X_END = 168;
    private static final int GATHERED_SLOT_Y_END = 69;

    public DrillHeadMenu(int containerId, Inventory playerInventory, FriendlyByteBuf extraData) {
        super(MenuTypesRegistry.DRILL_HEAD.get(), containerId, playerInventory, extraData, DrillHeadBlockEntity.CONTAINER_DATA_SIZE);
    }

    public DrillHeadMenu(int containerId, Inventory playerInventory, DrillBaseBlockEntity blockEntity, ContainerData data) {
        super(MenuTypesRegistry.DRILL_HEAD.get(), containerId, playerInventory, blockEntity, data);
    }

    @Override
    protected void addSlots(Container container) {
        int slotId = 0;

        // Fuel slot
        super.addSlot(new DrillHeadFuelSlot(this, container, slotId++, FUEL_SLOT_X, FUEL_SLOT_Y));

        // Consumption slots
        for(int row = 0; row < 3; row++) {
            for(int column = 0; column < 3; column++) {
                super.addSlot(new Slot(container, slotId++, CONSUMPTION_SLOT_X_START + column * SLOT_SIZE_PLUS_2, CONSUMPTION_SLOT_Y_START + row * SLOT_SIZE_PLUS_2));
            }
        }

        // Gathered slots
        for(int row = 0; row < 3; row++) {
            for(int column = 0; column < 3; column++) {
                super.addSlot(new Slot(container, slotId++, GATHERED_SLOT_X_START + column * SLOT_SIZE_PLUS_2, GATHERED_SLOT_Y_START + row * SLOT_SIZE_PLUS_2));
            }
        }
    }

    public boolean isFuel(ItemStack stack) {
        return this.level.fuelValues().isFuel(stack);
    }

    public float getLitProgress() {
        int i = this.data.get(1);
        if (i == 0) {
            i = 200;
        }

        return Mth.clamp((float)this.data.get(0) / (float)i, 0.0F, 1.0F);
    }

    public boolean isLit() {
        return this.data.get(0) > 0;
    }

    public boolean isPowered() {
        return this.data.get(2) > 0;
    }
}
