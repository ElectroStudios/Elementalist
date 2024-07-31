package net.electro.elementalist.client.renderer.mobs;

import net.electro.elementalist.client.model.mobs.LesserDemonModel;
import net.electro.elementalist.entity.mobs.LesserDemonEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoEntityRenderer;


public class LesserDemonRenderer extends GeoEntityRenderer<LesserDemonEntity> {

    public LesserDemonRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new LesserDemonModel());
        this.shadowRadius = 0.3f;
//        this.addLayer(new LayerGlowingAreasGeo<>(this, getGeoModelProvider()::getTextureResource, getGeoModelProvider()::getModelResource, RenderType::eyes));

    }

    @Override
    public ResourceLocation getTextureLocation(LesserDemonEntity animatable) {
        return this.getGeoModel().getTextureResource(animatable);
    }

    @Override
    public RenderType getRenderType(LesserDemonEntity animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return super.getRenderType(animatable, texture, bufferSource, partialTick);
    }
}
