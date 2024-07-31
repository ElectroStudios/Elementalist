package net.electro.elementalist.client.renderer.mobs;

import net.electro.elementalist.client.model.mobs.BakenekoModel;
import net.electro.elementalist.entity.mobs.BakenekoEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class BakenekoRenderer extends GeoEntityRenderer<BakenekoEntity> {

    public BakenekoRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BakenekoModel());
        this.shadowRadius = 0.8f;
       // this.addLayer(new LayerGlowingAreasGeo<>(this, getGeoModelProvider()::getTextureResource, getGeoModelProvider()::getModelResource, RenderType::eyes));

    }

    @Override
    public ResourceLocation getTextureLocation(BakenekoEntity animatable) {
        return this.getGeoModel().getTextureResource(animatable);
    }

    @Override
    public RenderType getRenderType(BakenekoEntity animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return super.getRenderType(animatable, texture, bufferSource, partialTick);
    }
}
