package the_fireplace.caterpillar.common.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import the_fireplace.caterpillar.common.block.entity.AbstractCaterpillarBlockEntity;
import the_fireplace.caterpillar.common.block.util.CaterpillarBlocksUtil;

import java.util.Collections;
import java.util.List;

public abstract class AbstractCaterpillarMenu extends AbstractContainerMenu {

    private final ContainerLevelAccess access;

    public final ContainerData data;

    private final Level level;

    private AbstractCaterpillarBlockEntity blockEntity;

    public static final int SLOT_SIZE_PLUS_2 = 18;

    private final int INVENTORY_SLOT_X_START = 8;

    private final int INVENTORY_SLOT_Y_START = 84;

    private final int HOTBAR_SLOT_X_START = 8;

    private final int HOTBAR_SLOT_Y_START = 142;

    public AbstractCaterpillarMenu(MenuType<?> menuType, int id, Inventory playerInventory, FriendlyByteBuf extraData, int containerDataSize) {
        this(menuType, id, playerInventory, playerInventory.player.level.getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(containerDataSize));
    }

    public AbstractCaterpillarMenu(MenuType<?> menuType, int id, Inventory playerInventory, BlockEntity blockEntity, ContainerData data) {
        super(menuType, id);
        this.level = playerInventory.player.level;
        this.blockEntity = (AbstractCaterpillarBlockEntity) blockEntity;
        BE_INVENTORY_SLOT_COUNT = this.blockEntity.size;
        this.access = ContainerLevelAccess.create(this.level, blockEntity.getBlockPos());
        this.data = data;

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
        
        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
           this.addSlots(handler);
        });

        addDataSlots(data);
    }

    protected abstract void addSlots(IItemHandler handler);

    //  0 - 8 = hotbar slots (which will map to the InventoryPlayer slot numbers 0 - 8)
    //  9 - 35 = player inventory slots (which map to the InventoryPlayer slot numbers 9 - 35)
    //  36 - 44 = BlockEntityInventory slots, which map to our BlockEntity slot numbers 0 - 8)
    protected static final int HOTBAR_SLOT_COUNT = 9;
    protected static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    protected static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    protected static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    protected static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    protected static final int VANILLA_FIRST_SLOT_INDEX = 0;
    protected static final int BE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;
    protected final int BE_INVENTORY_SLOT_COUNT;

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot sourceSlot = slots.get(index);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // This is a vanilla container slot so merge the stack into the BE inventory
            if (!moveItemStackTo(sourceStack, BE_INVENTORY_FIRST_SLOT_INDEX, BE_INVENTORY_FIRST_SLOT_INDEX + BE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else if (index < BE_INVENTORY_FIRST_SLOT_INDEX + BE_INVENTORY_SLOT_COUNT) {
            // This is a BE slot so merge the stack into the players inventory
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + index);
            return ItemStack.EMPTY;
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(player, sourceStack);
        return copyOfSourceStack;
    }

    protected void addPlayerInventory(Inventory playerInventory) {
        this.addPlayerInventory(playerInventory, INVENTORY_SLOT_X_START, INVENTORY_SLOT_Y_START);
    }

    protected void addPlayerInventory(Inventory playerInventory, int inventoryX, int inventoryY) {
        for(int row = 0; row < 3; row++) {
            for(int column = 0; column < 9; column++) {
                super.addSlot(new Slot(playerInventory, column + row * 9 + 9, inventoryX + column * SLOT_SIZE_PLUS_2, inventoryY + row * SLOT_SIZE_PLUS_2));
            }
        }
    }

    protected void addPlayerHotbar(Inventory playerInventory) {
        this.addPlayerInventory(playerInventory, HOTBAR_SLOT_X_START, HOTBAR_SLOT_Y_START);
    }

    protected void addPlayerHotbar(Inventory playerInventory, int hotbarX, int hotbarY) {
        for(int column = 0; column < 9; column++) {
            super.addSlot(new Slot(playerInventory, column, hotbarX + column * SLOT_SIZE_PLUS_2, hotbarY));
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(this.access, player, this.blockEntity.getBlockState().getBlock());
    }

    public List<AbstractCaterpillarBlockEntity> getConnectedCaterpillarBlockEntities() {
        return CaterpillarBlocksUtil.getConnectedCaterpillarBlockEntities(this.level, this.blockEntity.getBlockPos(), Collections.emptyList());
    }
}
