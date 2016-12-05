package the_fireplace.caterpillar.timers;

import net.minecraft.client.Minecraft;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.Reference;

import java.util.TimerTask;

public class TimerMain extends TimerTask{

	public boolean inSetup = false;
	private boolean onlyrunOnce = false;
	private boolean savedyet = false;
	private void checkReady() {
		if (Caterpillar.proxy.getWorld() != null )
		{
			if (Caterpillar.proxy.getWorld().loadedEntityList != null)
			{
				if (Caterpillar.proxy.getWorld().loadedEntityList.size() > 0)
				{
					if (!Reference.loaded)
					{
						if (!this.inSetup)
						{
							this.inSetup = true;
							this.worldLoadedfromMod();
							Reference.loaded = true;
							Reference.printDebug("Mod is running!");
						}
					}

				}
			}
		}
	}

	@Override
	public void run() {
		if (!this.onlyrunOnce)
		{
			this.onlyrunOnce = true;
		}

		this.checkReady();


		if (Reference.loaded)
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
		if (Reference.loaded)
		{
			if (!Caterpillar.proxy.isServerSide())
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
	public void worldLoadedfromMod() {
		Reference.printDebug("World loaded, starting mod!");

		Caterpillar.instance.readNBTDrills();

		Reference.printDebug("World loaded, finished!");
	}

}
