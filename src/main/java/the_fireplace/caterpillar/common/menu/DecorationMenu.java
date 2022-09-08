package the_fireplace.caterpillar.common.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;
import the_fireplace.caterpillar.common.block.entity.DecorationBlockEntity;
import the_fireplace.caterpillar.common.menu.syncdata.DecorationContainerData;
import the_fireplace.caterpillar.core.init.MenuInit;

public class DecorationMenu extends AbstractCaterpillarMenu {

    private static final int DECORATION_SLOT_X_START = 62;

    private static final int DECORATION_SLOT_Y_START = 17;

    public DecorationMenu(int id, Inventory playerInventory, FriendlyByteBuf extraData) {
        super(MenuInit.DECORATION.get(), id, playerInventory, extraData, DecorationContainerData.SIZE);
    }

    public DecorationMenu(int id, Inventory playerInventory, DecorationBlockEntity blockEntity, ContainerData data) {
        super(MenuInit.DECORATION.get(), id, playerInventory, blockEntity, data);
    }

    @Override
    protected void addSlots(IItemHandler handler) {
        for(int row = 0; row < 3; row++) {
            for(int column = 0; column < 3; column++) {
                if (row != 1 || column != 1) {
                    super.addSlot(new SlotItemHandler(handler, column + row * 3, DECORATION_SLOT_X_START + column * SLOT_SIZE_PLUS_2, DECORATION_SLOT_Y_START + row * SLOT_SIZE_PLUS_2));
                }
            }
        }

        this.scrollTo(this.getSelectedMap());
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot sourceSlot = slots.get(index);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;
        ItemStack sourceStack = sourceSlot.getItem();

        return sourceStack.copy();
    }

    /*
        Placement 0 -> 0 - 8
        Placement 1 -> 9 - 17
        Placement 2 -> 18 - 26
        Placement 3 -> 27 - 35
        Placement 4 -> 36 - 44
        Placement 5 -> 45 - 53
        Placement 6 -> 54 - 62
        Placement 7 -> 63 - 71
        Placement 8 -> 72 - 80
        Placement 9 -> 81 - 89
     */
    public void scrollTo(float scrollOffs) {
        int i = 9;
        int j = (int)((double)(scrollOffs * (float)i) + 0.5D);
        if (j < 0) {
            j = 0;
        }

        if (this.blockEntity instanceof DecorationBlockEntity decorationBlockEntity) {
            decorationBlockEntity.setSelectedMap(j);

            int finalJ = j;
            this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
                for(int row = 0; row < 3; row++) {
                    for(int column = 0; column < 3; column++) {
                        if (row != 1 || column != 1) {
                            int placementSlotId = column + row * 3 + (finalJ * 9);
                            ItemStack placementStack = handler.getStackInSlot(placementSlotId);
                            Slot decorationSlot = super.getSlot(column + row * 3);
                            decorationSlot.set(placementStack);
                            this.broadcastChanges();
                            System.out.println("placementSlotId: " + placementSlotId);
                            this.blockEntity.requiresUpdate = true;
                        }
                    }
                }
            });
        }
    }

    @Override
    public void clicked(int pSlotId, int pButton, @NotNull ClickType pClickType, @NotNull Player pPlayer) {
        super.clicked(pSlotId, pButton, pClickType, pPlayer);
    }

    public int getSelectedMap() {
        if (this.blockEntity instanceof DecorationBlockEntity decorationBlockEntity) {
            return decorationBlockEntity.getSelectedMap();
        }
        return 0;
    }
}
