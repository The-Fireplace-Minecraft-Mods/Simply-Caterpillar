package dev.the_fireplace.caterpillar.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import dev.the_fireplace.caterpillar.block.entity.AbstractCaterpillarBlockEntity;

public abstract class AbstractScrollableMenu extends AbstractCaterpillarMenu {
    private float scrollOffs;
    private boolean scrolling;

    public AbstractScrollableMenu(MenuType<?> menuType, int id, Inventory playerInventory, AbstractCaterpillarBlockEntity blockEntity, ContainerData data, int inventorySize) {
        super(menuType, id, playerInventory, blockEntity, data, inventorySize);
    }

    public AbstractScrollableMenu(MenuType<?> menuType, int id, Inventory playerInventory, FriendlyByteBuf extraData, int containerDataSize, int inventorySize) {
        super(menuType, id, playerInventory, extraData, containerDataSize, inventorySize);
    }

    public abstract void scrollTo(int scrollTo);

    public float getScrollOffs() {
        return this.scrollOffs;
    }

    public void setScrollOffs(float scrollOffs) {
        this.scrollOffs = scrollOffs;
    }

    public boolean isScrolling() {
        return scrolling;
    }

    public void setScrolling(boolean scrolling) {
        this.scrolling = scrolling;
    }
}
