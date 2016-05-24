package the_fireplace.caterpillar.parts;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;
import the_fireplace.caterpillar.abstracts.AbstractRunnerWidgets;
import the_fireplace.caterpillar.guis.GuiDrillHead;

import java.awt.*;


public class PartsGuiWidgets{

	public boolean drawA;
	public boolean drawB;
	public boolean drawH = true;
	protected PartsTexture guiTextureA;
	protected PartsTexture guiTextureB;
	public int Height;
	protected GuiDrillHead myGui;

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
	public PartsGuiWidgets(String Name, GuiDrillHead myGui, int X, int Y,  int Width, int Height)
	{
		this.Name = Name;
		this.Y = Y;
		this.X = X;
		this.Height = Height;
		this.Width = Width;
		this.myGui = myGui;
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

			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.scale(this.ScaleXA, this.ScaleYA, 1);

			Minecraft.getMinecraft().getTextureManager().bindTexture(this.guiTextureA.guiTexture);
			this.myGui.drawTexturedModalRect( (int)((this.getGuiX() + this.X)/this.ScaleXA), (int)((this.getGuiY() + this.Y + this.guiTextureA.Height - CustomH)/this.ScaleYA), this.guiTextureA.X, this.guiTextureA.Y + this.guiTextureA.Height - CustomH, this.guiTextureA.Width, CustomH);


			GlStateManager.scale(1f/this.ScaleXA, 1f/this.ScaleYA, 1);
		}
		if (this.drawB)
		{
			int CustomH = (int) (this.guiTextureB.Height * this.YPercentShownB);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.scale(this.ScaleXB, this.ScaleYB, 1);
			Minecraft.getMinecraft().getTextureManager().bindTexture(this.guiTextureB.guiTexture);
			this.myGui.drawTexturedModalRect((int)((this.getGuiX() + this.X)/this.ScaleXB), (int)((this.getGuiY() + this.Y + this.guiTextureB.Height - CustomH)/this.ScaleYB), this.guiTextureB.X, this.guiTextureB.Y + this.guiTextureB.Height - CustomH, this.guiTextureB.Width, CustomH);
			GlStateManager.scale(1f/this.ScaleXB, 1f/this.ScaleYB, 1);
		}
	}
	public int getGuiX()
	{
		int k = (this.myGui.width - this.xSize) / 2;
		return k;
	}
	public int getGuiY()
	{
		int l = (this.myGui.height - this.ySize) / 2;
		return l;
	}
	public int getMouseX()
	{
		int i = Mouse.getX() * this.myGui.width / Minecraft.getMinecraft().displayWidth;
		return i;
	}

	public int getMouseY()
	{
		int j = this.myGui.height - Mouse.getY() * this.myGui.height / Minecraft.getMinecraft().displayHeight - 1;
		return j;
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
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				GlStateManager.scale(smallX, smallY, 1);
				for (int i = 0; i < newText.length; i++) {
					String placing = newText[i];
					this.myGui.getfontRendererObj().drawString(placing,(int)((this.X + 5)/smallX),  (int)((this.Y + this.Height/2  + (i-(float)newText.length/2)*4)/smallY) , Color.BLACK.getRGB());
				}

				GlStateManager.scale(1/smallX, 1/smallY, 1);
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
