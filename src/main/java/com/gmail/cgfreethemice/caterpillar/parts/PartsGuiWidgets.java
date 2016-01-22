package com.gmail.cgfreethemice.caterpillar.parts;

import org.lwjgl.input.Mouse;

import com.gmail.cgfreethemice.caterpillar.Caterpillar.GuiTabs;
import com.gmail.cgfreethemice.caterpillar.guis.GuiDrillHead;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;


public class PartsGuiWidgets{
	
	public int Y;
	public int X;
	public int Height;
	public int Width;
	public String Name;
	protected GuiDrillHead myGui;
	protected PartsTexture guiTextureA;
	protected PartsTexture guiTextureB;
    protected int xSize = 176;
    protected int ySize = 166;
    public double YPercentShownA = 1;
    public double YPercentShownB = 1;
	public boolean drawA;
	public boolean drawB;
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
	public void setTexture(PartsTexture guiTextureA, PartsTexture guiTextureB)
	{
		this.guiTextureA = guiTextureA;
		this.guiTextureB = guiTextureB;
	}

	public void drawGuiWidgets() {
		
		
		if (this.drawA)
		{
			int CustomH = (int) (this.guiTextureA.Height * this.YPercentShownA);
			
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			Minecraft.getMinecraft().getTextureManager().bindTexture(this.guiTextureA.guiTexture);
			this.myGui.drawTexturedModalRect( this.getGuiX() + this.X, this.getGuiY() + this.Y + this.guiTextureA.Height - CustomH, this.guiTextureA.X, this.guiTextureA.Y + this.guiTextureA.Height - CustomH, this.guiTextureA.Width, CustomH);
		}
		if (this.drawB)
		{
			int CustomH = (int) (this.guiTextureB.Height * this.YPercentShownB);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			Minecraft.getMinecraft().getTextureManager().bindTexture(this.guiTextureB.guiTexture);
			this.myGui.drawTexturedModalRect( this.getGuiX() + this.X, this.getGuiY() + this.Y + this.guiTextureB.Height - CustomH, this.guiTextureB.X, this.guiTextureB.Y + this.guiTextureB.Height - CustomH, this.guiTextureB.Width, CustomH);
		}
	}

	public void clickedGuiWidgets() {

		if (this.getMouseX() > this.getGuiX() +  this.X && this.getMouseX() <= this.getGuiX() + this.X + this.Width)
		{
			if (this.getMouseY() > this.getGuiY() + this.Y && this.getMouseY() <= this.getGuiY() + this.Y + this.Height)
			{
				this.myGui.guiWidgetClicked(this);	
			}
		}
			
	}

	public void hooverdGuiWidgets() {
		
		if (this.getMouseX() > this.getGuiX() +  this.X && this.getMouseX() <= this.getGuiX() + this.X + this.Width)
		{
			if (this.getMouseY() > this.getGuiY() + this.Y && this.getMouseY() <= this.getGuiY() + this.Y + this.Height)
			{
				this.myGui.guiWidgetHoover(this);	
			}
		}
		
	}
}
