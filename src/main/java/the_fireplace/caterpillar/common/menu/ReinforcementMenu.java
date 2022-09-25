package the_fireplace.caterpillar.common.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import the_fireplace.caterpillar.common.block.entity.ReinforcementBlockEntity;
import the_fireplace.caterpillar.core.init.MenuInit;
import the_fireplace.caterpillar.core.network.PacketHandler;
import the_fireplace.caterpillar.core.network.packet.client.ReinforcementSyncSelectedReplacerC2SPacket;

import static the_fireplace.caterpillar.common.block.entity.ReinforcementBlockEntity.INVENTORY_SIZE;

public class ReinforcementMenu extends AbstractCaterpillarMenu {

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
        int slotId = 0;

        // Reinforcement top slots
        for (int column = 0; column <= 4; column++) {
            super.addSlot(new SlotItemHandler(handler, slotId++, REINFORCEMENT_SLOT_X_START + column * SLOT_SIZE_PLUS_2, REINFORCEMENT_SLOT_Y_START));
        }

        // Reinforcement left slots
        for (int row = 0; row <= 2; row++) {
            super.addSlot(new SlotItemHandler(handler, slotId++, REINFORCEMENT_SLOT_X_START, REINFORCEMENT_SLOT_Y_START + (1 + row) * SLOT_SIZE_PLUS_2));
        }

        // Reinforcement right slots
        for (int row = 0; row <= 2; row++) {
            super.addSlot(new SlotItemHandler(handler, slotId++, REINFORCEMENT_SLOT_X_START + 4 * SLOT_SIZE_PLUS_2, REINFORCEMENT_SLOT_Y_START + (1 + row) * SLOT_SIZE_PLUS_2));
        }

        // Reinforcement bottom slots
        for (int column = 0; column <= 4; column++) {
            super.addSlot(new SlotItemHandler(handler, slotId++, REINFORCEMENT_SLOT_X_START + column * SLOT_SIZE_PLUS_2, REINFORCEMENT_SLOT_Y_START + 4 * SLOT_SIZE_PLUS_2));
        }
    }

    public byte[] getSelectedReplacers() {
        if (this.blockEntity instanceof ReinforcementBlockEntity reinforcementBlockEntity) {
            return reinforcementBlockEntity.getReplacers(reinforcementBlockEntity.getSelectedReplacer());
        }

        return new byte[4];
    }

    public byte[] getReplacers(int side) {
        if (this.blockEntity instanceof ReinforcementBlockEntity reinforcementBlockEntity) {
            return reinforcementBlockEntity.getReplacers(side);
        }

        return new byte[4];
    }

    public int getSelectedReplacer() {
        if (this.blockEntity instanceof ReinforcementBlockEntity reinforcementBlockEntity) {
            return reinforcementBlockEntity.getSelectedReplacer();
        }

        return 0;
    }

    public void setSelectedReplacer(int selectedReplacer) {
        if (this.blockEntity instanceof ReinforcementBlockEntity reinforcementBlockEntity) {
            reinforcementBlockEntity.setSelectedReplacer(selectedReplacer);
        }
    }

    public void scrollTo(int scrollToReplacer) {
        if (this.blockEntity instanceof ReinforcementBlockEntity reinforcementBlockEntity) {
            reinforcementBlockEntity.setSelectedReplacer(scrollToReplacer);

            PacketHandler.sendToServer(new ReinforcementSyncSelectedReplacerC2SPacket(scrollToReplacer, reinforcementBlockEntity.getBlockPos()));
        }
    }
}
