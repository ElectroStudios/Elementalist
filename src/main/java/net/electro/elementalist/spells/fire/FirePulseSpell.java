package net.electro.elementalist.spells.fire;

import net.electro.elementalist.entities.projectiles.FireballBasic;
import net.electro.elementalist.entities.spells.FirePulseEntity;
import net.electro.elementalist.spells.SpellsMaster;
import net.electro.elementalist.util.DamageDealer;
import net.minecraft.world.entity.player.Player;

public class FirePulseSpell extends SpellsMaster {
    public FirePulseSpell() {
        manaCost = 40;
        spellId = 1;
        spellCooldownTicks = 60;
        baseDamage = 10;
    }
    @Override
    protected void initialize_spell(Player player) {
        FirePulseEntity firePulse = new FirePulseEntity(player, baseDamage);
        player.level.addFreshEntity(firePulse);
    }
}
