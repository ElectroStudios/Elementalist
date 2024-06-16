package net.electro.elementalist.client.renderer.projectiles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.electro.elementalist.Elementalist;
import net.electro.elementalist.client.renderer.spellentities.SpellMasterRenderer;
import net.electro.elementalist.entities.projectiles.AirbladeProjectile;
import net.electro.elementalist.entities.spells.MasterSpellEntity;
import net.electro.elementalist.entities.spells.lightning.ThunderboltEntity;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class AirbladeRenderer extends EntityRenderer<AirbladeProjectile> {
    public AirbladeRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public ResourceLocation getTextureLocation(AirbladeProjectile pEntity) {
        return new ResourceLocation(Elementalist.MOD_ID, "textures/entities/airblade.png");
    }


    @Override
    public void render(AirbladeProjectile pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        pPoseStack.pushPose();
//        pPoseStack.mulPose(Vector3f.YP.rotationDegrees(-pEntity.getYRot() + 270));
//        pPoseStack.mulPose(Vector3f.ZP.rotationDegrees(-pEntity.getXRot()));
        pPoseStack.mulPose(Axis.YP.rotationDegrees(pEntity.getYRot() + 180));
        pPoseStack.mulPose(Axis.XP.rotationDegrees(pEntity.getXRot()));
        pPoseStack.scale(1 + pEntity.tickCount * 0.2f, 1 + pEntity.tickCount * 0.2f, 1 + pEntity.tickCount * 0.2f);
//        pPoseStack.mulPose(Vector3f.XP.rotationDegrees(pEntity.tickCount * 20));
        VertexConsumer vertexConsumer = pBuffer.getBuffer(RenderType.entityTranslucentEmissive(getTextureLocation(pEntity)));
        Matrix4f matrix4f = pPoseStack.last().pose();
        Matrix3f matrix3f = pPoseStack.last().normal();
        vertexConsumer.vertex(matrix4f,-0.5f, 0, -0.5f).color(0xFFFFFFFF).uv(0, 0)
                .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(matrix3f, 0f, 1f, 0f).endVertex();
        vertexConsumer.vertex(matrix4f,-0.5f, 0, 0.5f).color(0xFFFFFFFF).uv(0, 1)
                .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(matrix3f, 0f, 1f, 0f).endVertex();
        vertexConsumer.vertex(matrix4f, 0.5f, 0, 0.5f).color(0xFFFFFFFF).uv(1, 1)
                .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(matrix3f, 0f, 1f, 0f).endVertex();
        vertexConsumer.vertex(matrix4f, 0.5f, 0, -0.5f).color(0xFFFFFFFF).uv(1, 0)
                .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(matrix3f, 0f, 1f, 0f).endVertex();
        pPoseStack.popPose();
    }
}
