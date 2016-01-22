package com.gmail.cgfreethemice.caterpillar;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class Config {

	public static Configuration configuration;

	public static boolean autoupdate;
	public static boolean useparticles;
	public static boolean debugversion;
	public static boolean breakbedrock;
	public static boolean enablesounds;

	public static float timeout;

	public static String[] curentupdate;

	public static void init(File SuggestedConfigurationFile)
	{

		configuration = new Configuration(SuggestedConfigurationFile);

		load();
		save();
	}

	public static void load()
	{
		configuration.load();

		autoupdate = configuration.getBoolean("autoupdate", "general", true, "Allow EP to autoupdate.");
		timeout = configuration.getFloat("timeout", "general", 1f, 0.1f, 5f, "How many minutes before auto update(if enabled) times out.");
		useparticles = configuration.getBoolean("use particles", "general", true, "Use particles for effect");
		breakbedrock = configuration.getBoolean("break bedrock", "general", false, "Can the drill break bedrock");
		enablesounds = configuration.getBoolean("enable sounds", "general", true, "Does the drill make noise when it moves?");

		String[] tmp = new String[1];
		tmp[0] = "Alpha release.";
		configuration.getStringList("0.0.18", "update notes",tmp , "changing these settings won't do anything.");

		tmp[0] = "Beta release.";
		configuration.getStringList("0.1.18", "update notes",tmp , "changing these settings won't do anything.");

		tmp = new String[7];
		tmp[0] = "Fixes: Major crash, that could corrupt saves.";
		tmp[1] = "Fixes: Other crashes.";
		tmp[2] = "Fixes: Drill not dropping when it's placed wrong.";
		tmp[3] = "Fixes: Config button not showing.";
		tmp[4] = "Fixes: When you break drillhead, items in inventory not droppping.";
		tmp[5] = "Changes the way the cat. moves (the correct way).";
		tmp[6] = "Adds: Particles.";
		configuration.getStringList("0.2.18", "update notes",tmp , "changing these settings won't do anything.");

		tmp = new String[16];
		tmp[0] = "Adds: Storage unit.";
		tmp[1] = "Adds: Labels on GUI.";
		tmp[2] = "Adds: Block names localized.";
		tmp[3] = "Adds: Drag.";
		tmp[4] = "Adds: More section the slow it moves.";
		tmp[5] = "Adds: More section the more it burns the fuel.";
		tmp[6] = "Adds: Tooltip for drag/burn time.";
		tmp[7] = "Adds: Decoration tab, designer.";
		tmp[8] = "Adds: Reinforcement tab, designer.";
		tmp[9] = "Updates: Recipes to use OreDictonary for more mod compatibility.";
		tmp[10] = "Fixes: Save bug.";
		tmp[11] = "Fixes: Caterpillar now has its own creative tab.";
		tmp[12] = "Fixes: Typos.";
		tmp[13] = "Fixes: Reinforcement placing cobblestone in tunnel.";
		tmp[14] = "Fixes: Drill head letting water/lava flow by.";
		tmp[15] = "Fixes: Now runs on multiplayer/servers.";

		configuration.getStringList("0.3.18", "update notes",tmp , "changing these setting won't do anything.");
		
		tmp = new String[1];
		tmp[0] = "What next";
		
		configuration.getStringList("0.4.18", "update notes",tmp , "changing these setting won't do anything.");
		curentupdate = tmp.clone();


		//configuration.getStringList("0.5.18", "update notes",tmp , "changing these setting won't do anything.");

	}
	public static void save()
	{
		configuration.save();
	}
}
