package net.electro.elementalist.spell.fire;

import net.electro.elementalist.Elementalist;
import net.electro.elementalist.entity.spells.fire.FirePulseEntity;
import net.electro.elementalist.spell.SpellBase;
import net.electro.elementalist.util.DamageType;
import net.electro.elementalist.util.Element;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class FirePulseSpell extends SpellBase {
    public FirePulseSpell() {
        spellId = new ResourceLocation(Elementalist.MOD_ID, "fire_pulse");
        damageType = new DamageType(10, Element.FIRE, 5, 1);
        manaCost = 40;
        skillPointCost = 1;
        spellCooldownTicks = 100;
        spellIcon = new ResourceLocation(Elementalist.MOD_ID, "textures/gui/spellicons/fire_pulse_spell_icon.png");
        children = List.of(
                new ResourceLocation(Elementalist.MOD_ID, "fire_breath")
        );
        range = 15;
    }
    @Override
    protected void initializeSpell(LivingEntity player) {
        FirePulseEntity firePulse = new FirePulseEntity(player, damageType);
        player.level().addFreshEntity(firePulse);
    }
}
