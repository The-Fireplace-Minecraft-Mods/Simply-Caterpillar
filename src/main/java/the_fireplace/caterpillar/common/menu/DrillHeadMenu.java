package the_fireplace.caterpillar.common.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import the_fireplace.caterpillar.common.block.entity.DrillHeadBlockEntity;
import the_fireplace.caterpillar.common.menu.slot.CaterpillarFuelSlot;
import the_fireplace.caterpillar.common.menu.util.CaterpillarMenuUtil;
import the_fireplace.caterpillar.core.init.MenuInit;

import static the_fireplace.caterpillar.common.menu.util.CaterpillarMenuUtil.isFuel;

public class DrillHeadMenu extends AbstractCaterpillarMenu {

    private static final int CONSUMPTION_SLOT_X_START = 8;

    private static final int CONSUMPTION_SLOT_Y_START = 17;

    private static final int GATHERED_SLOT_X_START = 116;

    private static final int GATHERED_SLOT_Y_START = 17;

    private static final int FUEL_SLOT_X = 80;

    private static final int FUEL_SLOT_Y = 53;

    public DrillHeadMenu(int id, Inventory playerInventory, FriendlyByteBuf extraData) {
        super(MenuInit.DRILL_HEAD.get(), id, playerInventory, extraData, 4);
    }

    public DrillHeadMenu(int id, Inventory playerInventory, BlockEntity entity, ContainerData data) {
        super(MenuInit.DRILL_HEAD.get(), id, playerInventory, entity, data);
    }

    @Override
    protected void addSlots(IItemHandler handler) {
        int slotId = 0;

        // Drill_head Consumption slots
        for(int row = 0; row < 3; row++) {
            for(int column = 0; column < 3; column++) {
                super.addSlot(new SlotItemHandler(handler, slotId++, CONSUMPTION_SLOT_X_START + column * SLOT_SIZE_PLUS_2, CONSUMPTION_SLOT_Y_START + row * SLOT_SIZE_PLUS_2));
            }
        }

        // Drill_head fuel slot
        super.addSlot(new CaterpillarFuelSlot(handler, slotId++, FUEL_SLOT_X, FUEL_SLOT_Y));


        // Drill_head Gathered slots
        for(int row = 0; row < 3; row++) {
            for(int column = 0; column < 3; column++) {
                super.addSlot(new SlotItemHandler(handler, slotId++, GATHERED_SLOT_X_START + column * SLOT_SIZE_PLUS_2, GATHERED_SLOT_Y_START + row * SLOT_SIZE_PLUS_2));
            }
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot sourceSlot = slots.get(index);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // This is a vanilla container slot so merge the stack into the BE inventory

            if (CaterpillarMenuUtil.isFuel(sourceStack)) {
                if (!moveItemStackTo(sourceStack, BE_INVENTORY_FIRST_SLOT_INDEX + DrillHeadBlockEntity.FUEl_SLOT, BE_INVENTORY_FIRST_SLOT_INDEX + DrillHeadBlockEntity.FUEl_SLOT + 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else {
                return super.quickMoveStack(player, index);
            }
        } else {
            return super.quickMoveStack(player, index);
        }

        return copyOfSourceStack;
    }
}
