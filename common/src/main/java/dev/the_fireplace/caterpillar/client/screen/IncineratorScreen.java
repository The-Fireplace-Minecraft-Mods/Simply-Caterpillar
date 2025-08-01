package dev.the_fireplace.caterpillar.client.screen;

import dev.the_fireplace.caterpillar.client.screen.util.ScreenTabs;
import dev.the_fireplace.caterpillar.inventory.AbstractCaterpillarMenu;
import dev.the_fireplace.caterpillar.inventory.IncineratorMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class IncineratorScreen extends AbstractCaterpillarScreen<IncineratorMenu> {
    public IncineratorScreen(IncineratorMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, ScreenTabs.INCINERATOR);
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    @Override
    protected void slotClicked(Slot slot, int slotId, int mouseButton, ClickType type) {
        if (!this.isIncineratorSlot(slotId)) {
            super.slotClicked(slot, slotId, mouseButton, type);
            return;
        }

        int beSlotId = slotId - AbstractCaterpillarMenu.BE_INVENTORY_SLOT_START_INDEX;
        ItemStack stack;
        ItemStack heldItem = this.menu.getCarried().copy();

        if (heldItem.isEmpty()) {
            stack = ItemStack.EMPTY;
        } else if (true) { // TODO: check if item not already in inventory
            stack = heldItem;
            stack.setCount(1);
        } else {
            return;
        }

        this.menu.getSlot(beSlotId).set(stack);
        this.menu.setCarried(heldItem);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (this.hoveredSlot != null && this.isIncineratorSlot(this.hoveredSlot.index)) {
            // Prevent dragging items from incinerator slots
            return false;
        }

        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    private boolean isIncineratorSlot(int slot) {
        return slot >= AbstractCaterpillarMenu.BE_INVENTORY_SLOT_START_INDEX && slot < this.menu.BE_INVENTORY_SLOT_END_INDEX;
    }
}
