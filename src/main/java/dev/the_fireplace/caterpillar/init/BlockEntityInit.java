package dev.the_fireplace.caterpillar.init;

import dev.the_fireplace.caterpillar.Caterpillar;
import dev.the_fireplace.caterpillar.block.entity.DrillBaseBlockEntity;
import dev.the_fireplace.caterpillar.block.entity.DrillHeadBlockEntity;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class BlockEntityInit {

    public static BlockEntityType<DrillBaseBlockEntity> DRILL_BASE;

    public static BlockEntityType<DrillHeadBlockEntity> DRILL_HEAD;

    public static void registerBlockEntities() {
        DRILL_BASE = Registry.register(Registry.BLOCK_ENTITY_TYPE, new ResourceLocation(Caterpillar.MOD_ID, "drill_base"), BlockEntityType.Builder.of(DrillBaseBlockEntity::new, BlockInit.DRILL_BASE).build(null));

        DRILL_HEAD = Registry.register(Registry.BLOCK_ENTITY_TYPE, new ResourceLocation(Caterpillar.MOD_ID, "drill_head"), BlockEntityType.Builder.of(DrillHeadBlockEntity::new, BlockInit.DRILL_HEAD).build(null));
    }
}
