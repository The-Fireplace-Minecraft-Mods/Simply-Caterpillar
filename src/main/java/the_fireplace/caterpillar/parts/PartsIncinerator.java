package the_fireplace.caterpillar.parts;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import the_fireplace.caterpillar.Reference;
import the_fireplace.caterpillar.containers.CaterpillarData;

public class PartsIncinerator extends PartsTabbed implements Cloneable {

	public NonNullList<ItemStack> placementMap;

	public PartsIncinerator()
	{
		this.placementMap = NonNullList.withSize(12, ItemStack.EMPTY);
		this.placementMap = NonNullList.withSize(0, new ItemStack(Blocks.DIRT));
		this.placementMap = NonNullList.withSize(1, new ItemStack(Blocks.GRAVEL));
		this.placementMap = NonNullList.withSize(2, new ItemStack(Blocks.SAND));


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
		for (int i = 0; i < this.placementMap.size(); i++) {
			tmp.placementMap = NonNullList.withSize(CaterpillarData.getMaxSize(), ItemStack.EMPTY).copy();
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
