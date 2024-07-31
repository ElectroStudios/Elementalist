package net.electro.elementalist.client.model.spellentities;

import net.electro.elementalist.Elementalist;
import net.electro.elementalist.entity.spells.MagicCircleEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class MagicCircleModel extends GeoModel<MagicCircleEntity> {
    @Override
    public ResourceLocation getModelResource(MagicCircleEntity object) {
        return new ResourceLocation(Elementalist.MOD_ID, "geo/magic_circle.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(MagicCircleEntity object) {
        return null;
    }

    @Override
    public ResourceLocation getAnimationResource(MagicCircleEntity animatable) {
        return new ResourceLocation(Elementalist.MOD_ID, "animations/magic_circle.animation.json");
    }
}
