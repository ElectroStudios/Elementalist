package net.electro.elementalist.spells;


import net.electro.elementalist.data.SpellStateProvider;
import net.electro.elementalist.entities.mobs.ElementalistMob;
import net.electro.elementalist.util.DamageType;
import net.electro.elementalist.util.Element;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public abstract class SpellsMaster {
    public DamageType damageType;
    public int manaCost;
    public int spellId;
    public int skillPointCost;
    public int spellCooldownTicks;
    public ResourceLocation spellIcon;
    public String spellString;
    public List<Integer> children;

    protected void initialize_spell(LivingEntity owner) {

    }
    public void activate(LivingEntity owner) {
        if (owner instanceof Player player) {
            if (!owner.level.isClientSide()) {
                owner.getCapability(SpellStateProvider.SPELL_STATE).ifPresent(spellState -> {
                    if (manaCost <= spellState.getMana() && spellState.isSpellReady(spellId)) {
                        initialize_spell(owner);
                        spellState.subMana(manaCost, player);
                        spellState.addSpellCooldown(spellId, spellCooldownTicks, player);
                    }
                });
            }
        }
        else if (owner instanceof ElementalistMob elementalistMob) {
            initialize_spell(elementalistMob);
        }
    }



}
