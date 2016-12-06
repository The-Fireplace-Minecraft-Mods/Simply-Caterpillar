package the_fireplace.caterpillar.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.io.File;

public class ProxyCommon {

	public File getDataDir(){
		return FMLCommonHandler.instance().getMinecraftServerInstance().getDataDirectory();
	}
	public World getWorld(){
		if (FMLCommonHandler.instance().getMinecraftServerInstance().worlds != null)
		{
			if (FMLCommonHandler.instance().getMinecraftServerInstance().worlds.length >0)
			{
				return FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld();
			}
		}
		return null;
	}
	public void registerRenders()
	{}
	public String translateToLocal(String s, Object... args){
		return s;
	}

	public EntityPlayer getPlayerEntity(MessageContext ctx) {
		return ctx.getServerHandler().playerEntity;
	}

	public void closeDrillGui(){}

	public String getModifiedPath(String s){
		return s;
	}
}
