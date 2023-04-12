package net.electro.elementalist.entities.mobs;

import net.electro.elementalist.spells.SpellsMaster;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;

public class ElementalistMob extends Monster {
    public int maxMana;
    public int currentMana = 0;
    private Map<Integer, Integer> spellCooldowns = new HashMap<Integer, Integer>();
    protected ElementalistMob(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level.isClientSide()) {
            if (this.tickCount % 20 == 0) {
                currentMana = Math.min(currentMana + 10, maxMana);
            }
            if (this.tickCount % 5 == 0) {
                spellCooldowns.replaceAll((k, v) -> Math.max(v - 5, 0));
            }
        }
    }

    public boolean isSpellReady(int spellId) {
        return !spellCooldowns.containsKey(spellId) || spellCooldowns.get(spellId) == 0;
    }

    public void activateSpell(SpellsMaster spell) {
        spell.activate(this);
        currentMana -= spell.manaCost;
        spellCooldowns.put(spell.spellId, spell.spellCooldownTicks);
    }
}
