package net.electro.elementalist.client.renderer.spellentities;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.electro.elementalist.Elementalist;
import net.electro.elementalist.client.model.spellentities.ShieldSpellModel;
import net.electro.elementalist.entity.spells.ShieldSpellEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class ShieldSpellRenderer extends GeoEntityRenderer<ShieldSpellEntity> {

    protected final int FRAME_AMOUNT = 15;
    protected final ResourceLocation[] TEXTURE_LOCATIONS = new ResourceLocation[FRAME_AMOUNT];
    protected final String TEXTURE_LOCATION_STRING = "textures/entities/spellcircles/shieldcircle/shield_circle_";
    public ShieldSpellRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ShieldSpellModel());
        setupTextures();
    }

//    @Override
//    public Color getRenderColor(ShieldSpellEntity animatable, float partialTick, PoseStack poseStack, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, int packedLight) {
//        return Color.ofRGBA(255, 224, 209, 255);
//    }


    @Override
    public RenderType getRenderType(ShieldSpellEntity animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityTranslucentEmissive(texture);
    }

    @Override
    public void render(ShieldSpellEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        poseStack.translate(0, 1.5f, 0);
        poseStack.mulPose(Axis.YP.rotationDegrees(-animatable.getYRot() + 180));
        poseStack.mulPose(Axis.XP.rotationDegrees(-animatable.getXRot()));
        poseStack.popPose();
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    @Override
    protected int getBlockLightLevel(ShieldSpellEntity pEntity, BlockPos pPos) {
        return 15;
    }
    private void setupTextures() {
        for (int i = 0; i < FRAME_AMOUNT; i++) {
            TEXTURE_LOCATIONS[i] = new ResourceLocation(Elementalist.MOD_ID, TEXTURE_LOCATION_STRING + i + ".png");

        }
    }

    @Override
    public ResourceLocation getTextureLocation(ShieldSpellEntity pEntity) {
        return TEXTURE_LOCATIONS[Math.min(pEntity.tickCount, FRAME_AMOUNT-1)];
    }
}
