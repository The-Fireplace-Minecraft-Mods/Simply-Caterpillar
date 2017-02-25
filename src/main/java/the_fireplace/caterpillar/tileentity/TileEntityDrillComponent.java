package the_fireplace.caterpillar.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.ITickable;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.blocks.BlockDrillBase;
import the_fireplace.caterpillar.blocks.BlockDrillHeads;
import the_fireplace.caterpillar.containers.CaterpillarData;
import the_fireplace.caterpillar.containers.ContainerDrillHead;

import javax.annotation.Nonnull;

public class TileEntityDrillComponent extends TileEntityLockable implements ITickable
{
	protected String customName;
	public boolean isSelected = false;
	public TileEntityDrillComponent()
	{
		//Reference.printDebug("No Remote World?");
	}
	private CaterpillarData getCaterpillarData()
	{
		if (this.isSelected)
		{
			return Caterpillar.instance.getSelectedCaterpillar();
		}
		return Caterpillar.instance.getContainerCaterpillar(this.pos, this.world);
	}
	private ItemStack[] ensureValidStacksizes(ItemStack[] toFix)
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
			return this.ensureValidStacksizes(Caterpillar.instance.getInventory(this.getCaterpillarData(), this.getCaterpillarData().tabs.selected));
		} catch (Exception e) {
			Caterpillar.proxy.closeDrillGui();
			return null;
		}
	}

	@Override
	public int getSizeInventory()
	{
		return this.getItemStacks().length;
	}
	private void setCustomItem(int index, ItemStack itemStack)
	{
		if (index < this.getItemStacks().length)
		{
			this.getItemStacks()[index] = itemStack;
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
	public ItemStack removeStackFromSlot(int index)
	{
		this.clear();
		return null;
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

	@Override
	@Nonnull
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
		if (this.getCaterpillarData() != null)
		{
			if (this.getCaterpillarData().tabs.selected.equals(Caterpillar.GuiTabs.DECORATION) || this.getCaterpillarData().tabs.selected.equals(Caterpillar.GuiTabs.REINFORCEMENT))
			{
				return 1;
			}
		}
		return 64;
	}

	@Override
	public boolean isUsableByPlayer(@Nonnull EntityPlayer player)
	{
		return this.world.getTileEntity(this.pos) == this && player.getDistanceSq(this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory(@Nonnull EntityPlayer player) {}

	@Override
	public void closeInventory(@Nonnull EntityPlayer player) {}

	@Override
	public boolean isItemValidForSlot(int index, @Nonnull ItemStack stack)
	{
		return true;
	}

	@Override
	@Nonnull
	public String getGuiID()
	{
		return Caterpillar.MODID  + ":" + this.blockType.getUnlocalizedName().substring(5);
	}

	@Override
	@Nonnull
	public Container createContainer(@Nonnull InventoryPlayer playerInventory, @Nonnull EntityPlayer playerIn)
	{
		return new ContainerDrillHead(playerIn, this, this.getCaterpillarData());
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
}