package dev.the_fireplace.caterpillar.menu;

import dev.the_fireplace.caterpillar.block.entity.DrillHeadBlockEntity;
import dev.the_fireplace.caterpillar.init.MenuInit;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.math.BlockPos;

public class DrillHeadMenu extends ScreenHandler {

    private final Inventory inventory;
    private BlockPos pos;

    public DrillHeadMenu(int containerId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(containerId, playerInventory, new SimpleInventory(DrillHeadBlockEntity.INVENTORY_SIZE));
        pos = buf.readBlockPos();
    }

    public DrillHeadMenu(int containerId, PlayerInventory playerInventory, Inventory inventory) {
        super(MenuInit.DRILL_HEAD, containerId);
        checkSize(inventory, DrillHeadBlockEntity.INVENTORY_SIZE);
        this.inventory = inventory;
        pos = BlockPos.ORIGIN;
        inventory.onOpen(playerInventory.player);
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int i) {
        return null;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }
}
