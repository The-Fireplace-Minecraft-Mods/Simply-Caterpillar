package com.gmail.cgfreethemice.caterpillar.handlers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.util.zip.ZipException;

import com.gmail.cgfreethemice.caterpillar.Caterpillar;
import com.gmail.cgfreethemice.caterpillar.Reference;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class HandlerNBTTag {

	public String FileName;
	public String modID;

	public HandlerNBTTag(String ModID) {
		this.modID = ModID;
	}
	public String SavedWorldFolder()
	{
		if (Reference.theWorldServer() != null)
		{
			return Reference.theWorldServer().getSaveHandler().getWorldDirectoryName();
		}
		return "";
	}

	private String checkPath(String Path)
	{
		String FileSeparator = Caterpillar.proxy.getDataDir().separator;
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

	public NBTTagCompound readNBTSettings(String folderLocation)
	{
		NBTTagCompound par1NBTTagCompoundSettings;
		File f = new File(folderLocation,  FileName);
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
				e.printStackTrace();
			}
		}
		else
		{
			NBTTagCompound nbt = new NBTTagCompound();
			saveNBTSettings(nbt, folderLocation);
			return nbt;
		}
		return null;
	}

	public void saveNBTSettings(NBTTagCompound nbt, String folderLocation)
	{
		NBTTagCompound par1NBTTagCompoundSettings = new NBTTagCompound();
		try
		{
			par1NBTTagCompoundSettings.setTag(this.modID, nbt);
			File f = new File(folderLocation,  FileName);

			CompressedStreamTools.writeCompressed(par1NBTTagCompoundSettings, new FileOutputStream(f));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public String getFolderLocationModDirectory() {
		String FileSeparator = Caterpillar.proxy.getDataDir().separator;
		String MainLocation = checkPath(Caterpillar.proxy.getDataDir().getAbsolutePath());

		File versionsDir = new File(MainLocation, "mods");
		if (versionsDir.exists() == false)
		{
			versionsDir.mkdir();
		}
		MainLocation = checkPath(versionsDir.getAbsolutePath());
		return MainLocation;
	}

	public String getFolderLocationMod() {
		String FileSeparator = Caterpillar.proxy.getDataDir().separator;
		String MainLocation = checkPath(Caterpillar.proxy.getDataDir().getAbsolutePath());

		File versionsDir = new File(MainLocation, Reference.MODID);
		if (versionsDir.exists() == false)
		{
			versionsDir.mkdir();
		}
		MainLocation = checkPath(versionsDir.getAbsolutePath());
		return MainLocation;
	}

	public String getFolderLocationWorld() {

		String FileSeparator = Caterpillar.proxy.getDataDir().separator;
		String MainLocation = checkPath(Caterpillar.proxy.getDataDir().getAbsolutePath());
		if (Caterpillar.proxy.isServer() == false)
		{
			MainLocation = checkPath(MainLocation + "saves");
		}
		MainLocation = checkPath(MainLocation + SavedWorldFolder());
		MainLocation = checkPath(MainLocation + Reference.MODID);

		File versionsDir = new File(MainLocation);
		if (versionsDir.exists() == false)
		{
			versionsDir.mkdir();
		}
		MainLocation = checkPath(versionsDir.getAbsolutePath());
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

			int frequance = tmpNBT.getInteger("frequance");
			return tmpIS.clone();
		}
		return null;
	}

	public NBTTagCompound writeItemStacks(ItemStack[] its)
	{
		NBTTagCompound tmpNBT = new NBTTagCompound();

		int i = 0;
		for (i = 0; i < its.length; i++) {
			tmpNBT.setTag(i + "Item", writeItemStack(its[i]));
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
			try {
				toAdd = its.getItem();
				StackSize = (byte)its.stackSize;
				Damage = (short)its.getItemDamage();
			} catch (Exception e) {
			}

		}
		ResourceLocation resourcelocation = (ResourceLocation)Item.itemRegistry.getNameForObject(toAdd);
		tmpNBT.setString("id", resourcelocation == null ? "minecraft:air" : resourcelocation.toString());
		tmpNBT.setByte("Count", StackSize);
		tmpNBT.setShort("Damage", Damage);

		return tmpNBT;
	}
}