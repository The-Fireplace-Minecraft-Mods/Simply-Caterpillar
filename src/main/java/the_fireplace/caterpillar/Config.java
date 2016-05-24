package the_fireplace.caterpillar;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.io.File;

public class Config {

	public static boolean firstuse;

	public static boolean breakbedrock;
	public static Configuration configuration;
	public static boolean enablesounds;

	public static boolean[] tutorial;

	public static boolean useparticles;

	public static void init(File SuggestedConfigurationFile)
	{
		configuration = new Configuration(SuggestedConfigurationFile);
		tutorial = new boolean[9];
		load();
		save();
	}
	public static void load()
	{
		configuration.load();

		firstuse  = configuration.getBoolean("firstuse", "general", true, "Show First Time Use Message?");
		useparticles = configuration.getBoolean("use particles", "general", true, "Use particles for effect");
		breakbedrock = configuration.getBoolean("break bedrock", "general", false, "Can the drill break bedrock");
		enablesounds = configuration.getBoolean("enable sounds", "general", true, "Does the drill make noise when it moves?");

		tutorial[0] = configuration.getBoolean("show tutorial 1", "tutorial", true, "Does this show the drill head tutorial?");
		tutorial[1] = configuration.getBoolean("show tutorial 2", "tutorial", true, "Does this show the coal tutorial?");
		tutorial[2] = configuration.getBoolean("show tutorial 3", "tutorial", true, "Does this show the power tutorial?");
		tutorial[3] = configuration.getBoolean("show tutorial 4", "tutorial", true, "Does this show the storage tutorial?");
		tutorial[4] = configuration.getBoolean("show tutorial 5", "tutorial", true, "Does this show the selection tutorial for the decoration?");
		tutorial[5] = configuration.getBoolean("show tutorial 6", "tutorial", true, "Does this show the selection zero tutorial for the decoration?");
		tutorial[6] = configuration.getBoolean("show tutorial 7", "tutorial", true, "Does this show the patter tutorial for the decoration");
		tutorial[7] = configuration.getBoolean("show tutorial 8", "tutorial", true, "Does this show the reinforcement tutorial 1");
		tutorial[8] = configuration.getBoolean("show tutorial 9", "tutorial", true, "Does this show the reinforcement tutorial 2");
	}

	public static void forceSave()
	{
		Property prop = configuration.get("general", "firstuse", true);
		prop.setValue(Config.firstuse);

		for (int i = 0; i < tutorial.length; i++) {
			int y = i  +1;
			prop = configuration.get("tutorial", "show tutorial " + y, true);
			prop.setValue(Config.tutorial[i]);
		}
		configuration.save();
	}

	public static void save()
	{
		configuration.save();
	}
}
