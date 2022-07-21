package the_fireplace.caterpillar.parts;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class PartsIncinerator extends PartsTabbed implements Cloneable {

	public ItemStack[] placementMap;

	public PartsIncinerator()
	{
		this.placementMap = new ItemStack[12];
		this.placementMap[0] = new ItemStack(Blocks.DIRT);
		this.placementMap[1] = new ItemStack(Blocks.GRAVEL);
		this.placementMap[2] = new ItemStack(Blocks.SAND);

		boolean found = false;

		CompoundNBT tmpNBT =  Reference.MainNBT.readNBTSettings(Reference.MainNBT.getFolderLocationWorld(), "IncineratorDefault.dat");
		if (tmpNBT != null)
		{
			if (tmpNBT.contains("incinerator"))
			{
				this.readNBT(tmpNBT.getCompound("incinerator"));
				found = true;
			}
		}
		if (!found)
		{
			tmpNBT =  Reference.MainNBT.readNBTSettings(Reference.MainNBT.getFolderLocationMod(), "IncineratorDefault.txt");
			if (tmpNBT != null)
			{
				if (tmpNBT.contains("incinerator"))
				{
					this.readNBT(tmpNBT.getCompound("incinerator"));
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
	public void readNBT(CompoundNBT NBTconCat)
	{
		super.readNBT(NBTconCat);

		CompoundNBT NBTconCatsub = NBTconCat.getCompound("toburn") ;

		this.placementMap = Reference.MainNBT.readItemStacks(NBTconCatsub);

	}
	@Override
	public CompoundNBT saveNBT()
	{
		CompoundNBT NBTconCat = super.saveNBT();
		NBTconCat.put("toburn", Reference.MainNBT.writeItemStacks(this.placementMap));
		return NBTconCat;
	}
}
