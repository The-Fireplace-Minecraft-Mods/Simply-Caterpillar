package dev.the_fireplace.caterpillar.block.entity;

import dev.the_fireplace.caterpillar.Constants;
import dev.the_fireplace.caterpillar.inventory.IncineratorMenu;
import dev.the_fireplace.caterpillar.registry.BlockEntityTypesRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class IncineratorBlockEntity extends DrillBaseBlockEntity {

    public static final Component TITLE = Component.translatable(
            "container." + Constants.MOD_ID + ".incinerator"
    );

    public static final int INVENTORY_SIZE = 9;

    public IncineratorBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityTypesRegistry.INCINERATOR.get(), pos, state, INVENTORY_SIZE);

        this.setDefaultIncinerationBlocks();
    }

    private void setDefaultIncinerationBlocks() {
        this.setItem(0, new ItemStack(Blocks.GRAVEL));
        this.setItem(1, new ItemStack(Blocks.SAND));
        this.setItem(2, new ItemStack(Blocks.RED_SAND));
        this.setItem(3, new ItemStack(Blocks.COBBLESTONE));
        this.setItem(4, new ItemStack(Blocks.DIRT));
    }

    @Override
    public Component getDisplayName() {
        return TITLE;
    }

    @Override
    public IncineratorMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new IncineratorMenu(containerId, playerInventory, this, new SimpleContainerData(0));
    }
}
