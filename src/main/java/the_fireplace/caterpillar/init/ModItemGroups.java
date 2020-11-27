package the_fireplace.caterpillar.init;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import the_fireplace.caterpillar.Caterpillar;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class ModItemGroups {

    public static final ItemGroup MOD_ITEM_GROUP = new ModItemGroup(Caterpillar.MOD_ID, () -> new ItemStack(ModBlocks.DRILL_BASE.get()));

    public static final class ModItemGroup extends ItemGroup {

        @Nonnull
        private final Supplier<ItemStack> iconSupplier;

        public ModItemGroup(@Nonnull final String name, @Nonnull final Supplier<ItemStack> iconSupplier) {
            super(name);
            this.iconSupplier = iconSupplier;
        }

        @Override
        @Nonnull
        public ItemStack createIcon() {
            return iconSupplier.get();
        }

    }

}
