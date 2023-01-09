package net.electro.elementalist.spells;


import net.electro.elementalist.data.SpellStateProvider;
import net.minecraft.world.entity.player.Player;

public abstract class SpellsMaster {
    public float baseDamage;
    public int manaCost;
    public int spellId;
    public int spellCooldownTicks;

    protected void initialize_spell(Player player) {

    }
    public void activate(Player player) {
        if (!player.level.isClientSide()) {
            player.getCapability(SpellStateProvider.SPELL_STATE).ifPresent(spellState -> {
                if (manaCost <= spellState.getMana() && spellState.isSpellReady(spellId)) {
                    initialize_spell(player);
                    spellState.subMana(manaCost, player);
                    spellState.addSpellCooldown(spellId, spellCooldownTicks, player);
                }
            });
        }
    }



}
