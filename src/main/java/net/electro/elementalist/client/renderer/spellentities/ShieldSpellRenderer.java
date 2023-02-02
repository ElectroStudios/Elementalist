package net.electro.elementalist.client.renderer.spellentities;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.electro.elementalist.Elementalist;
import net.electro.elementalist.client.models.spellentities.ShieldSpellModel;
import net.electro.elementalist.entities.spells.ShieldSpellEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import software.bernie.geckolib3.core.util.Color;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

public class ShieldSpellRenderer extends GeoProjectilesRenderer<ShieldSpellEntity> {

    protected final int FRAME_AMOUNT = 15;
    protected final ResourceLocation[] TEXTURE_LOCATIONS = new ResourceLocation[FRAME_AMOUNT];
    protected final String TEXTURE_LOCATION_STRING = "textures/entities/spellcircles/shieldcircle/shield_circle_";
    public ShieldSpellRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ShieldSpellModel());
        setupTextures();
    }

    @Override
    public Color getRenderColor(ShieldSpellEntity animatable, float partialTick, PoseStack poseStack, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, int packedLight) {
        return Color.ofRGBA(255, 224, 209, 255);
    }

    @Override
    public RenderType getRenderType(ShieldSpellEntity animatable, float partialTick, PoseStack poseStack,
                                    @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer,
                                    int packedLight, ResourceLocation texture) {
        poseStack.popPose();
        poseStack.pushPose();
        poseStack.translate(0, 1.5f, 0);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(-animatable.getYRot() + 180));
        poseStack.mulPose(Vector3f.XP.rotationDegrees(-animatable.getXRot()));
        return RenderType.entityTranslucent(texture);
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
