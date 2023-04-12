package net.electro.elementalist.spells.ice;

import net.electro.elementalist.Elementalist;
import net.electro.elementalist.entities.projectiles.FireballBasic;
import net.electro.elementalist.entities.projectiles.IceSpear;
import net.electro.elementalist.spells.SpellsMaster;
import net.electro.elementalist.util.DamageType;
import net.electro.elementalist.util.Element;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class IceSpearSpell extends SpellsMaster {
    public IceSpearSpell() {
        damageType = new DamageType(10, Element.ICE, 0, 1);
        manaCost = 40;
        spellId = 6;
        spellCooldownTicks = 80;
        spellIcon = new ResourceLocation(Elementalist.MOD_ID, "textures/gui/spellicons/fireball_spell_icon.png");
        spellString = "ice_spear";
        children = List.of();
    }
    @Override
    protected void initialize_spell(LivingEntity player) {
        IceSpear iceSpear = new IceSpear(player, damageType);
        player.level.addFreshEntity(iceSpear);
    }
}
