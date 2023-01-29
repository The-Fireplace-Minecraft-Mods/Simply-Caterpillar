package dev.the_fireplace.caterpillar.menu;

import dev.the_fireplace.caterpillar.block.entity.DrillHeadBlockEntity;
import dev.the_fireplace.caterpillar.init.MenuInit;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class DrillHeadMenu extends AbstractContainerMenu {

    private final Container inventory;

    public DrillHeadMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, new SimpleContainer(DrillHeadBlockEntity.INVENTORY_SIZE));
    }

    public DrillHeadMenu(int containerId, Inventory playerInventory, Container inventory) {
        super(MenuInit.DRILL_HEAD, containerId);
        checkContainerSize(inventory, DrillHeadBlockEntity.INVENTORY_SIZE);
        this.inventory = inventory;
        inventory.startOpen(playerInventory.player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return null;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.inventory.stillValid(player);
    }
}
