package the_fireplace.caterpillar.guis;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.Config;

import java.util.ArrayList;
import java.util.List;

public class GuiConfiguration extends GuiConfig{

	public GuiConfiguration(GuiScreen guiScreen)
	{
		super(guiScreen, generateConfigList(), Caterpillar.MODID, true, false, Caterpillar.MODNAME);
	}

	public static List<IConfigElement> generateConfigList()
	{
		ArrayList elements = new ArrayList();

		elements.add(new ConfigElement(Config.configuration.getCategory("general")));
		elements.add(new ConfigElement(Config.configuration.getCategory("tutorial")));
		elements.add(new ConfigElement(Config.configuration.getCategory("advanced")));

		return elements;
	}
}
