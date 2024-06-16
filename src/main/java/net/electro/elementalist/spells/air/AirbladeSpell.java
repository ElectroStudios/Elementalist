package net.electro.elementalist.spells.air;

import net.electro.elementalist.Elementalist;
import net.electro.elementalist.entities.projectiles.AirbladeProjectile;
import net.electro.elementalist.spells.SpellMaster;
import net.electro.elementalist.util.DamageType;
import net.electro.elementalist.util.Element;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class AirbladeSpell extends SpellMaster {
    public AirbladeSpell() {
        damageType = new DamageType(6, Element.AIR, 0, 1);
        manaCost = 35;
        spellId = 10;
        skillPointCost = 1;
        spellCooldownTicks = 40;
        spellIcon = new ResourceLocation(Elementalist.MOD_ID, "textures/gui/spellicons/fire_explosion_spell_icon.png");
        spellName = "airblade";
        children = List.of();
        range = 20;
    }
    @Override
    protected void initialize_spell(LivingEntity player) {
        AirbladeProjectile airblade = new AirbladeProjectile(player, damageType);
        player.level().addFreshEntity(airblade);
    }
}
