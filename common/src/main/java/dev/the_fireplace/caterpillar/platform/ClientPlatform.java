package dev.the_fireplace.caterpillar.platform;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class ClientPlatform {
    @ExpectPlatform
    public static <T extends Entity> void registerEntityRenderer(Supplier<EntityType<T>> type, EntityRendererProvider<T> renderProvider)  {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static <T extends Block> void registerRenderLayer(Supplier<T> block, RenderType type) {
        throw new AssertionError();
    }
}
