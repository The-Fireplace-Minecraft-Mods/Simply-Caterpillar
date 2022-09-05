package the_fireplace.caterpillar.common.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import the_fireplace.caterpillar.common.block.entity.ReinforcementBlockEntity;
import the_fireplace.caterpillar.core.init.MenuInit;

public class ReinforcementMenu extends AbstractCaterpillarMenu {

    private static final int REINFORCEMENT_SLOT_X_START = 44;

    private static final int REINFORCEMENT_SLOT_Y_START = 4;

    private final int INVENTORY_SLOT_X_START = 8;

    private final int INVENTORY_SLOT_Y_START = 107;

    private final int HOTBAR_SLOT_X_START = 8;

    private final int HOTBAR_SLOT_Y_START = 165;

    public ReinforcementMenu(int id, Inventory playerInventory, FriendlyByteBuf extraData) {
        super(MenuInit.REINFORCEMENT.get(), id, playerInventory, extraData, 0);
    }

    public ReinforcementMenu(int id, Inventory playerInventory, ReinforcementBlockEntity blockEntity, ContainerData data) {
        super(MenuInit.REINFORCEMENT.get(), id, playerInventory, blockEntity, data);
    }

    @Override
    protected void addPlayerInventory(Inventory playerInventory) {
        super.addPlayerInventory(playerInventory, INVENTORY_SLOT_X_START, INVENTORY_SLOT_Y_START);
    }

    @Override
    protected void addPlayerHotbar(Inventory playerInventory) {
        super.addPlayerHotbar(playerInventory, HOTBAR_SLOT_X_START, HOTBAR_SLOT_Y_START);
    }

    @Override
    protected void addSlots(IItemHandler handler) {
        int slotId = 0;

        // Reinforcement top slots
        for(int column = 0; column < 5; column++) {
            super.addSlot(new SlotItemHandler(handler, slotId++, REINFORCEMENT_SLOT_X_START + column * SLOT_SIZE_PLUS_2, REINFORCEMENT_SLOT_Y_START));
        }

        // Reinforcement sides slots
        for(int row  = 0; row < 3; row++) {
            for (int column = 0; column < 2; column++) {
                if (column == 0) { // LEFT side
                    super.addSlot(new SlotItemHandler(handler, slotId++, REINFORCEMENT_SLOT_X_START, REINFORCEMENT_SLOT_Y_START + (1 + row) * SLOT_SIZE_PLUS_2));
                } else { // RIGHT side
                    super.addSlot(new SlotItemHandler(handler, slotId++, REINFORCEMENT_SLOT_X_START + 4 * SLOT_SIZE_PLUS_2, REINFORCEMENT_SLOT_Y_START + (1 + row) * SLOT_SIZE_PLUS_2));
                }
            }
        }

        // Reinforcement bottom slots
        for(int column = 0; column < 5; column++) {
            super.addSlot(new SlotItemHandler(handler, slotId++, REINFORCEMENT_SLOT_X_START + column * SLOT_SIZE_PLUS_2, REINFORCEMENT_SLOT_Y_START + 4 * 18));
        }
    }
}
