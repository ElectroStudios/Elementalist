package net.electro.elementalist.client.renderer.mobs;

import net.electro.elementalist.client.model.mobs.WaterSpiritModel;
import net.electro.elementalist.entity.mobs.WaterSpiritEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoEntityRenderer;


public class WaterSpiritRenderer extends GeoEntityRenderer<WaterSpiritEntity> {

    public WaterSpiritRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new WaterSpiritModel());
        this.shadowRadius = 0.3f;
    }

    @Override
    public ResourceLocation getTextureLocation(WaterSpiritEntity animatable) {
        return this.getGeoModel().getTextureResource(animatable);
    }

    @Override
    public RenderType getRenderType(WaterSpiritEntity animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return super.getRenderType(animatable, texture, bufferSource, partialTick);
    }
}
