package net.electro.elementalist.client.model.mobs;

import net.electro.elementalist.Elementalist;
import net.electro.elementalist.entity.mobs.LesserDemonEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class LesserDemonModel extends GeoModel<LesserDemonEntity> {

    @Override
    public ResourceLocation getModelResource(LesserDemonEntity object) {
        return new ResourceLocation(Elementalist.MOD_ID, "geo/mobs/lesser_demon.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(LesserDemonEntity object) {
        return new ResourceLocation(Elementalist.MOD_ID, "textures/entities/mobs/lesser_demon.png");
    }

    @Override
    public ResourceLocation getAnimationResource(LesserDemonEntity animatable) {
        return new ResourceLocation(Elementalist.MOD_ID, "animations/mobs/lesser_demon.animation.json");
    }
}
