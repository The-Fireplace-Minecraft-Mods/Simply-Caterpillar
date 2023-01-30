package dev.the_fireplace.caterpillar.block.entity;

import dev.the_fireplace.caterpillar.block.entity.util.ImplementedInventory;
import dev.the_fireplace.caterpillar.init.BlockEntityInit;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

public class DrillBaseBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory, ImplementedInventory {

    protected DefaultedList<ItemStack> inventory;

    public DrillBaseBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(BlockEntityInit.DRILL_BASE, blockPos, blockState, 0);
    }

    public DrillBaseBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState, int inventorySize) {
        super(blockEntityType, blockPos, blockState);

        this.inventory = DefaultedList.ofSize(inventorySize, ItemStack.EMPTY);
    }

    public DrillBaseBlockEntity(BlockEntityType<?> type, BlockPos blockPos, BlockState blockState) {
        super(type, blockPos, blockState);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public ScreenHandler createMenu(int containerId, PlayerInventory playerInventory, PlayerEntity player) {
        return null;
    }

    @Override
    public Text getDisplayName() {
        return Text.empty();
    }

    @Override
    protected void writeNbt(NbtCompound compoundTag) {
        Inventories.writeNbt(compoundTag, this.inventory);
        super.writeNbt(compoundTag);
    }

    @Override
    public void readNbt(NbtCompound compoundTag) {
        super.readNbt(compoundTag);
        Inventories.readNbt(compoundTag, this.inventory);
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(this.pos);
    }
}
