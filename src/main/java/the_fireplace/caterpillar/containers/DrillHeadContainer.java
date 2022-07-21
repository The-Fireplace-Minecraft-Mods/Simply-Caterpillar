package the_fireplace.caterpillar.containers;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.FurnaceTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.fml.network.IContainerFactory;
import net.minecraftforge.items.ItemStackHandler;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.init.BlockInit;
import the_fireplace.caterpillar.init.ContainerInit;
import the_fireplace.caterpillar.init.ModCreativeTab;
import the_fireplace.caterpillar.tileentity.DrillHeadBlockEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class DrillHeadContainer extends AbstractContainerMenu {

    private CaterpillarContainer caterpillar;

    private ItemStackHandler inventory;
    public final DrillHeadBlockEntity tileEntity;
    private final IWorldPosCallable canInteractWithCallable;

    /**
     * Logical-client-side constructor, called from {@link net.minecraft.inventory.container.ContainerType#create(IContainerFactory)}
     * Calls the logical-server-side constructor with the TileEntity at the pos in the PacketBuffer
     */
    public DrillHeadContainer(final int id, final Inventory playerInventory) {
        this(id, playerInventory, getTileEntity(playerInventory, data), getTileEntity(playerInventory, data).Caterpillar());
    }

    /**
     * Constructor called logical-server-side from {@link DrillHeadBlockEntity#createMenu}
     * and logical-client-side from {@link #DrillHeadContainer(int, PlayerInventory, PacketBuffer)}
     */
    public DrillHeadContainer(final int id, final Inventory playerInventory, final DrillHeadBlockEntity tileEntity, final CaterpillarContainer caterpillar) {
        super(ContainerInit.DRILL_HEAD.get(), id);

        this.caterpillar = caterpillar;
        this.caterpillar.drillHeadContainer = this;
        this.tileEntity = tileEntity;
        // TODO: fix this issue
        this.canInteractWithCallable = IWorldPosCallable.of(tileEntity.getWorld(), tileEntity.getPos());

        int row, column, ID = 0;

        // Burner
        this.addSlot(new Slot(tileEntityInventory, ID++, 8 + (4) * 18, 7 + (3) * 18));

        int IDMiddle = (CaterpillarContainer.getMaxSize() + this.caterpillar.storage.added - this.caterpillar.storage.startingIndex) / 2;

        // Left Side
        for (row = 0; row < (IDMiddle/3); ++row)
        {
            for (column = 0; column < 3; ++column)
            {
                this.addSlot(new Slot(tileEntityInventory, ID++, 8 + column * 18, -100));
            }
        }

        // Right side
        for (row = 0; row < (IDMiddle/3); ++row)
        {
            for (column = 0; column < 3; ++column)
            {
                this.addSlot(new Slot(tileEntityInventory, ID++, 8 + (column + 6), -100));
            }
        }

        Caterpillar.LOGGER.debug("Slot Count: " + ID);

        this.caterpillar.updateScroll(this);

        // PlayerInventory
        for (row = 0; row < 3; ++row)
        {
            for (column = 0; column < 9; ++column)
            {
                this.addSlot(new Slot(playerInventory, column + row * 9 + 9, 8 + column *18, 84 + row *18));
            }
        }

        for (column = 0; column < 9; ++column)
        {
            this.addSlot(new Slot(playerInventory, row, 8 + row * 18, 142));
        }
    }

    private static DrillHeadBlockEntity getTileEntity(final PlayerInventory playerInventory, final PacketBuffer data) {
        Objects.requireNonNull(playerInventory, "playerInventory cannot be null");
        Objects.requireNonNull(data, "data cannot be null");
        final TileEntity tileAtPos = playerInventory.player.world.getTileEntity(data.readBlockPos());
        if (tileAtPos instanceof DrillHeadBlockEntity) {
            return (DrillHeadBlockEntity) tileAtPos;
        }
        throw new IllegalStateException("Tile entity is not correct!" + tileAtPos);
    }

    @Override
    public boolean canInteractWith(@Nonnull final PlayerEntity player) {
        return isWithinUsableDistance(canInteractWithCallable, player, BlockInit.DRILL_BASE.get());
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
    }

    private boolean isWood(ItemStack itemStack1)
    {
        Block thisBlock = Block.getBlockFromItem(itemStack1.getItem());
        if (thisBlock != null)
        {
            if (thisBlock.getDefaultState().getMaterial().equals(Material.WOOD))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, PlayerEntity player) {
        if (this.caterpillar != null)
        {
            if (this.caterpillar.tabs.selected.isCrafting)
            {
                if (slotId < this.getInventory().size() - 36)
                {
                    if (slotId > -1)
                    {
                        ItemStack decoration =  null;

                        ItemStack whatToKeep = player.inventory.getItemStack();
                        if (whatToKeep != null)
                        {
                            // TODO: check Nullable last value
                            decoration = new ItemStack(whatToKeep.getItem(), 1);
                        }
                        Slot slot1 = this.getSlot(slotId);
                        slot1.putStack(decoration);
                        this.detectAndSendChanges();
                        return null;
                    }
                }
            }
        }
        return super.slotClick(slotId, dragType, clickTypeIn, player);
    }

    @Nullable
    @Override
    public ItemStack transferStackInSlot(final PlayerEntity playerIn, final int index) {
        ItemStack itemstack = null;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemStack1 = slot.getStack();
            itemstack = itemStack1.copy();
            if (this.caterpillar == null)
            {
                return null;
            }

            if (index < this.inventorySlots.size() - 36)
            {
                if (!this.mergeItemStack(itemStack1, this.inventorySlots.size() - 36, this.inventorySlots.size(), false))
                {
                    return null;
                }
            }
            else if (this.caterpillar.tabs.selected.equals(ModCreativeTab.MOD_ITEM_GROUP))
            {
                if (FurnaceTileEntity.isFuel(itemStack1) && !this.isWood(itemStack1))
                {
                    if (!this.mergeItemStack(itemStack1, 0, 1, false))
                    {
                        return null;
                    }
                }
                else
                {
                    if (!this.mergeItemStack(itemStack1, 1, this.inventorySlots.size() - 36, false))
                    {
                        return null;
                    }
                }
            }
            else //if (!this.mergeItemStack(itemstack1, 0, caterpillarTileEntity.getSizeInventory(), false))
            {
                return null;
            }

            if (itemStack1.getStack().getCount() == 0)
            {
                slot.putStack(null);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemStack1.getStack().getCount() == itemstack.getStack().getCount())
            {
                return null;
            }

            slot.onTake(playerIn, itemStack1);
        }

        return itemstack;
    }

    public void updateCaterpillar(CaterpillarContainer caterpillar)
    {
        this.caterpillar = caterpillar;
        this.caterpillar.drillHeadContainer = this;
    }
}
