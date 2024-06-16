package net.electro.elementalist.spells.fire;

import net.electro.elementalist.Elementalist;
import net.electro.elementalist.entities.spells.fire.FirePulseEntity;
import net.electro.elementalist.spells.SpellMaster;
import net.electro.elementalist.util.DamageType;
import net.electro.elementalist.util.Element;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class FirePulseSpell extends SpellMaster {
    public FirePulseSpell() {
        damageType = new DamageType(10, Element.FIRE, 5, 1);
        manaCost = 40;
        spellId = 1;
        skillPointCost = 1;
        spellCooldownTicks = 100;
        spellIcon = new ResourceLocation(Elementalist.MOD_ID, "textures/gui/spellicons/fire_pulse_spell_icon.png");
        spellName = "fire_pulse";
        children = List.of(2, 3);
        range = 15;
    }
    @Override
    protected void initialize_spell(LivingEntity player) {
        FirePulseEntity firePulse = new FirePulseEntity(player, damageType);
        player.level().addFreshEntity(firePulse);
    }
}
