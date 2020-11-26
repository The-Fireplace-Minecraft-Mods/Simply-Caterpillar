package the_fireplace.caterpillar.parts;

import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import the_fireplace.caterpillar.Reference;
import the_fireplace.caterpillar.containers.CaterpillarData;

import java.util.ArrayList;
import java.util.List;

public class PartsDecoration extends PartsTabbed implements Cloneable {
	public NonNullList<ItemStack> placementMap;
	public byte selected = 0;
	public int countindex = 0;
	public final int maxSlots = 10;
	public PartsDecoration ()
	{
		this.placementMap = new ArrayList<>();
		for (int i = 0; i < this.maxSlots; i++) {
			this.placementMap = NonNullList.withSize(8, ItemStack.EMPTY);
			this.placementMap = NonNullList.withSize(4, new ItemStack(Blocks.RAIL));
		}
		this.placementMap = NonNullList.withSize(0, new ItemStack(Blocks.OAK_PLANKS));
		this.placementMap = NonNullList.withSize(3, new ItemStack(Blocks.OAK_PLANKS));
		this.placementMap = NonNullList.withSize(5, new ItemStack(Blocks.OAK_PLANKS));

		this.placementMap = NonNullList.withSize(1, new ItemStack(Blocks.OAK_FENCE));
		this.placementMap = NonNullList.withSize(6, new ItemStack(Blocks.OAK_FENCE));
		this.placementMap = NonNullList.withSize(2, new ItemStack(Blocks.OAK_FENCE));
		this.placementMap = NonNullList.withSize(7, new ItemStack(Blocks.OAK_FENCE));

		this.placementMap = NonNullList.withSize(2, new ItemStack(Blocks.TORCH));
		this.placementMap = NonNullList.withSize(7, new ItemStack(Blocks.TORCH));

		this.placementMap = NonNullList.withSize(2, new ItemStack(Blocks.REDSTONE_TORCH));
		this.placementMap = NonNullList.withSize(4, new ItemStack(Blocks.RAIL));


		boolean found = false;
		CompoundNBT tmpNBT =  Reference.MainNBT.readNBTSettings(Reference.MainNBT.getFolderLocationWorld(), "DecorationDefault.dat");
		if (tmpNBT != null)
		{
			if (tmpNBT.contains("decoration"))
			{
				this.readNBT(tmpNBT.getCompound("decoration"));
				found = true;
			}
		}
		if (!found)
		{
			tmpNBT =  Reference.MainNBT.readNBTSettings(Reference.MainNBT.getFolderLocationMod(), "DecorationDefault.txt");
			if (tmpNBT != null)
			{
				if (tmpNBT.contains("decoration"))
				{
					this.readNBT(tmpNBT.getCompound("decoration"));
				}
			}
		}
	}
	@Override
	public PartsDecoration clone()
	{
		PartsDecoration thisClone = new PartsDecoration();
		thisClone.placementMap.clear();
		for (int i = 0; i < this.maxSlots; i++) {
			thisClone.placementMap = NonNullList.withSize(CaterpillarData.getMaxSize(), ItemStack.EMPTY).clone();
		}
		thisClone.selected = this.selected;
		return thisClone;
	}

	public NonNullList<ItemStack> getSelectedInventory()
	{
		return this.placementMap;
	}

	@Override
	public void readNBT(CompoundNBT NBTconCat)
	{
		super.readNBT(NBTconCat);
		this.placementMap.clear();
		for (int i = 0; i < this.maxSlots; i++) {
			CompoundNBT NBTconCatsub = NBTconCat.getCompound("decoration" + i);
			this.placementMap.add(Reference.MainNBT.readItemStacks(NBTconCatsub));
		}
		this.countindex = NBTconCat.getInt("countindex");
	}
	@Override
	public CompoundNBT saveNBT()
	{
		CompoundNBT NBTconCat = super.saveNBT();
		for (int i = 0; i < this.maxSlots; i++) {
			NBTconCat.put("decoration" + i, Reference.MainNBT.writeItemStacks(this.placementMap.get(i)));
		}
		NBTconCat.putInt("countindex", this.countindex);
		return NBTconCat;
	}
}
