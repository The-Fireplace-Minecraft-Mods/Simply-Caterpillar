package the_fireplace.caterpillar.parts;

import net.minecraft.nbt.CompoundNBT;

public class PartsMovement {

    public int total = 0;
    public int value = 0;
    public PartsMovement()
    {

    }
    public void readNBT(CompoundNBT NBTconCat)
    {
        this.value = NBTconCat.getInt("value");
        this.total = NBTconCat.getInt("total");
    }
    public CompoundNBT saveNBT()
    {
        CompoundNBT NBTconCat = new CompoundNBT();
        NBTconCat.putInt("value", this.value);
        NBTconCat.putInt("total", this.total);
        return NBTconCat;
    }
}