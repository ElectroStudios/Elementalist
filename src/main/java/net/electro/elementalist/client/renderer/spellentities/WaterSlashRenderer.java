package net.electro.elementalist.client.renderer.spellentities;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.electro.elementalist.client.models.spellentities.WaterSlashModel;
import net.electro.elementalist.entities.spells.water.WaterSlashEntity;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;


public class WaterSlashRenderer extends SpellMasterRenderer<WaterSlashEntity> {
    protected WaterSlashModel model;
    public WaterSlashRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, 10, "textures/entities/waterslash/water_slash_");
        this.model = new WaterSlashModel(pContext.bakeLayer(WaterSlashModel.LAYER_LOCATION));
    }

    @Override
    public void render(Entity pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        pPoseStack.pushPose();
        this.model.setupAnim((WaterSlashEntity) pEntity, pPartialTick, 0, 0, 0, 0);
        VertexConsumer vertexconsumer = pBuffer.getBuffer(this.model.renderType(getTextureLocation(pEntity)));
        pPoseStack.mulPose(Vector3f.YP.rotationDegrees(-pEntity.getYRot() + 180));
        pPoseStack.scale(8f, 8f, 8f);
        this.model.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        pPoseStack.popPose();
        super.render(pEntity, pEntityYaw, pPartialTick, pPoseStack, pBuffer, pPackedLight);
    }
}
