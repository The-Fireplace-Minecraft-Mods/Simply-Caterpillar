package com.gmail.cgfreethemice.caterpillar.tileentity;

import java.util.Random;

import com.gmail.cgfreethemice.caterpillar.Caterpillar;
import com.gmail.cgfreethemice.caterpillar.Caterpillar.GuiTabs;
import com.gmail.cgfreethemice.caterpillar.Reference;
import com.gmail.cgfreethemice.caterpillar.blocks.BlockCollector;
import com.gmail.cgfreethemice.caterpillar.blocks.BlockDecoration;
import com.gmail.cgfreethemice.caterpillar.blocks.BlockDrillBase;
import com.gmail.cgfreethemice.caterpillar.blocks.BlockDrillHeads;
import com.gmail.cgfreethemice.caterpillar.blocks.BlockReinforcements;
import com.gmail.cgfreethemice.caterpillar.blocks.BlockStorage;
import com.gmail.cgfreethemice.caterpillar.containers.ContainerCaterpillar;
import com.gmail.cgfreethemice.caterpillar.containers.ContainerDrillHead;
import com.gmail.cgfreethemice.caterpillar.inits.InitBlocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntityLockable;

public class TileEntityDrillHead extends TileEntityLockable implements IInventory, IUpdatePlayerListBox
{
	private static final Random RNG = new Random();

	protected String customName;
	public boolean isRemote = false;
	public ContainerDrillHead MyDrillhead;
	public boolean isSelected = false;
	public TileEntityDrillHead()
	{
		//Reference.printDebug("No Remote World?");
	}
	private ContainerCaterpillar MyCaterpillar()
	{
		if (this.isSelected)
		{
			return Caterpillar.instance.getSelectedCaterpillar();
		}
		return Caterpillar.instance.getContainerCaterpillar(this.pos);
	}
	private ItemStack[] FixZeroItem(ItemStack[] toFix)
	{
		for (int i = 0; i < toFix.length; i++) {
			ItemStack K = toFix[i];
			if (K != null)
			{
				if (K.stackSize < 1)
				{
					toFix[i] = null;
				}
			}
		}
		return toFix;
	}
	public ItemStack[] getItemStacks()
	{

		try {
			return  FixZeroItem(Caterpillar.instance.getInventory(this.MyCaterpillar(), this.MyCaterpillar().tabs.selected));
		} catch (Exception e) {
			return new ItemStack[256];
		}

	}

	@Override
	public int getSizeInventory()
	{
		return this.getItemStacks().length;
	}
	private void setCustomItem(int index, ItemStack inwego)
	{
		if (index < this.getItemStacks().length)
		{
			this.getItemStacks()[index] = inwego;
		}
	}
	private ItemStack getCustomItem(int index)
	{
		if (index >= this.getItemStacks().length)
		{
			return null;
		}
		return this.getItemStacks()[index];
	}
	@Override
	public ItemStack getStackInSlot(int index)
	{

		return this.getCustomItem(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count)
	{
		if (this.getCustomItem(index) != null)
		{
			ItemStack itemstack;

			if (this.getCustomItem(index).stackSize <= count)
			{
				itemstack =this.getCustomItem(index);
				this.setCustomItem(index, null);
				this.markDirty();
				return itemstack;
			}
			else
			{
				itemstack =this.getCustomItem(index).splitStack(count);

				if (this.getCustomItem(index).stackSize == 0)
				{
					this.setCustomItem(index, null);
				}

				this.markDirty();
				return itemstack;
			}
		}
		else
		{
			return null;
		}
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int index)
	{
		this.clear();
		return null;
	}

	public int getDispenseSlot()
	{
		int i = -1;
		int j = 1;

		for (int k = 0; k <this.getItemStacks().length; ++k)
		{
			if (this.getItemStacks()[k] != null && RNG.nextInt(j++) == 0)
			{
				i = k;
			}
		}

		return i;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack)
	{
		this.setCustomItem(index, stack);

		if (stack != null && stack.stackSize > this.getInventoryStackLimit())
		{
			stack.stackSize = this.getInventoryStackLimit();
		}
		this.markDirty();
	}

	/**
	 * Add the given ItemStack to this Dispenser. Return the Slot the Item was placed in or -1 if no free slot is
	 * available.
	 */
	public int addItemStack(ItemStack stack)
	{
		for (int i = 0; i <this.getItemStacks().length; ++i)
		{
			if (this.getItemStacks()[i] == null ||this.getItemStacks()[i].getItem() == null)
			{
				this.setInventorySlotContents(i, stack);
				return i;
			}
		}

		return -1;
	}

	/**
	 * Gets the name of this command sender (usually username, but possibly "Rcon")
	 */
	@Override
	public String getName()
	{
		return this.hasCustomName() ? this.customName : "Item Teleporter";
	}

	public void setCustomName(String customName)
	{
		this.customName = customName;
	}

	/**
	 * Returns true if this thing is named
	 */
	@Override
	public boolean hasCustomName()
	{
		return this.customName != null;
	}

	@Override
	public int getInventoryStackLimit()
	{
		if (this.MyCaterpillar() != null)
		{

			if (this.MyCaterpillar().tabs.selected.equals(GuiTabs.DECORATION) || this.MyCaterpillar().tabs.selected.equals(GuiTabs.REINFORCEMENT))
			{
				return 1;
			}
		}
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return this.worldObj.getTileEntity(this.pos) != this ? false : player.getDistanceSq(this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory(EntityPlayer player) {}

	@Override
	public void closeInventory(EntityPlayer player) {}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack)
	{
		return true;
	}

	@Override
	public String getGuiID()
	{

		return Reference.MODID  + ":" + this.blockType.getUnlocalizedName().substring(5);
	}

	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
	{

		return new ContainerDrillHead(playerIn, this, this.MyCaterpillar());
	}

	@Override
	public int getField(int id)
	{
		return 0;
	}

	@Override
	public void setField(int id, int value) {}

	@Override
	public int getFieldCount()
	{
		return 0;
	}
	public boolean MergeStack(TileEntityLockable tmpTileE, Item toAdd)
	{
		for(int i=0;i<tmpTileE.getSizeInventory();i++)
		{
			if (tmpTileE.getStackInSlot(i) != null)
			{
				ItemStack thisPlace = tmpTileE.getStackInSlot(i);
				if (thisPlace.getItem().equals(toAdd))
				{
					if (thisPlace.stackSize < thisPlace.getMaxStackSize())
					{
						thisPlace = new ItemStack(toAdd, thisPlace.stackSize + 1);
						tmpTileE.setInventorySlotContents(i, thisPlace);
						return true;
					}
				}
			}
		}
		for(int i=0;i<tmpTileE.getSizeInventory();i++)
		{
			if (tmpTileE.getStackInSlot(i) == null)
			{
				ItemStack thisPlace = new ItemStack(toAdd, 1);
				tmpTileE.setInventorySlotContents(i, thisPlace);
				return true;
			}
		}
		return false;
	}
	@Override
	public void clear()
	{
	}
	@Override
	public void update() {

		IBlockState blockdriller =  this.worldObj.getBlockState(this.pos);
		if (blockdriller.getBlock() instanceof BlockDrillHeads)
		{
			((BlockDrillHeads)InitBlocks.drillheads).movieMe(this.worldObj, this.pos, this.worldObj.getBlockState(this.pos));
			return;
		}
		if (blockdriller.getBlock() instanceof BlockCollector)
		{
			((BlockCollector)InitBlocks.collector).movieMe(this.worldObj, this.pos, this.worldObj.getBlockState(this.pos));
			return;
		}
		if (blockdriller.getBlock() instanceof BlockReinforcements)
		{
			((BlockReinforcements)InitBlocks.reinforcements).movieMe(this.worldObj, this.pos, this.worldObj.getBlockState(this.pos));
			return;
		}
		if (blockdriller.getBlock() instanceof BlockDecoration)
		{
			((BlockDecoration)InitBlocks.decoration).movieMe(this.worldObj, this.pos, this.worldObj.getBlockState(this.pos));
			return;
		}
		if (blockdriller.getBlock() instanceof BlockStorage)
		{
			((BlockStorage)InitBlocks.storage).movieMe(this.worldObj, this.pos, this.worldObj.getBlockState(this.pos));
			return;
		}
		((BlockDrillBase)InitBlocks.drillbase).movieMe(this.worldObj, this.pos, this.worldObj.getBlockState(this.pos));



	}
}