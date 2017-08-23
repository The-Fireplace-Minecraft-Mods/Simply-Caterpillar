package the_fireplace.caterpillar.handlers;

import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.Reference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipException;

public class HandlerNBTTag {

	public String modID;

	public HandlerNBTTag(String ModID) {
		this.modID = ModID;
	}
	private World theWorldServer()
	{
		try {
			return FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld();
		} catch (Exception e) {
			return null;
		}
	}

	public String SavedWorldFolder()
	{
		if (this.theWorldServer() != null)
		{
			return this.theWorldServer().getSaveHandler().getWorldDirectory().getName();//TODO: Make sure this is correct
		}
		return "";
	}

	private String checkPath(String Path)
	{
		String FileSeparator = File.separator;
		String ASP = Path;
		if (ASP.endsWith("."))
		{
			ASP = ASP.substring(0, ASP.length() - 1);
		}
		if (!ASP.endsWith(FileSeparator))
		{
			ASP = ASP + FileSeparator;
		}
		return ASP;
	}

	public NBTTagCompound readNBTSettings(String folderLocation, String FileName)
	{
		NBTTagCompound par1NBTTagCompoundSettings;
		File f = new File(folderLocation, FileName);
		if (f.exists())
		{
			try
			{
				par1NBTTagCompoundSettings = CompressedStreamTools.readCompressed(new FileInputStream(f));

				return par1NBTTagCompoundSettings.getCompoundTag(this.modID);
			}
			catch (ZipException e)
			{
				f.delete();
			}
			catch (Exception e)
			{
				Reference.printDebug("NBT Error: " + folderLocation + "\\" + FileName);
				NBTTagCompound nbt = new NBTTagCompound();
				this.saveNBTSettings(nbt, folderLocation, FileName);
				return nbt;
			}
		}
		else
		{
			NBTTagCompound nbt = new NBTTagCompound();
			this.saveNBTSettings(nbt, folderLocation, FileName);
			return nbt;
		}
		return null;
	}

	public void saveNBTSettings(NBTTagCompound nbt, String folderLocation, String FileName)
	{
		NBTTagCompound par1NBTTagCompoundSettings = new NBTTagCompound();
		par1NBTTagCompoundSettings.setTag(this.modID, nbt);
		File f = new File(folderLocation,  FileName);

		try {
			CompressedStreamTools.writeCompressed(par1NBTTagCompoundSettings, new FileOutputStream(f));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getFolderLocationModDirectory() {
		//String FileSeparator = Caterpillar.proxy.getDataDir().separator;
		String MainLocation = this.checkPath(Caterpillar.proxy.getDataDir().getAbsolutePath());

		File versionsDir = new File(MainLocation, "mods");
		if (!versionsDir.exists())
		{
			versionsDir.mkdir();
		}
		MainLocation = this.checkPath(versionsDir.getAbsolutePath());
		return MainLocation;
	}

	public String getFolderLocationMod() {
		//String FileSeparator = Caterpillar.proxy.getDataDir().separator;
		String MainLocation = this.checkPath(Caterpillar.proxy.getDataDir().getAbsolutePath());

		File versionsDir = new File(MainLocation, Caterpillar.MODID);
		if (!versionsDir.exists())
		{
			versionsDir.mkdir();
		}
		MainLocation = this.checkPath(versionsDir.getAbsolutePath());
		return MainLocation;
	}

	public String getFolderLocationWorld() {
		//String FileSeparator = Caterpillar.proxy.getDataDir().separator;
		String MainLocation = this.checkPath(Caterpillar.proxy.getDataDir().getAbsolutePath());
		if (!Caterpillar.proxy.isServerSide())
		{
			MainLocation = this.checkPath(MainLocation + "saves");
		}
		MainLocation = this.checkPath(MainLocation + this.SavedWorldFolder());
		MainLocation = this.checkPath(MainLocation + Caterpillar.MODID);

		File versionsDir = new File(MainLocation);
		if (!versionsDir.exists())
		{
			versionsDir.mkdir();
		}
		MainLocation = this.checkPath(versionsDir.getAbsolutePath());
		return MainLocation;
	}

	public ItemStack readItemStack(NBTTagCompound tmpNBT)
	{
		String id = tmpNBT.getString("id");
		if (id.equals("minecraft:air"))
		{
			return null;
		}
		int Count = tmpNBT.getByte("Count");
		int Damage = tmpNBT.getShort("Damage");
		return new ItemStack(Item.getByNameOrId(id), Count, Damage);

	}

	public ItemStack[] readItemStacks(NBTTagCompound tmpNBT)
	{

		if (tmpNBT.hasKey("Count"))
		{
			int size = tmpNBT.getInteger("Count");
			ItemStack[] tmpIS= new ItemStack[size];
			for(int i=0;i<size;i++)
			{
				tmpIS[i] = this.readItemStack(tmpNBT.getCompoundTag(i + "Item"));
			}

			//int frequance = tmpNBT.getInteger("frequance");
			return tmpIS.clone();
		}
		return null;
	}

	public NBTTagCompound writeItemStacks(ItemStack[] its)
	{
		NBTTagCompound tmpNBT = new NBTTagCompound();

		int i = 0;
		for (i = 0; i < its.length; i++) {
			tmpNBT.setTag(i + "Item", this.writeItemStack(its[i]));
		}
		tmpNBT.setInteger("Count", its.length);
		return tmpNBT;
	}

	public NBTTagCompound writeItemStack(ItemStack its)
	{
		NBTTagCompound tmpNBT = new NBTTagCompound();
		Item toAdd = null;
		byte StackSize = 0;
		short Damage = 0;
		if (its != null)
		{
			toAdd = its.getItem();
			StackSize = (byte)its.getCount();
			Damage = (short)its.getItemDamage();
		}
		ResourceLocation resourcelocation = Item.REGISTRY.getNameForObject(toAdd);
		tmpNBT.setString("id", resourcelocation == null ? "minecraft:air" : resourcelocation.toString());
		tmpNBT.setByte("Count", StackSize);
		tmpNBT.setShort("Damage", Damage);

		return tmpNBT;
	}
}