package the_fireplace.caterpillar.common.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import the_fireplace.caterpillar.common.block.entity.ReinforcementBlockEntity;
import the_fireplace.caterpillar.core.init.MenuInit;

import static the_fireplace.caterpillar.common.block.entity.ReinforcementBlockEntity.*;

public class ReinforcementMenu extends AbstractScrollableMenu {

    private static final int REINFORCEMENT_SLOT_X_START = 44;

    private static final int REINFORCEMENT_SLOT_Y_START = 17;

    public ReinforcementMenu(int id, Inventory playerInventory, FriendlyByteBuf extraData) {
        super(MenuInit.REINFORCEMENT.get(), id, playerInventory, extraData, 0, INVENTORY_SIZE);
    }

    public ReinforcementMenu(int id, Inventory playerInventory, ReinforcementBlockEntity blockEntity, ContainerData data) {
        super(MenuInit.REINFORCEMENT.get(), id, playerInventory, blockEntity, data, INVENTORY_SIZE);
    }

    @Override
    protected void addPlayerInventory(Inventory playerInventory) {
        int INVENTORY_SLOT_X_START = 8;
        int INVENTORY_SLOT_Y_START = 120;
        super.addPlayerInventory(playerInventory, INVENTORY_SLOT_X_START, INVENTORY_SLOT_Y_START);
    }

    @Override
    protected void addPlayerHotbar(Inventory playerInventory) {
        int HOTBAR_SLOT_X_START = 8;
        int HOTBAR_SLOT_Y_START = 178;
        super.addPlayerHotbar(playerInventory, HOTBAR_SLOT_X_START, HOTBAR_SLOT_Y_START);
    }

    @Override
    protected void addSlots(IItemHandler handler) {
        int i = 3;
        int j = (int)((double)(this.getScrollOffs() * (float)i) + 0.5D);
        if (j < 0) {
            j = 0;
        }
        int scrollToReplacer = j;

        int slotId = 0;

        // Reinforcement top slots
        for (int column = 0; column <= 4; column++) {
            if (scrollToReplacer == REPLACER_CEILING) {
                super.addSlot(new SlotItemHandler(handler, slotId, REINFORCEMENT_SLOT_X_START + column * SLOT_SIZE_PLUS_2, REINFORCEMENT_SLOT_Y_START));
            }
            slotId++;
        }

        // Reinforcement left slots
        for (int row = 0; row <= 2; row++) {
            if (scrollToReplacer == REPLACER_LEFT) {
                super.addSlot(new SlotItemHandler(handler, slotId, REINFORCEMENT_SLOT_X_START, REINFORCEMENT_SLOT_Y_START + (1 + row) * SLOT_SIZE_PLUS_2));
            }
            slotId++;
        }

        // Reinforcement right slots
        for (int row = 0; row <= 2; row++) {
            if (scrollToReplacer == REPLACER_RIGHT) {
                super.addSlot(new SlotItemHandler(handler, slotId, REINFORCEMENT_SLOT_X_START + 4 * SLOT_SIZE_PLUS_2, REINFORCEMENT_SLOT_Y_START + (1 + row) * SLOT_SIZE_PLUS_2));
            }
            slotId++;
        }

        // Reinforcement bottom slots
        for (int column = 0; column <= 4; column++) {
            if (scrollToReplacer == REPLACER_FLOOR) {
                super.addSlot(new SlotItemHandler(handler, slotId, REINFORCEMENT_SLOT_X_START + column * SLOT_SIZE_PLUS_2, REINFORCEMENT_SLOT_Y_START + 4 * SLOT_SIZE_PLUS_2));
            }
            slotId++;
        }
    }

    public byte[] getSelectedReplacers() {
        int i = 3;
        int j = (int)((double)(this.getScrollOffs() * (float)i) + 0.5D);
        if (j < 0) {
            j = 0;
        }

        if (this.blockEntity instanceof ReinforcementBlockEntity reinforcementBlockEntity) {
            return reinforcementBlockEntity.getReplacers(j);
        }

        return new byte[4];
    }

    public byte[] getReplacers(int side) {
        if (this.blockEntity instanceof ReinforcementBlockEntity reinforcementBlockEntity) {
            return reinforcementBlockEntity.getReplacers(side);
        }

        return new byte[4];
    }

    public void scrollTo(int scrollToReplacer) {
        if (this.blockEntity instanceof ReinforcementBlockEntity reinforcementBlockEntity) {
            reinforcementBlockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(reinforcementHandler -> {
                while (this.slots.size() != BE_INVENTORY_FIRST_SLOT_INDEX) {
                    this.slots.remove(this.slots.size() - 1);
                }

                int slotId = 0;

                // Reinforcement top slots
                for (int column = 0; column <= 4; column++) {
                    if (scrollToReplacer == REPLACER_CEILING) {
                        super.addSlot(new SlotItemHandler(reinforcementHandler, slotId, REINFORCEMENT_SLOT_X_START + column * SLOT_SIZE_PLUS_2, REINFORCEMENT_SLOT_Y_START));
                    }
                    slotId++;
                }

                // Reinforcement left slots
                for (int row = 0; row <= 2; row++) {
                    if (scrollToReplacer == REPLACER_LEFT) {
                        super.addSlot(new SlotItemHandler(reinforcementHandler, slotId, REINFORCEMENT_SLOT_X_START, REINFORCEMENT_SLOT_Y_START + (1 + row) * SLOT_SIZE_PLUS_2));
                    }
                    slotId++;
                }

                // Reinforcement right slots
                for (int row = 0; row <= 2; row++) {
                    if (scrollToReplacer == REPLACER_RIGHT) {
                        super.addSlot(new SlotItemHandler(reinforcementHandler, slotId, REINFORCEMENT_SLOT_X_START + 4 * SLOT_SIZE_PLUS_2, REINFORCEMENT_SLOT_Y_START + (1 + row) * SLOT_SIZE_PLUS_2));
                    }
                    slotId++;
                }

                // Reinforcement bottom slots
                for (int column = 0; column <= 4; column++) {
                    if (scrollToReplacer == REPLACER_FLOOR) {
                        super.addSlot(new SlotItemHandler(reinforcementHandler, slotId, REINFORCEMENT_SLOT_X_START + column * SLOT_SIZE_PLUS_2, REINFORCEMENT_SLOT_Y_START + 4 * SLOT_SIZE_PLUS_2));
                    }
                    slotId++;
                }
            });
        }
    }
}
