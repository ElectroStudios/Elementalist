package net.electro.elementalist.client.renderer.item;

import net.electro.elementalist.client.models.items.GrimoireModel;
import net.electro.elementalist.item.ElementalistGrimoire;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class GrimoireRenderer extends GeoItemRenderer<ElementalistGrimoire> {
    public GrimoireRenderer() {
        super(new GrimoireModel());
    }
}
