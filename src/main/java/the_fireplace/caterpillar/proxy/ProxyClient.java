package the_fireplace.caterpillar.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import the_fireplace.caterpillar.inits.InitBlocks;

import java.io.File;

public class ProxyClient extends ProxyCommon{

	@Override
	public File getDataDir(){
		return Minecraft.getMinecraft().mcDataDir;
	}
	@Override
	public World getWorld(){
		return Minecraft.getMinecraft().world;
	}
	@Override
	@Deprecated
	public boolean isServerSide()
	{
		return false;
	}
	@Override
	public void registerRenders()
	{
		InitBlocks.registerRenders();
	}
	@Override
	public String translateToLocal(String s, Object... args){
		return I18n.format(s, args);
	}

	@Override
	public EntityPlayer getPlayerEntity(MessageContext ctx) {
		return (ctx.side.isClient() ? Minecraft.getMinecraft().player : super.getPlayerEntity(ctx));
	}
}
