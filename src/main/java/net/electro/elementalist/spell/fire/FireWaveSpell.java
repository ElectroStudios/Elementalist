package net.electro.elementalist.spell.fire;

import net.electro.elementalist.Elementalist;
import net.electro.elementalist.entity.spells.fire.FireWaveEntity;
import net.electro.elementalist.spell.SpellBase;
import net.electro.elementalist.util.DamageType;
import net.electro.elementalist.util.Element;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class FireWaveSpell extends SpellBase {
    public FireWaveSpell() {
        spellId = new ResourceLocation(Elementalist.MOD_ID, "fire_wave");
        damageType = new DamageType(14, Element.FIRE, 4, 1);
        manaCost = 50;
        skillPointCost = 1;
        spellCooldownTicks = 180;
        spellIcon = new ResourceLocation(Elementalist.MOD_ID, "textures/gui/spellicons/fire_breath_spell_icon.png");
        children = List.of(
                new ResourceLocation(Elementalist.MOD_ID, "fire_explosion")
        );
        range = 15;
    }
    @Override
    protected void initializeSpell(LivingEntity player) {
        FireWaveEntity fireWave = new FireWaveEntity(player, damageType);
        player.level().addFreshEntity(fireWave);
    }
}
