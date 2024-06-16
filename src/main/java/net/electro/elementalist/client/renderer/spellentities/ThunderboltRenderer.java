package net.electro.elementalist.client.renderer.spellentities;

import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.electro.elementalist.entities.spells.MasterSpellEntity;
import net.electro.elementalist.entities.spells.lightning.ThunderboltEntity;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class ThunderboltRenderer extends SpellMasterRenderer<ThunderboltEntity> {
    public ThunderboltRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, 6, "textures/entities/thunderbolt/thunderbolt_");
    }


    @Override
    public void render(MasterSpellEntity pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        pPoseStack.pushPose();
        pPoseStack.mulPose(Axis.YP.rotationDegrees(-pEntity.getYRot() + 270));
        pPoseStack.mulPose(Axis.ZP.rotationDegrees(-pEntity.getXRot()));
        pPoseStack.mulPose(Axis.XP.rotationDegrees(pEntity.tickCount * 20));
        VertexConsumer vertexConsumer = pBuffer.getBuffer(RenderType.entityTranslucentEmissive(getTextureLocation(pEntity)));
        Matrix4f matrix4f = pPoseStack.last().pose();
        Matrix3f matrix3f = pPoseStack.last().normal();
        float range = ((ThunderboltEntity)pEntity).getDistance();
        vertexConsumer.vertex(matrix4f,0, 0, -0.5f).color(0xFFFFFFFF).uv(0, 2)
                .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(matrix3f, 0f, 1f, 0f).endVertex();
        vertexConsumer.vertex(matrix4f,0, 0, 0.5f).color(0xFFFFFFFF).uv(1, 2)
                .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(matrix3f, 0f, 1f, 0f).endVertex();
        vertexConsumer.vertex(matrix4f,range, 0, 0.5f).color(0xFFFFFFFF).uv(1, 0)
                .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(matrix3f, 0f, 1f, 0f).endVertex();
        vertexConsumer.vertex(matrix4f,range, 0, -0.5f).color(0xFFFFFFFF).uv(0, 0)
                .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(matrix3f, 0f, 1f, 0f).endVertex();
        pPoseStack.popPose();

        pPoseStack.pushPose();
        pPoseStack.mulPose(Axis.YP.rotationDegrees(-pEntity.getYRot() + 270));
        pPoseStack.mulPose(Axis.ZP.rotationDegrees(-pEntity.getXRot()));
        pPoseStack.mulPose(Axis.XP.rotationDegrees(pEntity.tickCount * 20 + 90));
        matrix4f = pPoseStack.last().pose();
        matrix3f = pPoseStack.last().normal();
        vertexConsumer.vertex(matrix4f,0, 0, -0.5f).color(0xFFFFFFFF).uv(0, 2)
                .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(matrix3f, 0f, 1f, 0f).endVertex();
        vertexConsumer.vertex(matrix4f,0, 0, 0.5f).color(0xFFFFFFFF).uv(1, 2)
                .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(matrix3f, 0f, 1f, 0f).endVertex();
        vertexConsumer.vertex(matrix4f,range, 0, 0.5f).color(0xFFFFFFFF).uv(1, 0)
                .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(matrix3f, 0f, 1f, 0f).endVertex();
        vertexConsumer.vertex(matrix4f,range, 0, -0.5f).color(0xFFFFFFFF).uv(0, 0)
                .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(matrix3f, 0f, 1f, 0f).endVertex();
        pPoseStack.popPose();
    }


    @Override
    public boolean shouldRender(MasterSpellEntity pLivingEntity, Frustum pCamera, double pCamX, double pCamY, double pCamZ) {
        return true;
    }
}
