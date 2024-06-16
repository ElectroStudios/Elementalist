package net.electro.elementalist.spells;


import net.electro.elementalist.data.SpellStateProvider;
import net.electro.elementalist.entities.mobs.ElementalistMob;
import net.electro.elementalist.util.DamageType;
import net.electro.elementalist.util.Element;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public abstract class SpellMaster {
    public DamageType damageType;
    public float range = 0;
    public int manaCost;
    public int spellId;
    public int skillPointCost;
    public int spellCooldownTicks;
    public ResourceLocation spellIcon;
    public String spellName;
    public List<Integer> children;

    public Element getElement() {
        return this.damageType.ELEMENT;
    }

    protected void initialize_spell(LivingEntity owner) {

    }
    public void activate(LivingEntity owner) {
        if (owner instanceof Player player) {
            if (!owner.level().isClientSide()) {
                owner.getCapability(SpellStateProvider.SPELL_STATE).ifPresent(spellState -> {
                    if (manaCost > spellState.getMana()) {
                        if (player instanceof ServerPlayer serverPlayer) {
                            serverPlayer.connection.send(new ClientboundSetActionBarTextPacket(Component.literal("Not enough mana")));
                        }
                    } else if (!spellState.isSpellReady(spellId)) {
                        if (player instanceof ServerPlayer serverPlayer) {
                            serverPlayer.connection.send(new ClientboundSetActionBarTextPacket(Component.literal("Spell not ready")));
                        }
                    }
                    else {
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
