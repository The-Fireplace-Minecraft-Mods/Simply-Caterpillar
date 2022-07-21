package the_fireplace.caterpillar.parts;

import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PartsReinforcement extends PartsTabbed {
	public ItemStack[] reinforcementMap = new ItemStack[16];
	public List<int[]> replacers = new ArrayList<>();

	public PartsReinforcement()
	{
		this.setMap();
		this.setReplacers();

		boolean found = false;
		CompoundNBT tmpNBT =  Reference.MainNBT.readNBTSettings(Reference.MainNBT.getFolderLocationWorld(), "ReinforcementDefault.dat");
		if (tmpNBT != null)
		{
			if (tmpNBT.contains("reinforcement"))
			{
				this.readNBT(tmpNBT.getCompound("reinforcement"));
				found = true;
			}
		}
		if (!found)
		{
			tmpNBT =  Reference.MainNBT.readNBTSettings(Reference.MainNBT.getFolderLocationMod(), "ReinforcementDefault.txt");
			if (tmpNBT != null)
			{
				if (tmpNBT.contains("reinforcement"))
				{
					this.readNBT(tmpNBT.getCompound("reinforcement"));
				}
			}
		}
	}

	private void setMap() {
		for (int i = 0; i < 16; i++) {
			this.reinforcementMap[i] = new ItemStack(Blocks.COBBLESTONE);
		}
	}

	private void setReplacers() {
		for (int i = 0; i < 4; i++) {
			this.replacers.add(new int[5]);
		}
		this.replacers.get(0)[0] = 0;
		this.replacers.get(0)[1] = 1;
		this.replacers.get(0)[2] = 1;
		this.replacers.get(0)[3] = 1;
		this.replacers.get(0)[4] = 0;

		this.replacers.get(1)[0] = 0;
		this.replacers.get(1)[1] = 1;
		this.replacers.get(1)[2] = 1;
		this.replacers.get(1)[3] = 0;
		this.replacers.get(1)[4] = 0;

		this.replacers.get(2)[0] = 0;
		this.replacers.get(2)[1] = 1;
		this.replacers.get(2)[2] = 1;
		this.replacers.get(2)[3] = 0;
		this.replacers.get(2)[4] = 0;

		this.replacers.get(3)[0] = 1;
		this.replacers.get(3)[1] = 1;
		this.replacers.get(3)[2] = 1;
		this.replacers.get(3)[3] = 0;
		this.replacers.get(3)[4] = 0;
	}

	@Override
	public void readNBT(CompoundNBT NBTconCat)
	{
		super.readNBT(NBTconCat);

		this.reinforcementMap = new  ItemStack[16];
		this.reinforcementMap = Reference.MainNBT.readItemStacks(NBTconCat);
		if (this.reinforcementMap.length < 16)
		{
			this.reinforcementMap = new  ItemStack[16];
			for (int i = 0; i < 16; i++) {
				this.reinforcementMap[i] = new ItemStack(Blocks.COBBLESTONE);
			}
		}


		this.replacers.clear();
		for (int i = 0; i < 4; i++) {
			if (NBTconCat.contains("replacers" + i))
			{
				this.replacers.add(NBTconCat.getIntArray("replacers" + i));
			}
		}
		// convert old saves.
		if (this.replacers.get(0).length == 4 || this.replacers.size() != 4)
		{
			this.replacers.clear();
			this.setReplacers();
		}
		if (this.reinforcementMap.length !=  16)
		{
			this.setMap();
		}
	}
	@Override
	public CompoundNBT saveNBT()
	{
		CompoundNBT NBTconCat = Reference.MainNBT.writeItemStacks(this.reinforcementMap);
		NBTconCat = super.saveNBT(NBTconCat);
		for (int i = 0; i < 4; i++) {
			NBTconCat.putIntArray("replacers" + i, this.replacers.get(i));
		}

		return NBTconCat;
	}
}
