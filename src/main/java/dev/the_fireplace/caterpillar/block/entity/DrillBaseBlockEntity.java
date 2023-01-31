package dev.the_fireplace.caterpillar.block.entity;

import dev.the_fireplace.caterpillar.block.DrillBaseBlock;
import dev.the_fireplace.caterpillar.block.entity.util.ImplementedInventory;
import dev.the_fireplace.caterpillar.config.ConfigHolder;
import dev.the_fireplace.caterpillar.init.BlockEntityInit;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class DrillBaseBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory, ImplementedInventory {

    public NonNullList<ItemStack> inventory;

    protected int timer = 0;

    public DrillBaseBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(BlockEntityInit.DRILL_BASE, blockPos, blockState, 0);
    }

    public DrillBaseBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState, int inventorySize) {
        super(blockEntityType, blockPos, blockState);

        this.timer = 0;

        this.inventory = NonNullList.withSize(inventorySize, ItemStack.EMPTY);
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

    public void move() {
        Direction direction = this.getBlockState().getValue(DrillBaseBlock.FACING);
        BlockPos nextPos = this.getBlockPos().relative(direction);

        this.getLevel().setBlockAndUpdate(nextPos, this.getBlockState());
        this.getLevel().removeBlock(this.getBlockPos(), false);

        if (ConfigHolder.enableSounds) {
            this.getLevel().playSound(null, this.getBlockPos(), SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
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

    @Override
    public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {
        buf.writeBlockPos(this.worldPosition);
    }

    public void setInventory(NonNullList<ItemStack> inventory) {
        this.inventory = inventory;
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(this.inventory.size());

        for (int i = 0; i < this.inventory.size(); i++) {
            inventory.setItem(i, this.inventory.get(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    // TODO: Need to implement this
    protected boolean takeItemFromCaterpillarConsumption(Item item) {
        return true;
    }

    // TODO: Need to implement this
    protected ItemStack insertItemStackToCaterpillarGathered(ItemStack stack) {
        return stack;
    }

    // TODO: Need to implement this
    protected void removeItemFromCaterpillarGathered(Item item) {

    }
}
