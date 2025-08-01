package dev.the_fireplace.caterpillar.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.the_fireplace.caterpillar.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import java.util.Optional;

public class SoundsRegistry {

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Constants.MOD_ID, Registries.SOUND_EVENT);

    public static final RegistrySupplier<SoundEvent> INCINERATOR_BURN = register("block_incinerator_burn");

    public static void init() {
        SOUNDS.register();
    }

    private static RegistrySupplier<SoundEvent> register(String name) {
        return register(Constants.getId(name));
    }

    private static RegistrySupplier<SoundEvent> register(ResourceLocation id) {
        return SOUNDS.register(id, () -> new SoundEvent(id, Optional.of(1.0F)));
    }
}
