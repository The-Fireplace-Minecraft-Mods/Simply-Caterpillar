package com.gmail.cgfreethemice.caterpillar.guis;

import java.util.ArrayList;
import java.util.List;

import com.gmail.cgfreethemice.caterpillar.Config;
import com.gmail.cgfreethemice.caterpillar.Reference;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

public class GuiConfiguration extends GuiConfig{

	public GuiConfiguration(GuiScreen guiScreen)
	{
		super(guiScreen, generateConfigList(), Reference.MODID, true, false, Reference.Name);
	}

	public static List<IConfigElement> generateConfigList()
	{
		ArrayList elements = new ArrayList();

		elements.add(new ConfigElement(Config.configuration.getCategory("general")));
		elements.add(new ConfigElement(Config.configuration.getCategory("update notes")));

		return elements;
	}
}
