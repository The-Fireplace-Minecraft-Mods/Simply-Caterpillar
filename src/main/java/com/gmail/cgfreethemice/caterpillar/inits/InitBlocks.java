package com.gmail.cgfreethemice.caterpillar.inits;

import com.gmail.cgfreethemice.caterpillar.Caterpillar;
import com.gmail.cgfreethemice.caterpillar.Reference;
import com.gmail.cgfreethemice.caterpillar.blocks.BlockCollector;
import com.gmail.cgfreethemice.caterpillar.blocks.BlockDecoration;
import com.gmail.cgfreethemice.caterpillar.blocks.BlockDrillBase;
import com.gmail.cgfreethemice.caterpillar.blocks.BlockDrillHeads;
import com.gmail.cgfreethemice.caterpillar.blocks.BlockReinforcements;
import com.gmail.cgfreethemice.caterpillar.blocks.BlockStorage;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class InitBlocks {

	public static Block drillheads;
	public static Block reinforcements;
	public static Block decoration;
	public static Block collector;
	public static Block drillbase;
	public static Block storage;

	public static void init()
	{
		drillheads = new BlockDrillHeads().setUnlocalizedName("drillheads").setCreativeTab(Caterpillar.TabCaterpillar);
		reinforcements = new BlockReinforcements().setUnlocalizedName("reinforcements").setCreativeTab(Caterpillar.TabCaterpillar);
		decoration = new BlockDecoration().setUnlocalizedName("decoration").setCreativeTab(Caterpillar.TabCaterpillar);
		collector = new BlockCollector().setUnlocalizedName("collector").setCreativeTab(Caterpillar.TabCaterpillar);
		drillbase = new BlockDrillBase().setUnlocalizedName("drillbase").setCreativeTab(Caterpillar.TabCaterpillar);
		storage = new BlockStorage().setUnlocalizedName("storage").setCreativeTab(Caterpillar.TabCaterpillar);
	}

	public static void register() {
		GameRegistry.registerBlock(drillheads, drillheads.getUnlocalizedName().substring(5));
		GameRegistry.registerBlock(reinforcements, reinforcements.getUnlocalizedName().substring(5));
		GameRegistry.registerBlock(decoration, decoration.getUnlocalizedName().substring(5));
		GameRegistry.registerBlock(collector, collector.getUnlocalizedName().substring(5));
		GameRegistry.registerBlock(drillbase, drillbase.getUnlocalizedName().substring(5));
		GameRegistry.registerBlock(storage, storage.getUnlocalizedName().substring(5));
	}
	@SideOnly(Side.CLIENT)
	public static void registerRenders()
	{
		registerRender(drillheads);
		registerRender(reinforcements);
		registerRender(decoration);
		registerRender(collector);
		registerRender(drillbase);
		registerRender(storage);
	}
	@SideOnly(Side.CLIENT)
	public static void registerRender(Block block)
	{
		Item item = Item.getItemFromBlock(block);
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation(Reference.MODID + ":" + item.getUnlocalizedName().substring(5), "inventory"));
	}

}
