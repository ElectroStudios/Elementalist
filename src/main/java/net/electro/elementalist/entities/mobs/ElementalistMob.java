package net.electro.elementalist.entities.mobs;

import net.electro.elementalist.spells.SpellMaster;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ElementalistMob extends Monster {
    public int maxMana;
    public int currentMana = 0;
    public int attackInterval = 0;
    public List<SpellMaster> availableSpells;
    private Map<Integer, Integer> spellCooldowns = new HashMap<Integer, Integer>();
    private Map<Integer, Integer> spellUsedAmount = new HashMap<Integer, Integer>();
    protected ElementalistMob(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide()) {
            if (this.tickCount % 20 == 0) {
                currentMana = Math.min(currentMana + 10, maxMana);
            }
            if (this.tickCount % 5 == 0) {
                spellCooldowns.replaceAll((k, v) -> Math.max(v - 5, 0));
                attackInterval = Math.max(0, attackInterval - 5);
            }
        }
    }

    public boolean isSpellReady(int spellId) {
        return !spellCooldowns.containsKey(spellId) || spellCooldowns.get(spellId) == 0;
    }

    public int getSpellCooldown(int spellId) {
        return spellCooldowns.getOrDefault(spellId, 0);
    }

    public int getSpellUsedAmount(int spellId) {
        return spellUsedAmount.getOrDefault(spellId, 1);
    }

    public void activateSpell(SpellMaster spell) {
        spell.activate(this);
        currentMana -= spell.manaCost;
        spellCooldowns.put(spell.spellId, spell.spellCooldownTicks);

        if (spellUsedAmount.containsKey(spell.spellId)) {
            spellUsedAmount.put(spell.spellId, spellUsedAmount.get(spell.spellId) + 1);
        } else {
            spellUsedAmount.put(spell.spellId, 2);
        }

        attackInterval += 20;
    }
}
