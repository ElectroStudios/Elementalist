package net.electro.elementalist.event;

import net.electro.elementalist.Elementalist;
import net.electro.elementalist.client.ClientSpellStateData;
import net.electro.elementalist.data.ElementalistStatsProvider;
import net.electro.elementalist.data.SpellStateProvider;
import net.electro.elementalist.networking.ModMessages;
import net.electro.elementalist.networking.packet.ManaSyncS2CPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

public class ModEvents {
    @Mod.EventBusSubscriber(modid = Elementalist.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModEventBusEvents {

    }

    @Mod.EventBusSubscriber(modid = Elementalist.MOD_ID)
    public static class ModForgeEvents {
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
                    if (event.player.tickCount % 5 == 0) {
                        ClientSpellStateData.decreaseSpellCooldowns();
                    }
                    ClientSpellStateData.decrementAltInterval();
                }
            }
        }

        @SubscribeEvent
        public static void onPlayerJoinWorld(EntityJoinLevelEvent event) {
            if(!event.getLevel().isClientSide()) {
                if(event.getEntity() instanceof ServerPlayer player) {
                    player.getCapability(SpellStateProvider.SPELL_STATE).ifPresent(spellState -> {
                        ModMessages.sendToPlayer(new ManaSyncS2CPacket(spellState.getMana()), player);
                    });
                }
            }
        }
    }

}
