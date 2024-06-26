package net.electro.elementalist.spells.fire;

import net.electro.elementalist.Elementalist;
import net.electro.elementalist.entities.spells.fire.FireBreathEntity;
import net.electro.elementalist.spells.SpellMaster;
import net.electro.elementalist.util.DamageType;
import net.electro.elementalist.util.Element;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class FireBreathSpell extends SpellMaster {
    public FireBreathSpell() {
        damageType = new DamageType(12, Element.FIRE, 5, 2);
        manaCost = 50;
        spellId = 2;
        skillPointCost = 1;
        spellCooldownTicks = 140;
        spellIcon = new ResourceLocation(Elementalist.MOD_ID, "textures/gui/spellicons/fire_breath_spell_icon.png");
        spellName = "fire_breath";
        children = List.of(7);
        range = 25;
    }
    @Override
    protected void initialize_spell(LivingEntity player) {
        FireBreathEntity fireBreath = new FireBreathEntity(player, damageType);
        player.level().addFreshEntity(fireBreath);
    }
}
