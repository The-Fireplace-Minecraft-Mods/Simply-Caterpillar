package dev.the_fireplace.caterpillar.init;

import dev.the_fireplace.caterpillar.Caterpillar;
import dev.the_fireplace.caterpillar.entity.SeatEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import org.intellij.lang.annotations.Identifier;

public class EntityInit {

    public static final EntityType<SeatEntity> SEAT = Registry.register(Registry.ENTITY_TYPE, new ResourceLocation(Caterpillar.MOD_ID, "seat"),
            FabricEntityTypeBuilder.create(MobCategory.MISC, SeatEntity::new).dimensions(EntityDimensions.fixed(0.0F, 0.0F)).build());
}

