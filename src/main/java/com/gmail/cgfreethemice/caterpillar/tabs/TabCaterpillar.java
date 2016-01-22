package com.gmail.cgfreethemice.caterpillar.tabs;

import com.gmail.cgfreethemice.caterpillar.inits.InitBlocks;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

/**
 * @author The_Fireplace
 */
public class TabCaterpillar extends CreativeTabs {
	public TabCaterpillar() {
		super("caterpillar");
	}

	@Override
	public Item getTabIconItem() {
		return Item.getItemFromBlock(InitBlocks.drillbase);
	}
}