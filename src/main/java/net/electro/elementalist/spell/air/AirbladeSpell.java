package net.electro.elementalist.spell.air;

import net.electro.elementalist.Elementalist;
import net.electro.elementalist.entity.projectiles.AirbladeProjectile;
import net.electro.elementalist.spell.SpellBase;
import net.electro.elementalist.util.DamageType;
import net.electro.elementalist.util.Element;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class AirbladeSpell extends SpellBase {
    public AirbladeSpell() {
        spellId = new ResourceLocation(Elementalist.MOD_ID, "airblade");
        damageType = new DamageType(6, Element.AIR, 0, 1);
        manaCost = 35;
        skillPointCost = 1;
        spellCooldownTicks = 40;
        spellIcon = new ResourceLocation(Elementalist.MOD_ID, "textures/gui/spellicons/fire_explosion_spell_icon.png");
        children = List.of();
        range = 20;
    }
    @Override
    protected void initializeSpell(LivingEntity player) {
        AirbladeProjectile airblade = new AirbladeProjectile(player, damageType);
        player.level().addFreshEntity(airblade);
    }
}
