package the_fireplace.caterpillar.parts;

import net.minecraft.nbt.NBTTagCompound;

public class PartsDrag {

    public int total = 0;
    public int value = 0;
    public PartsDrag()
    {

    }
    public void readNBT(NBTTagCompound NBTconCat)
    {
        this.value = NBTconCat.getInteger("value");
        this.total = NBTconCat.getInteger("total");
    }
    public NBTTagCompound saveNBT()
    {
        NBTTagCompound NBTconCat = new NBTTagCompound();
        NBTconCat.setInteger("value", this.value);
        NBTconCat.setInteger("total", this.total);
        return NBTconCat;
    }
}