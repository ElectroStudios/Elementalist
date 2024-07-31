package net.electro.elementalist.event;

import net.electro.elementalist.Elementalist;
import net.electro.elementalist.client.ClientSpellStateData;
import net.electro.elementalist.client.gui.GrimoireScreen;
import net.electro.elementalist.client.gui.SpellSelectWheelGui;
import net.electro.elementalist.client.gui.SpellStateGui;
import net.electro.elementalist.client.keybinding.keyaction.KeyActionBase;
import net.electro.elementalist.client.keybinding.keyaction.ToggleCombatModeKeyAction;
import net.electro.elementalist.client.model.projectiles.FireballBasicModel;
import net.electro.elementalist.client.model.projectiles.IceSpearModel;
import net.electro.elementalist.client.model.spellentities.WaterSlashModel;
import net.electro.elementalist.client.model.spellentities.WaterStreamModel;
import net.electro.elementalist.registry.ParticleRegistry;
import net.electro.elementalist.client.particle.fire.FireExplosionParticles;
import net.electro.elementalist.client.particle.fire.FireFlashParticles;
import net.electro.elementalist.client.renderer.mobs.BakenekoRenderer;
import net.electro.elementalist.client.renderer.mobs.LesserDemonRenderer;
import net.electro.elementalist.client.renderer.mobs.WaterSpiritRenderer;
import net.electro.elementalist.client.renderer.projectiles.AirbladeRenderer;
import net.electro.elementalist.client.renderer.projectiles.FireballBasicRenderer;
import net.electro.elementalist.client.renderer.projectiles.IceSpearRenderer;
import net.electro.elementalist.client.renderer.spellentities.*;
import net.electro.elementalist.registry.EntityRegistry;
import net.electro.elementalist.item.ElementalistGrimoire;
import net.electro.elementalist.item.bracelets.ChargedStaff;
import net.electro.elementalist.registry.MessageRegistry;
import net.electro.elementalist.networking.ActivateShieldC2SPacket;
import net.electro.elementalist.networking.ActivateSpellC2SPacket;
import net.electro.elementalist.client.keybinding.KeyBindingRegistry;
import net.electro.elementalist.networking.MovementSkillInputC2SPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

public class ClientEvents {
    @Mod.EventBusSubscriber(modid= Elementalist.MOD_ID, value = Dist.CLIENT)
    public static class ClientForgeEvents {
        private static final Minecraft mc = Minecraft.getInstance();
        private static final List<KeyActionBase> keyActions = List.of(
                new ToggleCombatModeKeyAction()
        );

        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event) {

            if (mc.player == null) {
                return;
            }

            if (mc.screen == null) {
                if (event.getAction() == 1) {
                    if (event.getKey() == mc.options.keyLeft.getKey().getValue()) {
                        if (ClientSpellStateData.isInDodgeLeftInterval()) {
                            ClientSpellStateData.setDodgeLeftInterval(0);
                            MessageRegistry.sendToServer(new MovementSkillInputC2SPacket("dodgeLeft"));
                        } else {
                            ClientSpellStateData.setDodgeLeftInterval(8);
                        }
                    } else if (event.getKey() == mc.options.keyRight.getKey().getValue()) {
                        if (ClientSpellStateData.isInDodgeRightInterval()) {
                            ClientSpellStateData.setDodgeRightInterval(0);
                            MessageRegistry.sendToServer(new MovementSkillInputC2SPacket("dodgeRight"));
                        } else {
                            ClientSpellStateData.setDodgeRightInterval(8);
                        }
                    } else if (event.getKey() == mc.options.keyJump.getKey().getValue()) {
                        if (!mc.player.onGround()) {
                            MessageRegistry.sendToServer(new MovementSkillInputC2SPacket("jump"));
                        }
                    } else if (event.getKey() == mc.options.keyShift.getKey().getValue()) {
                        MessageRegistry.sendToServer(new MovementSkillInputC2SPacket("activateFallProtection"));
                    } else if (event.getKey() == mc.options.keySprint.getKey().getValue()) {
                        MessageRegistry.sendToServer(new MovementSkillInputC2SPacket("activateMovementSlowdown"));
                    }
                } else if (event.getAction() == 0) {
                    if (event.getKey() == mc.options.keyShift.getKey().getValue()) {
                        MessageRegistry.sendToServer(new MovementSkillInputC2SPacket("deactivateFallProtection"));
                    } else if (event.getKey() == mc.options.keySprint.getKey().getValue()) {
                        MessageRegistry.sendToServer(new MovementSkillInputC2SPacket("deactivateMovementSlowdown"));
                    }
                }
            }

            if (event.getKey() == KeyBindingRegistry.ALTERNATE_SPELLS_KEY.getKey().getValue()) {
                if (event.getAction() == 0) {
                    if (ClientSpellStateData.isInAltInterval()) {
                        ClientSpellStateData.setAltInterval(0);
                        ItemStack heldItem = mc.player.getMainHandItem();
                        if (heldItem.getItem() instanceof ChargedStaff) {
                            MessageRegistry.sendToServer(new ActivateShieldC2SPacket());
                        }
                    } else {
                        ClientSpellStateData.setAltInterval(20);
                    }
                    ClientSpellStateData.setIsSpellAlternateActive(false);
                } else if (event.getAction() == 1) {
                    ClientSpellStateData.setIsSpellAlternateActive(true);
                }
            }

            if (mc.screen == null || mc.screen instanceof SpellSelectWheelGui) {
                ItemStack heldItem = mc.player.getMainHandItem();
                if (heldItem.getItem() instanceof ChargedStaff) {
                    if (event.getKey() == KeyBindingRegistry.SPELL_SELECT_KEY.getKey().getValue()) {
                        if (mc.screen instanceof SpellSelectWheelGui && event.getAction() == 0) {
                            mc.player.closeContainer();
                        } else if (event.getAction() == 1) {
                            mc.setScreen(new SpellSelectWheelGui());
                        }
                    }
                }
            }

            keyActions
                    .stream()
                    .filter(keyAction -> keyAction.keyMapping.getKey().getValue() == event.getKey())
                    .findFirst()
                    .ifPresent(triggeredKeyAction -> triggeredKeyAction.trigger(event));

        }

        @SubscribeEvent
        public static void onMouseClick(InputEvent.InteractionKeyMappingTriggered event)
        {
            if (event.isCanceled()) {
                return;
            }

            Player player = mc.player;
            if (player == null) {
                return;
            }

            ItemStack heldItem = player.getMainHandItem();
            if (heldItem.getItem() instanceof ChargedStaff) {
                if (event.isAttack()) {
                    event.setSwingHand(false);
                    event.setCanceled(true);
                    if (KeyBindingRegistry.ALTERNATE_SPELLS_KEY.isDown()) {
                        MessageRegistry.sendToServer(new ActivateSpellC2SPacket(2));
                    }
                    else {
                        MessageRegistry.sendToServer(new ActivateSpellC2SPacket(0));
                    }
                }
                else if(event.isUseItem()) {
                    event.setCanceled(true);
                    if (KeyBindingRegistry.ALTERNATE_SPELLS_KEY.isDown()) {
                        MessageRegistry.sendToServer(new ActivateSpellC2SPacket(3));
                    } else {
                        MessageRegistry.sendToServer(new ActivateSpellC2SPacket(1));
                    }
                }
            }
            if (heldItem.getItem() instanceof ElementalistGrimoire) {
                if (event.isUseItem()) {
                    event.setCanceled(true);
                    if (mc.screen == null) {
                        mc.setScreen(new GrimoireScreen());
                    } else if (mc.screen instanceof GrimoireScreen) {
                        mc.player.closeContainer();
                    }
                }
            }
        }

        @SubscribeEvent
        public static void onRenderGameOverlay(RenderGuiOverlayEvent.Pre event) {
            if (ClientSpellStateData.getCombatMode() && event.getOverlay().id() == VanillaGuiOverlay.HOTBAR.id()) {
                event.setCanceled(true);
            }
        }

    }

    @Mod.EventBusSubscriber(modid = Elementalist.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents {

        @SubscribeEvent
        public static void onRegisterOverlays(RegisterGuiOverlaysEvent event) {
            event.registerAbove(VanillaGuiOverlay.HOTBAR.id(), "spell_state", SpellStateGui.instance);
        }

        @SubscribeEvent
        public static void registerParticleFactories(final RegisterParticleProvidersEvent event) {
            event.registerSpriteSet(ParticleRegistry.FIRE_EXPLOSION_PARTICLES.get(), FireExplosionParticles.Provider::new);
            event.registerSpriteSet(ParticleRegistry.FIRE_FLASH_PARTICLES.get(), FireFlashParticles.Provider::new);
        }
        @SubscribeEvent
        public static void onKeyRegister (RegisterKeyMappingsEvent event) {
            event.register(KeyBindingRegistry.SPELL_SELECT_KEY);
            event.register(KeyBindingRegistry.ALTERNATE_SPELLS_KEY);
            event.register(KeyBindingRegistry.TOGGLE_COMBAT_MODE_KEY);
        }

        @SubscribeEvent
        public static void entityRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(EntityRegistry.FIREBALL_BASIC.get(), FireballBasicRenderer::new);
            event.registerEntityRenderer(EntityRegistry.FIRE_PULSE.get(), NoopRenderer::new);
            event.registerEntityRenderer(EntityRegistry.FIRE_BREATH.get(), NoopRenderer::new);
            event.registerEntityRenderer(EntityRegistry.FIRE_EXPLOSION.get(), NoopRenderer::new);
            event.registerEntityRenderer(EntityRegistry.FIRE_WAVE.get(), NoopRenderer::new);
            event.registerEntityRenderer(EntityRegistry.FIRE_CLUSTER_EXPLOSION.get(), NoopRenderer::new);
            event.registerEntityRenderer(EntityRegistry.FIRE_CLUSTER_EXPLOSION_PART.get(), NoopRenderer::new);
            event.registerEntityRenderer(EntityRegistry.ICICLE_BARRAGE.get(), NoopRenderer::new);
            event.registerEntityRenderer(EntityRegistry.WATER_SLASH.get(), WaterSlashRenderer::new);
            event.registerEntityRenderer(EntityRegistry.WATER_STREAM.get(), WaterStreamRenderer::new);
            event.registerEntityRenderer(EntityRegistry.ICE_SPEAR.get(), IceSpearRenderer::new);
            event.registerEntityRenderer(EntityRegistry.THUNDERBOLT.get(), ThunderboltRenderer::new);
            event.registerEntityRenderer(EntityRegistry.AIRBLADE.get(), AirbladeRenderer::new);
            event.registerEntityRenderer(EntityRegistry.SHIELD_SPELL.get(), ShieldSpellRenderer::new);
            event.registerEntityRenderer(EntityRegistry.MAGIC_CIRCLE.get(), MagicCircleRenderer::new);
            event.registerEntityRenderer(EntityRegistry.WATER_SPIRIT.get(), WaterSpiritRenderer::new);
            event.registerEntityRenderer(EntityRegistry.LESSER_DEMON.get(), LesserDemonRenderer::new);
            event.registerEntityRenderer(EntityRegistry.BAKENEKO.get(), BakenekoRenderer::new);
        }

        @SubscribeEvent
        public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
            event.registerLayerDefinition(FireballBasicModel.LAYER_LOCATION, FireballBasicModel::createBodyLayer);
            event.registerLayerDefinition(WaterSlashModel.LAYER_LOCATION, WaterSlashModel::createBodyLayer);
            event.registerLayerDefinition(WaterStreamModel.LAYER_LOCATION, WaterStreamModel::createBodyLayer);
            event.registerLayerDefinition(IceSpearModel.LAYER_LOCATION, IceSpearModel::createBodyLayer);
        }

    }
}
