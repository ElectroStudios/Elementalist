package net.electro.elementalist.spell.lightning;

import net.electro.elementalist.Elementalist;
import net.electro.elementalist.entity.spells.lightning.ThunderboltEntity;
import net.electro.elementalist.spell.SpellBase;
import net.electro.elementalist.util.DamageType;
import net.electro.elementalist.util.Element;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class ThunderboltSpell extends SpellBase {
    public ThunderboltSpell() {
        damageType = new DamageType(4, Element.LIGHTNING, 0, 0);
        manaCost = 30;
        spellId = new ResourceLocation(Elementalist.MOD_ID, "thunderbolt");
        skillPointCost = 1;
        spellCooldownTicks = 40;
        spellIcon = new ResourceLocation(Elementalist.MOD_ID, "textures/gui/spellicons/fire_explosion_spell_icon.png");
        children = List.of();
        range = 20;
    }
    @Override
    protected void initializeSpell(LivingEntity player) {
        ThunderboltEntity thunderbolt = new ThunderboltEntity(player, damageType);
        player.level().addFreshEntity(thunderbolt);
    }
}
