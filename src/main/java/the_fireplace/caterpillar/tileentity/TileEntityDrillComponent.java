package the_fireplace.caterpillar.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.blocks.BlockDrillBase;
import the_fireplace.caterpillar.blocks.BlockDrillHeads;
import the_fireplace.caterpillar.containers.CaterpillarData;
import the_fireplace.caterpillar.containers.ContainerDrillHead;
import the_fireplace.caterpillar.guis.GuiDrillHead;
public class TileEntityDrillComponent extends TileEntityLockable implements ITickable
{
	protected String customName;
	public boolean isSelected = false;
	public TileEntityDrillComponent()
	{
		//Reference.printDebug("No Remote World?");
	}
	private CaterpillarData MyCaterpillar()
	{
		if (this.isSelected)
		{
			return Caterpillar.instance.getSelectedCaterpillar();
		}
		return Caterpillar.instance.getContainerCaterpillar(this.pos, this.world);
	}
	private NonNullList<ItemStack> ensureValidStacksizes(NonNullList<ItemStack> nonNullList)
	{
		for (int i = 0; i < nonNullList.size(); i++) {
			ItemStack K = nonNullList.get(i);
			if (K != null)
			{
				if (K.getCount() < 1)
				{
					nonNullList.isEmpty();
				}
			}
		}
		return nonNullList;
	}
	public NonNullList<ItemStack> getItemStacks()
	{
		try {
			return  this.ensureValidStacksizes(Caterpillar.instance.getInventory(this.MyCaterpillar(), this.MyCaterpillar().tabs.selected));
		} catch (Exception e) {
			if (!Caterpillar.proxy.isServerSide()){
				Minecraft.getMinecraft().currentScreen = null;
			}
			if (!Caterpillar.proxy.isServerSide())
			{
				if (Minecraft.getMinecraft().currentScreen instanceof GuiDrillHead)
				{
					Minecraft.getMinecraft().currentScreen = null;
				}
			}
			return NonNullList.withSize(256, ItemStack.EMPTY);
		}

	}

	@Override
	public int getSizeInventory()
	{
		return this.getItemStacks().size();
	}
	private void setCustomItem(int index, ItemStack inwego)
	{
		if (index < this.getItemStacks().size())
		{
			this.getItemStacks()[index] = inwego;
		}
	}
	private ItemStack getCustomItem(int index)
	{
		if (index >= this.getItemStacks().size())
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

			if (this.getCustomItem(index).getCount() <= count)
			{
				itemstack =this.getCustomItem(index);
				this.setCustomItem(index, null);
				this.markDirty();
				return itemstack;
			}
			else
			{
				itemstack =this.getCustomItem(index).splitStack(count);

				if (this.getCustomItem(index).getCount() == 0)
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
	public ItemStack removeStackFromSlot(int index)
	{
		this.clear();
		return null;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack)
	{
		this.setCustomItem(index, stack);

		if (stack != null && stack.getCount() > this.getInventoryStackLimit())
		{
			stack.equals(this.getInventoryStackLimit());
		}
		this.markDirty();
	}

	@Override
	public String getName()
	{
		return this.hasCustomName() ? this.customName : "Caterpillar";
	}

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
			if (this.MyCaterpillar().tabs.selected.equals(Caterpillar.GuiTabs.DECORATION) || this.MyCaterpillar().tabs.selected.equals(Caterpillar.GuiTabs.REINFORCEMENT))
			{
				return 1;
			}
		}
		return 64;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player)
	{
		return this.world.getTileEntity(this.pos) == this && player.getDistanceSq(this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D) <= 64.0D;
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
		return Caterpillar.MODID  + ":" + this.blockType.getUnlocalizedName().substring(5);
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

	@Override
	public void clear()
	{
	}
	@Override
	public void update() {
		IBlockState blockdriller =  this.world.getBlockState(this.pos);

		if (blockdriller.getBlock() instanceof BlockDrillBase || blockdriller.getBlock() instanceof BlockDrillHeads)
		{
			((BlockDrillBase)blockdriller.getBlock()).calculateMovement(this.world, this.pos, this.world.getBlockState(this.pos));
		}
	}
	@Override
	public boolean isEmpty() {
		return false;
	}
}