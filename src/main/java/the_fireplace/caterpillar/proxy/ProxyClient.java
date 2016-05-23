package the_fireplace.caterpillar.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import the_fireplace.caterpillar.inits.InitBlocks;

import java.io.File;

public class ProxyClient extends ProxyCommon{

	@Override
	public boolean checkLoaded()
	{
		return Minecraft.getMinecraft().currentScreen instanceof GuiMainMenu || this.getWorld() == null;
	}
	@Override
	public File getDataDir(){
		return Minecraft.getMinecraft().mcDataDir;
	}
	@Override
	public EntityPlayer getPlayer(){
		return Minecraft.getMinecraft().thePlayer;
	}
	@Override
	public World getWorld(){
		return Minecraft.getMinecraft().theWorld;
	}
	@Override
	public boolean isServer()
	{
		return false;
	}
	@Override
	public void registerRenders()
	{
		InitBlocks.registerRenders();
	}
}
