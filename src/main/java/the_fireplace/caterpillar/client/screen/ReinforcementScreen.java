package the_fireplace.caterpillar.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import the_fireplace.caterpillar.client.screen.util.ScreenTabs;
import the_fireplace.caterpillar.common.block.entity.ReinforcementBlockEntity;
import the_fireplace.caterpillar.common.menu.ReinforcementMenu;
import the_fireplace.caterpillar.core.network.PacketHandler;
import the_fireplace.caterpillar.core.network.packet.client.CaterpillarSyncSlotC2SPacket;

import static the_fireplace.caterpillar.common.block.entity.ReinforcementBlockEntity.INVENTORY_SIZE;
import static the_fireplace.caterpillar.common.menu.AbstractCaterpillarMenu.BE_INVENTORY_FIRST_SLOT_INDEX;

public class ReinforcementScreen extends AbstractCaterpillarScreen<ReinforcementMenu> {
    public ReinforcementScreen(ReinforcementMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, ScreenTabs.REINFORCEMENT);
    }

    @Override
    protected void renderLabels(@NotNull PoseStack stack, int mouseX, int mouseY) {
        super.inventoryLabelY = super.imageHeight - 94;

        this.font.draw(stack, super.playerInventoryTitle, super.inventoryLabelX, super.inventoryLabelY, 0x404040);
    }

    @Override
    protected void slotClicked(@NotNull Slot slot, int slotId, int mouseButton, @NotNull ClickType type) {
        if (!this.isReinforcementSlot(slotId) || slot == null) {
            super.slotClicked(slot, slotId, mouseButton, type);
            return;
        }

        int reinforcementSlotId = slotId - BE_INVENTORY_FIRST_SLOT_INDEX;
        ItemStack reinforcementStack;
        ItemStack carried = this.menu.getCarried().copy();

        if (carried.isEmpty()) {
            reinforcementStack = ItemStack.EMPTY;
        } else {
            reinforcementStack = carried.copy();
            reinforcementStack.setCount(1);
        }

        this.menu.slots.get(BE_INVENTORY_FIRST_SLOT_INDEX + reinforcementSlotId).set(reinforcementStack);
        this.menu.setCarried(carried);

        if (this.menu.blockEntity instanceof ReinforcementBlockEntity reinforcementBlockEntity) {
            reinforcementBlockEntity.setStackInSlot(reinforcementSlotId, reinforcementStack);

            PacketHandler.sendToServer(new CaterpillarSyncSlotC2SPacket(reinforcementSlotId, reinforcementStack, reinforcementBlockEntity.getBlockPos()));
        }
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (getSlotUnderMouse() != null && this.isReinforcementSlot(getSlotUnderMouse().index)) {
            slotClicked(getSlotUnderMouse(), getSlotUnderMouse().index, 0, ClickType.PICKUP);
            return true;
        }

        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    private boolean isReinforcementSlot(int slotId) {
        return slotId >= BE_INVENTORY_FIRST_SLOT_INDEX && slotId < BE_INVENTORY_FIRST_SLOT_INDEX + INVENTORY_SIZE;
    }
}
