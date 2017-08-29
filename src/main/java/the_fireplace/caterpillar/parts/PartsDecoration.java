package the_fireplace.caterpillar.parts;

import the_fireplace.caterpillar.Reference;
import the_fireplace.caterpillar.containers.CaterpillarData;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;

import java.util.ArrayList;
import java.util.List;

public class PartsDecoration extends PartsTabbed implements Cloneable{
	public NonNullList<ItemStack> placementMap;
	public int selected = 0;
	public int countindex = 0;
	public final int maxSlots = 10;
	public PartsDecoration ()
	{
		this.placementMap = new ArrayList<>();
		for (int i = 0; i < this.maxSlots; i++) {
			this.placementMap = NonNullList.withSize(8, ItemStack.EMPTY);
			this.placementMap = NonNullList.withSize(4, new ItemStack(Blocks.RAIL));
		}
		this.placementMap = NonNullList.withSize(0, new ItemStack(Blocks.PLANKS));
		this.placementMap = NonNullList.withSize(3, new ItemStack(Blocks.PLANKS));
		this.placementMap = NonNullList.withSize(5, new ItemStack(Blocks.PLANKS));

		this.placementMap = NonNullList.withSize(1, new ItemStack(Blocks.OAK_FENCE));
		this.placementMap = NonNullList.withSize(6, new ItemStack(Blocks.OAK_FENCE));
		this.placementMap = NonNullList.withSize(2, new ItemStack(Blocks.OAK_FENCE));
		this.placementMap = NonNullList.withSize(7, new ItemStack(Blocks.OAK_FENCE));

		this.placementMap = NonNullList.withSize(2, new ItemStack(Blocks.TORCH));
		this.placementMap = NonNullList.withSize(7, new ItemStack(Blocks.TORCH));

		this.placementMap = NonNullList.withSize(2, new ItemStack(Blocks.REDSTONE_TORCH));
		this.placementMap = NonNullList.withSize(4, new ItemStack(Blocks.GOLDEN_RAIL));

		boolean found = false;
		NBTTagCompound tmpNBT =  Reference.MainNBT.readNBTSettings(Reference.MainNBT.getFolderLocationWorld(), "DecorationDefault.dat");
		if (tmpNBT != null)
		{
			if (tmpNBT.hasKey("decoration"))
			{
				this.readNBT(tmpNBT.getCompoundTag("decoration"));
				found = true;
			}
		}
		if (!found)
		{
			tmpNBT =  Reference.MainNBT.readNBTSettings(Reference.MainNBT.getFolderLocationMod(), "DecorationDefault.txt");
			if (tmpNBT != null)
			{
				if (tmpNBT.hasKey("decoration"))
				{
					this.readNBT(tmpNBT.getCompoundTag("decoration"));
				}
			}
		}
	}
	@Override
	public PartsDecoration clone()
	{
		PartsDecoration thisclone = new PartsDecoration();
		thisclone.placementMap.clear();
		for (int i = 0; i < this.maxSlots; i++) {
			thisclone.placementMap = NonNullList.withSize(CaterpillarData.getMaxSize(), ItemStack.EMPTY).clone();
		}
		thisclone.selected = this.selected;
		return thisclone;
	}
	public NonNullList<ItemStack> getSelectedInventory()
	{
		return this.placementMap;
	}

	@Override
	public void readNBT(NBTTagCompound NBTconCat)
	{
		super.readNBT(NBTconCat);
		this.placementMap.clear();
		for (int i = 0; i < this.maxSlots; i++) {
			NBTTagCompound NBTconCatsub = NBTconCat.getCompoundTag("decoration" + i);
			this.placementMap.add(Reference.MainNBT.readItemStacks(NBTconCatsub));
		}
		this.countindex = NBTconCat.getInteger("countindex");
	}
	@Override
	public NBTTagCompound saveNBT()
	{
		NBTTagCompound NBTconCat = super.saveNBT();
		for (int i = 0; i < this.maxSlots; i++) {
			NBTconCat.setTag("decoration" + i, Reference.MainNBT.writeItemStacks(this.placementMap.get(i)));
		}
		NBTconCat.setInteger("countindex", this.countindex);
		return NBTconCat;
	}
}
