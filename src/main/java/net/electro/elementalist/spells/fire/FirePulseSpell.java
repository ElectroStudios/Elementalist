package net.electro.elementalist.spells.fire;

import net.electro.elementalist.Elementalist;
import net.electro.elementalist.entities.spells.fire.FirePulseEntity;
import net.electro.elementalist.spells.SpellsMaster;
import net.electro.elementalist.util.DamageType;
import net.electro.elementalist.util.Element;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class FirePulseSpell extends SpellsMaster {
    public FirePulseSpell() {
        damageType = new DamageType(10, Element.FIRE, 5, 1);
        manaCost = 40;
        spellId = 1;
        spellCooldownTicks = 100;
        spellIcon = new ResourceLocation(Elementalist.MOD_ID, "textures/gui/spellicons/fire_pulse_spell_icon.png");
        spellString = "fire_pulse";
        children = List.of(2, 3);
    }
    @Override
    protected void initialize_spell(Player player) {
        FirePulseEntity firePulse = new FirePulseEntity(player, damageType);
        player.level.addFreshEntity(firePulse);
    }
}
