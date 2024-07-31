package net.electro.elementalist.spell.fire;

import net.electro.elementalist.Elementalist;
import net.electro.elementalist.entity.projectiles.FireballBasicProjectile;
import net.electro.elementalist.spell.SpellBase;
import net.electro.elementalist.util.DamageType;
import net.electro.elementalist.util.Element;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class FireBallSpell extends SpellBase {
    public FireBallSpell() {
        spellId = new ResourceLocation(Elementalist.MOD_ID, "fireball");
        damageType = new DamageType(7, Element.FIRE, 3, 1);
        manaCost = 30;
        spellCooldownTicks = 60;
        spellIcon = new ResourceLocation(Elementalist.MOD_ID, "textures/gui/spellicons/fireball_spell_icon.png");
        children = List.of(
                new ResourceLocation(Elementalist.MOD_ID, "fire_pulse"),
                new ResourceLocation(Elementalist.MOD_ID, "fire_wave")
        );
        range = 20f;
    }
    @Override
    protected void initializeSpell(LivingEntity player) {
        FireballBasicProjectile fireball = new FireballBasicProjectile(player, damageType);
        player.level().addFreshEntity(fireball);
    }
}
