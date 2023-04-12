package net.electro.elementalist.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.electro.elementalist.Elementalist;
import net.electro.elementalist.client.ClientSpellStateData;
import net.electro.elementalist.item.bracelets.ChargedStaff;
import net.electro.elementalist.spells.SpellsMaster;
import net.electro.elementalist.util.ElementalistMaps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class SpellStateGui extends GuiComponent {
    Minecraft mc = Minecraft.getInstance();
    ResourceLocation activeSpellFrameTexture = new ResourceLocation(Elementalist.MOD_ID, "textures/gui/active_spell_frame.png");
    ResourceLocation inactiveSpellFrameTexture = new ResourceLocation(Elementalist.MOD_ID, "textures/gui/inactive_spell_frame.png");
    ResourceLocation spellCooldownCoverTexture = new ResourceLocation(Elementalist.MOD_ID, "textures/gui/cooldown_cover.png");
    ResourceLocation manaTexture = new ResourceLocation(Elementalist.MOD_ID, "textures/gui/mana.png");
    public boolean shouldDisplayHUD() {
        ItemStack mainHand = mc.player.getMainHandItem();
        return mainHand.getItem() instanceof ChargedStaff;
    }

    private void drawSpellFrame(PoseStack poseStack, float x, float y, int selectedSpellNumber) {
        ItemStack heldItem = mc.player.getMainHandItem();
        if (((ChargedStaff)heldItem.getItem()).hasNbtData(selectedSpellNumber, heldItem)) {
            int spellId = ((ChargedStaff) heldItem.getItem()).getNbtData(selectedSpellNumber, heldItem);
            int currentCooldown = ClientSpellStateData.getCooldown(spellId);
            SpellsMaster spell = ElementalistMaps.spellMap.get(spellId);
            ResourceLocation spellIcon = spell.spellIcon;

            poseStack.pushPose();

            int maxCooldown = spell.spellCooldownTicks;

            if (currentCooldown > 0) {
                performBlit(inactiveSpellFrameTexture, poseStack, (int) x, (int) y, 0, 0, 32, 32, 32, 32);
            } else {
                performBlit(activeSpellFrameTexture, poseStack, (int) x, (int) y, 0, 0, 32, 32, 32, 32);
            }

            performBlit(spellIcon, poseStack, (int) x, (int) y, 0, 0, 32, 32, 32, 32);

            int cooldownOffset = (int) (32 * (currentCooldown / (float) maxCooldown));
            performBlit(spellCooldownCoverTexture, poseStack, (int) x, (int) y + 32 - cooldownOffset, 0, 32 - cooldownOffset, 32, cooldownOffset, 32, 32);

            performBlit(manaTexture, poseStack, (int) x + 2, (int) y + 34, 0, 0, 8, 8, 8, 8);
            poseStack.popPose();
            poseStack.pushPose();
            mc.font.draw(poseStack, Integer.toString(spell.manaCost), (int) x + 12, (int) y + 34, 0xFFFFFFFF);
            poseStack.popPose();
        }
        else {
            poseStack.pushPose();
            performBlit(inactiveSpellFrameTexture, poseStack, (int) x, (int) y, 0, 0, 32, 32, 32, 32);
            poseStack.popPose();
        }
    }

    public void drawHUD(PoseStack poseStack) {
        if (!shouldDisplayHUD()) {
            return;
        }

        int guiHeight = mc.getWindow().getGuiScaledHeight();
        int guiWidth = mc.getWindow().getGuiScaledWidth();

        float anchorPointX = guiWidth / 10f;
        float anchorPointY = guiHeight - guiHeight / 20f;


        poseStack.pushPose();
        performBlit(manaTexture, poseStack, (int)anchorPointX + 4, (int)anchorPointY + 4, 0, 0, 8, 8, 8, 8);
        poseStack.popPose();
        poseStack.pushPose();
        mc.font.drawShadow(poseStack, Integer.toString(ClientSpellStateData.getMana()), anchorPointX + 12, anchorPointY + 4,0xFFFFFFFF);
        poseStack.popPose();
        drawSpellFrame(poseStack, anchorPointX - 18, anchorPointY - 86, 0);
        drawSpellFrame(poseStack, anchorPointX + 18, anchorPointY - 86, 1);
        drawSpellFrame(poseStack, anchorPointX - 18, anchorPointY - 42, 2);
        drawSpellFrame(poseStack, anchorPointX + 18, anchorPointY - 42, 3);

    }

    private void performBlit(ResourceLocation texture, PoseStack pPoseStack, int pX, int pY,
                             float pUOffset, float pVOffset, int pWidth, int pHeight,
                             int pTextureWidth, int pTextureHeight) {
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, texture);
        blit(pPoseStack, pX, pY, pUOffset, pVOffset, pWidth, pHeight, pTextureWidth, pTextureHeight);
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
    }
}
