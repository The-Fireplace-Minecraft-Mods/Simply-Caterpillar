package dev.the_fireplace.caterpillar.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public abstract class AbstractCaterpillarMenu extends AbstractContainerMenu {

    public static final int SLOT_SIZE_PLUS_2 = 18;
    public static final int HOTBAR_SLOT_COUNT = 9;
    public static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    public static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    public static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    public static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    public static final int VANILLA_FIRST_SLOT_INDEX = 0;
    public static final int BE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    // private final int BE_INVENTORY_SLOT_COUNT;

    private static final int INVENTORY_SLOT_X_START = 8;
    private static final int INVENTORY_SLOT_Y_START = 84;

    private final Container container;
    private final ContainerData data;
    private final Level level;

    protected AbstractCaterpillarMenu(MenuType<?> menuType, int containerId, Inventory playerInventory, int containerDataSize, int inventorySize) {
        this(menuType, containerId, playerInventory, new SimpleContainer(inventorySize), new SimpleContainerData(containerDataSize), containerDataSize, inventorySize);
    }

    protected AbstractCaterpillarMenu(MenuType<?> menuType, int containerId, Inventory playerInventory, Container container, ContainerData data, int containerDataSize, int inventorySize) {
        super(menuType, containerId);
        checkContainerSize(container, inventorySize);
        checkContainerDataCount(data, containerDataSize);

        this.container = container;
        this.data = data;
        this.level = playerInventory.player.level();

        this.addStandardInventorySlots(playerInventory, INVENTORY_SLOT_X_START, INVENTORY_SLOT_Y_START);
        this.addSlots(container);

        this.addDataSlots(data);
    }

    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return null;
    }

    protected boolean isFuel(ItemStack stack) {
        return this.level.fuelValues().isFuel(stack);
    }

    protected abstract void addSlots(Container container);
}
