package net.electro.elementalist.spells.lightning;

import net.electro.elementalist.Elementalist;
import net.electro.elementalist.entities.spells.lightning.ThunderboltEntity;
import net.electro.elementalist.spells.SpellMaster;
import net.electro.elementalist.util.DamageType;
import net.electro.elementalist.util.Element;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class ThunderboltSpell extends SpellMaster {
    public ThunderboltSpell() {
        damageType = new DamageType(4, Element.LIGHTNING, 0, 0);
        manaCost = 30;
        spellId = 9;
        skillPointCost = 1;
        spellCooldownTicks = 40;
        spellIcon = new ResourceLocation(Elementalist.MOD_ID, "textures/gui/spellicons/fire_explosion_spell_icon.png");
        spellName = "thunderbolt";
        children = List.of();
        range = 20;
    }
    @Override
    protected void initialize_spell(LivingEntity player) {
        ThunderboltEntity thunderbolt = new ThunderboltEntity(player, damageType);
        player.level().addFreshEntity(thunderbolt);
    }
}
