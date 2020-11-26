package the_fireplace.caterpillar.blocks;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.tabs.CaterpillarTab;

public class BlockItemBase extends BlockItem {

    public BlockItemBase(Block blockIn) {
        super(blockIn, new Item.Properties().group(CaterpillarTab.CATERPILLAR_TAB));
    }
}
