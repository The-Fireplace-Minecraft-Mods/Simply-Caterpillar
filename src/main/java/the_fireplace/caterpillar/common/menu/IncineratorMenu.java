package the_fireplace.caterpillar.common.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import the_fireplace.caterpillar.common.block.entity.IncineratorBlockEntity;
import the_fireplace.caterpillar.core.init.MenuInit;

public class IncineratorMenu extends AbstractCaterpillarMenu {

    private static final int INCINERATOR_SLOT_X_START = 62;

    private static final int INCINERATOR_SLOT_Y_START = 17;

    public IncineratorMenu(int id, Inventory playerInventory, FriendlyByteBuf extraData) {
        super(MenuInit.INCINERATOR.get(), id, playerInventory, extraData, 0, IncineratorBlockEntity.INVENTORY_SIZE);
    }

    public IncineratorMenu(int id, Inventory playerInventory, IncineratorBlockEntity blockEntity, ContainerData data) {
        super(MenuInit.INCINERATOR.get(), id, playerInventory, blockEntity, data, IncineratorBlockEntity.INVENTORY_SIZE);
    }

    @Override
    protected void addSlots(IItemHandler handler) {
        for(int row = 0; row < 3; row++) {
            for(int column = 0; column < 3; column++) {
                super.addSlot(new SlotItemHandler(handler, column + row * 3, INCINERATOR_SLOT_X_START + column * SLOT_SIZE_PLUS_2, INCINERATOR_SLOT_Y_START + row * SLOT_SIZE_PLUS_2));
            }
        }
    }
}
