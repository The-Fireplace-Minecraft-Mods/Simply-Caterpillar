package com.gmail.cgfreethemice.caterpillar.containers;

import com.gmail.cgfreethemice.caterpillar.Caterpillar.GuiTabs;
import com.gmail.cgfreethemice.caterpillar.Reference;
import com.gmail.cgfreethemice.caterpillar.parts.PartsDecoration;
import com.gmail.cgfreethemice.caterpillar.parts.PartsDrag;
import com.gmail.cgfreethemice.caterpillar.parts.PartsReinforcement;
import com.gmail.cgfreethemice.caterpillar.parts.PartsStorage;
import com.gmail.cgfreethemice.caterpillar.parts.PartsTabs;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;

public class ContainerCaterpillar {

	public ItemStack[] inventory;
	public int burntime;
	public BlockPos pos;
	public int maxburntime;
	public String name;
	public int headTick;
	public PartsDrag drag;
	public PartsStorage storage;
	public PartsDecoration decoration;
	public PartsReinforcement reinforcement;
	public PartsTabs tabs;
	public boolean running = true;
	public ContainerDrillHead myDrillHead;
	public ContainerCaterpillar(BlockPos drillhead, String key)
	{
		this.inventory = new ItemStack[ContainerCaterpillar.getMaxSize()];
		this.burntime = 0;
		this.name = key;
		this.headTick = 0;
		this.drag = new PartsDrag();
		this.storage = new PartsStorage();
		this.decoration = new PartsDecoration();
		this.reinforcement = new PartsReinforcement();
		this.tabs = new PartsTabs();

		this.updatePos(drillhead);
	}
	public static int getMaxSize()
	{
		return 25;
	}
	public void updateScroll(Container myDrillHeads ) {
		int i;
		int j;
		int k;
		j = 0;
		k = 0;
		for (i = 1; i < this.getMaxSize() + this.storage.added; ++i)
		{
			Slot AddingSlot = myDrillHeads.getSlot(i);
			AddingSlot.yDisplayPosition = -1000;
		}
		for (i = this.storage.startingIndex; i < this.storage.startingIndex + 12; ++i)
		{

			Slot AddingSlot = myDrillHeads.getSlot(i);
			AddingSlot.yDisplayPosition = 7 + k * 18;
			j++;
			if (j > 2)
			{
				k++;
				j = 0;
			}
		}
		j = 0;
		k = 0;
		int Middle = (this.getMaxSize() + this.storage.added - 1) / 2;
		//Reference.printDebug( this.addedStorage + "," + Middle);
		for (i = this.storage.startingIndex + Middle; i < this.storage.startingIndex + Middle + 12; ++i)
		{

			Slot AddingSlot = myDrillHeads.getSlot(i);
			AddingSlot.yDisplayPosition = 7 + k * 18;
			j++;
			if (j > 2)
			{
				k++;
				j = 0;
			}
		}
	}
	public void setSlotPos(Slot thisSlot, int xpos, int ypos)
	{
		thisSlot.xDisplayPosition =  xpos;
		thisSlot.yDisplayPosition =  ypos;
	}
	public void resetSlots(Container myDrillHeads)
	{
		resetSlots(myDrillHeads, true);
	}
	public void resetSlots(Container myDrillHeads, boolean changepos)
	{
		for (int i = 0; i < ContainerCaterpillar.getMaxSize() + this.storage.added; ++i)
		{
			Slot AddingSlot = myDrillHeads.getSlot(i);
			//AddingSlot.putStack(null);
			if (changepos)
			{
				this.setSlotPos(AddingSlot, 12, -1000);
			}
		}
	}
	public void placeSlotsforReinforcements(Container myDrillHeads)
	{
		try {
			this.resetSlots(myDrillHeads);
			int ID = 0;
			
			//top
			Slot AddingSlot = myDrillHeads.getSlot(ID);
			AddingSlot.putStack(this.reinforcement.reinforcementMap[ID]);
			this.setSlotPos(AddingSlot, 8 + (3 + -1) * 18, -1 + (1 + -2) * 18);
			ID++;
			AddingSlot = myDrillHeads.getSlot(ID);
			AddingSlot.putStack(this.reinforcement.reinforcementMap[ID]);
			this.setSlotPos(AddingSlot, 8 + (3 + 0) * 18, -1 + (1 + -2) * 18);
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
			this.setSlotPos(AddingSlot, 8 + (3 + -1) * 18, -1 + (1 + -1) * 18);
			ID++;
			AddingSlot = myDrillHeads.getSlot(ID);
			AddingSlot.putStack(this.reinforcement.reinforcementMap[ID]);
			this.setSlotPos(AddingSlot, 8 + (3 + -1) * 18, -1 + (1 + 0) * 18);
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
			this.setSlotPos(AddingSlot, 8 + (3 + 3) * 18, -1 + (1 + -1) * 18);
			ID++;
			AddingSlot = myDrillHeads.getSlot(ID);
			AddingSlot.putStack(this.reinforcement.reinforcementMap[ID]);
			this.setSlotPos(AddingSlot, 8 + (3 + 3) * 18, -1 + (1 + 0) * 18);
			ID++;
			AddingSlot = myDrillHeads.getSlot(ID);
			AddingSlot.putStack(this.reinforcement.reinforcementMap[ID]);
			this.setSlotPos(AddingSlot, 8 + (3 + 3) * 18, -1 + (1 + 1) * 18);
			ID++;
			
			//lower
			AddingSlot = myDrillHeads.getSlot(ID);
			AddingSlot.putStack(this.reinforcement.reinforcementMap[ID]);
			this.setSlotPos(AddingSlot, 8 + (3 + 0) * 18, -1 + (1 + 2) * 18);
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
			ID++;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
		for (int i = 8; i < ContainerCaterpillar.getMaxSize() + this.storage.added; ++i)
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
		Slot AddingSlot = myDrillHeads.getSlot(ID);
		AddingSlot.putStack(this.inventory[ID]);
		this.setSlotPos(myDrillHeads.getSlot(ID), 8 + (4) * 18, 7 + (3) * 18);

		//Left Side
		int IDMiddle   = (ContainerCaterpillar.getMaxSize() + this.storage.added - 1 )/2;
		ID++;
		for (i = 0; i < (IDMiddle/3); ++i)
		{
			for (j = 0; j < 3; ++j)
			{
				AddingSlot = myDrillHeads.getSlot(ID);
				AddingSlot.putStack(this.inventory[ID]);
				this.setSlotPos(myDrillHeads.getSlot(ID), 8 + j * 18, -100);
				ID++;
			}
		}

		//Right Side

		for (i = 0; i < (IDMiddle/3); ++i)
		{
			for (j = 0; j < 3; ++j)
			{
				AddingSlot = myDrillHeads.getSlot(ID);
				AddingSlot.putStack(this.inventory[ID]);
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
		int Middleindex = (this.inventory.length - 1) / 2;
		Middleindex++;

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
	public ContainerCaterpillar clone()
	{
		String key = this.name;
		BlockPos posP = new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ());
		ContainerCaterpillar newCatp = new ContainerCaterpillar(posP, key);
		newCatp.inventory = this.inventory.clone();
		newCatp.maxburntime = this.maxburntime;
		newCatp.storage.added = this.storage.added;
		newCatp.burntime = this.burntime;
		newCatp.decoration = this.decoration.clone();
		newCatp.reinforcement.reinforcementMap = this.reinforcement.reinforcementMap.clone();

		return newCatp;
	}
	public void changeStorage( int Change) {
		if (this.storage.added + Change < 0)
		{
			return;
		}
		ItemStack[] tmpIT = new ItemStack[ContainerCaterpillar.getMaxSize() + this.storage.added + Change];
		
		int MiddleA = this.inventory.length / 2;
		int MiddleB = (tmpIT.length - 1) / 2;
		
		if (MiddleB > MiddleA)
		{
			ItemStack[] tmpITRight = new ItemStack[MiddleB];		
			for (int i = 0; i < this.inventory.length; i++) {
				if (i <= MiddleA)
				{
					tmpIT[i] = this.inventory[i];
				}
				else
				{
					tmpITRight[i - MiddleA] = this.inventory[i];
				}
			}
			for (int i = 0; i < tmpITRight.length; i++) {
				tmpIT[i + MiddleB] =  tmpITRight[i];
			}
			
		}
		else
		{
			for (int i = 0; i < this.inventory.length; i++) {
				if (i <= MiddleB )
				{tmpIT[i] = this.inventory[i];
					
				}else if (i > MiddleB && i < MiddleA)
				{
					if (this.inventory[i] != null)
					{
						Reference.dropItem(Reference.theWorldServer(), pos, this.inventory[i]);
					}
				}else if (i > MiddleA && i <= MiddleA + MiddleB)
				{
					tmpIT[i - MiddleB] = this.inventory[i];
				}
				else
				{
					if (this.inventory[i] != null)
					{
						Reference.dropItem(Reference.theWorldServer(), pos, this.inventory[i]);
					}
				}
			}
			/*
			for (int i = 0; i < this.inventory.length; i++) {
				if (i < tmpIT.length)
				{
					tmpIT[i] = this.inventory[i];				
				}
				else
				{
					if (this.inventory[i] != null)
					{
						Reference.dropItem(Reference.theWorldServer(), pos, this.inventory[i]);
					}
				}
	
			}
			*/
		}
		this.storage.startingIndex = 1;
		this.inventory = tmpIT.clone();
		this.storage.added = this.storage.added + Change;
	}
	public static ContainerCaterpillar readCatapiller(NBTTagCompound NBTconCat)
	{
		String key = NBTconCat.getString("name");
		BlockPos posP = new BlockPos(NBTconCat.getInteger("X"), NBTconCat.getInteger("Y"), NBTconCat.getInteger("Z"));
		ContainerCaterpillar newCatp = new ContainerCaterpillar(posP, key);
		newCatp.tabs.selected = GuiTabs.values()[NBTconCat.getInteger("selectedtab")];
		newCatp.decoration.selected = NBTconCat.getInteger("decorationsselected");
		newCatp.maxburntime = NBTconCat.getInteger("burntimemax");
		newCatp.storage.added = NBTconCat.getInteger("addedStorage");
		newCatp.burntime = NBTconCat.getInteger("burntime");
		newCatp.running = NBTconCat.getBoolean("running");
		newCatp.inventory = Reference.MainNBT.readItemStacks(NBTconCat);
		if (NBTconCat.hasKey("decorations"))
		{
			newCatp.decoration.readNBT(NBTconCat.getCompoundTag("decorations"));
		}
		if (NBTconCat.hasKey("drag"))
		{
			newCatp.drag.readNBT(NBTconCat.getCompoundTag("drag"));
		}
		if (NBTconCat.hasKey("Reinforcement"))
		{
			newCatp.reinforcement.readNBT(NBTconCat.getCompoundTag("Reinforcement"));
		}
		return newCatp;
	}
	public NBTTagCompound writeNBTCatapillar() {
		NBTTagCompound NBTconCat = Reference.MainNBT.writeItemStacks(this.inventory);
		NBTconCat.setTag("decorations", this.decoration.saveNBT());
		NBTconCat.setTag("Reinforcement", this.reinforcement.saveNBT());
		NBTconCat.setTag("drag", this.drag.saveNBT());
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
