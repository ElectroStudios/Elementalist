package net.electro.elementalist.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.electro.elementalist.Elementalist;
import net.electro.elementalist.client.ClientSpellStateData;
import net.electro.elementalist.data.ElementalistStatsProvider;
import net.electro.elementalist.data.SpellStateProvider;
import net.electro.elementalist.item.bracelets.BraceletMaster;
import net.electro.elementalist.item.bracelets.FireBracelet;
import net.electro.elementalist.util.SpellIdMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.ScreenEvent;

public class SpellStateGui extends GuiComponent {
    Minecraft mc = Minecraft.getInstance();
    ResourceLocation active_spell_frame_texture = new ResourceLocation(Elementalist.MOD_ID, "textures/gui/active_spell_frame.png");
    ResourceLocation inactive_spell_frame_texture = new ResourceLocation(Elementalist.MOD_ID, "textures/gui/inactive_spell_frame.png");
    ResourceLocation spell_cooldown_cover_texture = new ResourceLocation(Elementalist.MOD_ID, "textures/gui/cooldown_cover.png");
    public boolean shouldDisplayHUD() {
        ItemStack mainHand = mc.player.getMainHandItem();
        return mainHand.getItem() instanceof BraceletMaster;
    }

    private void drawSpellFrame(PoseStack poseStack, float x, float y, int selectedSpellNumber) {
        ItemStack heldItem = mc.player.getMainHandItem();
        int spellId = ((BraceletMaster)heldItem.getItem()).getNbtData(selectedSpellNumber, heldItem);
        int currentCooldown = ClientSpellStateData.getCooldown(spellId);

        int maxCooldown = SpellIdMap.map.get(spellId).spellCooldownTicks;

        if (currentCooldown > 0) {
            RenderSystem.setShaderTexture(0, inactive_spell_frame_texture);
        }
        else {
            RenderSystem.setShaderTexture(0, active_spell_frame_texture);
        }
        blit(poseStack, (int)x, (int)y, 0, 0, 32, 32, 32, 32);

        int cooldownOffset = (int)(32 * (currentCooldown / (float)maxCooldown));
        RenderSystem.setShaderTexture(0, spell_cooldown_cover_texture);
        blit(poseStack, (int)x, (int)y + 32 - cooldownOffset, 0, 32 - cooldownOffset, 32, cooldownOffset, 32, 32);
    }

    public void drawHUD(PoseStack poseStack) {
        if (!shouldDisplayHUD()) {
            return;
        }

        int guiHeight = mc.getWindow().getGuiScaledHeight();
        int guiWidth = mc.getWindow().getGuiScaledWidth();

        float anchorPointX = guiWidth / 10f;
        float anchorPointY = guiHeight - guiHeight / 20f;

        mc.font.draw(poseStack, "Mana: " + ClientSpellStateData.getMana(), anchorPointX, anchorPointY,0xFFFFFFFF);
        drawSpellFrame(poseStack, anchorPointX - 18, anchorPointY - 40, 0);
        drawSpellFrame(poseStack, anchorPointX + 18, anchorPointY - 40, 1);

    }
}
