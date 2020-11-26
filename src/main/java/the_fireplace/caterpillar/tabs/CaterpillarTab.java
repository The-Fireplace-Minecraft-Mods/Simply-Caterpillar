package the_fireplace.caterpillar.tabs;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import the_fireplace.caterpillar.inits.ModBlocks;

public class CaterpillarTab extends ItemGroup {
    public static final CaterpillarTab CATERPILLAR_TAB = new CaterpillarTab(ItemGroup.GROUPS.length, "caterpillarTab");

    private CaterpillarTab(int index, String label)
    {
        super(index, label);
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(ModBlocks.DRILL_BASE.get());
    }
}
