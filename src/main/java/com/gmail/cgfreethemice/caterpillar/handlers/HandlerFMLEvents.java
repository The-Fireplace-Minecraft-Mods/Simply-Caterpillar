package com.gmail.cgfreethemice.caterpillar.handlers;

import com.gmail.cgfreethemice.caterpillar.Config;
import com.gmail.cgfreethemice.caterpillar.Reference;

import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HandlerFMLEvents {



	@SubscribeEvent
	public void onConfigurationChanged(ConfigChangedEvent.OnConfigChangedEvent event)
	{
		if (event.modID.equals(Reference.MODID))
		{
			Config.save();
			Config.load();
		}
		//ConfigurationBackpack.loadConfiguration();
	}
}
