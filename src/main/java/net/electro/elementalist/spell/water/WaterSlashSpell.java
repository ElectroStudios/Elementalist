package net.electro.elementalist.spell.water;

import net.electro.elementalist.Elementalist;
import net.electro.elementalist.entity.spells.water.WaterSlashEntity;
import net.electro.elementalist.spell.SpellBase;
import net.electro.elementalist.util.DamageType;
import net.electro.elementalist.util.Element;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class WaterSlashSpell extends SpellBase {
    public WaterSlashSpell() {
        spellId = new ResourceLocation(Elementalist.MOD_ID, "water_slash");
        damageType = new DamageType(10, Element.WATER, 0, 2);
        manaCost = 25;
        spellCooldownTicks = 50;
        spellIcon = new ResourceLocation(Elementalist.MOD_ID, "textures/gui/spellicons/water_slash_spell_icon.png");
        children = List.of();
        range = 3;
    }
    @Override
    protected void initializeSpell(LivingEntity player) {
        WaterSlashEntity waterSlash = new WaterSlashEntity(player, damageType);
        player.level().addFreshEntity(waterSlash);
    }
}
