package the_fireplace.caterpillar.containers;

import the_fireplace.caterpillar.Caterpillar.GuiTabs;
import the_fireplace.caterpillar.Reference;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import the_fireplace.caterpillar.parts.*;

public class CaterpillarData implements Cloneable{

	public ItemStack[] inventory;
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
		this.inventory = new ItemStack[CaterpillarData.getMaxSize()];
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
	public static int getMaxSize()
	{
		return 25;
	}
	public void updateScroll(Container myDrillHeads) {
		int i;
		int column = 0;
		int row = 0;
		for (i = this.storage.startingIndex; i < getMaxSize() + this.storage.added; ++i)
		{
			Slot addingSlot = myDrillHeads.getSlot(i);
			addingSlot.yPos = -1000;
		}
		for (i = this.storage.startingIndex; i < this.storage.startingIndex + 12; ++i)
		{
			Slot AddingSlot = myDrillHeads.getSlot(i);
			AddingSlot.yPos = 7 + row * 18;
			column++;
			if (column > 2)
			{
				row++;
				column = 0;
			}
		}
		column = 0;
		row = 0;
		int middle = (getMaxSize() + this.storage.added - this.storage.startingIndex) / 2;
		Reference.printDebug( this.storage.added + "," + middle);
		for (i = this.storage.startingIndex + middle; i < this.storage.startingIndex + middle + 12; ++i)
		{

			Slot addingSlot = myDrillHeads.getSlot(i);
			addingSlot.yPos = 7 + row * 18;
			column++;
			if (column > 2)
			{
				row++;
				column = 0;
			}
		}
	}
	public void setSlotPos(Slot thisSlot, int xpos, int ypos)
	{
		thisSlot.xPos =  xpos;
		thisSlot.yPos =  ypos;
	}
	public void resetSlots(Container myDrillHeads)
	{
		this.resetSlots(myDrillHeads, true);
	}
	public void resetSlots(Container myDrillHeads, boolean changepos)
	{
		for (int i = 0; i < CaterpillarData.getMaxSize() + this.storage.added; ++i)
		{
			Slot addingSlot = myDrillHeads.getSlot(i);
			//addingSlot.putStack(null);
			if (changepos)
			{
				this.setSlotPos(addingSlot, 12, -1000);
			}
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
		for (int i = 8; i < CaterpillarData.getMaxSize() + this.storage.added; ++i)
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
		for (int i = 12; i < CaterpillarData.getMaxSize() + this.storage.added; ++i)
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
		addingSlot.putStack(this.inventory[ID]);
		this.setSlotPos(myDrillHeads.getSlot(ID), 8 + (4) * 18, 7 + (3) * 18);
		ID++;

		//Left Side
		int idMiddle = (CaterpillarData.getMaxSize() + this.storage.added - this.storage.startingIndex )/2;
		for (i = 0; i < (idMiddle/3); ++i)
		{
			for (j = 0; j < 3; ++j)
			{
				addingSlot = myDrillHeads.getSlot(ID);
				addingSlot.putStack(this.inventory[ID]);
				this.setSlotPos(myDrillHeads.getSlot(ID), 8 + j * 18, -100);
				ID++;
			}
		}

		//Right Side
		for (i = 0; i < (idMiddle/3); ++i)
		{
			for (j = 0; j < 3; ++j)
			{
				addingSlot = myDrillHeads.getSlot(ID);
				addingSlot.putStack(this.inventory[ID]);
				this.setSlotPos(myDrillHeads.getSlot(ID), 8 + (j + 6) * 18, -100);
				ID++;
			}
		}

		this.updateScroll(myDrillHeads);
	}
	public void updatePos(BlockPos drillhead)
	{
		this.pos = new BlockPos(drillhead.getX(), drillhead.getY(), drillhead.getZ()) ;
	}
	public boolean addToOutInventory(ItemStack toAdd)
	{
		int Middleindex = (this.inventory.length - this.storage.startingIndex) / 2;
		Middleindex += this.storage.startingIndex;

		for (int i = Middleindex; i < this.inventory.length; i++) {
			ItemStack slot = this.inventory[i];
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
		for (int i = Middleindex; i < this.inventory.length; i++) {
			if (this.inventory[i] == null)
			{
				this.inventory[i] = new ItemStack(toAdd.getItem(), toAdd.stackSize, toAdd.getItemDamage());
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
		newCatp.inventory = this.inventory.clone();
		newCatp.maxburntime = this.maxburntime;
		newCatp.storage.added = this.storage.added;
		newCatp.burntime = this.burntime;
		newCatp.decoration = this.decoration.clone();
		newCatp.reinforcement.reinforcementMap = this.reinforcement.reinforcementMap.clone();

		return newCatp;
	}
	public void changeStorage( int Change, World worldIn) {
		if (this.storage.added + Change < 0)
		{
			return;
		}
		ItemStack[] tmpIT = new ItemStack[CaterpillarData.getMaxSize() + this.storage.added + Change];

		int MiddleOldStorage = (this.inventory.length - this.storage.startingIndex) / 2;
		int MiddleNewStorage = (tmpIT.length - this.storage.startingIndex) / 2;
		int offset = this.storage.startingIndex;
		if (MiddleNewStorage > MiddleOldStorage)// if new inv is bigger than the old one, short it
		{
			ItemStack[] tmpITRight = new ItemStack[MiddleNewStorage];
			for (int i = 0; i < this.inventory.length; i++) {
				if (i < MiddleOldStorage + offset)
				{
					tmpIT[i] = this.inventory[i];
				}
				else
				{
					tmpITRight[i - MiddleOldStorage + offset] = this.inventory[i];
				}
			}
			System.arraycopy(tmpITRight, 0, tmpIT, MiddleNewStorage - offset, tmpITRight.length);

		}
		else if (MiddleNewStorage < MiddleOldStorage)
		{
			int newchange = Math.abs(Change);
			newchange /= 2;
			for (int i = 0; i < this.inventory.length; i++) {
				if (i < MiddleNewStorage + offset )
				{
					tmpIT[i] = this.inventory[i];
				}else if (i >= MiddleNewStorage + offset && i < MiddleOldStorage + offset)
				{
					if (this.inventory[i] != null)
					{
						Reference.dropItem(worldIn, this.pos, this.inventory[i]);
					}
				}else if (i >= MiddleOldStorage + offset && i < MiddleOldStorage + MiddleNewStorage + offset )
				{

					tmpIT[i - newchange ] = this.inventory[i];
				}
				else
				{
					if (this.inventory[i] != null)
					{
						Reference.dropItem(worldIn, this.pos, this.inventory[i]);
					}
				}
			}
		}
		else
		{
			tmpIT = this.inventory.clone();
		}
		this.storage.startingIndex = 1;
		this.inventory = tmpIT.clone();
		this.storage.added = this.storage.added + Change;
	}
	public static CaterpillarData readCaterpiller(NBTTagCompound NBTconCat)
	{
		String key = NBTconCat.getString("name");
		BlockPos posP = new BlockPos(NBTconCat.getInteger("X"), NBTconCat.getInteger("Y"), NBTconCat.getInteger("Z"));
		CaterpillarData newCatp = new CaterpillarData(posP, key);
		newCatp.tabs.selected = GuiTabs.values()[NBTconCat.getInteger("selectedtab")];
		newCatp.decoration.selected = NBTconCat.getInteger("decorationsselected");
		newCatp.maxburntime = NBTconCat.getInteger("burntimemax");
		newCatp.storage.added = NBTconCat.getInteger("addedStorage");
		newCatp.burntime = NBTconCat.getInteger("burntime");
		newCatp.running = NBTconCat.getBoolean("running");
		newCatp.inventory = Reference.MainNBT.readItemStacks(NBTconCat);

		if (newCatp.inventory.length < getMaxSize() + newCatp.storage.added || newCatp.inventory.length > getMaxSize() + newCatp.storage.added){
			ItemStack[] tmpY = new ItemStack[getMaxSize() + newCatp.storage.added];

			Reference.printDebug("Inventory length was wrong had to resize: " + newCatp.inventory.length + "," + tmpY.length);
			int length = newCatp.inventory.length;
			if (tmpY.length < length)
			{
				length = tmpY.length;
			}
			System.arraycopy(newCatp.inventory, 0, tmpY, 0, length);
			newCatp.inventory = tmpY.clone();

		}

		if (NBTconCat.hasKey("incinerator"))
		{
			newCatp.incinerator.readNBT(NBTconCat.getCompoundTag("incinerator"));
		}
		if (NBTconCat.hasKey("decoration"))
		{
			newCatp.decoration.readNBT(NBTconCat.getCompoundTag("decoration"));
		}
		if (NBTconCat.hasKey("movementTicks")){
			newCatp.movement.readNBT(NBTconCat.getCompoundTag("movementTicks"));
		}
		if (NBTconCat.hasKey("reinforcement"))
		{
			newCatp.reinforcement.readNBT(NBTconCat.getCompoundTag("reinforcement"));
		}
		return newCatp;
	}
	public NBTTagCompound writeNBTCaterpillar() {
		NBTTagCompound NBTconCat = Reference.MainNBT.writeItemStacks(this.inventory);
		NBTconCat.setTag("decoration", this.decoration.saveNBT());
		NBTconCat.setTag("reinforcement", this.reinforcement.saveNBT());
		NBTconCat.setTag("incinerator", this.incinerator.saveNBT());
		NBTconCat.setTag("movementTicks", this.movement.saveNBT());
		NBTconCat.setString("name", this.name);
		NBTconCat.setInteger("selectedtab", this.tabs.selected.value);
		NBTconCat.setInteger("decorationsselected", this.decoration.selected);
		NBTconCat.setInteger("burntime", this.burntime);
		NBTconCat.setInteger("addedStorage", this.storage.added);
		NBTconCat.setInteger("burntimemax", this.maxburntime);
		NBTconCat.setBoolean("running", this.running);
		NBTconCat.setInteger("X", this.pos.getX());
		NBTconCat.setInteger("Y", this.pos.getY());
		NBTconCat.setInteger("Z", this.pos.getZ());
		return NBTconCat;
	}
}
