package net.electro.elementalist.client.renderer.spellentities;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.electro.elementalist.Elementalist;
import net.electro.elementalist.client.models.spellentities.MagicCircleModel;
import net.electro.elementalist.entities.spells.MagicCircleEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.object.Color;
import software.bernie.geckolib.renderer.GeoEntityRenderer;


public class MagicCircleRenderer extends GeoEntityRenderer<MagicCircleEntity> {

    protected final int FRAME_AMOUNT = 15;
    protected final ResourceLocation[] TEXTURE_LOCATIONS = new ResourceLocation[FRAME_AMOUNT];
    protected final String TEXTURE_LOCATION_STRING = "textures/entities/spellcircles/shieldcircle1/shield_circle1_";
    public MagicCircleRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new MagicCircleModel());
        setupTextures();
    }

//    @Override
//    public Color getRenderColor(MagicCircleEntity animatable, float partialTick, PoseStack poseStack, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, int packedLight) {
//        return Color.ofTransparent(animatable.getColor());
//    }

    @Override
    public RenderType getRenderType(MagicCircleEntity animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityTranslucentEmissive(texture);
    }

    @Override
    public void render(MagicCircleEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        poseStack.translate(0, 1.5f, 0);
        poseStack.mulPose(Axis.YP.rotationDegrees(-animatable.getYRot() + 180));
        poseStack.mulPose(Axis.XP.rotationDegrees(-animatable.getXRot()));
        poseStack.popPose();
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }


    @Override
    protected int getBlockLightLevel(MagicCircleEntity pEntity, BlockPos pPos) {
        return 15;
    }
    private void setupTextures() {
        for (int i = 0; i < FRAME_AMOUNT; i++) {
            TEXTURE_LOCATIONS[i] = new ResourceLocation(Elementalist.MOD_ID, TEXTURE_LOCATION_STRING + i + ".png");

        }
    }

    @Override
    public ResourceLocation getTextureLocation(MagicCircleEntity pEntity) {
        return TEXTURE_LOCATIONS[Math.min(pEntity.tickCount, FRAME_AMOUNT-1)];
    }
}
