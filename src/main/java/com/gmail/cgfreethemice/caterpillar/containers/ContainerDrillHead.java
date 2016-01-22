package com.gmail.cgfreethemice.caterpillar.containers;

import com.gmail.cgfreethemice.caterpillar.Caterpillar.GuiTabs;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;

public class ContainerDrillHead extends Container
{
	private IInventory playerInventory;
	private IInventory tileEntityInventory;
	private ContainerCaterpillar myCaterpillar;
	private EntityPlayerMP player;
	public ContainerDrillHead(EntityPlayer player, IInventory tileEntityInventoryIn, ContainerCaterpillar inCaterpillar)
	{
		if (player instanceof EntityPlayerMP)
		{
			this.player = (EntityPlayerMP) player;
		}
		this.playerInventory = player.inventory;
		this.myCaterpillar = inCaterpillar;
		this.myCaterpillar.myDrillHead = this;
		this.tileEntityInventory = tileEntityInventoryIn;
		int i;
		int j;
		int ID = 0;

		//Burner
		this.addSlotToContainer(new Slot(tileEntityInventoryIn, ID, 8 + (4) * 18, 7 + (3) * 18));

		//Left Side
		int IDMiddle   = (ContainerCaterpillar.getMaxSize() + this.myCaterpillar.storage.added - 1 )/2;
		ID++;
		for (i = 0; i < (IDMiddle/3); ++i)
		{
			for (j = 0; j < 3; ++j)
			{
				this.addSlotToContainer(new Slot(tileEntityInventoryIn,ID, 8 + j * 18, -100));
				ID++;
			}
		}

		//Right Side

		for (i = 0; i < (IDMiddle/3); ++i)
		{
			for (j = 0; j < 3; ++j)
			{
				this.addSlotToContainer(new Slot(tileEntityInventoryIn, ID, 8 + (j + 6) * 18, -100));
				ID++;
			}
		}


		this.myCaterpillar.updateScroll(this);


		//Player Inventory
		for (i = 0; i < 3; ++i)
		{
			for (j = 0; j < 9; ++j)
			{
				this.addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (i = 0; i < 9; ++i)
		{
			this.addSlotToContainer(new Slot(playerInventory, i, 8 + i * 18, 142));
		}
	}
	public void updateCaterpillar(ContainerCaterpillar caterpillar)
	{
		this.myCaterpillar = caterpillar;
		this.myCaterpillar.myDrillHead = this;
	}
	@Override
	public ItemStack slotClick(int slotId, int clickedButton, int mode, EntityPlayer playerIn)
	{
		if (this.myCaterpillar != null)
		{
			if (this.myCaterpillar.tabs.selected.isCrafting)
			{
				if (slotId < this.getInventory().size() - 36)
				{
					if (slotId > -1)
					{
						ItemStack decorstion =  null;

						ItemStack whattoKeep = playerIn.inventory.getItemStack();
						if (whattoKeep != null)
						{
							decorstion = new ItemStack(whattoKeep.getItem(), 1, whattoKeep.getItemDamage());
						}
						Slot slot1 = this.getSlot(slotId);
						slot1.putStack(decorstion);
						this.detectAndSendChanges();
						return null;
					}
				}
			}
		}
		if (mode == 1)
		{
			if (slotId == 0)
			{
				if (this.myCaterpillar.tabs.selected.equals(GuiTabs.MAIN))
				{
					//return null;
				}
			}
		}
		return super.slotClick(slotId, clickedButton, mode, playerIn);

	}
	@Override
	public void detectAndSendChanges()
	{


		//super.detectAndSendChanges();

		/*if (this.player != null)
		{
			this.myCaterpillar.inventory[1] = new ItemStack(Blocks.bedrock);
			Caterpillar.network.sendTo(new PacketCaterpillarControls(this.myCaterpillar), this.player);
		}*/

	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return this.tileEntityInventory.isUseableByPlayer(playerIn);
	}

	/**
	 * Take a stack from the specified inventory slot.
	 */
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
	{
		ItemStack itemstack = null;
		Slot slot = (Slot)this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if (this.myCaterpillar == null)
			{
				return null;
			}
			


			
			
			if (index < tileEntityInventory.getSizeInventory())
			{
				if (!this.mergeItemStack(itemstack1, tileEntityInventory.getSizeInventory(), tileEntityInventory.getSizeInventory() + 36, true))
				{
					return null;
				}
			}
			else if (this.myCaterpillar.tabs.selected.equals(GuiTabs.MAIN))
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
                    if (!this.mergeItemStack(itemstack1, 1, tileEntityInventory.getSizeInventory(), false))
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
				slot.putStack((ItemStack)null);
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
	private boolean isWood(ItemStack itemstack1)
	{
		Block thisBlock = Block.getBlockFromItem(itemstack1.getItem());
		if (thisBlock != null)
		{
			if (thisBlock.getMaterial().equals(Material.wood))
			{
				return true;
			}
		}
		return false;
		
		
	}
}