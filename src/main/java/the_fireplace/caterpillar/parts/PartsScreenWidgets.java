package the_fireplace.caterpillar.parts;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.gui.ScreenUtils;
import the_fireplace.caterpillar.abstracts.AbstractRunnerWidgets;
import the_fireplace.caterpillar.client.guis.DrillHeadScreen;


public class PartsScreenWidgets {

	public boolean drawA;
	public boolean drawB;
	public boolean drawH = true;
	protected PartsTexture guiTextureA;
	protected PartsTexture guiTextureB;
	public int Height;
	protected DrillHeadScreen drillHeadScreen;

	public String Name;
	public String text;
	public int Width;
	public int X;
	protected int xSize = 176;
	public int Y;
	public double YPercentShownA = 1;
	public double YPercentShownB = 1;

	public double ScaleXA = 1;
	public double ScaleXB = 1;

	public double ScaleYA = 1;
	public double ScaleYB = 1;
	public AbstractRunnerWidgets hooverrun;
	public AbstractRunnerWidgets hoovernotrun;
	public AbstractRunnerWidgets clicked;
	public AbstractRunnerWidgets beforeDraw;

	protected int ySize = 166;
	public PartsScreenWidgets(String Name, DrillHeadScreen drillHeadScreen, int X, int Y, int Width, int Height)
	{
		this.Name = Name;
		this.Y = Y;
		this.X = X;
		this.Height = Height;
		this.Width = Width;
		this.drillHeadScreen = drillHeadScreen;
		this.guiTextureA = null;
		this.guiTextureB = null;
		this.text = "";
		this.hooverrun = null;
		this.hoovernotrun = null;
		this.clicked = null;
		this.beforeDraw = null;
	}

	public void clickedGuiWidgets() {

		if (this.getMouseX() > this.getGuiX() +  this.X && this.getMouseX() <= this.getGuiX() + this.X + this.Width)
		{
			if (this.getMouseY() > this.getGuiY() + this.Y && this.getMouseY() <= this.getGuiY() + this.Y + this.Height)
			{
				if (this.clicked !=null)
				{
					this.clicked.run(this);
				}
			}
		}

	}
	public void drawGuiWidgets() {

		if (this.beforeDraw !=null)
		{
			this.beforeDraw.run(this);
		}

		if (this.drawA)
		{
			int CustomH = (int) (this.guiTextureA.Height * this.YPercentShownA);

			RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
			RenderSystem.scaled(this.ScaleXA, this.ScaleYA, 1);

			Minecraft.getInstance().getTextureManager().bindTexture(this.guiTextureA.guiTexture);
			ScreenUtils.drawTexturedModalRect( (int)((this.getGuiX() + this.X)/this.ScaleXA), (int)((this.getGuiY() + this.Y + this.guiTextureA.Height - CustomH)/this.ScaleYA), this.guiTextureA.X, this.guiTextureA.Y + this.guiTextureA.Height - CustomH, this.guiTextureA.Width, CustomH, 0f);


			RenderSystem.scaled(1f/this.ScaleXA, 1f/this.ScaleYA, 1);
		}
		if (this.drawB)
		{
			int CustomH = (int) (this.guiTextureB.Height * this.YPercentShownB);
			RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
			RenderSystem.scaled(this.ScaleXB, this.ScaleYB, 1);
			Minecraft.getInstance().getTextureManager().bindTexture(this.guiTextureB.guiTexture);
			ScreenUtils.drawTexturedModalRect((int)((this.getGuiX() + this.X)/this.ScaleXB), (int)((this.getGuiY() + this.Y + this.guiTextureB.Height - CustomH)/this.ScaleYB), this.guiTextureB.X, this.guiTextureB.Y + this.guiTextureB.Height - CustomH, this.guiTextureB.Width, CustomH, 0f);
			RenderSystem.scaled(1f/this.ScaleXB, 1f/this.ScaleYB, 1);
		}
	}
	public int getGuiX()
	{
		return (this.drillHeadScreen.width - this.xSize) / 2;
	}
	public int getGuiY()
	{
		return (this.drillHeadScreen.height - this.ySize) / 2;
	}
	public int getMouseX()
	{
		return (int)Minecraft.getInstance().mouseHelper.getMouseX() * this.drillHeadScreen.width / Minecraft.getInstance().currentScreen.width;
	}

	public int getMouseY()
	{
		return this.drillHeadScreen.height - (int)Minecraft.getInstance().mouseHelper.getMouseY() * this.drillHeadScreen.height / Minecraft.getInstance().currentScreen.height - 1;
	}
	public void drawforgroundlayer()
	{
		if (!this.text.equals(""))
		{
			String[] newText = this.text.split("\\\n");
			if (newText.length > 0)
			{
				float smallX = 1 / (float)newText.length;
				float smallY =  1 / (float)newText.length;
				RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
				RenderSystem.scaled(smallX, smallY, 1);
				for (int i = 0; i < newText.length; i++) {
					String placing = newText[i];
					// TODO: drawString need MatrixStack
					// this.drillHeadScreen.getFontRenderer().drawString(placing,(int)((this.X + 5)/smallX),  (int)((this.Y + this.Height/2  + (i-(float)newText.length/2)*4)/smallY) , Color.BLACK.getRGB());
				}

				RenderSystem.scaled(1/smallX, 1/smallY, 1);
			}
		}


	}
	public void hooverdGuiWidgets() {
		if (this.drawH)
		if (this.getMouseX() > this.getGuiX() +  this.X && this.getMouseX() <= this.getGuiX() + this.X + this.Width)
		{
			if (this.getMouseY() > this.getGuiY() + this.Y && this.getMouseY() <= this.getGuiY() + this.Y + this.Height)
			{
				if (this.hooverrun != null)
				{
					this.hooverrun.run(this);
				}
				return;
			}
		}
		if (this.hoovernotrun != null)
		{
			this.hoovernotrun.run(this);
		}
	}

	public void setTexture(PartsTexture guiTextureA, PartsTexture guiTextureB)
	{
		this.guiTextureA = guiTextureA;
		this.guiTextureB = guiTextureB;
	}
}
