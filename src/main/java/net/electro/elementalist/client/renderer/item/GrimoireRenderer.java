package net.electro.elementalist.client.renderer.item;

import net.electro.elementalist.client.model.items.GrimoireModel;
import net.electro.elementalist.item.ElementalistGrimoire;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class GrimoireRenderer extends GeoItemRenderer<ElementalistGrimoire> {
    public GrimoireRenderer() {
        super(new GrimoireModel());
    }
}
