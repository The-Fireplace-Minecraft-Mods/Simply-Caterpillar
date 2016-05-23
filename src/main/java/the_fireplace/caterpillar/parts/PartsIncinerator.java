package the_fireplace.caterpillar.parts;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import the_fireplace.caterpillar.Reference;

public class PartsIncinerator extends PartsTabbed implements Cloneable {

	public ItemStack[] placementMap;

	public PartsIncinerator()
	{
		this.placementMap = new ItemStack[12];
		this.placementMap[0] = new ItemStack(Blocks.DIRT);
		this.placementMap[1] = new ItemStack(Blocks.GRAVEL);
		this.placementMap[2] = new ItemStack(Blocks.SAND);


		boolean found = false;
		NBTTagCompound tmpNBT =  Reference.MainNBT.readNBTSettings(Reference.MainNBT.getFolderLocationWorld(), "IncineratorDefault.dat");
		if (tmpNBT != null)
		{
			if (tmpNBT.hasKey("incinerator"))
			{
				this.readNBT(tmpNBT.getCompoundTag("incinerator"));
				found = true;
			}
		}
		if (!found)
		{
			tmpNBT =  Reference.MainNBT.readNBTSettings(Reference.MainNBT.getFolderLocationMod(), "IncineratorDefault.txt");
			if (tmpNBT != null)
			{
				if (tmpNBT.hasKey("incinerator"))
				{
					this.readNBT(tmpNBT.getCompoundTag("incinerator"));
				}
			}
		}


	}


	@Override
	public PartsIncinerator clone()
	{
		PartsIncinerator tmp = new PartsIncinerator();
		for (int i = 0; i < this.placementMap.length; i++) {
			tmp.placementMap[i] = this.placementMap[i].copy();
		}
		return tmp;
	}

	@Override
	public void readNBT(NBTTagCompound NBTconCat)
	{
		super.readNBT(NBTconCat);

		NBTTagCompound NBTconCatsub = NBTconCat.getCompoundTag("toburn") ;

		this.placementMap = Reference.MainNBT.readItemStacks(NBTconCatsub);

	}
	@Override
	public NBTTagCompound saveNBT()
	{

		NBTTagCompound NBTconCat = super.saveNBT();
		NBTconCat.setTag("toburn", Reference.MainNBT.writeItemStacks(this.placementMap));
		return NBTconCat;
	}
}
