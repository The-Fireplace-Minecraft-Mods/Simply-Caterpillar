package the_fireplace.caterpillar.parts;

import the_fireplace.caterpillar.guis.DrillHeadScreen;

public class PartsTabs {
	public final int max = 8; //not used, but 8 is the max...;
	// I remember, it so that the containerDrillHead/tileentity can access what tab its on
	public DrillHeadScreen.GUI_TABS selected = DrillHeadScreen.GUI_TABS.MAIN;

	public PartsTabs()
	{

	}
}
