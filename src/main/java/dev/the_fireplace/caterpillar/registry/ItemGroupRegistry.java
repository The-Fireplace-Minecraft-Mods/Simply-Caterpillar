package dev.the_fireplace.caterpillar.registry;

import dev.the_fireplace.caterpillar.Caterpillar;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ItemGroupRegistry {

    public static CreativeModeTab CATERPILLAR_TAB;


    public static void registerCreativeModeTab() {
        CATERPILLAR_TAB = Registry.register(
            BuiltInRegistries.CREATIVE_MODE_TAB,
            new ResourceLocation(Caterpillar.MOD_ID, "caterpillar"),
            FabricItemGroup.builder()
                .title(Component.translatable("itemGroup.simplycaterpillar.caterpillar"))
                .icon(() -> new ItemStack(BlockRegistry.DRILL_HEAD))
                .displayItems((parameters, output) -> {
                    output.accept(BlockRegistry.DRILL_BASE);
                    output.accept(BlockRegistry.DRILL_HEAD);
                    output.accept(BlockRegistry.DRILL_SEAT);
                    output.accept(BlockRegistry.COLLECTOR);
                    output.accept(BlockRegistry.REINFORCEMENT);
                    output.accept(BlockRegistry.INCINERATOR);
                    output.accept(BlockRegistry.STORAGE);
                    output.accept(BlockRegistry.DECORATION);
                    output.accept(BlockRegistry.TRANSPORTER);
                    output.accept(ItemRegistry.WRITABLE_PATTERN_BOOK);
                })
                .build()
        );
    }
}
