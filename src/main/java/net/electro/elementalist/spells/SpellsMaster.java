package net.electro.elementalist.spells;


import net.electro.elementalist.data.SpellStateProvider;
import net.electro.elementalist.util.DamageType;
import net.electro.elementalist.util.Element;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public abstract class SpellsMaster {
    public DamageType damageType;
    public int manaCost;
    public int spellId;
    public int spellCooldownTicks;
    public ResourceLocation spellIcon;
    public String spellString;
    public List<Integer> children;

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
