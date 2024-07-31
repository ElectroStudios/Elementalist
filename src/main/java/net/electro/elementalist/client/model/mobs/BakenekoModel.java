package net.electro.elementalist.client.model.mobs;

import net.electro.elementalist.Elementalist;
import net.electro.elementalist.entity.mobs.BakenekoEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class BakenekoModel extends GeoModel<BakenekoEntity> {

    @Override
    public ResourceLocation getModelResource(BakenekoEntity object) {
        return new ResourceLocation(Elementalist.MOD_ID, "geo/mobs/bakeneko.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BakenekoEntity object) {
        return new ResourceLocation(Elementalist.MOD_ID, "textures/entities/mobs/bakeneko.png");
    }

    @Override
    public ResourceLocation getAnimationResource(BakenekoEntity animatable) {
        return new ResourceLocation(Elementalist.MOD_ID, "animations/mobs/bakeneko.animation.json");
    }
}
