package the_fireplace.caterpillar.client.screen;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import the_fireplace.caterpillar.client.screen.util.ScreenTabs;
import the_fireplace.caterpillar.common.block.entity.IncineratorBlockEntity;
import the_fireplace.caterpillar.common.menu.IncineratorMenu;
import the_fireplace.caterpillar.core.network.PacketHandler;
import the_fireplace.caterpillar.core.network.packet.client.CaterpillarSyncSlotC2SPacket;

import static the_fireplace.caterpillar.common.block.entity.IncineratorBlockEntity.INVENTORY_SIZE;
import static the_fireplace.caterpillar.common.menu.AbstractCaterpillarMenu.BE_INVENTORY_FIRST_SLOT_INDEX;

public class IncineratorScreen extends AbstractCaterpillarScreen<IncineratorMenu> {
    public IncineratorScreen(IncineratorMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, ScreenTabs.INCINERATOR);
    }

    @Override
    protected void slotClicked(@NotNull Slot slot, int slotId, int mouseButton, @NotNull ClickType type) {
        if (!this.isIncineratorSlot(slotId) || slot == null) {
            super.slotClicked(slot, slotId, mouseButton, type);
            return;
        }

        int incineratorSlotId = slotId - BE_INVENTORY_FIRST_SLOT_INDEX;
        ItemStack stack;
        ItemStack carried = this.menu.getCarried().copy();

        if (carried.isEmpty()) {
            stack = ItemStack.EMPTY;
        } else if (!this.menu.itemIsAlreadyInInventory(carried.getItem())) {
            stack = carried.copy();
            stack.setCount(1);
        } else {
            return;
        }

        this.menu.slots.get(BE_INVENTORY_FIRST_SLOT_INDEX + incineratorSlotId).set(stack);
        this.menu.setCarried(carried);

        if (this.menu.blockEntity instanceof IncineratorBlockEntity incineratorBlockEntity) {
            incineratorBlockEntity.setStackInSlot(incineratorSlotId, stack);

            PacketHandler.sendToServer(new CaterpillarSyncSlotC2SPacket(incineratorSlotId, stack, incineratorBlockEntity.getBlockPos()));
        }
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (getSlotUnderMouse() != null && this.isIncineratorSlot(getSlotUnderMouse().index)) {
            // Don't allow dragging items into the incinerator slots
            return true;
        }

        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    private boolean isIncineratorSlot(int slotId) {
        return slotId >= BE_INVENTORY_FIRST_SLOT_INDEX && slotId < BE_INVENTORY_FIRST_SLOT_INDEX + INVENTORY_SIZE;
    }
}
