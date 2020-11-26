package the_fireplace.caterpillar.handlers;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.Reference;
import the_fireplace.caterpillar.containers.CaterpillarData;

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
            // TODO: replace FMLCommonHandler
            // return ServerLifecycleHooks.getCurrentServer().getWorld().getWorldServer();
            //return FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld();
        } catch (Exception e) {
            return null;
        }
        // TODO: Remove after replacing FMLCommonHandler
        return null;
    }

    public String SavedWorldFolder()
    {
        if (this.theWorldServer() != null)
        {
            return this.theWorldServer().getSaveHandler().getWorldDirectory().getName();
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

        File versionsDir = new File(MainLocation, Caterpillar.MOD_ID);
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
        MainLocation = this.checkPath(MainLocation + Caterpillar.MOD_ID);

        File versionsDir = new File(MainLocation);
        if (!versionsDir.exists())
        {
            versionsDir.mkdir();
        }
        MainLocation = this.checkPath(versionsDir.getAbsolutePath());
        return MainLocation;
    }

    public NonNullList<ItemStack> readItemStack(CompoundNBT tmpNBT)
    {
        String id = tmpNBT.getString("id");
        if (id.equals("minecraft:air"))
        {
            return null;
        }
        int Count = tmpNBT.getByte("Count");
        int Damage = tmpNBT.getShort("Damage");
        return NonNullList.withSize(CaterpillarData.getMaxSize(), new ItemStack(Item.getByNameOrId((id), Count, Damage)));
    }

    public NonNullList<ItemStack> readItemStacks(CompoundNBT tmpNBT) {
        if (tmpNBT.contains("Count"))
        {
            int size = tmpNBT.getInt("Count");
            NonNullList<ItemStack> tmpIS = NonNullList.withSize(CaterpillarData.getMaxSize(), ItemStack.EMPTY);
            for(int i=0;i<size;i++)
            {
                tmpIS = this.readItemStack(tmpNBT.getCompound(i + "Item"));
            }

            //int frequance = tmpNBT.getInteger("frequance");
            return tmpIS.clone();
        }
        return null;
    }

    public CompoundNBT writeItemStacks(NonNullList<ItemStack> inventory)
    {
        CompoundNBT tmpNBT = new CompoundNBT();

        int i = 0;
        for (i = 0; i < inventory.size(); i++) {
            tmpNBT.put(i + "Item", this.writeItemStack(inventory.get(i)));
        }
        tmpNBT.putInt("Count", inventory.size());
        return tmpNBT;
    }

    public CompoundNBT writeItemStack(ItemStack its)
    {
        CompoundNBT tmpNBT = new CompoundNBT();
        Item toAdd = null;
        byte StackSize = 0;
        short Damage = 0;
        if (its != null)
        {
            toAdd = its.getItem();
            StackSize = (byte)its.getCount();
            Damage = (short)its.getDamage();
        }
        ResourceLocation resourcelocation = Item.REGISTRY.getNameForObject(toAdd);
        tmpNBT.putString("id", resourcelocation == null ? "minecraft:air" : resourcelocation.toString());
        tmpNBT.putByte("Count", StackSize);
        tmpNBT.putShort("Damage", Damage);

        return tmpNBT;
    }

    public CompoundNBT readNBTSettings(String folderLocation, String FileName) {
        CompoundNBT par1NBTTagCompoundSettings;
        File f = new File(folderLocation, FileName);
        if (f.exists())
        {
            try
            {
                par1NBTTagCompoundSettings = CompressedStreamTools.readCompressed(new FileInputStream(f));

                return par1NBTTagCompoundSettings.getCompound(this.modID);
            }
            catch (ZipException e)
            {
                f.delete();
            }
            catch (Exception e)
            {
                Reference.printDebug("NBT Error: " + folderLocation + "\\" + FileName);
                CompoundNBT nbt = new CompoundNBT();
                this.saveNBTSettings(nbt, folderLocation, FileName);
                return nbt;
            }
        }
        else
        {
            CompoundNBT nbt = new CompoundNBT();
            this.saveNBTSettings(nbt, folderLocation, FileName);
            return nbt;
        }
        return null;
    }

    public void saveNBTSettings(CompoundNBT nbt, String folderLocation, String FileName)
    {
        CompoundNBT par1NBTTagCompoundSettings = new CompoundNBT();
        par1NBTTagCompoundSettings.put(this.modID, nbt);
        File f = new File(folderLocation,  FileName);

        try {
            CompressedStreamTools.writeCompressed(par1NBTTagCompoundSettings, new FileOutputStream(f));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
