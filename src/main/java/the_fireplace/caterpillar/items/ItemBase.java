package the_fireplace.caterpillar.items;

import net.minecraft.item.Item;
import the_fireplace.caterpillar.Caterpillar;

public class ItemBase extends Item {

    public ItemBase() {
        super(new Item.Properties().group(Caterpillar.TAB));
    }
}
