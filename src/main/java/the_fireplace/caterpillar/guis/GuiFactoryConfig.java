package the_fireplace.caterpillar.guis;

import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;

public class GuiFactoryConfig
implements IModGuiFactory
{
	@Override
	public IModGuiFactory.RuntimeOptionGuiHandler getHandlerFor(IModGuiFactory.RuntimeOptionCategoryElement element)
	{
		return null;
	}

	@Override
	public void initialize(Minecraft minecraftInstance)
	{
	}

	@Override
	public Class<? extends GuiScreen> mainConfigGuiClass()
	{
		return GuiConfiguration.class;
	}

	@Override
	public Set<IModGuiFactory.RuntimeOptionCategoryElement> runtimeGuiCategories()
	{
		return null;
	}
}