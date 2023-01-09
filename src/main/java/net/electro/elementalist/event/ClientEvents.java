package net.electro.elementalist.event;

import net.electro.elementalist.Elementalist;
import net.electro.elementalist.client.ClientSpellStateData;
import net.electro.elementalist.client.gui.SpellStateGui;
import net.electro.elementalist.client.models.projectiles.FireballBasicModel;
import net.electro.elementalist.client.particle.ModParticles;
import net.electro.elementalist.client.particle.fireexplosion.FireExplosionParticles;
import net.electro.elementalist.client.particle.fireexplosion.FireFlashParticles;
import net.electro.elementalist.client.renderer.projectiles.FireballBasicRenderer;
import net.electro.elementalist.entities.ModEntities;
import net.electro.elementalist.item.bracelets.BraceletMaster;
import net.electro.elementalist.networking.ModMessages;
import net.electro.elementalist.networking.packet.ActivateSpellC2SPacket;
import net.electro.elementalist.networking.packet.UnlockSpellC2SPacket;
import net.electro.elementalist.client.KeyBinding;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class ClientEvents {
    @Mod.EventBusSubscriber(modid= Elementalist.MOD_ID, value = Dist.CLIENT)
    public static class ClientForgeEvents {
        private static final SpellStateGui spellStateGui = new SpellStateGui();

        @SubscribeEvent
        public static void renderHUD(final RenderGuiOverlayEvent.Post event) {
            spellStateGui.drawHUD(event.getPoseStack());
        }
        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event) {
            if (KeyBinding.SPELL_SELECT_KEY.consumeClick()) {
                ModMessages.sendToServer(new UnlockSpellC2SPacket());
            }
        }

        @SubscribeEvent
        public static void onMouseClick(InputEvent.InteractionKeyMappingTriggered event)
        {
            if (event.isCanceled()) {
                return;
            }

            Minecraft mc = Minecraft.getInstance();
            Player player = mc.player;
            if (player == null) {
                return;
            }

            ItemStack heldItem = player.getMainHandItem();
            if (heldItem.getItem() instanceof BraceletMaster) {
                if (event.isAttack()) {
                    event.setSwingHand(false);
                    event.setCanceled(true);
                    if (KeyBinding.ALTERNATE_SPELLS_KEY.isDown()) {

                    }
                    else {
                        ModMessages.sendToServer(new ActivateSpellC2SPacket(0));
                    }
                }
                else if(event.isUseItem()) {
                    event.setCanceled(true);
                    if (KeyBinding.ALTERNATE_SPELLS_KEY.isDown()) {

                    } else {
                        ModMessages.sendToServer(new ActivateSpellC2SPacket(1));
                    }
                }
            }
        }

    }

    @Mod.EventBusSubscriber(modid = Elementalist.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents {
        @SubscribeEvent
        public static void registerParticleFactories(final RegisterParticleProvidersEvent event) {
            event.register(ModParticles.FIRE_EXPLOSION_PARTICLES.get(),
                    FireExplosionParticles.Provider::new);
            event.register(ModParticles.FIRE_FLASH_PARTICLES.get(),
                    FireFlashParticles.Provider::new);
        }
        @SubscribeEvent
        public static void onKeyRegister (RegisterKeyMappingsEvent event) {
            event.register(KeyBinding.SPELL_SELECT_KEY);
        }

        @SubscribeEvent
        public static void entityRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(ModEntities.FIREBALL_BASIC.get(), FireballBasicRenderer::new);
            event.registerEntityRenderer(ModEntities.FIRE_PULSE.get(), NoopRenderer::new);
        }

        @SubscribeEvent
        public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
            event.registerLayerDefinition(FireballBasicModel.LAYER_LOCATION, FireballBasicModel::createBodyLayer);
        }

    }
}
