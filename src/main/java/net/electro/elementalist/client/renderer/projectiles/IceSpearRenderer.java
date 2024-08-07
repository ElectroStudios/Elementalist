package net.electro.elementalist.client.renderer.projectiles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.electro.elementalist.Elementalist;
import net.electro.elementalist.client.model.projectiles.IceSpearModel;
import net.electro.elementalist.entity.projectiles.IceSpearProjectile;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class IceSpearRenderer extends EntityRenderer<IceSpearProjectile> {
    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(Elementalist.MOD_ID, "textures/entities/ice_spear.png");
    protected IceSpearModel model;
    public IceSpearRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        this.model = new IceSpearModel(pContext.bakeLayer(IceSpearModel.LAYER_LOCATION));
    }

    @Override
    public void render(IceSpearProjectile pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        pPoseStack.pushPose();
        this.model.setupAnim(pEntity, pPartialTick, 0, 0, 0, 0);
        pPoseStack.mulPose(Axis.YP.rotationDegrees(pEntity.getYRot() + 180));
        pPoseStack.mulPose(Axis.XP.rotationDegrees(pEntity.getXRot()));
        VertexConsumer vertexconsumer = pBuffer.getBuffer(this.model.renderType(TEXTURE_LOCATION));
        this.model.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        pPoseStack.popPose();
        super.render(pEntity, pEntityYaw, pPartialTick, pPoseStack, pBuffer, pPackedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(IceSpearProjectile pEntity) {
        return TEXTURE_LOCATION;
    }
}
