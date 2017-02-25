package the_fireplace.caterpillar.parts;

import net.minecraft.nbt.NBTTagCompound;

public class PartsTabbed {

	public byte howclose = 0;
	public PartsTabbed()
	{

	}
	public void readNBT(NBTTagCompound NBTconCat)
	{
		if (NBTconCat.hasKey("howclose"))
		{
			this.howclose = NBTconCat.getByte("howclose");
		}
		else
		{
			this.howclose = 2;
		}
	}
	public NBTTagCompound saveNBT()
	{
		NBTTagCompound NBTconCat = new NBTTagCompound();
		NBTconCat.setByte("howclose", this.howclose);
		return NBTconCat;
	}
	public NBTTagCompound saveNBT(NBTTagCompound NBTconCat)
	{
		NBTconCat.setByte("howclose", this.howclose);
		return NBTconCat;
	}
}
