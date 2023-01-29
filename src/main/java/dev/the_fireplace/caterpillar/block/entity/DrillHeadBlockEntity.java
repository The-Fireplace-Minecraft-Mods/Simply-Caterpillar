package dev.the_fireplace.caterpillar.block.entity;

import dev.the_fireplace.caterpillar.Caterpillar;
import dev.the_fireplace.caterpillar.init.BlockEntityInit;
import dev.the_fireplace.caterpillar.menu.DrillHeadMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class DrillHeadBlockEntity extends DrillBaseBlockEntity {

    public static final Component TITLE = Component.translatable(
            "container." + Caterpillar.MOD_ID + ".drill_head"

    );

    public static final int INVENTORY_SIZE = 19;

    public DrillHeadBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntityInit.DRILL_HEAD, blockPos, blockState);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, DrillHeadBlockEntity blockEntity) {

    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new DrillHeadMenu(containerId, playerInventory, this);
    }

    @Override
    public Component getDisplayName() {
        return TITLE;
    }
}
