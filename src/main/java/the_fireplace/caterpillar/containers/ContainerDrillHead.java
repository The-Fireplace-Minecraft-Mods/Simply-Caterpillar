package the_fireplace.caterpillar.containers;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import the_fireplace.caterpillar.Caterpillar;

public class ContainerDrillHead extends Container
{
	private ContainerCaterpillar myCaterpillar;
	private IInventory playerInventory;
	private IInventory tileEntityInventory;
	public ContainerDrillHead(EntityPlayer player, IInventory tileEntityInventoryIn, ContainerCaterpillar inCaterpillar)
	{
		this.playerInventory = player.inventory;
		this.myCaterpillar = inCaterpillar;
		this.myCaterpillar.myDrillHead = this;
		this.tileEntityInventory = tileEntityInventoryIn;
		int i;
		int j;
		int ID = 0;

		//Burner
		this.addSlotToContainer(new Slot(tileEntityInventoryIn, ID++, 8 + (4) * 18, 7 + (3) * 18));

		//Left Side
		int IDMiddle   = (ContainerCaterpillar.getMaxSize() + this.myCaterpillar.storage.added - 1 )/2;
		//ID++;
		for (i = 0; i < (IDMiddle/3); ++i)
		{
			for (j = 0; j < 3; ++j)
			{
				this.addSlotToContainer(new Slot(tileEntityInventoryIn, ID++, 8 + j * 18, -100));
			}
		}

		//Right Side
		for (i = 0; i < (IDMiddle/3); ++i)
		{
			for (j = 0; j < 3; ++j)
			{
				this.addSlotToContainer(new Slot(tileEntityInventoryIn, ID++, 8 + (j + 6) * 18, -100));
			}
		}

		this.myCaterpillar.updateScroll(this);

		//Player Inventory
		for (i = 0; i < 3; ++i)
		{
			for (j = 0; j < 9; ++j)
			{
				this.addSlotToContainer(new Slot(this.playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (i = 0; i < 9; ++i)
		{
			this.addSlotToContainer(new Slot(this.playerInventory, i, 8 + i * 18, 142));
		}
	}
	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return this.tileEntityInventory.isUseableByPlayer(playerIn);
	}

	@Override
	public void detectAndSendChanges(){}

	private boolean isWood(ItemStack itemstack1)
	{
		Block thisBlock = Block.getBlockFromItem(itemstack1.getItem());
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
	public ItemStack slotClick(int slotId, int clickedButton, ClickType mode, EntityPlayer playerIn)
	{
		if (this.myCaterpillar != null)
		{
			if (this.myCaterpillar.tabs.selected.isCrafting)
			{
				if (slotId < this.getInventory().size() - 36)
				{
					if (slotId > -1)
					{
						ItemStack decoration =  null;

						ItemStack whattoKeep = playerIn.inventory.getItemStack();
						if (whattoKeep != null)
						{
							decoration = new ItemStack(whattoKeep.getItem(), 1, whattoKeep.getItemDamage());
						}
						Slot slot1 = this.getSlot(slotId);
						slot1.putStack(decoration);
						this.detectAndSendChanges();
						return null;
					}
				}
			}
		}
		/*if (mode == ClickType.QUICK_MOVE)
		{
			if (slotId == 0)
			{
				if (this.myCaterpillar.tabs.selected.equals(GuiTabs.MAIN))
				{
					//return null;
				}
			}
		}*/
		return super.slotClick(slotId, clickedButton, mode, playerIn);

	}

	/**
	 * Take a stack from the specified inventory slot.
	 */
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
	{
		ItemStack itemstack = null;
		Slot slot = this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if (this.myCaterpillar == null)
			{
				return null;
			}

			if (index < this.inventorySlots.size() - 36)
			{
				if (!this.mergeItemStack(itemstack1, this.inventorySlots.size() - 36, this.inventorySlots.size(), false))
				{
					return null;
				}
			}
			else if (this.myCaterpillar.tabs.selected.equals(Caterpillar.GuiTabs.MAIN))
			{
				if (TileEntityFurnace.isItemFuel(itemstack1) && !this.isWood(itemstack1))
				{
					if (!this.mergeItemStack(itemstack1, 0, 1, false))
					{
						return null;
					}
				}
				else
				{
					if (!this.mergeItemStack(itemstack1, 1, this.inventorySlots.size() - 36, false))
					{
						return null;
					}
				}
			}
			else //if (!this.mergeItemStack(itemstack1, 0, tileEntityInventory.getSizeInventory(), false))
			{
				return null;
			}

			if (itemstack1.stackSize == 0)
			{
				slot.putStack(null);
			}
			else
			{
				slot.onSlotChanged();
			}

			if (itemstack1.stackSize == itemstack.stackSize)
			{
				return null;
			}

			slot.onPickupFromSlot(playerIn, itemstack1);
		}

		return itemstack;
	}
	public void updateCaterpillar(ContainerCaterpillar caterpillar)
	{
		this.myCaterpillar = caterpillar;
		this.myCaterpillar.myDrillHead = this;
	}
}