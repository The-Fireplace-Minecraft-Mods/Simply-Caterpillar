package the_fireplace.caterpillar.parts;

import the_fireplace.caterpillar.Reference;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.List;

public class PartsDecoration extends PartsTabbed implements Cloneable{
	public List<ItemStack[]> placementMap;
	public byte selected = 0;
	public int countindex = 0;
	public final int maxSlots = 10;
	public PartsDecoration ()
	{
		this.placementMap = new ArrayList<>();
		for (int i = 0; i < this.maxSlots; i++) {
			this.placementMap.add(new ItemStack[8]);
			this.placementMap.get(i)[4] = new ItemStack(Blocks.RAIL);
		}
		this.placementMap.get(5)[0] = new ItemStack(Blocks.PLANKS);
		this.placementMap.get(5)[3] = new ItemStack(Blocks.PLANKS);
		this.placementMap.get(5)[5] = new ItemStack(Blocks.PLANKS);

		this.placementMap.get(5)[1] = new ItemStack(Blocks.OAK_FENCE);
		this.placementMap.get(5)[6] = new ItemStack(Blocks.OAK_FENCE);
		this.placementMap.get(5)[2] = new ItemStack(Blocks.OAK_FENCE);
		this.placementMap.get(5)[7] = new ItemStack(Blocks.OAK_FENCE);

		this.placementMap.get(6)[2] = new ItemStack(Blocks.TORCH);
		this.placementMap.get(6)[7] = new ItemStack(Blocks.TORCH);

		this.placementMap.get(7)[2] = new ItemStack(Blocks.REDSTONE_TORCH);
		this.placementMap.get(7)[4] = new ItemStack(Blocks.GOLDEN_RAIL);


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
			thisclone.placementMap.add(this.placementMap.get(i).clone());
		}
		thisclone.selected = this.selected;
		return thisclone;
	}
	public ItemStack[] getSelectedInventory()
	{
		return this.placementMap.get(this.selected);
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
