package net.electro.elementalist.spells.fire;

import net.electro.elementalist.Elementalist;
import net.electro.elementalist.entities.projectiles.FireballBasic;
import net.electro.elementalist.spells.SpellsMaster;
import net.electro.elementalist.util.DamageType;
import net.electro.elementalist.util.Element;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class FireBallSpell extends SpellsMaster {
    public FireBallSpell() {
        damageType = new DamageType(7, Element.FIRE, 3, 1);
        manaCost = 30;
        spellId = 0;
        spellCooldownTicks = 60;
        spellIcon = new ResourceLocation(Elementalist.MOD_ID, "textures/gui/spellicons/fireball_spell_icon.png");
        spellString = "fireball";
        children = List.of(1);
    }
    @Override
    protected void initialize_spell(LivingEntity player) {
        FireballBasic fireball = new FireballBasic(player, damageType);
        player.level.addFreshEntity(fireball);
    }
}
