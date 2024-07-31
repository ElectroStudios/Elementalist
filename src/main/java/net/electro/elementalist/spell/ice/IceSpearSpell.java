package net.electro.elementalist.spell.ice;

import net.electro.elementalist.Elementalist;
import net.electro.elementalist.entity.projectiles.IceSpearProjectile;
import net.electro.elementalist.spell.SpellBase;
import net.electro.elementalist.util.DamageType;
import net.electro.elementalist.util.Element;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class IceSpearSpell extends SpellBase {
    public IceSpearSpell() {
        spellId = new ResourceLocation(Elementalist.MOD_ID, "ice_spear");
        damageType = new DamageType(7, Element.ICE, 0, 1);
        manaCost = 40;
        spellCooldownTicks = 80;
        spellIcon = new ResourceLocation(Elementalist.MOD_ID, "textures/gui/spellicons/fireball_spell_icon.png");
        children = List.of();
        range = 30;
    }
    @Override
    protected void initializeSpell(LivingEntity player) {
        IceSpearProjectile iceSpear = new IceSpearProjectile(player, damageType);
        player.level().addFreshEntity(iceSpear);
    }
}
