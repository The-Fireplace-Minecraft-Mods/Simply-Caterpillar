package the_fireplace.caterpillar.timers;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.Config;
import the_fireplace.caterpillar.Reference;

import java.util.TimerTask;

public class TimerMain  extends TimerTask{

	public boolean inSetup = false;
	private boolean onlyrunOnce = false;
	private long runtime = 0;
	private boolean savedyet = false;
	private long starttime = 0;
	private void checkReady() {
		if (Caterpillar.proxy.getWorld() != null )
		{
			if (Caterpillar.proxy.getWorld().loadedEntityList != null)
			{
				if (Caterpillar.proxy.getWorld().loadedEntityList.size() > 0)
				{
					if (!Reference.Loaded)
					{
						if (!this.inSetup)
						{
							this.inSetup = true;
							this.worldLoadedfromMod();
							Reference.Loaded = true;
							Reference.printDebug("Mod is running!");
						}
					}

				}
			}
		}
	}

	@Override
	public void run() {
		long thissecond = System.currentTimeMillis();
		this.runtime = thissecond;

		if (!this.onlyrunOnce)
		{
			this.onlyrunOnce = true;
		}

		this.checkReady();


		if (Reference.Loaded)
		{
			if (Reference.checkLoaded())
			{
				Caterpillar.instance.reset();
				return;

			}
			this.runningTickfromMod();
		}

	}

	public void runningTickfromMod()
	{
		if (Reference.Loaded)
		{
			if (!Caterpillar.proxy.isServer())
			{
				if (Minecraft.getMinecraft().currentScreen != null)
				{
					if (!this.savedyet)
					{
						this.savedyet = true;
						Reference.printDebug("Menu: Saving...");
						Caterpillar.instance.saveNBTDrills();
					}
				}
				else
				{
					if (!Caterpillar.proxy.isServer())
					{
						if (Config.firstuse)
						{
							this.callguiScreen();
						}
					}
					this.savedyet = false;
				}
			}

			Caterpillar.instance.saveCount++;
			if (Caterpillar.instance.saveCount > 6000)
			{
				Caterpillar.instance.saveCount = 0;
			}
		}
	}
	@SideOnly(Side.CLIENT)
	private void callguiScreen() {
		//Minecraft.getMinecraft().displayGuiScreen(new GuiMessage());
	}
	public void setTime()
	{
		this.runtime = System.currentTimeMillis();
		this.starttime = System.currentTimeMillis();
	}
	public void worldLoadedfromMod() {
		Reference.printDebug("World Loaded, starting mod!");

		Caterpillar.instance.readNBTDrills();

		Caterpillar.instance.clearOldBarrierBlocks();

		Caterpillar.instance.ModTasks.setTime();

		Reference.printDebug("World Loaded, finished!");
	}

}
