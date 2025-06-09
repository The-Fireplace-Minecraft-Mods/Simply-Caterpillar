package dev.the_fireplace.caterpillar.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.the_fireplace.caterpillar.Constants;
import dev.the_fireplace.caterpillar.block.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Supplier;

public class BlocksRegistry {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Constants.MOD_ID, Registries.BLOCK);

    public static final RegistrySupplier<DrillBaseBlock> DRILL_BASE = registerBlock("drill_base", () -> new DrillBaseBlock(baseProperties("drill_base")));
    public static final RegistrySupplier<DrillHeadBlock> DRILL_HEAD = registerBlock("drill_head", () -> new DrillHeadBlock(baseProperties("drill_head")));
    public static final RegistrySupplier<DrillSeatBlock> DRILL_SEAT = registerBlock("drill_seat", () -> new DrillSeatBlock(baseProperties("drill_seat")));
    public static final RegistrySupplier<CollectorBlock> COLLECTOR = registerBlock("collector", () -> new CollectorBlock(baseProperties("collector")));
    public static final RegistrySupplier<ReinforcementBlock> REINFORCEMENT = registerBlock("reinforcement", () -> new ReinforcementBlock(baseProperties("reinforcement")));
    public static final RegistrySupplier<StorageBlock> STORAGE = registerBlock("storage", () -> new StorageBlock(baseProperties("storage")));
    public static final RegistrySupplier<IncineratorBlock> INCINERATOR = registerBlock("incinerator", () -> new IncineratorBlock(baseProperties("incinerator")));
    public static final RegistrySupplier<DecorationBlock> DECORATION = registerBlock("decoration", () -> new DecorationBlock(baseProperties("decoration")));
    public static final RegistrySupplier<TransporterBlock> TRANSPORTER = registerBlock("transporter", () -> new TransporterBlock(baseProperties("transporter")));

    public static void init() {
        BLOCKS.register();
    }

    public static <T extends Block> RegistrySupplier<T> registerBlock(String name, Supplier<T> block) {
        return BLOCKS.register(Constants.getId(name), block);
    }

    public static ResourceKey<Block> createBlockId(String name) {
        return ResourceKey.create(Registries.BLOCK, Constants.getId(name));
    }

    public static BlockBehaviour.Properties baseProperties(String name){
        return BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).sound(SoundType.STONE).setId(createBlockId(name));
    }
}
