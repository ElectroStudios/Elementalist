package net.electro.elementalist.spells.ice;

import net.electro.elementalist.Elementalist;
import net.electro.elementalist.entities.spells.ice.IcicleBarrageEntity;
import net.electro.elementalist.spells.SpellMaster;
import net.electro.elementalist.util.DamageType;
import net.electro.elementalist.util.Element;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class IcicleBarrageSpell extends SpellMaster {
    public IcicleBarrageSpell() {
        damageType = new DamageType(5, Element.ICE, 0, 1);
        manaCost = 60;
        spellId = 11;
        spellCooldownTicks = 100;
        spellIcon = new ResourceLocation(Elementalist.MOD_ID, "textures/gui/spellicons/fireball_spell_icon.png");
        spellName = "icicle_barrage";
        children = List.of();
        skillPointCost = 1;
        range = 25;
    }
    @Override
    protected void initialize_spell(LivingEntity player) {
        IcicleBarrageEntity icicleBarrage = new IcicleBarrageEntity(player, damageType);
        player.level().addFreshEntity(icicleBarrage);
    }
}
