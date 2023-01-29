package dev.the_fireplace.caterpillar.block.entity;

import dev.the_fireplace.caterpillar.block.entity.util.ImplementedInventory;
import dev.the_fireplace.caterpillar.init.BlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class DrillBaseBlockEntity extends BlockEntity implements MenuProvider, ImplementedInventory {

    private final NonNullList<ItemStack> inventory = NonNullList.withSize(1, ItemStack.EMPTY);

    public DrillBaseBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntityInit.DRILL_BASE, blockPos, blockState);
    }

    public DrillBaseBlockEntity(BlockEntityType<?> type, BlockPos blockPos, BlockState blockState) {
        super(type, blockPos, blockState);
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return null;
    }

    @Override
    public Component getDisplayName() {
        return Component.empty();
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        ContainerHelper.saveAllItems(compoundTag, this.inventory);
        super.saveAdditional(compoundTag);
    }

    @Override
    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);
        ContainerHelper.loadAllItems(compoundTag, this.inventory);
    }
}
