package com.gmail.cgfreethemice.caterpillar.parts;

import java.util.ArrayList;
import java.util.List;

import com.gmail.cgfreethemice.caterpillar.Reference;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class PartsReinforcement {
	public ItemStack[] reinforcementMap = new ItemStack[16];
	public List<int[]> replacers = new ArrayList();

	public PartsReinforcement()
	{
		this.setMap();
		this.setReplacers();
	}

	private void setMap() {
		for (int i = 0; i < 16; i++) {
			reinforcementMap[i] = new ItemStack(Blocks.cobblestone);
		}		
	}

	private void setReplacers() {
		for (int i = 0; i < 4; i++) {
			replacers.add(new int[5]);
		}
		replacers.get(0)[0] = 0;
		replacers.get(0)[1] = 1;
		replacers.get(0)[2] = 1;
		replacers.get(0)[3] = 1;
		replacers.get(0)[4] = 0;

		replacers.get(1)[0] = 0;
		replacers.get(1)[1] = 1;
		replacers.get(1)[2] = 1;
		replacers.get(1)[3] = 0;
		replacers.get(1)[4] = 0;

		replacers.get(2)[0] = 0;
		replacers.get(2)[1] = 1;
		replacers.get(2)[2] = 1;
		replacers.get(2)[3] = 0;
		replacers.get(2)[4] = 0;

		replacers.get(3)[0] = 1;
		replacers.get(3)[1] = 1;
		replacers.get(3)[2] = 1;
		replacers.get(3)[3] = 0;
		replacers.get(3)[4] = 0;
	}

	public void readNBT(NBTTagCompound NBTconCat)
	{
		
		reinforcementMap = new  ItemStack[16];
		reinforcementMap = Reference.MainNBT.readItemStacks(NBTconCat);
		if (reinforcementMap.length < 16)
		{
			reinforcementMap = new  ItemStack[16];
			for (int i = 0; i < 16; i++) {
				reinforcementMap[i] = new ItemStack(Blocks.cobblestone);
			}
		}
	
		
		replacers.clear();
		for (int i = 0; i < 4; i++) {
			if (NBTconCat.hasKey("replacers" + i))
			{
				replacers.add(NBTconCat.getIntArray("replacers" + i));
			}
		}
// convert old saves.
		if (replacers.get(0).length == 4 || replacers.size() != 4)
		{
			replacers.clear();
			this.setReplacers();
		}
		if (reinforcementMap.length !=  16)
		{
			this.setMap();
		}	
	}
	public NBTTagCompound saveNBT()
	{
		NBTTagCompound NBTconCat = new NBTTagCompound();
		NBTconCat = Reference.MainNBT.writeItemStacks(reinforcementMap);
		for (int i = 0; i < 4; i++) {
			NBTconCat.setIntArray("replacers" + i, replacers.get(i));			
		}
		
		
		
		return NBTconCat;

	}

}
