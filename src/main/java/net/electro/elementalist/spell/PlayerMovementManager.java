package net.electro.elementalist.spell;

import net.electro.elementalist.data.SpellStateProvider;
import net.electro.elementalist.util.Utility;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class PlayerMovementManager {
    private ServerPlayer player;
    private static final AttributeModifier SLOW_DOWN = new AttributeModifier("slow_down", -0.08, AttributeModifier.Operation.ADDITION);
    public PlayerMovementManager(ServerPlayer player) {
        this.player = player;
    }

    public void performMovementAction(String skillName) {
        switch (skillName) {
            case "dodgeLeft" -> {
                player.hurtMarked = true;
                player.setDeltaMovement(player.getDeltaMovement().add(Utility.getLeftVector(player)).add(0, 0.3f, 0));
            }
            case "dodgeRight" -> {
                player.hurtMarked = true;
                player.setDeltaMovement(player.getDeltaMovement().add(Utility.getRightVector(player)).add(0, 0.3f, 0));
            }
            case "jump" -> {
                if (!player.onGround()) {
                    player.getCapability(SpellStateProvider.SPELL_STATE).ifPresent(spellState -> {
                        if (spellState.isJumpAvailable()) {
                            player.hurtMarked = true;
                            player.setDeltaMovement(player.getDeltaMovement()
                                    .add(Utility.multiplyVec3ByFloat(player.getForward(), 0.8f))
                                    .add(0, 0.5f, 0));
                            spellState.decrementJumpsAvailable();
                        }
                    });
                }
            }
            case "activateFallProtection" ->
                    player.getCapability(SpellStateProvider.SPELL_STATE).ifPresent(spellState -> {
                        spellState.setFallProtection(true);
                    });
            case "deactivateFallProtection" ->
                    player.getCapability(SpellStateProvider.SPELL_STATE).ifPresent(spellState -> {
                        spellState.setFallProtection(false);
                    });
            case "activateMovementSlowdown" -> {
                if (!player.onGround()) {
                    player.hurtMarked = true;
                    player.getAttribute(net.minecraftforge.common.ForgeMod.ENTITY_GRAVITY.get()).addTransientModifier(SLOW_DOWN);
                    player.setDeltaMovement(Utility.multiplyVec3ByFloat(player.getDeltaMovement(), 0.1f));
                }
            }
            case "deactivateMovementSlowdown" -> {
                player.getAttribute(net.minecraftforge.common.ForgeMod.ENTITY_GRAVITY.get()).removeModifier(SLOW_DOWN);
            }
        }
    }
}
