package the_fireplace.caterpillar.parts;

import the_fireplace.caterpillar.Caterpillar;

public class PartsTabs {
	public final int max = 8; //not used, but 8 is the max...;
	// I remember, it so that the containerDrillHead/tileentity can access what tab its on
	public Caterpillar.GuiTabs selected = Caterpillar.GuiTabs.MAIN;

	public PartsTabs()
	{

	}
}
