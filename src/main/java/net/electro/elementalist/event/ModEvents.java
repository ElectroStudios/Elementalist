package net.electro.elementalist.event;

import net.electro.elementalist.Elementalist;
import net.electro.elementalist.client.ClientSpellStateData;
import net.electro.elementalist.data.ElementalistStatsProvider;
import net.electro.elementalist.data.SpellStateProvider;
import net.electro.elementalist.registry.EffectRegistry;
import net.electro.elementalist.networking.*;
import net.electro.elementalist.registry.EntityRegistry;
import net.electro.elementalist.entity.mobs.BakenekoEntity;
import net.electro.elementalist.entity.mobs.LesserDemonEntity;
import net.electro.elementalist.entity.mobs.WaterSpiritEntity;
import net.electro.elementalist.registry.MessageRegistry;
import net.electro.elementalist.util.*;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import java.util.List;


public class ModEvents {
    @Mod.EventBusSubscriber(modid = Elementalist.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModEventBusEvents {
        @SubscribeEvent
        public static void entityAttributeEvent(EntityAttributeCreationEvent event) {
            event.put(EntityRegistry.WATER_SPIRIT.get(), WaterSpiritEntity.setAttributes());
            event.put(EntityRegistry.LESSER_DEMON.get(), LesserDemonEntity.setAttributes());
            event.put(EntityRegistry.BAKENEKO.get(), BakenekoEntity.setAttributes());
        }
    }

    @Mod.EventBusSubscriber(modid = Elementalist.MOD_ID)
    public static class ModForgeEvents {

//        @SubscribeEvent
//        private void enqueueIMC (final InterModEnqueueEvent event) {
//            InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE,
//                    () -> SlotTypePreset.BELT.getMessageBuilder().build());
//        }

        @SubscribeEvent
        public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
            if(event.getObject() instanceof Player) {
                if(!event.getObject().getCapability(ElementalistStatsProvider.ELEMENTALIST_STATS).isPresent()) {
                    event.addCapability(new ResourceLocation(Elementalist.MOD_ID, "stats"), new ElementalistStatsProvider());
                }
                if(!event.getObject().getCapability(SpellStateProvider.SPELL_STATE).isPresent()) {
                    event.addCapability(new ResourceLocation(Elementalist.MOD_ID, "state"), new SpellStateProvider());
                }
            }
        }

        @SubscribeEvent
        public static void onPlayerCloned(PlayerEvent.Clone event) {
            if(event.isWasDeath()) {
                event.getOriginal().reviveCaps();
                event.getOriginal().getCapability(ElementalistStatsProvider.ELEMENTALIST_STATS).ifPresent(oldStore -> {
                    event.getEntity().getCapability(ElementalistStatsProvider.ELEMENTALIST_STATS).ifPresent(newStore -> {
                        newStore.copyFrom(oldStore);
                    });
                });
                event.getOriginal().invalidateCaps();
            }
        }

        @SubscribeEvent
        public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
            if (event.phase == TickEvent.Phase.START) {
                if (event.side == LogicalSide.SERVER) {
                    event.player.getCapability(SpellStateProvider.SPELL_STATE).ifPresent(spellState -> {
                        if (event.player.tickCount % 20 == 0) {
                            spellState.addMana(10, event.player);
                        }
                        if (event.player.tickCount % 5 == 0) {
                            spellState.decreaseSpellCooldowns();
                        }
                    });
                }
                else {
                    if (event.player.isLocalPlayer()) {
                        if (event.player.tickCount % 5 == 0) {
                            ClientSpellStateData.decreaseSpellCooldowns();
                        }
                        ClientSpellStateData.decrementAltInterval();
                        ClientSpellStateData.decrementDodgeLeftInterval();
                        ClientSpellStateData.decrementDodgeRightInterval();
                    }
                }
            }
        }

        @SubscribeEvent
        public static void onJump(LivingEvent.LivingJumpEvent event) {
            if (event.getEntity().hasEffect(EffectRegistry.FROZEN.get())) {
                event.getEntity().setDeltaMovement(0, 0, 0);
            }
        }

        @SubscribeEvent
        public static void onFallDamage(LivingFallEvent event) {
            if (event.getEntity() instanceof Player player) {
                player.getCapability(SpellStateProvider.SPELL_STATE).ifPresent(spellState -> {
                    if (spellState.getFallProtection()) {
                        ((ServerLevel)player.level()).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.STONE.defaultBlockState()),
                                player.position().x, player.position().y, player.position().z, (int)(event.getDistance()*5),
                                event.getDistance()/20f, 0.2, event.getDistance()/20f, 0);
//                        ParticleUtil.createParticleCircle(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.STONE.defaultBlockState()),
//                                player.level, event.getDistance()/20f, player.position(), 0, 0, 0f);
                        List<LivingEntity> ignoredEntities = List.of(player);
                        DamageDealer damageDealer = new DamageDealer(player.position(), player, player, ignoredEntities,
                                new DamageType(4f, Element.EARTH, 0, 1f));
                        damageDealer.dealDamageSphere(event.getDistance() / 4, event.getDistance() / 4);
                        event.setDistance(Math.max(0, event.getDistance() - 20));
                    }
                    spellState.setJumpsLeft(2);
                });
            }
        }

        @SubscribeEvent
        public static void onPlayerJoinWorld(EntityJoinLevelEvent event) {
            if(!event.getLevel().isClientSide()) {
                if(event.getEntity() instanceof ServerPlayer player) {
                    player.getCapability(SpellStateProvider.SPELL_STATE).ifPresent(spellState -> {
                        MessageRegistry.sendToPlayer(new ManaSyncS2CPacket(spellState.getMana()), player);
                    });
                    player.getCapability(ElementalistStatsProvider.ELEMENTALIST_STATS).ifPresent(elementalistStats -> {
                        MessageRegistry.sendToPlayer(new PlayerLevelSyncS2CPacket(elementalistStats.getPlayerLevel()), player);
                        MessageRegistry.sendToPlayer(new UnlockedSpellsSyncS2CPacket(elementalistStats.getUnlockedSpellsList()), player);
                        for (Element element : ElementalistMaps.elementToIndexMap.keySet()) {
                            int elementIndex = ElementalistMaps.elementToIndexMap.get(element);
                            MessageRegistry.sendToPlayer(new ElementLevelSyncS2CPacket(elementalistStats.getElementLevel(element),
                                    elementIndex), player);
                            MessageRegistry.sendToPlayer(new ElementExperienceSyncS2CPacket(elementalistStats.getElementExperience(element),
                                    elementIndex), player);
                            MessageRegistry.sendToPlayer(new ElementSkillPointsSyncS2CPacket(elementalistStats.getElementSkillPoints(element),
                                    elementIndex), player);
                        }
                    });
                }
            }
        }
    }

}