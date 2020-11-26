package the_fireplace.caterpillar.parts;

import net.minecraft.nbt.CompoundNBT;

public class PartsTabbed {

	public byte howclose = 0;
	public PartsTabbed()
	{

	}
	public void readNBT(CompoundNBT NBTconCat)
	{
		if (NBTconCat.contains("howclose"))
		{
			this.howclose = NBTconCat.getByte("howclose");
		}
		else
		{
			this.howclose = 2;
		}
	}
	public CompoundNBT saveNBT()
	{
		CompoundNBT NBTconCat = new CompoundNBT();
		NBTconCat.putByte("howclose", this.howclose);
		return NBTconCat;
	}
	public CompoundNBT saveNBT(CompoundNBT NBTconCat)
	{
		NBTconCat.putByte("howclose", this.howclose);
		return NBTconCat;
	}
}
