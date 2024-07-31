package net.electro.elementalist.spell.fire;

import net.electro.elementalist.Elementalist;
import net.electro.elementalist.entity.spells.fire.FireExplosionEntity;
import net.electro.elementalist.spell.SpellBase;
import net.electro.elementalist.util.DamageType;
import net.electro.elementalist.util.Element;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class FireExplosionSpell extends SpellBase {
    public FireExplosionSpell() {
        spellId = new ResourceLocation(Elementalist.MOD_ID, "fire_explosion");
        damageType = new DamageType(10, Element.FIRE, 0, 0);
        manaCost = 70;
        skillPointCost = 2;
        spellCooldownTicks = 200;
        spellIcon = new ResourceLocation(Elementalist.MOD_ID, "textures/gui/spellicons/fire_explosion_spell_icon.png");
        children = List.of(
                new ResourceLocation(Elementalist.MOD_ID, "fire_cluster_explosion")
        );
        range = 30;
    }
    @Override
    protected void initializeSpell(LivingEntity player) {
        FireExplosionEntity fireExplosion = new FireExplosionEntity(player, damageType);
        player.level().addFreshEntity(fireExplosion);
    }
}
