package net.electro.elementalist.spell.ice;

import net.electro.elementalist.Elementalist;
import net.electro.elementalist.entity.spells.ice.IcicleBarrageEntity;
import net.electro.elementalist.spell.SpellBase;
import net.electro.elementalist.util.DamageType;
import net.electro.elementalist.util.Element;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class IcicleBarrageSpell extends SpellBase {
    public IcicleBarrageSpell() {
        spellId = new ResourceLocation(Elementalist.MOD_ID, "icicle_barrage");
        damageType = new DamageType(5, Element.ICE, 0, 1);
        manaCost = 60;
        spellCooldownTicks = 100;
        spellIcon = new ResourceLocation(Elementalist.MOD_ID, "textures/gui/spellicons/fireball_spell_icon.png");
        children = List.of();
        skillPointCost = 1;
        range = 25;
    }
    @Override
    protected void initializeSpell(LivingEntity player) {
        IcicleBarrageEntity icicleBarrage = new IcicleBarrageEntity(player, damageType);
        player.level().addFreshEntity(icicleBarrage);
    }
}
