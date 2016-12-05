package the_fireplace.caterpillar;

import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CommonEvents {

	/*@SubscribeEvent//There is no need to register it if the contents are noted out.
	public void onWorldSave(WorldEvent.Save event)
	{
		//Reference.theWorldServer().tickUpdates(true);
		//Reference.printDebug("SAVING");
	}*/

	@SubscribeEvent
	public void onConfigurationChanged(ConfigChangedEvent.OnConfigChangedEvent event)
	{
		if (event.getModID().equals(Caterpillar.MODID))
		{
			Config.save();
			Config.load();
		}
	}
}
