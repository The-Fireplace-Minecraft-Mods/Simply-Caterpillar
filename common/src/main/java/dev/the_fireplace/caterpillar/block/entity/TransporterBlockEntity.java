package dev.the_fireplace.caterpillar.block.entity;

import dev.the_fireplace.caterpillar.Constants;
import dev.the_fireplace.caterpillar.block.TransporterBlock;
import dev.the_fireplace.caterpillar.inventory.TransporterMenu;
import dev.the_fireplace.caterpillar.registry.BlockEntityTypesRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import org.jetbrains.annotations.Nullable;

import static dev.the_fireplace.caterpillar.block.TransporterBlock.HALF;

public class TransporterBlockEntity extends DrillBaseBlockEntity {

    public static final Component TITLE = Component.translatable("container." + Constants.MOD_ID + ".transporter");

    public static final int INVENTORY_SIZE = 27;

    public TransporterBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityTypesRegistry.TRANSPORTER.get(), pos, state, INVENTORY_SIZE);
    }

    @Override
    public void move() {}

    private void transport() {}

    public boolean hasMinecartChest() {
        BlockState belowState = level.getBlockState(this.getBlockPos().below());

        return belowState.getBlock() instanceof TransporterBlock && belowState.getValue(HALF) == DoubleBlockHalf.LOWER;
    }

    public void releaseMinecartChest() {

    }

    @Override
    public Component getDisplayName() {
        return TITLE;
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new TransporterMenu(containerId, playerInventory, this, new SimpleContainerData(0));
    }
}
