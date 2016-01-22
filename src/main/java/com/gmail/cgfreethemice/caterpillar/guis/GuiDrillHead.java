package com.gmail.cgfreethemice.caterpillar.guis;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.lwjgl.input.Mouse;

import com.gmail.cgfreethemice.caterpillar.Caterpillar;
import com.gmail.cgfreethemice.caterpillar.Caterpillar.GuiTabs;
import com.gmail.cgfreethemice.caterpillar.Caterpillar.Replacement;
import com.gmail.cgfreethemice.caterpillar.Reference;
import com.gmail.cgfreethemice.caterpillar.containers.ContainerCaterpillar;
import com.gmail.cgfreethemice.caterpillar.containers.ContainerDrillHead;
import com.gmail.cgfreethemice.caterpillar.packets.PacketCaterpillarControls;
import com.gmail.cgfreethemice.caterpillar.parts.PartsGuiWidgets;
import com.gmail.cgfreethemice.caterpillar.parts.PartsTexture;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiDrillHead extends GuiContainer
{
	/** The player inventory bound to this GUI. */
	private final InventoryPlayer playerInventory;
	/** The inventory contained within the corresponding Dispenser. */
	public IInventory dispenserInventory;
	private ContainerCaterpillar catapillar;
	private HashMap<GuiTabs, List<PartsGuiWidgets>> widgetsHolder;
	private List<PartsGuiWidgets> selectedWidgets;
	
	public GuiDrillHead(EntityPlayer player, IInventory dispenserInv, ContainerCaterpillar DH)
	{
		super(new ContainerDrillHead(player, dispenserInv, DH));
		this.playerInventory = player.inventory;
		this.dispenserInventory = dispenserInv;
		this.widgetsHolder = new HashMap<Caterpillar.GuiTabs, List<PartsGuiWidgets>>();
		this.setupWidgets();
		this.catapillar = DH;
		this.catapillar.tabs.selected = GuiTabs.MAIN;
		this.sendUpdates();
	}
	public void setupWidgets()
	{
		try {
			for (GuiTabs tab: GuiTabs.values()) {
				List<PartsGuiWidgets> toAddWid =  new ArrayList<PartsGuiWidgets>();
				PartsGuiWidgets tmpAdding = null;
				PartsTexture guiTextureA = null;
				PartsTexture guiTextureB = null;
				switch (tab) {
				case MAIN://drill head
					tmpAdding = new PartsGuiWidgets("power",this,81 - 18, 45 + 17, 16, 16);
					guiTextureA = new PartsTexture("poweron", GuiTabs.MAIN.guiTextures, 176 + 14 + 16, 0 , 16, 16);
					guiTextureB = new PartsTexture("poweroff", GuiTabs.MAIN.guiTextures, 176 + 14, 0, 16, 16);			
					tmpAdding.setTexture(guiTextureA, guiTextureB);
					toAddWid.add(tmpAdding);
					
					tmpAdding = new PartsGuiWidgets("burner",this,81, 45,12, 14);
					guiTextureA = new PartsTexture("burner", GuiTabs.MAIN.guiTextures,176, 0, 12, 14);							
					tmpAdding.setTexture(guiTextureA, null);
					toAddWid.add(tmpAdding);
					
					int indexForBar = 2;
					tmpAdding = new PartsGuiWidgets("drag",this, 81 - 18, 58 - 52, 5, 42);
					guiTextureA = new PartsTexture("dragbase", GuiTabs.MAIN.guiTextures, 176 + (indexForBar *10), 12 + 56 - 52, 5, 42);
					guiTextureB = new PartsTexture("draginfo", GuiTabs.MAIN.guiTextures, 176 + (indexForBar *10) + 5, 12 + 3, 5, 42);			
					tmpAdding.setTexture(guiTextureA, guiTextureB);
					toAddWid.add(tmpAdding);
					
					break;
				case DECORATION://decoration
					tmpAdding = new PartsGuiWidgets("selected",this,13+12, 44, 18, 18);			
					toAddWid.add(tmpAdding);
					for (int i = 0; i < 9; i++) {
						tmpAdding = new PartsGuiWidgets("selection" + i,this,6 +18*i, 6, 18, 18);			
						toAddWid.add(tmpAdding);
					}

					
					
					
					break;
				case REINFORCEMENT://decoration
					
					tmpAdding = new PartsGuiWidgets("background",this, 0, -23, 176, 29);
					guiTextureA = new PartsTexture("background", GuiTabs.REINFORCEMENT.guiTextures, 0, 166 , 176, 29);	
					tmpAdding.setTexture(guiTextureA, null);
					tmpAdding.drawA = true;
					toAddWid.add(tmpAdding);
					
			 		for (int i = 0; i < 5; i++) {//Left												
						tmpAdding = new PartsGuiWidgets("check,1," + i,this,63 + 4*0 + 4*0, 35 - 18 + 4*i, 4, 4);
						guiTextureA = new PartsTexture("check0", GuiTabs.REINFORCEMENT.guiTextures, 176 + 0*4, 0 , 4, 4);
						guiTextureB = new PartsTexture("check1", GuiTabs.REINFORCEMENT.guiTextures, 176 + 1*4, 0, 4, 4);			
						tmpAdding.setTexture(guiTextureA, guiTextureB);
						toAddWid.add(tmpAdding);						
					}
			 		
					for (int i = 0; i < 5; i++) {//right
						tmpAdding = new PartsGuiWidgets("check,2," + i,this,51 + 58 + 4*0 + 4*0, 35 - 18 + 4*i, 4, 4);
						guiTextureA = new PartsTexture("check0", GuiTabs.REINFORCEMENT.guiTextures, 176 + 0*4, 0 , 4, 4);
						guiTextureB = new PartsTexture("check1", GuiTabs.REINFORCEMENT.guiTextures, 176 + 1*4, 0, 4, 4);			
						tmpAdding.setTexture(guiTextureA, guiTextureB);
						toAddWid.add(tmpAdding);
					}
					
					for (int i = 0; i < 5; i++) {//top
						tmpAdding = new PartsGuiWidgets("check,0," + i,this,57 + 23 + 4*i, 4*0, 4, 4);
						guiTextureA = new PartsTexture("check0", GuiTabs.REINFORCEMENT.guiTextures, 176 + 0*4, 0 , 4, 4);
						guiTextureB = new PartsTexture("check1", GuiTabs.REINFORCEMENT.guiTextures, 176 + 1*4, 0, 4, 4);			
						tmpAdding.setTexture(guiTextureA, guiTextureB);
						toAddWid.add(tmpAdding);
					}
					
					for (int i = 0; i < 5; i++) {//lower
						tmpAdding = new PartsGuiWidgets("check,3," + i,this,57 + 23 + 4*i, 12 + 34 + 4*0, 4, 4);
						guiTextureA = new PartsTexture("check0", GuiTabs.REINFORCEMENT.guiTextures, 176 + 0*4, 0 , 4, 4);
						guiTextureB = new PartsTexture("check1", GuiTabs.REINFORCEMENT.guiTextures, 176 + 1*4, 0, 4, 4);			
						tmpAdding.setTexture(guiTextureA, guiTextureB);
						toAddWid.add(tmpAdding);
					}
					
					
					break;
				}
				
				
				this.widgetsHolder.put(tab,toAddWid);
				
				
				
			}
			this.selectedWidgets = this.widgetsHolder.get(GuiTabs.MAIN);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public void guiWidgetClicked(PartsGuiWidgets widget)
	{
		if (widget.Name.equals("power"))
		{
			this.catapillar.running = !this.catapillar.running;
		}
		
		if (widget.Name.equals("burner"))
		{
			// do nothing
		}
		
		if (widget.Name.equals("drag"))
		{
			// do nothing
		}
		
		if (widget.Name.startsWith("selection"))
		{
			int index = Integer.parseInt(widget.Name.replace("selection", ""));
			index += 1;			
			this.catapillar.decoration.selected = index;
			this.catapillar.placeSlotsforDecorations(this.inventorySlots);
			this.dispenserInventory.markDirty();
			this.sendUpdates();
		}
		if (widget.Name.equals("selected"))
		{
			// do nothing
		}
		if (widget.Name.startsWith("check"))
		{
			String[] getinfo = widget.Name.split(",");
			int j = Integer.parseInt(getinfo[1]);
			int k = Integer.parseInt(getinfo[2]);
			if (this.catapillar.reinforcement.replacers.get(j)[k] == 0)
			{
				this.catapillar.reinforcement.replacers.get(j)[k] = 1;
			}
			else
			{
				this.catapillar.reinforcement.replacers.get(j)[k] = 0;
			}				
		}

		
	}
	public void guiWidgetHoover(PartsGuiWidgets widget)
	{
		
		List tmoString = new ArrayList<String>();
		
		if (widget.Name.equals("power"))
		{
			String powertxt = EnumChatFormatting.GREEN + StatCollector.translateToLocal("on");
			if (this.catapillar.running == false)
			{
				powertxt = EnumChatFormatting.RED + StatCollector.translateToLocal("off");
			}			
			tmoString.add(StatCollector.translateToLocal("power") + powertxt);			
		}
		
		if (widget.Name.equals("burner"))
		{
			int i1 = 0;
			int cbt1 = this.catapillar.burntime;
			if (cbt1 < 0)
			{
				cbt1 = 0;
			}
			if (this.catapillar.maxburntime > 0)
			{
				i1 = 100* (cbt1) / this.catapillar.maxburntime;
			}
			tmoString.add(StatCollector.translateToLocal("furnace") + i1 + "%");
		}
		if (widget.Name.equals("drag"))
		{
			String draggTxt = "" + EnumChatFormatting.GREEN + this.catapillar.drag.total + "/" + this.catapillar.drag.max;
			if (this.catapillar.drag.total > this.catapillar.drag.max * 0.5f)
			{
				draggTxt = "" + EnumChatFormatting.YELLOW + this.catapillar.drag.total + "/" + this.catapillar.drag.max;
			}
			if (this.catapillar.drag.total > this.catapillar.drag.max)
			{
				draggTxt = "" + EnumChatFormatting.RED + this.catapillar.drag.total + "/" + this.catapillar.drag.max;
			}
			tmoString.add(StatCollector.translateToLocal("drag") + draggTxt);
			tmoString.add(EnumChatFormatting.GRAY + StatCollector.translateToLocal("updatesaftermoving"));
		}
		if (widget.Name.startsWith("selection"))
		{
			int index = Integer.parseInt(widget.Name.replace("selection", ""));
			index += 1;
			tmoString.add(StatCollector.translateToLocal("clicktoselect")  + (index));
		}
		if (widget.Name.equals("selected"))
		{
			tmoString.add(StatCollector.translateToLocal("selected")  + (this.catapillar.decoration.selected));
		}
		if (widget.Name.startsWith("check"))
		{
			String[] getinfo = widget.Name.split(",");
			int j = Integer.parseInt(getinfo[1]);
			int k = Integer.parseInt(getinfo[2]);
			if (this.catapillar.reinforcement.replacers.get(j)[k] == 0)
			{
				tmoString.add(StatCollector.translateToLocal("wontreplace") + EnumChatFormatting.RED + Replacement.values()[k].name);
			}
			else
			{
				tmoString.add(StatCollector.translateToLocal("willreplace") + EnumChatFormatting.GREEN  + Replacement.values()[k].name);
			}
			
		}
		
		
		
		
		this.drawHoveringText(tmoString,widget.getMouseX() - widget.getGuiX(), widget.getMouseY() - widget.getGuiY());
	}
	
	/**
	 * Args : renderPartialTicks, mouseX, mouseY
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{



		
		switch (this.catapillar.tabs.selected) {
		case MAIN://drill head
			drawGuiDrillHeadBackgroundLayer();
			break;
		case DECORATION://decoration
			drawGuiDecorationBackgroundLayer();
			break;
		case REINFORCEMENT://REINFORCEMENT
			drawGuiReinforcementBackgroundLayer();
			break;
		default:
			drawGuiDrillHeadBackgroundLayer();
			break;
		}
		
		for (int i = 0; i < this.selectedWidgets.size(); i++) {
			PartsGuiWidgets widget = this.selectedWidgets.get(i);
			
			this.checkPower(widget);
			
			this.checkBurner(widget);
			
			this.checkDrag(widget);
			
			this.checkCheck(widget);
			
					
					
			
			widget.drawGuiWidgets();	
		}

		
		for (GuiTabs p : GuiTabs.values())
		{
			this.drawTabsBackground(p);
		}
	}
	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of the items). Args : mouseX, mouseY
	 */
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		for (GuiTabs p : GuiTabs.values())
		{
			this.drawTabsForeground(p);
		}
		
		switch (this.catapillar.tabs.selected) {
		case MAIN://drill head
			drawGuiDrillHeadForegroundLayer();
			break;
		case DECORATION://decoration
			drawGuiDecorationForegroundLayer();
			break;
		case REINFORCEMENT://decoration
			drawGuiReinforcementForegroundLayer();
			break;
		default:
			drawGuiDrillHeadForegroundLayer();
			break;
		}

		for (int i = 0; i < this.selectedWidgets.size(); i++) {
			PartsGuiWidgets widget = this.selectedWidgets.get(i);
			
			widget.hooverdGuiWidgets();	
		}
		
		
		for (GuiTabs p : GuiTabs.values())
		{
			this.drawTabsHover(p);
		}

	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		super.mouseClicked(mouseX, mouseY, mouseButton);

		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;		
		
		for (int i = 0; i < this.selectedWidgets.size(); i++) {
			PartsGuiWidgets widget = this.selectedWidgets.get(i);
			
			widget.clickedGuiWidgets();	
		}
		
		for (GuiTabs p : GuiTabs.values())
		{
			this.checkMouseClickTabs(mouseX, mouseY, k, l, p.value);
		}
	}
	
	private void checkCheck(PartsGuiWidgets widget) {
		if (widget.Name.startsWith("check"))
		{
			String[] getinfo = widget.Name.split(",");
			int j = Integer.parseInt(getinfo[1]);
			int k = Integer.parseInt(getinfo[2]);
			int whatcolor = this.catapillar.reinforcement.replacers.get(j)[k];
			if (whatcolor == 0)
			{
				widget.drawA = true;
				widget.drawB = false;
			}
			else
			{
				widget.drawA = false;
				widget.drawB = true;
			}
			
		}
	}
	private void checkDrag(PartsGuiWidgets widget) {
		if (widget.Name.equals("drag"))
		{
			int indexForBar = 2;
			double i1 = 0;
			int cbt1 = this.catapillar.drag.total;
			if (cbt1 < 0)
			{
				cbt1 = 0;
			}
			if (this.catapillar.drag.max < cbt1 && cbt1 > 0)
			{
				i1 = 1;
			}
			if (this.catapillar.drag.max > 0 &&  this.catapillar.drag.max >= cbt1)
			{
				i1 = (double)(cbt1) / (double)this.catapillar.drag.max;
			}
			widget.YPercentShownB = i1;
			widget.drawA = true;
			widget.drawB = true;
		}
	}
	private void checkBurner(PartsGuiWidgets widget) {
		if (widget.Name.equals("burner"))
		{
			widget.drawA = true;
			int ct = this.catapillar.burntime;
			if (ct < 0)
			{
				ct = 0;
				widget.drawA = false;
			}
			int maxBurn = this.catapillar.maxburntime;
			double total = (double)ct / (double)maxBurn;
			widget.YPercentShownA = total;
			
		}
	}
	private void checkPower(PartsGuiWidgets widget) {
		if (widget.Name.equals("power"))
		{
			if (this.catapillar.running)
			{
				widget.drawA = true;
				widget.drawB = false;
			}
			else
			{
				widget.drawA = false;
				widget.drawB = true;	
			}
		}
	}
	

	private void checkMouseClickTabs(int mouseX, int mouseY, int k, int l, int index) {
		//(k + this.xSize, l + 3 + index*20, 176, 58, 31, 20);
		int XSide = k - 31;
		int XWidth = 31;

		int YSide = l + 3 + index*20;
		int YHeight = 20;

		if (mouseX > XSide && mouseX < XSide + XWidth)
		{
			if (mouseY > YSide && mouseY < YSide + YHeight)
			{
				this.catapillar.tabs.selected = GuiTabs.values()[index];
				this.selectedWidgets = this.widgetsHolder.get(this.catapillar.tabs.selected);
				
				
				switch (this.catapillar.tabs.selected) {
				case MAIN://drill head
					this.catapillar.placeSlotsforMain(this.inventorySlots);
					this.sendUpdates();
					break;
				case DECORATION://decoration
					this.catapillar.decoration.selected = 0;
					this.catapillar.placeSlotsforDecorations(this.inventorySlots);
					this.sendUpdates();
					this.dispenserInventory.markDirty();
					break;
				case REINFORCEMENT://decoration
					this.catapillar.placeSlotsforReinforcements(this.inventorySlots);
					this.sendUpdates();
					this.dispenserInventory.markDirty();
					break;
				default:
					this.catapillar.placeSlotsforMain(this.inventorySlots);
					this.sendUpdates();
					break;
				}
			}
		}
	}
	private void drawTabsHover(GuiTabs p) {
		//(k + this.xSize, l + 3 + index*20, 176, 58, 31, 20);
		int i = Mouse.getX() * this.width / this.mc.displayWidth;
		int j = this.height - Mouse.getY() * this.height / this.mc.displayHeight - 1;
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;

		int XSide = k - 31;
		int XWidth = 31;

		int YSide = l + 3 + p.value*20;
		int YHeight = 20;

		if (i > XSide && i < XSide + XWidth)
		{
			if (j > YSide && j < YSide + YHeight)
			{
				List tmoString = new ArrayList<String>();
				tmoString.add(p.name);
				if (p.equals(GuiTabs.DECORATION) && this.catapillar.tabs.selected.equals(p))
				{
					tmoString.add(StatCollector.translateToLocal("switchtozero"));
				}
				this.drawHoveringText(tmoString, i - k, j - l);
			}
		}
	}
	private void drawTabsForeground(GuiTabs p) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(p.guiTextures);
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		String Caption = p.name;
		if (Caption.length() > 5)
		{
			Caption = Caption.substring(0, 3) + "...";
		}

		if (this.catapillar.tabs.selected.equals(p))
		{
			if (p.equals(GuiTabs.DECORATION))
			{
				this.fontRendererObj.drawString("  0",-31 + 1,  3 + p.value*20 + 5, Color.BLACK.getRGB());
			}
			else
			{
				this.fontRendererObj.drawString(Caption,-31 + 5 ,  3 + p.value*20 + 5, Color.BLACK.getRGB());
			}
		}
		else
		{
			this.fontRendererObj.drawString(Caption,-31 + 3,  3 + p.value*20 + 5, Color.GRAY.getRGB());
		}


	}
	private void drawTabsBackground(GuiTabs p) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(GuiTabs.MAIN.guiTextures);//has to be main, thats where the tab graphics are
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;


		if (this.catapillar.tabs.selected.equals(p))
		{
			this.drawTexturedModalRect(k - 31 + 3, l + 3 + p.value*20, 176, 58 + 20, 31, 20);
		}
		else
		{
			this.drawTexturedModalRect(k - 31, l + 3 + p.value*20, 176, 58, 31, 20);
		}
	}
	private void drawGuiReinforcementForegroundLayer() {
/*
		this.fontRendererObj.drawString(StatCollector.translateToLocal("ceiling"),  55 + 18, 3, Color.BLACK.getRGB());
		this.fontRendererObj.drawString(StatCollector.translateToLocal("leftwall"),  13 , 39, Color.BLACK.getRGB());
		this.fontRendererObj.drawString(StatCollector.translateToLocal("rightwall"),  123, 39, Color.BLACK.getRGB());

		this.fontRendererObj.drawString(StatCollector.translateToLocal("floor"),  55 + 20, 74, Color.BLACK.getRGB());
*/
	}	
	private void drawGuiDecorationForegroundLayer() {


		for (int i = 0; i < 9; i++) {
			int colort = Color.BLACK.getRGB();
			if (!this.catapillar.decoration.isInventoryEmpty(i + 1))
			{
				colort = Color.GREEN.getRGB();
			}
			if (this.catapillar.decoration.selected == i + 1)
			{
				colort = Color.BLUE.getRGB();
			}
			this.fontRendererObj.drawString("" + (i + 1), 13 + 18*i, 10, colort);
		}
		this.fontRendererObj.drawString(this.catapillar.decoration.selected + "", 13 + 18, 10 + 20*2, Color.BLUE.getRGB());

	}
	private void checkMouseClickSelection(int mouseX, int mouseY, int k, int l)
	{
		for (int index = 0; index < 9; index++) {

			int XSide = k + 4 + 18*index;
			int XWidth = 18;

			int YSide = l + 0 ;
			int YHeight = 20;

			if (mouseX > XSide && mouseX < XSide + XWidth)
			{
				if (mouseY > YSide && mouseY < YSide + YHeight)
				{
					this.catapillar.decoration.selected = index + 1;
					this.catapillar.placeSlotsforDecorations(this.inventorySlots);
					this.dispenserInventory.markDirty();
					this.sendUpdates();
					return;
				}
			}
		}
	}

	private void drawGuiReinforcementBackgroundLayer() {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(GuiTabs.REINFORCEMENT.guiTextures);
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;


		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
	}
	private void drawGuiDecorationBackgroundLayer() {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(GuiTabs.DECORATION.guiTextures);
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;


		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);



	}
	private void drawGuiDrillHeadForegroundLayer() {
		int where = (this.catapillar.storage.startingIndex - 1) / 3;
		
		for (int i = 0; i < 4; i++) {	
			where += 1;
			String s = where + "";
			this.fontRendererObj.drawString(s, 107, 12 + 18*i, 4210752);
		}

		this.fontRendererObj.drawString(StatCollector.translateToLocal("consumption"), 2, -8, Color.WHITE.getRGB());

		this.fontRendererObj.drawString(StatCollector.translateToLocal("gathered"), 120, -8, Color.WHITE.getRGB());

		int i = Mouse.getX() * this.width / this.mc.displayWidth;
		int j = this.height - Mouse.getY() * this.height / this.mc.displayHeight - 1;
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
	}


	private void drawGuiDrillHeadBackgroundLayer() {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(GuiTabs.MAIN.guiTextures);
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;


		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
	}



	@Override
	public void handleMouseInput() throws IOException
	{
		super.handleMouseInput();
		int speed = Mouse.getDWheel();


		if (speed != 0)
		{
			if (this.catapillar.tabs.selected.equals(GuiTabs.MAIN))
			{
				this.mouseWheelMoved(speed);
			}
		}

	}

	@Override
	public void onGuiClosed()
	{
		Reference.printDebug("GUI: Closing, " + this.catapillar.name);
		this.catapillar.tabs.selected = GuiTabs.MAIN;
		Caterpillar.network.sendToServer(new PacketCaterpillarControls(this.catapillar));

		Reference.printDebug("Closing: Saving...");
		Caterpillar.instance.saveNBTDrills();


		Caterpillar.instance.removeSelectedCaterpillar();


	}
	private void sendUpdates() {
		Reference.printDebug("GUI: Updateing Server, " + this.catapillar.name);
		boolean whatamI = this.catapillar.running;
		this.catapillar.running = false;
		Caterpillar.network.sendToServer(new PacketCaterpillarControls(this.catapillar));
		this.catapillar.running = whatamI;
	}

	public void mouseWheelMoved(int Speed)
	{
		if (Speed < 0)
		{
			this.catapillar.storage.startingIndex++;
			this.catapillar.storage.startingIndex++;
			this.catapillar.storage.startingIndex++;

			int Resetindex = ((this.catapillar.storage.added + ContainerCaterpillar.getMaxSize() - 1) / 2) - 11;

			if (Integer.compare(this.catapillar.storage.startingIndex, Resetindex)  >  0)
			{
				this.catapillar.storage.startingIndex = Resetindex;
			}
		}
		else
		{
			this.catapillar.storage.startingIndex--;
			this.catapillar.storage.startingIndex--;
			this.catapillar.storage.startingIndex--;

			if (this.catapillar.storage.startingIndex < 1)
			{
				this.catapillar.storage.startingIndex = 1;
			}
		}

		this.catapillar.updateScroll(this.inventorySlots);
	}
}