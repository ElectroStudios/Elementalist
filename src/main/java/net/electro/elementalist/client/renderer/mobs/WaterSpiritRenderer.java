package net.electro.elementalist.client.renderer.mobs;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.electro.elementalist.Elementalist;
import net.electro.elementalist.client.models.mobs.WaterSpiritModel;
import net.electro.elementalist.entities.mobs.WaterSpiritEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class WaterSpiritRenderer extends GeoEntityRenderer<WaterSpiritEntity> {

    public WaterSpiritRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new WaterSpiritModel());
        this.shadowRadius = 0.3f;
    }

    @Override
    public ResourceLocation getTextureLocation(WaterSpiritEntity animatable) {
        return new ResourceLocation(Elementalist.MOD_ID, "textures/entities/mobs/water_spirit.png");
    }

    @Override
    public RenderType getRenderType(WaterSpiritEntity animatable, float partialTick, PoseStack poseStack, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, int packedLight, ResourceLocation texture) {
        return RenderType.entityTranslucent(texture);
    }
}
