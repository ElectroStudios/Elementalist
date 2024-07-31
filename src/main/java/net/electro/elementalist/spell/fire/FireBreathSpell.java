package net.electro.elementalist.spell.fire;

import net.electro.elementalist.Elementalist;
import net.electro.elementalist.entity.spells.fire.FireBreathEntity;
import net.electro.elementalist.spell.SpellBase;
import net.electro.elementalist.util.DamageType;
import net.electro.elementalist.util.Element;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class FireBreathSpell extends SpellBase {
    public FireBreathSpell() {
        spellId = new ResourceLocation(Elementalist.MOD_ID, "fire_breath");
        damageType = new DamageType(12, Element.FIRE, 5, 2);
        manaCost = 50;
        skillPointCost = 1;
        spellCooldownTicks = 140;
        spellIcon = new ResourceLocation(Elementalist.MOD_ID, "textures/gui/spellicons/fire_breath_spell_icon.png");
        children = List.of();
        range = 25;
    }
    @Override
    protected void initializeSpell(LivingEntity player) {
        FireBreathEntity fireBreath = new FireBreathEntity(player, damageType);
        player.level().addFreshEntity(fireBreath);
    }
}
