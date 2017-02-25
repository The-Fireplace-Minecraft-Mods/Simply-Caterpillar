package the_fireplace.caterpillar.containers;

import com.google.common.collect.Lists;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.ArrayUtils;
import the_fireplace.caterpillar.Caterpillar.GuiTabs;
import the_fireplace.caterpillar.Reference;
import the_fireplace.caterpillar.parts.*;

import java.util.LinkedList;

public class CaterpillarData implements Cloneable{

	public LinkedList<ItemStack[]> inventoryPages;
	public ItemStack fuelSlotStack;
	public int pageIndex;
	public int burntime;
	public BlockPos pos;
	public int maxburntime;
	public String name;
	public int headTick;
	public PartsMovement movement;
	public PartsStorage storage;
	public PartsDecoration decoration;
	public PartsReinforcement reinforcement;
	public PartsIncinerator incinerator;
	public PartsTabs tabs;
	public boolean running = true;
	public ContainerDrillHead myDrillHead;
	public CaterpillarData(BlockPos drillhead, String key)
	{
		this.inventoryPages = Lists.newLinkedList();
		this.inventoryPages.add(new ItemStack[CaterpillarData.getInventoryPageSize()]);
		pageIndex=0;
		this.burntime = 0;
		this.name = key;
		this.headTick = 0;
		this.movement = new PartsMovement();
		this.storage = new PartsStorage();
		this.decoration = new PartsDecoration();
		this.reinforcement = new PartsReinforcement();
		this.tabs = new PartsTabs();
		this.incinerator = new PartsIncinerator();

		this.updatePos(drillhead);
	}

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
		for (int i = 0; i < CaterpillarData.getInventoryPageSize(); ++i)
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
		for (int i = 8; i < CaterpillarData.getInventoryPageSize(); ++i)
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
		for (int i = 12; i < CaterpillarData.getInventoryPageSize(); ++i)
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
	@Override
	public CaterpillarData clone()
	{
		String key = this.name;
		BlockPos posP = new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ());
		CaterpillarData newCatp = new CaterpillarData(posP, key);
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
	 * 	The amount to change by
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
	 * Recreate CaterpillarData from NBT
	 * @param catNbt
	 * 	The NBT Data to turn back in to a Caterpillar
	 * @return
	 * 	The new Caterpillar Data
	 */
	public static CaterpillarData readCaterpiller(NBTTagCompound catNbt)
	{
		String key = catNbt.getString("name");
		BlockPos posP = new BlockPos(catNbt.getInteger("X"), catNbt.getInteger("Y"), catNbt.getInteger("Z"));
		CaterpillarData newCatp = new CaterpillarData(posP, key);
		newCatp.tabs.selected = GuiTabs.values()[catNbt.getByte("selectedtab")];
		newCatp.decoration.selected = catNbt.getByte("decorationsselected");
		newCatp.maxburntime = catNbt.getInteger("burntimemax");
		newCatp.storage.storageComponentCount = catNbt.getByte("addedStorage");
		newCatp.burntime = catNbt.getInteger("burntime");
		newCatp.running = catNbt.getBoolean("running");
		newCatp.fuelSlotStack = ItemStack.loadItemStackFromNBT(catNbt.getCompoundTag("fuelStack"));
		ItemStack[] stacks = Reference.MainNBT.readItemStacks(catNbt);
		newCatp.inventoryPages = new LinkedList<>();
		while(stacks.length > 0){
			newCatp.inventoryPages.add(ArrayUtils.subarray(stacks, 0, getInventoryPageSize()));
			stacks = ArrayUtils.subarray(stacks, getInventoryPageSize(), stacks.length);
		}

		if (newCatp.inventoryPages.size() < newCatp.storage.storageComponentCount+1){
			Reference.printDebug("Inventory count too low, adding "+(newCatp.storage.storageComponentCount+1-newCatp.inventoryPages.size())+" pages.");
			for(int i=0;i<newCatp.storage.storageComponentCount+1-newCatp.inventoryPages.size();i++){
				newCatp.inventoryPages.add(new ItemStack[getInventoryPageSize()]);
			}
		}

		if (catNbt.hasKey("incinerator"))
		{
			newCatp.incinerator.readNBT(catNbt.getCompoundTag("incinerator"));
		}
		if (catNbt.hasKey("decoration"))
		{
			newCatp.decoration.readNBT(catNbt.getCompoundTag("decoration"));
		}
		if (catNbt.hasKey("movementTicks")){
			newCatp.movement.readNBT(catNbt.getCompoundTag("movementTicks"));
		}
		if (catNbt.hasKey("reinforcement"))
		{
			newCatp.reinforcement.readNBT(catNbt.getCompoundTag("reinforcement"));
		}
		return newCatp;
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
		NBTTagCompound NBTconCat = Reference.MainNBT.writeItemStacks(totalInv);
		if(this.fuelSlotStack != null)
			NBTconCat.setTag("fuelStack", this.fuelSlotStack.writeToNBT(new NBTTagCompound()));
		NBTconCat.setTag("decoration", this.decoration.saveNBT());
		NBTconCat.setTag("reinforcement", this.reinforcement.saveNBT());
		NBTconCat.setTag("incinerator", this.incinerator.saveNBT());
		NBTconCat.setTag("movementTicks", this.movement.saveNBT());
		NBTconCat.setString("name", this.name);
		NBTconCat.setByte("selectedtab", this.tabs.selected.value);
		NBTconCat.setByte("decorationsselected", this.decoration.selected);
		NBTconCat.setInteger("burntime", this.burntime);
		NBTconCat.setByte("addedStorage", this.storage.storageComponentCount);
		NBTconCat.setInteger("burntimemax", this.maxburntime);
		NBTconCat.setBoolean("running", this.running);
		NBTconCat.setInteger("X", this.pos.getX());
		NBTconCat.setInteger("Y", this.pos.getY());
		NBTconCat.setInteger("Z", this.pos.getZ());
		return NBTconCat;
	}
}
