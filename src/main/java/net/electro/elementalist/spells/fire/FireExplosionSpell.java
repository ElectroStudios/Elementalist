package net.electro.elementalist.spells.fire;

import net.electro.elementalist.Elementalist;
import net.electro.elementalist.entities.spells.fire.FireExplosionEntity;
import net.electro.elementalist.spells.SpellsMaster;
import net.electro.elementalist.util.DamageType;
import net.electro.elementalist.util.Element;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class FireExplosionSpell extends SpellsMaster {
    public FireExplosionSpell() {
        damageType = new DamageType(10, Element.FIRE, 0, 0);
        manaCost = 70;
        spellId = 3;
        spellCooldownTicks = 200;
        spellIcon = new ResourceLocation(Elementalist.MOD_ID, "textures/gui/spellicons/fire_explosion_spell_icon.png");
        spellString = "fire_explosion";
    }
    @Override
    protected void initialize_spell(Player player) {
        FireExplosionEntity fireExplosion = new FireExplosionEntity(player, damageType);
        player.level.addFreshEntity(fireExplosion);
    }
}
