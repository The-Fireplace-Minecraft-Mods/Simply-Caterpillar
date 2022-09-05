package the_fireplace.caterpillar.common.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandler;
import the_fireplace.caterpillar.core.init.MenuInit;

public class ReinforcementMenu extends AbstractCaterpillarMenu {
    public ReinforcementMenu(int id, Inventory playerInventory, FriendlyByteBuf extraData) {
        super(MenuInit.REINFORCEMENT.get(), id, playerInventory, extraData, 0);
    }

    public ReinforcementMenu(int id, Inventory playerInventory, BlockEntity blockEntity, ContainerData data) {
        super(MenuInit.REINFORCEMENT.get(), id, playerInventory, blockEntity, data);
    }

    @Override
    protected void addSlots(IItemHandler handler) {

    }
}
