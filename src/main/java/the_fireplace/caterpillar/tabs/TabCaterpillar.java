package the_fireplace.caterpillar.tabs;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.inits.InitBlocks;

import javax.annotation.Nonnull;

/**
 * @author The_Fireplace
 */
public class TabCaterpillar extends CreativeTabs {
	public TabCaterpillar() {
		super(Caterpillar.MODID);
	}

	@Override
	@Nonnull
	public Item getTabIconItem() {
		return Item.getItemFromBlock(InitBlocks.drillbase);
	}
}