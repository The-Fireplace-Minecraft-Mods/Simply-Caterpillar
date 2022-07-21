package the_fireplace.caterpillar.init;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import the_fireplace.caterpillar.Caterpillar;

public class CreativeModeTabsInit {

    public static final CreativeModeTab CREATIVE_MODE_TAB = new CreativeModeTab(Caterpillar.MOD_ID) {
        @Override
        public ItemStack makeIcon() {
            return ItemInit.DRILL_BASE_ITEM.get().getDefaultInstance();
        }
    };
}
