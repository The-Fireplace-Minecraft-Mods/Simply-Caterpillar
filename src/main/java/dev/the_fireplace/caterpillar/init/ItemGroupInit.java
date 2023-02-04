package dev.the_fireplace.caterpillar.init;

import dev.the_fireplace.caterpillar.Caterpillar;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ItemGroupInit {
    public static CreativeModeTab CATERPILLAR;

    public static void registerCreativeModeTab() {
        CATERPILLAR = FabricItemGroup.builder(new ResourceLocation(Caterpillar.MOD_ID, "caterpillar"))
                .title(Component.translatable("itemGroup.simplycaterpillar.caterpillar"))
                .icon(() -> new ItemStack(BlockInit.DRILL_HEAD))
                .build();
    }
}
