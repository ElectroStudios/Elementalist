package net.electro.elementalist.spell.fire;

import net.electro.elementalist.Elementalist;
import net.electro.elementalist.entity.spells.fire.FireClusterExplosionEntity;
import net.electro.elementalist.spell.SpellBase;
import net.electro.elementalist.util.DamageType;
import net.electro.elementalist.util.Element;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class FireClusterExplosionSpell extends SpellBase {
    public FireClusterExplosionSpell() {
        spellId = new ResourceLocation(Elementalist.MOD_ID, "fire_cluster_explosion");
        damageType = new DamageType(12, Element.FIRE, 3, 1);
        manaCost = 80;
        skillPointCost = 1;
        spellCooldownTicks = 200;
        spellIcon = new ResourceLocation(Elementalist.MOD_ID, "textures/gui/spellicons/fireball_spell_icon.png");
        children = List.of();
        range = 35;
    }
    @Override
    protected void initializeSpell(LivingEntity player) {
        FireClusterExplosionEntity fireClusterExplosion = new FireClusterExplosionEntity(player, damageType);
        player.level().addFreshEntity(fireClusterExplosion);
    }
}
