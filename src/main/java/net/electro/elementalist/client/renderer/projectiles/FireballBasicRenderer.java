package net.electro.elementalist.client.renderer.projectiles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.electro.elementalist.Elementalist;
import net.electro.elementalist.client.model.projectiles.FireballBasicModel;
import net.electro.elementalist.entity.projectiles.FireballBasicProjectile;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class FireballBasicRenderer extends EntityRenderer<FireballBasicProjectile> {
    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(Elementalist.MOD_ID, "textures/entities/fireball_basic.png");
    protected FireballBasicModel model;
    public FireballBasicRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        this.model = new FireballBasicModel(pContext.bakeLayer(FireballBasicModel.LAYER_LOCATION));
    }

    @Override
    public void render(FireballBasicProjectile pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        pPoseStack.pushPose();
        this.model.setupAnim(pEntity, pPartialTick, 0, 0, 0, 0);
        VertexConsumer vertexconsumer = pBuffer.getBuffer(this.model.renderType(TEXTURE_LOCATION));
        this.model.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        pPoseStack.popPose();
        super.render(pEntity, pEntityYaw, pPartialTick, pPoseStack, pBuffer, pPackedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(FireballBasicProjectile pEntity) {
        return TEXTURE_LOCATION;
    }
}
