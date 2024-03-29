package dev.the_fireplace.caterpillar.block.entity.util;

import dev.the_fireplace.caterpillar.network.PacketHandler;
import dev.the_fireplace.caterpillar.network.packet.server.CaterpillarSyncInventoryS2CPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class InventoryBlockEntity extends BlockEntity {

    public final int size;
    protected int timer;

    private final ItemStackHandler inventory;

    private LazyOptional<IItemHandlerModifiable> handler;


    public InventoryBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, int size) {
        super(type, pos, state);

        this.size = size;
        this.timer = 0;

        this.inventory = createInventory();
        this.handler = LazyOptional.of(() -> this.inventory);
    }

    protected ItemStackHandler createInventory() {
        return new ItemStackHandler(this.size) {
            @Override
            public void onContentsChanged(int slot) {
                setChanged();

                if(level != null && !level.isClientSide()) {
                    PacketHandler.sendToClients(new CaterpillarSyncInventoryS2CPacket(this, worldPosition));
                }
            }
        };
    }

    public void setInventory(ItemStackHandler inventory) {
        for (int i = 0; i < inventory.getSlots(); i++) {
            setStackInSlot(i, inventory.getStackInSlot(i));
        }
    }

    public ItemStackHandler getInventory() {
        return this.inventory;
    }

    public void clearInventory() {
        for (int i = 0; i < this.size; i++) {
            this.inventory.setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return this.handler.cast();
        }

        return super.getCapability(cap, side);
    }

    public ItemStack getStackInSlot(int slot) {
        return this.handler.map(inventory -> inventory.getStackInSlot(slot)).orElse(ItemStack.EMPTY);
    }

    public void setStackInSlot(int slot, ItemStack stack) {
        this.handler.ifPresent(handler -> handler.setStackInSlot(slot, stack));
    }

    public void removeStackInSlot(int slot) {
       this.setStackInSlot(slot, ItemStack.EMPTY);
    }

    public ItemStack insertItem(int slot, ItemStack stack) {
        ItemStack copy = stack.copy();
        copy.setCount(1);
        return this.handler.map(inventory -> inventory.insertItem(slot, copy, false)).orElse(ItemStack.EMPTY);
    }

    public ItemStack extractItem(int slot, int amount) {
        return this.handler.map(inventory -> inventory.extractItem(slot, amount, false)).orElse(ItemStack.EMPTY);
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);

        this.inventory.deserializeNBT(tag.getCompound("Inventory"));
    }

    @Override
    public void onLoad() {
        this.handler = LazyOptional.of(() -> this.inventory);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        tag.put("Inventory", this.inventory.serializeNBT());

        super.saveAdditional(tag);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();

        this.handler.invalidate();
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(this.inventory.getSlots());

        for (int i = 0; i < this.inventory.getSlots(); i++) {
            inventory.setItem(i, this.inventory.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }
}
