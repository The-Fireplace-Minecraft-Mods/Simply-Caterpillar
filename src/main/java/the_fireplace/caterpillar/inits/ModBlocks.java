package the_fireplace.caterpillar.inits;

import net.minecraft.block.Block;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.blocks.*;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Caterpillar.MOD_ID);

    // Blocks
    public static final RegistryObject<Block> DRILL_BLADES = BLOCKS.register("drill_blades", DrillBlades::new);
    public static final RegistryObject<Block> DRILL_BASE = BLOCKS.register("drill_base", DrillBase::new);
    public static final RegistryObject<Block> DRILL_HEADS = BLOCKS.register("drill_heads", DrillHeads::new);
    public static final RegistryObject<Block> REINFORCEMENTS = BLOCKS.register("reinforcements", Reinforcements::new);
    public static final RegistryObject<Block> DECORATION = BLOCKS.register("decoration", Decoration::new);
    public static final RegistryObject<Block> COLLECTOR = BLOCKS.register("collector", Collector::new);
    public static final RegistryObject<Block> STORAGE = BLOCKS.register("storage", Storage::new);
    public static final RegistryObject<Block> INCINERATOR = BLOCKS.register("incinerator", Incinerator::new);
}
