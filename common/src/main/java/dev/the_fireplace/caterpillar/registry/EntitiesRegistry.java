package dev.the_fireplace.caterpillar.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.the_fireplace.caterpillar.Constants;
import dev.the_fireplace.caterpillar.entity.SeatEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class EntitiesRegistry {
    private static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Constants.MOD_ID, Registries.ENTITY_TYPE);

    public static final RegistrySupplier<EntityType<SeatEntity>> SEAT = register("seat", EntityType.Builder.<SeatEntity>of((entityType, level) -> new SeatEntity(level), MobCategory.MISC).sized(0.0F, 0.0F));

    public static void init() {
        ENTITIES.register();
    }

    private static <T extends Entity> RegistrySupplier<EntityType<T>> register(String name, EntityType.Builder<T> builder) {
        return  ENTITIES.register(name, () -> builder.build(createEntityId(name)));
    }

    public static ResourceKey<EntityType<?>> createEntityId(String name) {
        return ResourceKey.create(Registries.ENTITY_TYPE, Constants.getId(name));
    }
}
