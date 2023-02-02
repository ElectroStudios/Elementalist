package net.electro.elementalist.client.renderer.spellentities;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.electro.elementalist.Elementalist;
import net.electro.elementalist.client.models.spellentities.WaterSlashModel;
import net.electro.elementalist.entities.spells.SpellMasterEntity;
import net.electro.elementalist.entities.spells.water.WaterSlashEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;


public abstract class SpellMasterRenderer<T extends Entity> extends EntityRenderer<Entity> {
    protected final int FRAME_AMOUNT;
    protected final ResourceLocation[] TEXTURE_LOCATIONS;
    protected final String TEXTURE_LOCATION_STRING;

    public SpellMasterRenderer(EntityRendererProvider.Context context, int frameAmount, String textureLocation) {
        super(context);
        this.FRAME_AMOUNT = frameAmount;
        this.TEXTURE_LOCATION_STRING = textureLocation;
        this.TEXTURE_LOCATIONS = new ResourceLocation[FRAME_AMOUNT];
        this.setupTextures();
    }
    public SpellMasterRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        this.FRAME_AMOUNT = 0;
        this.TEXTURE_LOCATION_STRING = null;
        this.TEXTURE_LOCATIONS = null;
    }

    private void setupTextures() {
        for (int i = 0; i < FRAME_AMOUNT; i++) {
            TEXTURE_LOCATIONS[i] = new ResourceLocation(Elementalist.MOD_ID, TEXTURE_LOCATION_STRING + i + ".png");
        }
    }

    @Override
    public ResourceLocation getTextureLocation(Entity pEntity) {
        return TEXTURE_LOCATIONS[Math.min(pEntity.tickCount, FRAME_AMOUNT-1)];
    }

}
