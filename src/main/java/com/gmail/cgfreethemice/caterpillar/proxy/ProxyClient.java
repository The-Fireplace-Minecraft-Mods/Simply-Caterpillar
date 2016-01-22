package com.gmail.cgfreethemice.caterpillar.proxy;

import java.io.File;

import com.gmail.cgfreethemice.caterpillar.inits.InitBlocks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;


public class ProxyClient extends ProxyCommon{


	@Override
	public void registerRenders()
	{
		InitBlocks.registerRenders();
	}
	@Override
	public World getWorld(){
		return Minecraft.getMinecraft().theWorld;
	}
	@Override
	public EntityPlayer getPlayer(){
		return Minecraft.getMinecraft().thePlayer;
	}
	@Override
	public File getDataDir(){
		return Minecraft.getMinecraft().mcDataDir;
	}
	@Override
	public boolean checkLoaded()
	{
		if (Minecraft.getMinecraft().currentScreen instanceof GuiMainMenu || this.getWorld() == null)
		{
			return true;

		}
		return false;
	}
	@Override
	public boolean isServer()
	{
		return false;
	}
}
