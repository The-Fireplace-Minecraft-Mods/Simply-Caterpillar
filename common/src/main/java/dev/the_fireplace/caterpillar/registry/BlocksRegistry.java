package dev.the_fireplace.caterpillar.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.the_fireplace.caterpillar.Constants;
import dev.the_fireplace.caterpillar.block.DrillBaseBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

import java.util.function.Supplier;

public class BlocksRegistry {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Constants.MOD_ID, Registries.BLOCK);

    public static final RegistrySupplier<Block> DRILL_BASE = registerBlock("drill_base", () -> new DrillBaseBlock(baseProperties("drill_base").mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));

    public static void init() {
        BLOCKS.register();
    }

    public static RegistrySupplier<Block> registerBlock(String name, Supplier<Block> block) {
        return BLOCKS.register(Constants.getId(name), block);
    }

    public static ResourceKey<Block> createBlockId(String name) {
        return ResourceKey.create(Registries.BLOCK, Constants.getId(name));
    }

    public static BlockBehaviour.Properties baseProperties(String name){
        return BlockBehaviour.Properties.of().setId(createBlockId(name));
    }
}
