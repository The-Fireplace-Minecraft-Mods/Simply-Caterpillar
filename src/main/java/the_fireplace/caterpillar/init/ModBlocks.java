package the_fireplace.caterpillar.init;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.blocks.*;

/**
 * Holds a list of all our {@link Block}s.
 * Suppliers that create Blocks are added to the DeferredRegister.
 * The DeferredRegister is then added to our mod event bus in our constructor.
 * When the Block Registry Event is fired by Forge and it is time for the mod to
 * register its Blocks, our Blocks are created and registered by the DeferredRegister.
 * The Block Registry Event will always be called before the Item registry is filled.
 * Note: This supports registry overrides.
 *
 */
public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Caterpillar.MOD_ID);

    // Blocks
    public static final RegistryObject<Block> DRILL_BLADES = BLOCKS.register("drill_blades", () -> new DrillBlades(Block.Properties.create(Material.BARRIER).hardnessAndResistance(5.0f, 6.0f).harvestLevel(0).harvestTool(ToolType.PICKAXE).setRequiresTool()));
    public static final RegistryObject<Block> DRILL_BASE = BLOCKS.register("drill_base", () -> new DrillBase(Block.Properties.create(Material.ROCK).hardnessAndResistance(5.0f, 6.0f).sound(SoundType.STONE).harvestLevel(1).harvestTool(ToolType.PICKAXE).setRequiresTool()));
    public static final RegistryObject<Block> DRILL_HEADS = BLOCKS.register("drill_heads", () -> new DrillHeads(Block.Properties.create(Material.ROCK).hardnessAndResistance(5.0f, 6.0f).sound(SoundType.STONE).harvestLevel(1).harvestTool(ToolType.PICKAXE).setRequiresTool()));
    public static final RegistryObject<Block> REINFORCEMENTS = BLOCKS.register("reinforcements", () -> new Reinforcements(Block.Properties.create(Material.ROCK).hardnessAndResistance(5.0f, 6.0f).sound(SoundType.STONE).harvestLevel(1).harvestTool(ToolType.PICKAXE).setRequiresTool()));
    public static final RegistryObject<Block> DECORATION = BLOCKS.register("decoration", () -> new Decoration(Block.Properties.create(Material.ROCK).hardnessAndResistance(5.0f, 6.0f).sound(SoundType.STONE).harvestLevel(1).harvestTool(ToolType.PICKAXE).setRequiresTool()));
    public static final RegistryObject<Block> COLLECTOR = BLOCKS.register("collector", () -> new Collector(Block.Properties.create(Material.ROCK).hardnessAndResistance(5.0f, 6.0f).sound(SoundType.STONE).harvestLevel(1).harvestTool(ToolType.PICKAXE).setRequiresTool()));
    public static final RegistryObject<Block> STORAGE = BLOCKS.register("storage", () -> new Storage(Block.Properties.create(Material.ROCK).hardnessAndResistance(5.0f, 6.0f).sound(SoundType.STONE).harvestLevel(1).harvestTool(ToolType.PICKAXE).setRequiresTool()));
    public static final RegistryObject<Block> INCINERATOR = BLOCKS.register("incinerator", () -> new Incinerator(Block.Properties.create(Material.ROCK).hardnessAndResistance(5.0f, 6.0f).sound(SoundType.STONE).harvestLevel(1).harvestTool(ToolType.PICKAXE).setRequiresTool()));
}