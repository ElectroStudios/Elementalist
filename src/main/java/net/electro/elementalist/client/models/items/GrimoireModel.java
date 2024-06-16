package net.electro.elementalist.client.models.items;

import net.electro.elementalist.Elementalist;
import net.electro.elementalist.item.ElementalistGrimoire;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class GrimoireModel extends GeoModel<ElementalistGrimoire> {

    @Override
    public ResourceLocation getModelResource(ElementalistGrimoire object) {
        return new ResourceLocation(Elementalist.MOD_ID, "geo/grimoire.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ElementalistGrimoire object) {
        return new ResourceLocation(Elementalist.MOD_ID, "textures/item/grimoire.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ElementalistGrimoire animatable) {
        return new ResourceLocation(Elementalist.MOD_ID, "animations/grimoire.animation.json");
    }
}
