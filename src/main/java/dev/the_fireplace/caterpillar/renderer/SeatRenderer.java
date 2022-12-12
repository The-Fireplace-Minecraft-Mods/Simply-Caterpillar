package dev.the_fireplace.caterpillar.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.the_fireplace.caterpillar.entity.SeatEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class SeatRenderer extends EntityRenderer<SeatEntity> {
    public SeatRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(SeatEntity seatEntity) {
        return null;
    }

    @Override
    protected void renderNameTag(SeatEntity entity, Component component, PoseStack stack, MultiBufferSource source, int light) {}
}
