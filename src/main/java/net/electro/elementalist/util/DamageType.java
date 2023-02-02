package net.electro.elementalist.util;

public class DamageType {
    public final float BASE_DAMAGE;
    public final Element ELEMENT;
    public final int ADDITIONAL_EFFECT_MULTIPLIER;
    public final float KNOCKBACK;
    public DamageType(float baseDamage, Element element, int additionalEffectMultiplier, float knockback) {
        this.BASE_DAMAGE = baseDamage;
        this.ELEMENT = element;
        this.ADDITIONAL_EFFECT_MULTIPLIER = additionalEffectMultiplier;
        this.KNOCKBACK = knockback;
    }
}
