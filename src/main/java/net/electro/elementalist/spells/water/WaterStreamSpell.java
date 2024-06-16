package net.electro.elementalist.spells.water;

import net.electro.elementalist.Elementalist;
import net.electro.elementalist.entities.spells.water.WaterStreamEntity;
import net.electro.elementalist.spells.SpellMaster;
import net.electro.elementalist.util.DamageType;
import net.electro.elementalist.util.Element;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class WaterStreamSpell extends SpellMaster {
    public WaterStreamSpell() {
        damageType = new DamageType(7, Element.WATER, 0, 4);
        manaCost = 40;
        spellId = 5;
        skillPointCost = 2;
        spellCooldownTicks = 140;
        spellIcon = new ResourceLocation(Elementalist.MOD_ID, "textures/gui/spellicons/water_stream_spell_icon.png");
        spellName = "water_stream";
        children = List.of();
        range = 15;
    }
    @Override
    protected void initialize_spell(LivingEntity player) {
        WaterStreamEntity waterStream = new WaterStreamEntity(player, damageType);
        player.level().addFreshEntity(waterStream);
    }
}
