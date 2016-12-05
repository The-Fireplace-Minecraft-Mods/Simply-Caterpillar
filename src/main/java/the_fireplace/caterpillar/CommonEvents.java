package the_fireplace.caterpillar;

import net.minecraft.client.Minecraft;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class CommonEvents {

	@SubscribeEvent
	public void onConfigurationChanged(ConfigChangedEvent.OnConfigChangedEvent event)
	{
		if (event.getModID().equals(Caterpillar.MODID))
		{
			Config.save();
			Config.load();
		}
	}

	@SubscribeEvent
	public void worldTick(TickEvent.ClientTickEvent event){
		if (Reference.loaded)
		{
			if (Minecraft.getMinecraft().currentScreen != null)
			{
				if (!Caterpillar.instance.savedyet)
				{
					Caterpillar.instance.savedyet = true;
					Reference.printDebug("Menu: Saving...");
					Caterpillar.instance.saveNBTDrills();
				}
			}
			else
			{
				Caterpillar.instance.savedyet = false;
			}
		}
	}

	@SubscribeEvent
	public void worldLoad(WorldEvent.Load event){
		if (event.getWorld() != null )
		{
			if (!Reference.loaded)
			{
				Reference.printDebug("World loading, starting mod!");
				Caterpillar.instance.readNBTDrills();
				Reference.loaded = true;
				Reference.printDebug("Mod is running!");
			}else
				Reference.printDebug("Error loading Caterpillar data: is already loaded");
		}else
			Reference.printDebug("Error loading Caterpillar data: null world");
	}

	@SubscribeEvent
	public void worldUnload(WorldEvent.Unload event){
		Caterpillar.instance.reset();
	}
}
