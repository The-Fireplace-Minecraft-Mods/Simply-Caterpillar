package com.gmail.cgfreethemice.caterpillar.parts;

import java.util.ArrayList;
import java.util.List;

import com.gmail.cgfreethemice.caterpillar.Reference;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class PartsDecoration {
	public List<ItemStack[]> placementMap;
	public int selected = 0;
	public final int maxSlots = 10;
	public PartsDecoration ()
	{
		placementMap = new ArrayList<ItemStack[]>();
		for (int i = 0; i < maxSlots; i++) {
			placementMap.add(new ItemStack[8]);
			placementMap.get(i)[4] = new ItemStack(Blocks.rail);
		}
		placementMap.get(5)[0] = new ItemStack(Blocks.planks);
		placementMap.get(5)[3] = new ItemStack(Blocks.planks);
		placementMap.get(5)[5] = new ItemStack(Blocks.planks);

		placementMap.get(5)[1] = new ItemStack(Blocks.oak_fence);
		placementMap.get(5)[6] = new ItemStack(Blocks.oak_fence);
		placementMap.get(5)[2] = new ItemStack(Blocks.oak_fence);
		placementMap.get(5)[7] = new ItemStack(Blocks.oak_fence);

		placementMap.get(6)[2] = new ItemStack(Blocks.torch);
		placementMap.get(6)[7] = new ItemStack(Blocks.torch);

		placementMap.get(7)[2] = new ItemStack(Blocks.redstone_torch);
		placementMap.get(7)[4] = new ItemStack(Blocks.golden_rail);


	}
	@Override
	public PartsDecoration clone()
	{
		PartsDecoration thisclone = new PartsDecoration();
		thisclone.placementMap.clear();
		for (int i = 0; i < maxSlots; i++) {
			thisclone.placementMap.add(this.placementMap.get(i).clone());
		}
		thisclone.selected = this.selected;
		return thisclone;
	}
	public ItemStack[] getSelectedInventory()
	{
		return placementMap.get(this.selected);
	}
	public boolean isInventoryEmpty(int index)
	{
		boolean isEmpty = true;
		ItemStack[] thisStack = this.placementMap.get(index);
		for (ItemStack element : thisStack) {
			if (element!=null)
			{
				isEmpty = false;
			}
		}
		return isEmpty;
	}
	public void readNBT(NBTTagCompound NBTconCat)
	{
		placementMap.clear();
		for (int i = 0; i < maxSlots; i++) {
			NBTTagCompound NBTconCatsub = NBTconCat.getCompoundTag("decoration" + i);
			placementMap.add(Reference.MainNBT.readItemStacks(NBTconCatsub));
		}
	}
	public NBTTagCompound saveNBT()
	{
		NBTTagCompound NBTconCat = new NBTTagCompound();
		for (int i = 0; i < maxSlots; i++) {
			NBTconCat.setTag("decoration" + i, Reference.MainNBT.writeItemStacks(placementMap.get(i)));
		}
		return NBTconCat;
	}
}
