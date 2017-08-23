package the_fireplace.caterpillar.tabs;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.inits.InitBlocks;

/**
 * @author The_Fireplace
 */
public class TabCaterpillar extends CreativeTabs {
	public TabCaterpillar() {
		super(Caterpillar.MODID);
	}

	@Override
	public ItemStack getTabIconItem() {
		return new ItemStack(Item.getItemFromBlock(InitBlocks.drillbase));
	}
}