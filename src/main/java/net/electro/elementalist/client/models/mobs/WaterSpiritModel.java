package net.electro.elementalist.client.models.mobs;

import net.electro.elementalist.Elementalist;
import net.electro.elementalist.entities.mobs.WaterSpiritEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class WaterSpiritModel extends AnimatedGeoModel<WaterSpiritEntity> {

    @Override
    public ResourceLocation getModelResource(WaterSpiritEntity object) {
        return new ResourceLocation(Elementalist.MOD_ID, "geo/mobs/water_spirit.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(WaterSpiritEntity object) {
        return new ResourceLocation(Elementalist.MOD_ID, "textures/entities/mobs/water_spirit.png");
    }

    @Override
    public ResourceLocation getAnimationResource(WaterSpiritEntity animatable) {
        return new ResourceLocation(Elementalist.MOD_ID, "animations/mobs/water_spirit.animation.json");
    }
}
