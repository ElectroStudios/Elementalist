package net.electro.elementalist.spells.fire;

import net.electro.elementalist.Elementalist;
import net.electro.elementalist.entities.projectiles.FireballBasicProjectile;
import net.electro.elementalist.spells.SpellMaster;
import net.electro.elementalist.util.DamageType;
import net.electro.elementalist.util.Element;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class FireBallSpell extends SpellMaster {
    public FireBallSpell() {
        damageType = new DamageType(7, Element.FIRE, 3, 1);
        manaCost = 30;
        spellId = 0;
        spellCooldownTicks = 60;
        spellIcon = new ResourceLocation(Elementalist.MOD_ID, "textures/gui/spellicons/fireball_spell_icon.png");
        spellName = "fireball";
        children = List.of(1);
        range = 20f;
    }
    @Override
    protected void initialize_spell(LivingEntity player) {
        FireballBasicProjectile fireball = new FireballBasicProjectile(player, damageType);
        player.level().addFreshEntity(fireball);
    }
}
