package net.electro.elementalist.client.model.spellentities;

import net.electro.elementalist.Elementalist;
import net.electro.elementalist.entity.spells.ShieldSpellEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ShieldSpellModel extends GeoModel<ShieldSpellEntity> {
    @Override
    public ResourceLocation getModelResource(ShieldSpellEntity object) {
        return new ResourceLocation(Elementalist.MOD_ID, "geo/shield_spell.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ShieldSpellEntity object) {
        return new ResourceLocation(Elementalist.MOD_ID, "textures/entities/spellcircles/spell_circle.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ShieldSpellEntity animatable) {
        return new ResourceLocation(Elementalist.MOD_ID, "animations/spell_shield.animation.json");
    }
}
