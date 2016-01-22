package com.gmail.cgfreethemice.caterpillar.proxy;

import java.io.File;

import com.gmail.cgfreethemice.caterpillar.Reference;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

public class ProxyCommon {

	public void registerRenders()
	{}
	public World getWorld(){
		if ( MinecraftServer.getServer().worldServers != null)
		{
			if ( MinecraftServer.getServer().worldServers.length >0)
			{
				return MinecraftServer.getServer().getEntityWorld();
			}
		}
		return null;
	}
	public EntityPlayer getPlayer(){
		return null;
	}
	public File getDataDir(){
		return MinecraftServer.getServer().getDataDirectory();
	}
	public boolean checkLoaded()
	{
		if (Reference.theWorldServer() == null)
		{
			return true;

		}
		return false;
	}
	public boolean isServer()
	{
		return true;
	}

}
