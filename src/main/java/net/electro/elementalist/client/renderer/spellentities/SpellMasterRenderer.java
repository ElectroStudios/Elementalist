package net.electro.elementalist.client.renderer.spellentities;

import com.mojang.blaze3d.vertex.PoseStack;
import net.electro.elementalist.Elementalist;
import net.electro.elementalist.entity.spells.MasterSpellEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;


public class SpellMasterRenderer<T extends MasterSpellEntity> extends EntityRenderer<MasterSpellEntity> {
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

    @Override
    public void render(MasterSpellEntity pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTick, pPoseStack, pBuffer, pPackedLight);
    }

    private void setupTextures() {
        for (int i = 0; i < FRAME_AMOUNT; i++) {
            TEXTURE_LOCATIONS[i] = new ResourceLocation(Elementalist.MOD_ID, TEXTURE_LOCATION_STRING + i + ".png");
        }
    }

    @Override
    public ResourceLocation getTextureLocation(MasterSpellEntity pEntity) {
        return TEXTURE_LOCATIONS[Math.min(pEntity.tickCount, FRAME_AMOUNT-1)];
    }
}
