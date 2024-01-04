package dev.the_fireplace.caterpillar.registry;

import dev.the_fireplace.caterpillar.Caterpillar;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CreativeModeTabRegistry {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Caterpillar.MOD_ID);

    public static final RegistryObject<CreativeModeTab> CATERPILLAR_TAB = CREATIVE_MODE_TABS.register(Caterpillar.MOD_ID,
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(BlockRegistry.DRILL_HEAD.get()))
                    .title(Component.translatable("itemGroup." + Caterpillar.MOD_ID))
                    .displayItems((parameters, output) -> {
                        output.accept(BlockRegistry.DRILL_BASE.get());
                        output.accept(BlockRegistry.DRILL_HEAD.get());
                        output.accept(BlockRegistry.DRILL_SEAT.get());
                        output.accept(BlockRegistry.COLLECTOR.get());
                        output.accept(BlockRegistry.REINFORCEMENT.get());
                        output.accept(BlockRegistry.INCINERATOR.get());
                        output.accept(BlockRegistry.STORAGE.get());
                        output.accept(BlockRegistry.DECORATION.get());
                        output.accept(BlockRegistry.TRANSPORTER.get());
                        /**
                         * TODO: Re-enable when fixed
                         *
                         *  output.accept(ItemRegistry.WRITABLE_PATTERN_BOOK.get());
                         */
                    })
                    .build());


    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
