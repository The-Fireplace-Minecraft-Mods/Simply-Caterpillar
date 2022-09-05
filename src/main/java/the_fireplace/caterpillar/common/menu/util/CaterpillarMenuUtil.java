package the_fireplace.caterpillar.common.menu.util;

import net.minecraft.world.item.ItemStack;

public class CaterpillarMenuUtil {
    public static boolean isFuel(ItemStack stack) {
        return net.minecraftforge.common.ForgeHooks.getBurnTime(stack, null) > 0;
    }
}
