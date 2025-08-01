package dev.the_fireplace.caterpillar.inventory;

import dev.the_fireplace.caterpillar.block.DrillBaseBlock;
import dev.the_fireplace.caterpillar.block.entity.DrillBaseBlockEntity;
import dev.the_fireplace.caterpillar.block.util.CaterpillarBlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.*;

import static dev.the_fireplace.caterpillar.block.DrillBaseBlock.FACING;

public abstract class AbstractCaterpillarMenu extends AbstractContainerMenu {

    public static final int SLOT_SIZE_PLUS_2 = 18;
    public static final int HOTBAR_SLOT_COUNT = 9;
    public static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    public static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    public static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    public static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    public static final int VANILLA_SLOT_START_INDEX = 0;
    public static final int VANILLA_SLOT_END_INDEX = VANILLA_SLOT_START_INDEX + VANILLA_SLOT_COUNT;
    public static final int BE_INVENTORY_SLOT_START_INDEX = VANILLA_SLOT_START_INDEX + VANILLA_SLOT_COUNT;
    private final int BE_INVENTORY_SLOT_COUNT;
    public final int BE_INVENTORY_SLOT_END_INDEX;

    private static final int INVENTORY_SLOT_X_START = 8;
    private static final int INVENTORY_SLOT_Y_START = 84;

    private final Container container;
    final ContainerData data;
    protected final Level level;

    public final DrillBaseBlockEntity blockEntity;
    private List<? extends DrillBaseBlock> connectedBlocks;

    protected AbstractCaterpillarMenu(MenuType<?> menuType, int containerId, Inventory playerInventory, FriendlyByteBuf extraData, int containerDataSize) {
        this(menuType, containerId, playerInventory, getBlockEntity(playerInventory, extraData), new SimpleContainerData(containerDataSize));
    }

    protected AbstractCaterpillarMenu(MenuType<?> menuType, int containerId, Inventory playerInventory, DrillBaseBlockEntity blockEntity, ContainerData data) {
        super(menuType, containerId);

        this.blockEntity = blockEntity;
        this.container = blockEntity;
        this.data = data;
        this.level = playerInventory.player.level();

        this.BE_INVENTORY_SLOT_COUNT = this.container.getContainerSize();
        this.BE_INVENTORY_SLOT_END_INDEX = BE_INVENTORY_SLOT_START_INDEX + this.BE_INVENTORY_SLOT_COUNT;

        this.setConnectedBlocks();

        this.addStandardInventorySlots(playerInventory, INVENTORY_SLOT_X_START, INVENTORY_SLOT_Y_START);
        this.addSlots(container);

        this.addDataSlots(data);
    }

    private static DrillBaseBlockEntity getBlockEntity(Inventory playerInventory, FriendlyByteBuf extraData) {
        BlockPos blockPos = extraData.readBlockPos();
        BlockEntity blockEntity = playerInventory.player.level().getBlockEntity(blockPos);

        if (blockEntity instanceof DrillBaseBlockEntity caterpillarBlockEntity) {
            return caterpillarBlockEntity;
        } else {
            throw new IllegalArgumentException("Expected a DrillBaseBlockEntity at " + blockPos + ", but found " + blockEntity);
        }
    }

    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot.hasItem()) {
            ItemStack stackInSlot = slot.getItem();
            itemStack = stackInSlot.copy();

            // Check if the slot clicked is one of the vanilla container slots
            if (index < VANILLA_SLOT_END_INDEX) {
                // This is a vanilla slot so merge the stack into the BE inventory
                if (!this.moveItemStackTo(stackInSlot, BE_INVENTORY_SLOT_START_INDEX, BE_INVENTORY_SLOT_END_INDEX, false)) return ItemStack.EMPTY;
            } else if (index < BE_INVENTORY_SLOT_END_INDEX) {
                // This is a BE slot so merge the stack into the player inventory
                if (!this.moveItemStackTo(stackInSlot, VANILLA_SLOT_START_INDEX, VANILLA_SLOT_END_INDEX, false)) return ItemStack.EMPTY;
            }

            // If the entire stack is being moved, clear the slot
            if (stackInSlot.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            slot.onTake(player, stackInSlot);
        }

        return itemStack;
    }

    protected abstract void addSlots(Container container);

    public List<? extends DrillBaseBlock> getConnectedBlocks() {
      return this.connectedBlocks;
    }

    private void setConnectedBlocks() {
        if (this.connectedBlocks == null) {
            BlockPos blockPos = this.blockEntity.getBlockPos();
            Direction direction = this.blockEntity.getBlockState().getValue(FACING);

            BlockPos caterpillarHeadPos = CaterpillarBlockUtil.getCaterpillarHeadPos(this.level, blockPos, direction);
            this.connectedBlocks = CaterpillarBlockUtil.getConnectedCaterpillarBlocks(this.level, caterpillarHeadPos);
        }
    }
}
