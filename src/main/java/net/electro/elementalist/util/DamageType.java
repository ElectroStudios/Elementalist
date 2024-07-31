package net.electro.elementalist.util;

import net.electro.elementalist.data.ElementalistStatsProvider;
import net.electro.elementalist.registry.MessageRegistry;
import net.electro.elementalist.networking.ElementExperienceSyncS2CPacket;
import net.electro.elementalist.networking.ElementLevelSyncS2CPacket;
import net.electro.elementalist.networking.ElementSkillPointsSyncS2CPacket;
import net.electro.elementalist.networking.PlayerLevelSyncS2CPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class DamageType {
    public final float BASE_DAMAGE;
    public final Element ELEMENT;
    public final int ADDITIONAL_EFFECT_MULTIPLIER;
    public final float KNOCKBACK;
    public final int EXP_MULTIPLIER = 3;
    public DamageType(float baseDamage, Element element, int additionalEffectMultiplier, float knockback) {
        this.BASE_DAMAGE = baseDamage;
        this.ELEMENT = element;
        this.ADDITIONAL_EFFECT_MULTIPLIER = additionalEffectMultiplier;
        this.KNOCKBACK = knockback;
    }

    public float calculateDamage(LivingEntity owner, Entity target) {
        if (owner instanceof Player){
            owner.getCapability(ElementalistStatsProvider.ELEMENTALIST_STATS).ifPresent(elementalistStats -> {
                elementalistStats.addExperience((int) BASE_DAMAGE * EXP_MULTIPLIER, ELEMENT);
                MessageRegistry.sendToPlayer(new ElementExperienceSyncS2CPacket(elementalistStats.getElementExperience(ELEMENT),
                        ElementalistMaps.elementToIndexMap.get(ELEMENT)), (ServerPlayer) owner);
                owner.sendSystemMessage(Component.literal("current exp: " + elementalistStats.getPlayerExperience()));
                if (elementalistStats.getPlayerExperience() >= Utility.getExperienceToNextLevel(elementalistStats.getPlayerLevel())) {
                    elementalistStats.newLevel();
                    MessageRegistry.sendToPlayer(new PlayerLevelSyncS2CPacket(elementalistStats.getPlayerLevel()), (ServerPlayer) owner);
                    owner.sendSystemMessage(Component.literal("current level: " + elementalistStats.getPlayerLevel()));
                }
                if (elementalistStats.getElementExperience(ELEMENT) >= Utility.getExperienceToNextLevel(elementalistStats.getElementLevel(ELEMENT))) {
                    elementalistStats.newElementLevel(ELEMENT);
                    MessageRegistry.sendToPlayer(new ElementLevelSyncS2CPacket(elementalistStats.getElementLevel(ELEMENT),
                            ElementalistMaps.elementToIndexMap.get(ELEMENT)), (ServerPlayer) owner);
                    MessageRegistry.sendToPlayer(new ElementSkillPointsSyncS2CPacket(elementalistStats.getElementSkillPoints(ELEMENT),
                            ElementalistMaps.elementToIndexMap.get(ELEMENT)), (ServerPlayer) owner);
                }
            });
        }
        return BASE_DAMAGE;
    }
}
