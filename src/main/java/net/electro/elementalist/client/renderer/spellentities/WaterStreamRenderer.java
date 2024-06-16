package net.electro.elementalist.client.renderer.spellentities;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.electro.elementalist.client.models.spellentities.WaterStreamModel;
import net.electro.elementalist.entities.spells.MasterSpellEntity;
import net.electro.elementalist.entities.spells.water.WaterStreamEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;


public class WaterStreamRenderer extends SpellMasterRenderer<WaterStreamEntity> {
    protected WaterStreamModel model;
    public WaterStreamRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, 12, "textures/entities/waterstream/water_stream_");
        this.model = new WaterStreamModel(pContext.bakeLayer(WaterStreamModel.LAYER_LOCATION));
    }

    @Override
    public void render(MasterSpellEntity pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        this.model.setupAnim((WaterStreamEntity) pEntity, pPartialTick, 0, 0, 0, 0);
        VertexConsumer vertexconsumer = pBuffer.getBuffer(this.model.renderType(getTextureLocation(pEntity)));
        pPoseStack.pushPose();
        pPoseStack.mulPose(Axis.YP.rotationDegrees(-pEntity.getYRot() + 180));
        pPoseStack.mulPose(Axis.XP.rotationDegrees(-pEntity.getXRot()));
        pPoseStack.scale(1f, 1f,
                4f * (((WaterStreamEntity)pEntity).getDistance() / ((WaterStreamEntity)pEntity).MAX_DISTANCE));
        this.model.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 0.7F);
        pPoseStack.popPose();
        super.render(pEntity, pEntityYaw, pPartialTick, pPoseStack, pBuffer, pPackedLight);
    }
}
