package net.electro.elementalist.spells.fire;

import net.electro.elementalist.Elementalist;
import net.electro.elementalist.entities.projectiles.FireballBasic;
import net.electro.elementalist.entities.spells.fire.FireClusterExplosionEntity;
import net.electro.elementalist.spells.SpellsMaster;
import net.electro.elementalist.util.DamageType;
import net.electro.elementalist.util.Element;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class FireClusterExplosion extends SpellsMaster {
    public FireClusterExplosion() {
        damageType = new DamageType(12, Element.FIRE, 3, 1);
        manaCost = 80;
        spellId = 8;
        skillPointCost = 1;
        spellCooldownTicks = 200;
        spellIcon = new ResourceLocation(Elementalist.MOD_ID, "textures/gui/spellicons/fireball_spell_icon.png");
        spellString = "fire_cluster_explosion";
        children = List.of();
    }
    @Override
    protected void initialize_spell(LivingEntity player) {
        FireClusterExplosionEntity fireClusterExplosion = new FireClusterExplosionEntity(player, damageType);
        player.level.addFreshEntity(fireClusterExplosion);
    }
}
