package net.electro.elementalist.event;

import net.electro.elementalist.Elementalist;
import net.electro.elementalist.client.ClientSpellStateData;
import net.electro.elementalist.client.gui.GrimoireScreen;
import net.electro.elementalist.client.gui.SpellSelectWheelGui;
import net.electro.elementalist.client.gui.SpellStateGui;
import net.electro.elementalist.client.models.projectiles.FireballBasicModel;
import net.electro.elementalist.client.models.projectiles.IceSpearModel;
import net.electro.elementalist.client.models.spellentities.WaterSlashModel;
import net.electro.elementalist.client.models.spellentities.WaterStreamModel;
import net.electro.elementalist.client.particle.ModParticles;
import net.electro.elementalist.client.particle.fire.FireExplosionParticles;
import net.electro.elementalist.client.particle.fire.FireFlashParticles;
import net.electro.elementalist.client.renderer.mobs.WaterSpiritRenderer;
import net.electro.elementalist.client.renderer.projectiles.FireballBasicRenderer;
import net.electro.elementalist.client.renderer.projectiles.IceSpearRenderer;
import net.electro.elementalist.client.renderer.spellentities.MagicCircleRenderer;
import net.electro.elementalist.client.renderer.spellentities.ShieldSpellRenderer;
import net.electro.elementalist.client.renderer.spellentities.WaterSlashRenderer;
import net.electro.elementalist.client.renderer.spellentities.WaterStreamRenderer;
import net.electro.elementalist.entities.ModEntities;
import net.electro.elementalist.entities.mobs.WaterSpiritEntity;
import net.electro.elementalist.item.ElementalistGrimoire;
import net.electro.elementalist.item.bracelets.ChargedStaff;
import net.electro.elementalist.networking.ModMessages;
import net.electro.elementalist.networking.packet.ActivateShieldC2SPacket;
import net.electro.elementalist.networking.packet.ActivateSpellC2SPacket;
import net.electro.elementalist.client.KeyBinding;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.opengl.GL11;

public class ClientEvents {
    @Mod.EventBusSubscriber(modid= Elementalist.MOD_ID, value = Dist.CLIENT)
    public static class ClientForgeEvents {
        private static final Minecraft mc = Minecraft.getInstance();
        private static final SpellStateGui spellStateGui = new SpellStateGui();

        @SubscribeEvent
        public static void renderHUD(final RenderGuiOverlayEvent.Post event) {
            spellStateGui.drawHUD(event.getPoseStack());


        }
        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event) {

            if (mc.player == null) {
                return;
            }

            if (event.getKey() == KeyBinding.ALTERNATE_SPELLS_KEY.getKey().getValue()) {
                if (event.getAction() == 0) {
                    if (ClientSpellStateData.isInAltInterval()) {
                        ClientSpellStateData.setAltInterval(0);
                        ItemStack heldItem = mc.player.getMainHandItem();
                        if (heldItem.getItem() instanceof ChargedStaff) {
                            ModMessages.sendToServer(new ActivateShieldC2SPacket());
                        }
                    }
                    else {
                        ClientSpellStateData.setAltInterval(20);
                    }
                    ClientSpellStateData.setIsSpellAlternateActive(false);
                }
                else if (event.getAction() == 1) {
                    ClientSpellStateData.setIsSpellAlternateActive(true);
                }
            }

            if (mc.screen == null || mc.screen instanceof SpellSelectWheelGui) {
                ItemStack heldItem = mc.player.getMainHandItem();
                if (heldItem.getItem() instanceof ChargedStaff) {
                    if (event.getKey() == KeyBinding.SPELL_SELECT_KEY.getKey().getValue()) {
                        if (mc.screen instanceof SpellSelectWheelGui && event.getAction() == 0) {
                            mc.player.closeContainer();
                        }
                        else if (event.getAction() == 1) {
                            mc.setScreen(new SpellSelectWheelGui());
                        }
                    }
                }
            }
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
                    if (KeyBinding.ALTERNATE_SPELLS_KEY.isDown()) {
                        ModMessages.sendToServer(new ActivateSpellC2SPacket(2));
                    }
                    else {
                        ModMessages.sendToServer(new ActivateSpellC2SPacket(0));
                    }
                }
                else if(event.isUseItem()) {
                    event.setCanceled(true);
                    if (KeyBinding.ALTERNATE_SPELLS_KEY.isDown()) {
                        ModMessages.sendToServer(new ActivateSpellC2SPacket(3));
                    } else {
                        ModMessages.sendToServer(new ActivateSpellC2SPacket(1));
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
            event.register(KeyBinding.ALTERNATE_SPELLS_KEY);
        }

        @SubscribeEvent
        public static void entityRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(ModEntities.FIREBALL_BASIC.get(), FireballBasicRenderer::new);
            event.registerEntityRenderer(ModEntities.FIRE_PULSE.get(), NoopRenderer::new);
            event.registerEntityRenderer(ModEntities.FIRE_BREATH.get(), NoopRenderer::new);
            event.registerEntityRenderer(ModEntities.FIRE_EXPLOSION.get(), NoopRenderer::new);
            event.registerEntityRenderer(ModEntities.FIRE_WAVE.get(), NoopRenderer::new);
            event.registerEntityRenderer(ModEntities.FIRE_CLUSTER_EXPLOSION.get(), NoopRenderer::new);
            event.registerEntityRenderer(ModEntities.FIRE_CLUSTER_EXPLOSION_PART.get(), NoopRenderer::new);
            event.registerEntityRenderer(ModEntities.WATER_SLASH.get(), WaterSlashRenderer::new);
            event.registerEntityRenderer(ModEntities.WATER_STREAM.get(), WaterStreamRenderer::new);
            event.registerEntityRenderer(ModEntities.ICE_SPEAR.get(), IceSpearRenderer::new);
            event.registerEntityRenderer(ModEntities.SHIELD_SPELL.get(), ShieldSpellRenderer::new);
            event.registerEntityRenderer(ModEntities.MAGIC_CIRCLE.get(), MagicCircleRenderer::new);
            event.registerEntityRenderer(ModEntities.WATER_SPIRIT.get(), WaterSpiritRenderer::new);
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
