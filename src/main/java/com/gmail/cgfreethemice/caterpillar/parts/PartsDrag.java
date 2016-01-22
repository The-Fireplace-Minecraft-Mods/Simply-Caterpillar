package com.gmail.cgfreethemice.caterpillar.parts;

import net.minecraft.nbt.NBTTagCompound;

public class PartsDrag {

	public int value = 0;
	public int max = 0;
	public int total = 0;
	public PartsDrag()
	{

	}
	public void readNBT(NBTTagCompound NBTconCat)
	{
		this.value = NBTconCat.getInteger("value");
		this.max = NBTconCat.getInteger("max");
		this.total = NBTconCat.getInteger("total");
	}
	public NBTTagCompound saveNBT()
	{
		NBTTagCompound NBTconCat = new NBTTagCompound();
		NBTconCat.setInteger("value", this.value);
		NBTconCat.setInteger("max", this.max);
		NBTconCat.setInteger("total", this.total);
		return NBTconCat;
	}
}
