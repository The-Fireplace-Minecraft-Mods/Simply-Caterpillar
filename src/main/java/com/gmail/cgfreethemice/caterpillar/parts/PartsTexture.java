package com.gmail.cgfreethemice.caterpillar.parts;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;

public class PartsTexture {
	protected ResourceLocation guiTexture;
	public int Y;
	public int X;
	public int Height;
	public int Width;
	public String Name;
	public PartsTexture (String Name, ResourceLocation guiTexture, int X, int Y, int Width, int Height)
	{
		this.Name = Name;
		this.guiTexture = guiTexture;
		this.Y = Y;
		this.X = X;
		this.Height = Height;
		this.Width = Width;
	}
	
}
