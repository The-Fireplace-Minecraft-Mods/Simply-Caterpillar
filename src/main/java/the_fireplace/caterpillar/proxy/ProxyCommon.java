package the_fireplace.caterpillar.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.io.File;

public class ProxyCommon {

	public boolean checkLoaded()
	{
		return this.getWorld() == null;
	}
	public File getDataDir(){
		return FMLCommonHandler.instance().getMinecraftServerInstance().getDataDirectory();
	}
	public EntityPlayer getPlayer(){
		return null;
	}
	public World getWorld(){
		if (FMLCommonHandler.instance().getMinecraftServerInstance().worldServers != null)
		{
			if (FMLCommonHandler.instance().getMinecraftServerInstance().worldServers.length >0)
			{
				return FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld();
			}
		}
		return null;
	}
	public boolean isServerSide()
	{
		return true;
	}
	public void registerRenders()
	{}
	public String translateToLocal(String s){
		return s;
	}
}
