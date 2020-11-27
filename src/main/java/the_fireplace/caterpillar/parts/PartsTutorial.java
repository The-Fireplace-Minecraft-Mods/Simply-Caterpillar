package the_fireplace.caterpillar.parts;

import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.client.guis.DrillHeadScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PartsTutorial {

	public int Height;
	protected DrillHeadScreen drillHeadScreen;
	public String Name;
	public int Width;
	protected int xSize = 176;
	protected int ySize = 166;
	protected int x;
	protected int y;
	protected int ID;
	protected boolean drawUp = false;

	public PartsTutorial(String Name, int ID, DrillHeadScreen drillHeadScreen, int x, int y, boolean drawUp)
	{
		this.Name = Name;
		this.drillHeadScreen = drillHeadScreen;
		this.drawUp = drawUp;
		this.x = x;
		this.y = y;
		this.ID = ID;
	}
	public int getGuiX()
	{
		return (this.xSize) / 2;
	}
	public int getGuiY()
	{
		return (this.ySize) / 2;
	}
	public int getMouseX()
	{
		return (int)getMouseX() * this.drillHeadScreen.width / Minecraft.getInstance().currentScreen.width;
	}

	public int getMouseY()
	{
		return this.drillHeadScreen.height - (int)getMouseY() * this.drillHeadScreen.height / Minecraft.getInstance().currentScreen.height - 1;
	}
	private void drawDownArrow(List tmoString)
	{
		String ctm = System.currentTimeMillis() + "";
		ctm = ctm.substring(ctm.length() - 3);
		int y = Integer.parseInt(ctm);

		if (y > -1 &&  y < 250)
		{
			tmoString.add(TextFormatting.GREEN + "              \\/");
			tmoString.add(TextFormatting.GREEN + "                 ");
		}
		else if (y > 250 &&  y < 500)
		{
			tmoString.add(TextFormatting.GREEN + "              \\/");
			tmoString.add(TextFormatting.GREEN + "              \\/");
		}
		else
		{
			tmoString.add(TextFormatting.GREEN + "");
			tmoString.add(TextFormatting.GREEN + "");
		}
	}
	private void drawUpArrow(List tmoString)
	{
		String ctm = System.currentTimeMillis() + "";
		ctm = ctm.substring(ctm.length() - 3);
		int y = Integer.parseInt(ctm);
		if (y > -1 &&  y < 250)
		{
			tmoString.add(TextFormatting.GREEN + "                ");
			tmoString.add(TextFormatting.GREEN + "              /\\");

		}
		else if (y > 250 &&  y < 500)
		{
			tmoString.add(TextFormatting.GREEN + "              /\\");
			tmoString.add(TextFormatting.GREEN + "              /\\");
		}
		else
		{
			tmoString.add("");
			tmoString.add("");
		}
	}
	public boolean checkClicked() {

		int k = (this.drillHeadScreen.width - this.xSize) / 2;
		int l = (this.drillHeadScreen.height - this.ySize) / 2;
		k += this.xSize / 2;
		l += this.ySize / 2;
		k += 8;
		l -= 16;

		//Reference.printDebug(this.getMouseX() + "," + this.getMouseY() + "/" + (k + this.x)  + "," + (l + this.y));
		if (this.getMouseX() > k +  this.x && this.getMouseX() <= k + this.x + 50)
		{
			if (this.getMouseY() > l + this.y && this.getMouseY() <= l + this.y + 15)
			{
				// TODO: fix tutorial
				//TutorialConfig.[this.ID] = false;
				//Config.forceSave();

				return true;
			}
		}
		return false;
	}
	public void draw()
	{
		List tmoString = new ArrayList<String>();
		if (this.drawUp)
		{
			this.drawUpArrow(tmoString);
		}
		else
		{
			tmoString.add("");
		}

		tmoString.add(TextFormatting.RED +  "" + TextFormatting.BOLD +  Caterpillar.proxy.translateToLocal("tutorial"));
		String x = Caterpillar.proxy.translateToLocal(this.Name);
		String[] xs = x.split("\\\\n");
		int yindex = 0;
		Collections.addAll(tmoString, xs);
		if (!this.drawUp)
		{
			this.drawDownArrow(tmoString);
		}


		if (tmoString.size() > 0)
		{
			// TODO: fix drawHoveringText
			// GuiUtils.drawHoveringText(tmoString, this.getGuiX() + this.x, this.getGuiY() + this.y);

			tmoString = new ArrayList<String>();
			tmoString.add(TextFormatting.GOLD +  "" + TextFormatting.BOLD +  Caterpillar.proxy.translateToLocal("tutorial.dismiss"));
			// TODO: fix drawHoveringText
			//GuiUtils.drawHoveringText(tmoString, this.getGuiX() + this.x, this.getGuiY() + this.y);
		}
	}

}
