package net.electro.elementalist.spells.fire;

import net.electro.elementalist.Elementalist;
import net.electro.elementalist.entities.spells.fire.FireWaveEntity;
import net.electro.elementalist.spells.SpellMaster;
import net.electro.elementalist.util.DamageType;
import net.electro.elementalist.util.Element;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class FireWaveSpell extends SpellMaster {
    public FireWaveSpell() {
        damageType = new DamageType(14, Element.FIRE, 4, 1);
        manaCost = 50;
        spellId = 7;
        skillPointCost = 1;
        spellCooldownTicks = 180;
        spellIcon = new ResourceLocation(Elementalist.MOD_ID, "textures/gui/spellicons/fire_breath_spell_icon.png");
        spellName = "fire_wave";
        children = List.of();
        range = 15;
    }
    @Override
    protected void initialize_spell(LivingEntity player) {
        FireWaveEntity fireWave = new FireWaveEntity(player, damageType);
        player.level().addFreshEntity(fireWave);
    }
}
