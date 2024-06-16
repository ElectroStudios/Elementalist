package net.electro.elementalist.spells.ice;

import net.electro.elementalist.Elementalist;
import net.electro.elementalist.entities.projectiles.IceSpearProjectile;
import net.electro.elementalist.spells.SpellMaster;
import net.electro.elementalist.util.DamageType;
import net.electro.elementalist.util.Element;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class IceSpearSpell extends SpellMaster {
    public IceSpearSpell() {
        damageType = new DamageType(7, Element.ICE, 0, 1);
        manaCost = 40;
        spellId = 6;
        spellCooldownTicks = 80;
        spellIcon = new ResourceLocation(Elementalist.MOD_ID, "textures/gui/spellicons/fireball_spell_icon.png");
        spellName = "ice_spear";
        children = List.of(11);
        range = 30;
    }
    @Override
    protected void initialize_spell(LivingEntity player) {
        IceSpearProjectile iceSpear = new IceSpearProjectile(player, damageType);
        player.level().addFreshEntity(iceSpear);
    }
}
