package dev.the_fireplace.caterpillar.menu;

import dev.the_fireplace.caterpillar.block.entity.DrillBaseBlockEntity;
import dev.the_fireplace.caterpillar.block.util.CaterpillarBlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static dev.the_fireplace.caterpillar.block.DrillBaseBlock.FACING;

public abstract class AbstractCaterpillarMenu extends AbstractContainerMenu {

    private final ContainerLevelAccess access;

    public final ContainerData data;

    public final DrillBaseBlockEntity blockEntity;

    private List<DrillBaseBlockEntity> caterpillarBlockEntities;

    private final Container inventory;

    public static final int SLOT_SIZE_PLUS_2 = 18;
    public static final int HOTBAR_SLOT_COUNT = 9;
    public static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    public static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    public static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    public static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    public static final int VANILLA_FIRST_SLOT_INDEX = 0;
    public static final int BE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    public final int BE_INVENTORY_SLOT_COUNT;

    public AbstractCaterpillarMenu(MenuType<?> menuType, int id, Inventory playerInventory, FriendlyByteBuf extraData, int containerDataSize, int inventorySize) {
        this(menuType, id, playerInventory, (DrillBaseBlockEntity) playerInventory.player.level.getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(containerDataSize), inventorySize);
    }

    public <T> AbstractCaterpillarMenu(MenuType<?> menuType, int id, Inventory playerInventory, DrillBaseBlockEntity blockEntity, ContainerData data, int inventorySize) {
        super(menuType, id);
        Level level = playerInventory.player.level;
        this.inventory = blockEntity;
        this.blockEntity = blockEntity;
        this.access = ContainerLevelAccess.create(level, this.blockEntity.getBlockPos());
        this.data = data;
        this.BE_INVENTORY_SLOT_COUNT = inventorySize;

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);

        addSlots(inventory);

        addDataSlots(data);

        BlockPos caterpillarHeadPos = CaterpillarBlockUtil.getCaterpillarHeadPos(level, this.blockEntity.getBlockPos(), this.blockEntity.getBlockState().getValue(FACING));
        caterpillarBlockEntities = CaterpillarBlockUtil.getConnectedCaterpillarBlockEntities(level, caterpillarHeadPos, new ArrayList<>());
    }

    public static boolean isFuel(ItemStack stack) {
        return AbstractFurnaceBlockEntity.isFuel(stack);
    }

    protected void addSlots(Container inventory) {
    }

    protected void addPlayerInventory(Inventory playerInventory) {
        int INVENTORY_SLOT_X_START = 8;
        int INVENTORY_SLOT_Y_START = 84;
        this.addPlayerInventory(playerInventory, INVENTORY_SLOT_X_START, INVENTORY_SLOT_Y_START);
    }

    protected void addPlayerInventory(Inventory playerInventory, int inventoryX, int inventoryY) {
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                super.addSlot(new Slot(playerInventory, column + row * 9 + 9, inventoryX + column * SLOT_SIZE_PLUS_2, inventoryY + row * SLOT_SIZE_PLUS_2));
            }
        }
    }

    protected void addPlayerHotbar(Inventory playerInventory) {
        int HOTBAR_SLOT_X_START = 8;
        int HOTBAR_SLOT_Y_START = 142;
        this.addPlayerHotbar(playerInventory, HOTBAR_SLOT_X_START, HOTBAR_SLOT_Y_START);
    }

    protected void addPlayerHotbar(Inventory playerInventory, int hotbarX, int hotbarY) {
        for (int column = 0; column < 9; column++) {
            super.addSlot(new Slot(playerInventory, column, hotbarX + column * SLOT_SIZE_PLUS_2, hotbarY));
        }
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return this.access.evaluate((level, pos) -> level.getBlockEntity(pos) == this.blockEntity, true);
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        Slot sourceSlot = slots.get(index);
        if (!sourceSlot.hasItem()) return ItemStack.EMPTY;
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

    public List<DrillBaseBlockEntity> getConnectedCaterpillarBlockEntities() {
        return caterpillarBlockEntities;
    }

    public void setConnectedCaterpillarBlockEntities(List<DrillBaseBlockEntity> caterpillarBlockEntities) {
        this.caterpillarBlockEntities = caterpillarBlockEntities;
    }
}
