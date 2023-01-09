package net.electro.elementalist.spells.fire;

import net.electro.elementalist.entities.projectiles.FireballBasic;
import net.electro.elementalist.spells.SpellsMaster;
import net.minecraft.world.entity.player.Player;

public class FireBallBasicSpell extends SpellsMaster {
    public FireBallBasicSpell () {
        manaCost = 30;
        spellId = 0;
        spellCooldownTicks = 40;
    }
    @Override
    protected void initialize_spell(Player player) {
        FireballBasic fireball = new FireballBasic(player);
        player.level.addFreshEntity(fireball);
    }
}
