package com.gmail.cgfreethemice.caterpillar.timers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import com.gmail.cgfreethemice.caterpillar.Caterpillar;
import com.gmail.cgfreethemice.caterpillar.Config;
import com.gmail.cgfreethemice.caterpillar.Reference;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;

public class TimerMain  extends TimerTask{

	private boolean onlyrunOnce = false;
	public boolean inSetup = false;
	private boolean savedyet = false;
	private long runtime = 0;
	private long starttime = 0;
	private List<Integer[]> movment= new ArrayList<Integer[]>();
	@Override
	public void run() {

		long thissecond = System.currentTimeMillis();
		long elapsed = thissecond - this.runtime;
		if (this.runtime == 0)
		{
			elapsed = 0;
		}
		this.runtime = thissecond;
		long total = thissecond - this.starttime;

		//Config.debugversion = true;
		if (Config.autoupdate && this.onlyrunOnce == false)
		{
			this.onlyrunOnce = true;
			this.getUpdate();
		}

		this.checkReady();


		if (Reference.Loaded == true)
		{
			if (Reference.checkLoaded())
			{
				Caterpillar.instance.reset();
				return;

			}
			this.runningTickfromMod(elapsed, total);
		}

	}
	public void setTime()
	{
		this.runtime = System.currentTimeMillis();
		this.starttime = System.currentTimeMillis();
	}
	private void checkReady() {
		/*if (Reference.theWorldServer() != null)
		{
			if (Reference.theWorldServer().loadedEntityList != null)
			{
				if (Reference.theWorldServer().loadedEntityList.size() > 0)
				{
					if (Reference.Loaded == false)
					{
						if (this.inSetup == false)
						{
							this.inSetup = true;
							this.worldLoadedfromMod();
							Reference.Loaded = true;
							Reference.printDebug("Mod is running! Server");
						}
					}

				}
			}
		}*/
		//Reference.thePlayerClient() != null &&
		if ( Reference.theWorldClient() != null )
		{
			if (Reference.theWorldClient().loadedEntityList != null)
			{
				if (Reference.theWorldClient().loadedEntityList.size() > 0)
				{
					//Reference.printDebug("Mod is running! 3");
					if (Reference.Loaded == false)
					{
						if (this.inSetup == false)
						{
							this.inSetup = true;
							this.worldLoadedfromMod();
							Reference.Loaded = true;



							Reference.getupdateenderpoison(Reference.url_running, "running.png");
							Reference.printDebug("Mod is running!");
						}
					}

				}
			}
		}
	}

	private void getUpdate() {
		//Config.debugversion = true;
		File versionsDir = new File(Reference.MainNBT.getFolderLocationMod() + "update.dat");
		if (versionsDir.exists())
		{
			try {
				Reference.printInfo("********************" + Reference.Name + " Update Start*******************");
				Reference.MainNBT.FileName = "update.dat";
				NBTTagCompound updateinfo =  Reference.MainNBT.readNBTSettings(Reference.MainNBT.getFolderLocationMod());


				versionsDir.delete();

				String url = updateinfo.getString("url");
				String date = updateinfo.getString("date");
				String version = updateinfo.getString("version");
				String name = updateinfo.getString("name");								

				String[] sversion = version.split("\\.");
				int nversion = Integer.valueOf(sversion[0]) * 1000;
				nversion = nversion + Integer.valueOf(sversion[1]);

				String thisversion = Reference.VERSION;
				String[] sthisversion = thisversion.split("\\.");

				int nsthisversion = Integer.valueOf(sthisversion[0]) * 1000;
				nsthisversion = nsthisversion + Integer.valueOf(sthisversion[1]);
				if (nversion > nsthisversion)
				{
					Reference.getupdateenderpoison(url, name);
					Reference.getupdateenderpoison(Reference.url_count, "data.png");
					runUpdateJAR(version);


					Reference.downloadAvailable = true;
					Reference.printInfo("Update Completed, waiting on you to close minecraft to finish!!");

				}
				else
				{
					Reference.printInfo("No updates found, last updated: " + date);
					if (nversion < nsthisversion)
					{
						Config.debugversion = true;
						Reference.printDebug("Debug Version: Enabled");
					}
					else
					{
						Config.debugversion = false;
					}
				}
				Reference.printInfo("********************" + Reference.Name + " Update Stop*******************");
			} catch (Exception e) {

				//e.printStackTrace();
			}
		}
	}
	private void runUpdateJAR(String NewVersion) {
		try {
			String modDir = Reference.MainNBT.getFolderLocationMod();

			ProcessBuilder pb = new ProcessBuilder("java", "-jar", "FileMover.jar");
			pb.directory(new File(modDir));
			pb.redirectOutput(new File(modDir, "Log-" + Reference.VERSION + " TO " + NewVersion + ".txt"));
			Process p = pb.start();
			//p.getOutputStream().

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void runningTickfromMod(long elapsedTime, long totalTime)
	{
		if (Reference.Loaded)
		{
			if (Caterpillar.proxy.isServer() == false)
			{
				if (Minecraft.getMinecraft().currentScreen != null)
				{
					if (savedyet == false)
					{
						savedyet = true;
						Reference.printDebug("Menu: Saving...");
						Caterpillar.instance.saveNBTDrills();
					}
				}
				else
				{
					savedyet = false;
				}
			}

			Caterpillar.instance.saveCount++;
			if (Caterpillar.instance.saveCount > 6000)
			{
				Caterpillar.instance.saveCount = 0;

				if (Caterpillar.proxy.isServer())
				{
					if (Config.autoupdate)
					{
						this.getUpdate();
					}
				}


				/*	Reference.printDebug("Timer: Saving...");
				Caterpillar.instance.saveNBTDrills();*/
			}
		}
	}
	public void worldLoadedfromMod() {
		// only run once, when the world loads.
		Reference.printDebug("World Loaded, starting mod!");
		if(Reference.thePlayerClient() != null){//Client side
			if (Reference.downloadAvailable == true)
			{
				Reference.thePlayerClient().addChatComponentMessage(Reference.format(EnumChatFormatting.RED,"*********************************************************"));
				Reference.thePlayerClient().addChatComponentMessage(Reference.format(EnumChatFormatting.DARK_PURPLE, Reference.Name + " has an update."));
				Reference.thePlayerClient().addChatComponentMessage(Reference.format(EnumChatFormatting.DARK_PURPLE, "It has been downloaded for you."));
				Reference.thePlayerClient().addChatComponentMessage(Reference.format(EnumChatFormatting.DARK_PURPLE, "All you need to do is exit minecraft, and restart it."));
				Reference.thePlayerClient().addChatComponentMessage(Reference.format(EnumChatFormatting.DARK_PURPLE,"This will finish the installation."));
				Reference.thePlayerClient().addChatComponentMessage(Reference.format(EnumChatFormatting.RED,"*********************************************************"));
			}
			else if (Reference.thisVersionRan() == false)
			{
				Reference.thePlayerClient().addChatComponentMessage(Reference.format(EnumChatFormatting.GREEN,"*********************************************************"));
				Reference.thePlayerClient().addChatComponentMessage(Reference.format(EnumChatFormatting.DARK_PURPLE, "Patch notes for " + Reference.Name + "-" + Reference.VERSION));
				for (int i = 0; i < Config.curentupdate.length; i++) {
					Reference.thePlayerClient().addChatComponentMessage(Reference.format(EnumChatFormatting.DARK_PURPLE, i + ": " + Config.curentupdate[i]));
				}


				Reference.thePlayerClient().addChatComponentMessage(Reference.format(EnumChatFormatting.GREEN,"*********************************************************"));
			}
		}else{//Server side
			if (Reference.downloadAvailable == true)
			{
				Reference.printInfo("*********************************************************");
				Reference.printInfo(Reference.Name + " has an update.");
				Reference.printInfo("It has been downloaded for you.");
				Reference.printInfo("All you need to do is exit minecraft, and restart it.");
				Reference.printInfo("This will finish the installation.");
				Reference.printInfo("*********************************************************");
			}
			else if (Reference.thisVersionRan() == false)
			{
				Reference.printInfo("*********************************************************");
				Reference.printInfo("Patch notes for " + Reference.Name + "-" + Reference.VERSION);
				for (int i = 0; i < Config.curentupdate.length; i++) {
					Reference.printInfo(i + ": " + Config.curentupdate[i]);
				}

				Reference.printInfo("*********************************************************");
			}
		}

		Caterpillar.instance.readNBTDrills();

		Caterpillar.instance.ModTasks.setTime();


		Reference.printDebug("World Loaded, finished!");
	}

}
