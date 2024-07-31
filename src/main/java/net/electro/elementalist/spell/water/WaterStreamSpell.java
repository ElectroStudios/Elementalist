package net.electro.elementalist.spell.water;

import net.electro.elementalist.Elementalist;
import net.electro.elementalist.entity.spells.water.WaterStreamEntity;
import net.electro.elementalist.spell.SpellBase;
import net.electro.elementalist.util.DamageType;
import net.electro.elementalist.util.Element;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class WaterStreamSpell extends SpellBase {
    public WaterStreamSpell() {
        spellId = new ResourceLocation(Elementalist.MOD_ID, "water_stream");
        damageType = new DamageType(7, Element.WATER, 0, 4);
        manaCost = 40;
        skillPointCost = 2;
        spellCooldownTicks = 140;
        spellIcon = new ResourceLocation(Elementalist.MOD_ID, "textures/gui/spellicons/water_stream_spell_icon.png");
        children = List.of();
        range = 15;
    }
    @Override
    protected void initializeSpell(LivingEntity player) {
        WaterStreamEntity waterStream = new WaterStreamEntity(player, damageType);
        player.level().addFreshEntity(waterStream);
    }
}
