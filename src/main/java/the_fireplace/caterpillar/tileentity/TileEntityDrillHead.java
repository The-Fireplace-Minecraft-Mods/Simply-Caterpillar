package the_fireplace.caterpillar.tileentity;

import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.ArrayUtils;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.Reference;
import the_fireplace.caterpillar.blocks.BlockDrillBase;
import the_fireplace.caterpillar.blocks.BlockDrillHeads;
import the_fireplace.caterpillar.containers.ContainerDrillHead;
import the_fireplace.caterpillar.parts.*;

import javax.annotation.Nonnull;
import java.util.LinkedList;

public class TileEntityDrillHead extends TileEntityLockable implements ITickable
{
	protected String customName;
	public boolean isSelected = false;

	public LinkedList<ItemStack[]> inventoryPages;
	public ItemStack fuelSlotStack;
	public int pageIndex;
	public int burntime;
	public int maxburntime;
	public int headTick;
	public PartsMovement movement;
	public PartsStorage storage;
	public PartsDecoration decoration;
	public PartsReinforcement reinforcement;
	public PartsIncinerator incinerator;
	public PartsTabs tabs;
	public boolean running = true;
	public ContainerDrillHead myDrillHead;

	public TileEntityDrillHead()
	{
		Reference.printDebug("Initializing Caterpillar");
		this.inventoryPages = Lists.newLinkedList();
		this.inventoryPages.add(new ItemStack[getInventoryPageSize()]);
		pageIndex=0;
		this.burntime = 0;
		this.headTick = 0;
		this.movement = new PartsMovement();
		this.storage = new PartsStorage();
		this.decoration = new PartsDecoration();
		this.reinforcement = new PartsReinforcement();
		this.tabs = new PartsTabs();
		this.incinerator = new PartsIncinerator();
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
			return this.ensureValidStacksizes(Caterpillar.instance.getInventory(this, this.tabs.selected));
		} catch (Exception e) {
			Reference.printDebug(e.getLocalizedMessage());
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
		if (this.tabs.selected.equals(Caterpillar.GuiTabs.DECORATION) || this.tabs.selected.equals(Caterpillar.GuiTabs.REINFORCEMENT))
		{
			return 1;
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
		return new ContainerDrillHead(playerIn, this);
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

	/*
	********BEGIN CATERPILLAR DATA***********
	 */
	/**
	 * Get the inventory that is currently loaded
	 * @return
	 * 	The current inventory
	 */
	public ItemStack[] getCurrentInventory(){
		return inventoryPages.get(pageIndex);
	}

	/**
	 * Gets the size of 1 inventory page
	 * @return
	 * 	The size of a page
	 */
	public static int getInventoryPageSize()
	{
		return 24;
	}
	public void setSlotPos(Slot thisSlot, int xpos, int ypos)
	{
		thisSlot.xPos =  xpos;
		thisSlot.yPos =  ypos;
	}

	/**
	 * Resets slot location
	 * @param myDrillHeads
	 * 	The container to reset the slots of
	 */
	public void resetSlots(Container myDrillHeads)
	{
		for (int i = 0; i < getInventoryPageSize(); ++i)
		{
			Slot addingSlot = myDrillHeads.getSlot(i);
			//addingSlot.putStack(null);
			this.setSlotPos(addingSlot, 12, -1000);
		}
	}
	public void placeSlotsforReinforcements(Container myDrillHeads)
	{
		this.resetSlots(myDrillHeads);
		int ID = 0;

		//top
		Slot AddingSlot = myDrillHeads.getSlot(ID);
		AddingSlot.putStack(this.reinforcement.reinforcementMap[ID]);
		this.setSlotPos(AddingSlot, 8 + (3 + -1) * 18, -1 + (1 + -2) * 18);
		ID++;
		AddingSlot = myDrillHeads.getSlot(ID);
		AddingSlot.putStack(this.reinforcement.reinforcementMap[ID]);
		this.setSlotPos(AddingSlot, 8 + (3) * 18, -1 + (1 + -2) * 18);
		ID++;
		AddingSlot = myDrillHeads.getSlot(ID);
		AddingSlot.putStack(this.reinforcement.reinforcementMap[ID]);
		this.setSlotPos(AddingSlot, 8 + (3 + 1) * 18, -1 + (1 + -2) * 18);
		ID++;
		AddingSlot = myDrillHeads.getSlot(ID);
		AddingSlot.putStack(this.reinforcement.reinforcementMap[ID]);
		this.setSlotPos(AddingSlot, 8 + (3 + 2) * 18, -1 + (1 + -2) * 18);
		ID++;


		//left
		AddingSlot = myDrillHeads.getSlot(ID);
		AddingSlot.putStack(this.reinforcement.reinforcementMap[ID]);
		this.setSlotPos(AddingSlot, 8 + (3 + -1) * 18, -1);
		ID++;
		AddingSlot = myDrillHeads.getSlot(ID);
		AddingSlot.putStack(this.reinforcement.reinforcementMap[ID]);
		this.setSlotPos(AddingSlot, 8 + (3 + -1) * 18, -1 + 18);
		ID++;
		AddingSlot = myDrillHeads.getSlot(ID);
		AddingSlot.putStack(this.reinforcement.reinforcementMap[ID]);
		this.setSlotPos(AddingSlot, 8 + (3 + -1) * 18, -1 + (1 + 1) * 18);
		ID++;
		AddingSlot = myDrillHeads.getSlot(ID);
		AddingSlot.putStack(this.reinforcement.reinforcementMap[ID]);
		this.setSlotPos(AddingSlot, 8 + (3 + -1) * 18, -1 + (1 + 2) * 18);
		ID++;

		//right
		AddingSlot = myDrillHeads.getSlot(ID);
		AddingSlot.putStack(this.reinforcement.reinforcementMap[ID]);
		this.setSlotPos(AddingSlot, 8 + (3 + 3) * 18, -1 + (1 +-2) * 18);
		ID++;
		AddingSlot = myDrillHeads.getSlot(ID);
		AddingSlot.putStack(this.reinforcement.reinforcementMap[ID]);
		this.setSlotPos(AddingSlot, 8 + (3 + 3) * 18, -1);
		ID++;
		AddingSlot = myDrillHeads.getSlot(ID);
		AddingSlot.putStack(this.reinforcement.reinforcementMap[ID]);
		this.setSlotPos(AddingSlot, 8 + (3 + 3) * 18, -1 + 18);
		ID++;
		AddingSlot = myDrillHeads.getSlot(ID);
		AddingSlot.putStack(this.reinforcement.reinforcementMap[ID]);
		this.setSlotPos(AddingSlot, 8 + (3 + 3) * 18, -1 + (1 + 1) * 18);
		ID++;

		//lower
		AddingSlot = myDrillHeads.getSlot(ID);
		AddingSlot.putStack(this.reinforcement.reinforcementMap[ID]);
		this.setSlotPos(AddingSlot, 8 + (3) * 18, -1 + (1 + 2) * 18);
		ID++;
		AddingSlot = myDrillHeads.getSlot(ID);
		AddingSlot.putStack(this.reinforcement.reinforcementMap[ID]);
		this.setSlotPos(AddingSlot, 8 + (3 + 1) * 18, -1 + (1 + 2) * 18);
		ID++;
		AddingSlot = myDrillHeads.getSlot(ID);
		AddingSlot.putStack(this.reinforcement.reinforcementMap[ID]);
		this.setSlotPos(AddingSlot, 8 + (3 + 2) * 18, -1 + (1 + 2) * 18);
		ID++;
		AddingSlot = myDrillHeads.getSlot(ID);
		AddingSlot.putStack(this.reinforcement.reinforcementMap[ID]);
		this.setSlotPos(AddingSlot, 8 + (3 + 3) * 18, -1 + (1 + 2) * 18);
		//ID++;
	}
	public void placeSlotsforDecorations(Container myDrillHeads)
	{
		this.resetSlots(myDrillHeads);
		//3X3 Grid
		int ID = 0;
		for (int i = 0; i < 3; ++i)
		{
			for (int j = 0; j < 3; ++j)
			{
				if (i != 1 || j != 1)
				{
					Slot AddingSlot = myDrillHeads.getSlot(ID);
					AddingSlot.putStack(this.decoration.getSelectedInventory()[ID]);
					this.setSlotPos(AddingSlot, 8 + (3 + i) * 18, 10 + (1 + j) * 18);
					ID++;
				}
			}

		}
		//Everything else
		for (int i = 8; i < getInventoryPageSize(); ++i)
		{
			Slot AddingSlot = myDrillHeads.getSlot(i);
			this.setSlotPos(AddingSlot, -100, -100);
		}

	}
	public void placeSlotsforIncinerator(Container myDrillHeads)
	{
		this.resetSlots(myDrillHeads);
		int ID = 0;
		for (int i = 0; i < 3; ++i)
		{
			for (int j = 0; j < 4; ++j)
			{
				Slot AddingSlot = myDrillHeads.getSlot(ID);
				AddingSlot.putStack(this.incinerator.placementMap[ID]);
				this.setSlotPos(AddingSlot, 62 + (i) * 18, 7 + (j) * 18);
				ID++;
			}

		}

		//Everything else
		for (int i = 12; i < getInventoryPageSize(); ++i)
		{
			Slot AddingSlot = myDrillHeads.getSlot(i);
			this.setSlotPos(AddingSlot, -100, -100);
		}


	}
	public void placeSlotsforMain(Container myDrillHeads)
	{
		this.resetSlots(myDrillHeads);
		int i;
		int j;
		int ID = 0;

		//Burner
		Slot addingSlot = myDrillHeads.getSlot(ID);
		addingSlot.putStack(fuelSlotStack);
		this.setSlotPos(myDrillHeads.getSlot(ID), 8 + (4) * 18, 7 + (3) * 18);
		ID++;

		//Left Side
		for (i = 0; i < 4; ++i)
		{
			for (j = 0; j < 3; ++j)
			{
				addingSlot = myDrillHeads.getSlot(ID);
				addingSlot.putStack(this.getCurrentInventory()[ID]);
				this.setSlotPos(myDrillHeads.getSlot(ID), 8 + j * 18, -100);
				ID++;
			}
		}

		//Right Side
		for (i = 0; i < 4; ++i)
		{
			for (j = 0; j < 3; ++j)
			{
				addingSlot = myDrillHeads.getSlot(ID);
				addingSlot.putStack(this.getCurrentInventory()[ID]);
				this.setSlotPos(myDrillHeads.getSlot(ID), 8 + (j + 6) * 18, -100);
				ID++;
			}
		}
	}

	/**
	 * Updates the BlockPos saved in the Caterpillar Data
	 * @param drillhead
	 * 	The BlockPos that the drillhead is at
	 */
	public void updatePos(BlockPos drillhead)
	{
		this.pos = new BlockPos(drillhead.getX(), drillhead.getY(), drillhead.getZ()) ;
	}

	/**
	 * Puts an ItemStack in the right hand side of the Caterpillar inventory
	 * @param toAdd
	 * 	The itemstack to add to the inventory
	 * @return
	 * 	True if the stack was successfully storageComponentCount, otherwise false
	 */
	public boolean addToRightHandSlots(ItemStack toAdd)
	{
		for (int i = 12; i < this.getCurrentInventory().length; i++) {
			ItemStack slot = this.getCurrentInventory()[i];
			if (slot != null)
			{
				if (slot.stackSize + toAdd.stackSize < 65)
				{
					if (slot.getItem().equals(toAdd.getItem()) && slot.getItemDamage() == toAdd.getItemDamage())
					{
						slot.stackSize = slot.stackSize +  toAdd.stackSize;
						return true;
					}
				}
			}
		}
		for (int i = 12; i < this.getCurrentInventory().length; i++) {
			if (this.getCurrentInventory()[i] == null)
			{
				this.getCurrentInventory()[i] = new ItemStack(toAdd.getItem(), toAdd.stackSize, toAdd.getItemDamage());
				return true;
			}
		}

		return false;

	}
	//TODO: Is clone needed?
	@Override
	public TileEntityDrillHead clone()
	{
		TileEntityDrillHead newCatp = new TileEntityDrillHead();
		newCatp.inventoryPages = (LinkedList<ItemStack[]>)this.inventoryPages.clone();
		newCatp.fuelSlotStack = fuelSlotStack.copy();
		newCatp.maxburntime = this.maxburntime;
		newCatp.storage.storageComponentCount = this.storage.storageComponentCount;
		newCatp.burntime = this.burntime;
		newCatp.decoration = this.decoration.clone();
		newCatp.reinforcement.reinforcementMap = this.reinforcement.reinforcementMap.clone();

		return newCatp;
	}

	/**
	 * Adjusts the number of storage components in the caterpillar.
	 * @param changeBy
	 * 	The pageIndex to change by
	 * @param worldIn
	 * 	The world the caterpillar is in
	 */
	public void changeStorage(int changeBy, World worldIn) {
		if (this.storage.storageComponentCount + changeBy < 0)
		{
			Reference.printDebug("Error: Attempted to reduce Caterpillar inventories to less than 1.");
			return;
		}

		if (changeBy < 0)
		{
			for (int i = 0; i < this.inventoryPages.getLast().length; i++) {
				if (this.inventoryPages.getLast()[i] != null)
					Reference.dropItem(worldIn, this.pos, this.inventoryPages.getLast()[i]);
			}
			inventoryPages.removeLast();
		}
		else if (changeBy > 0)
		{
			inventoryPages.add(new ItemStack[getInventoryPageSize()]);
		}
		this.storage.storageComponentCount += changeBy;
	}

	/**
	 * Read Cat data from NBT
	 * @param catNbt
	 * 	The NBT Data to read on to this TE
	 */
	public void readCaterpiller(NBTTagCompound catNbt)
	{
		tabs.selected = Caterpillar.GuiTabs.values()[catNbt.getByte("selectedtab")];
		decoration.selected = catNbt.getByte("decorationsselected");
		maxburntime = catNbt.getInteger("burntimemax");
		storage.storageComponentCount = catNbt.getByte("addedStorage");
		burntime = catNbt.getInteger("burntime");
		running = catNbt.getBoolean("running");
		fuelSlotStack = ItemStack.loadItemStackFromNBT(catNbt.getCompoundTag("fuelStack"));
		ItemStack[] stacks = Reference.MainNBT.readItemStacks(catNbt);
		inventoryPages = new LinkedList<>();
		if(stacks == null)
			stacks = new ItemStack[getInventoryPageSize()];
		while(stacks.length > 0){
			inventoryPages.add(ArrayUtils.subarray(stacks, 0, getInventoryPageSize()));
			stacks = ArrayUtils.subarray(stacks, getInventoryPageSize(), stacks.length);
		}

		if (inventoryPages.size() < storage.storageComponentCount+1){
			Reference.printDebug("Inventory count too low, adding "+(storage.storageComponentCount+1-inventoryPages.size())+" pages.");
			for(int i=0;i<storage.storageComponentCount+1-inventoryPages.size();i++){
				inventoryPages.add(new ItemStack[getInventoryPageSize()]);
			}
		}

		if (catNbt.hasKey("incinerator"))
		{
			incinerator.readNBT(catNbt.getCompoundTag("incinerator"));
		}
		if (catNbt.hasKey("decoration"))
		{
			decoration.readNBT(catNbt.getCompoundTag("decoration"));
		}
		if (catNbt.hasKey("movementTicks")){
			movement.readNBT(catNbt.getCompoundTag("movementTicks"));
		}
		if (catNbt.hasKey("reinforcement"))
		{
			reinforcement.readNBT(catNbt.getCompoundTag("reinforcement"));
		}
	}

	/**
	 * Turn the Caterpillar in to NBT Data.
	 * @return
	 * 	The NBT Form of the Caterpillar
	 */
	public NBTTagCompound writeNBTCaterpillar() {
		ItemStack[] totalInv = new ItemStack[0];
		for(ItemStack[] inv:inventoryPages)
			totalInv = ArrayUtils.addAll(totalInv, inv);
		NBTTagCompound catData = Reference.MainNBT.writeItemStacks(totalInv);
		if(this.fuelSlotStack != null)
			catData.setTag("fuelStack", this.fuelSlotStack.writeToNBT(new NBTTagCompound()));
		catData.setTag("decoration", this.decoration.saveNBT());
		catData.setTag("reinforcement", this.reinforcement.saveNBT());
		catData.setTag("incinerator", this.incinerator.saveNBT());
		catData.setTag("movementTicks", this.movement.saveNBT());
		catData.setByte("selectedtab", this.tabs.selected.value);
		catData.setByte("decorationsselected", this.decoration.selected);
		catData.setInteger("burntime", this.burntime);
		catData.setByte("addedStorage", this.storage.storageComponentCount);
		catData.setInteger("burntimemax", this.maxburntime);
		catData.setBoolean("running", this.running);
		catData.setInteger("X", this.pos.getX());
		catData.setInteger("Y", this.pos.getY());
		catData.setInteger("Z", this.pos.getZ());
		return catData;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		readCaterpiller(compound.getCompoundTag("catData"));
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);

		compound.setTag("catData", writeNBTCaterpillar());

		return compound;
	}
}