package dev.the_fireplace.caterpillar.registry.client;

import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import dev.the_fireplace.caterpillar.registry.EntitiesRegistry;
import net.minecraft.client.renderer.entity.NoopRenderer;

public class EntityRenderersRegistry {
    public static void register() {
        EntityRendererRegistry.register(EntitiesRegistry.SEAT, NoopRenderer::new);
    }
}
