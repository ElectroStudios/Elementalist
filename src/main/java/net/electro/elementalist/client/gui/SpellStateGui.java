package net.electro.elementalist.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.electro.elementalist.Elementalist;
import net.electro.elementalist.client.ClientSpellStateData;
import net.electro.elementalist.item.bracelets.ChargedStaff;
import net.electro.elementalist.registry.SpellRegistry;
import net.electro.elementalist.spell.SpellBase;
import net.electro.elementalist.util.ElementalistMaps;
import net.electro.elementalist.util.Utility;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

import java.awt.*;

public class SpellStateGui implements IGuiOverlay {
    public static final SpellStateGui instance = new SpellStateGui();

    Minecraft mc = Minecraft.getInstance();
    ResourceLocation activeSpellFrameTexture = new ResourceLocation(Elementalist.MOD_ID, "textures/gui/active_spell_frame.png");
    ResourceLocation inactiveSpellFrameTexture = new ResourceLocation(Elementalist.MOD_ID, "textures/gui/inactive_spell_frame.png");
    ResourceLocation spellCooldownCoverTexture = new ResourceLocation(Elementalist.MOD_ID, "textures/gui/cooldown_cover.png");
    ResourceLocation manaTexture = new ResourceLocation(Elementalist.MOD_ID, "textures/gui/mana.png");

    public boolean shouldDisplayHUD() {
        ItemStack mainHand = mc.player.getMainHandItem();
        return mainHand.getItem() instanceof ChargedStaff;
    }

    private void drawSpellFrame(GuiGraphics guiGraphics, float x, float y, int selectedSpellNumber) {
        PoseStack poseStack = guiGraphics.pose();
        ItemStack heldItem = mc.player.getMainHandItem();
        if (((ChargedStaff)heldItem.getItem()).hasNbtData(selectedSpellNumber, heldItem)) {
            ResourceLocation spellId = ((ChargedStaff) heldItem.getItem()).getNbtData(selectedSpellNumber, heldItem);
            int currentCooldown = ClientSpellStateData.getCooldown(spellId);
            SpellBase spell = SpellRegistry.getSpell(spellId);
            ResourceLocation spellIcon = spell.spellIcon;

            poseStack.pushPose();

            int maxCooldown = spell.spellCooldownTicks;

            if (currentCooldown > 0) {
                Utility.performBlit(guiGraphics, inactiveSpellFrameTexture, (int) x, (int) y, 0, 0,
                        32, 32, 32, 32, new Color(255, 255, 255 ,255));
            } else {
                Utility.performBlit(guiGraphics, activeSpellFrameTexture, (int) x, (int) y, 0, 0, 32, 32, 32, 32, new Color(255, 255, 255 ,255));
            }

            Utility.performBlit(guiGraphics, spellIcon, (int) x, (int) y, 0, 0, 32, 32, 32, 32, new Color(255, 255, 255 ,255));

            int cooldownOffset = (int) (32 * (currentCooldown / (float) maxCooldown));
            Utility.performBlit(guiGraphics, spellCooldownCoverTexture, (int) x, (int) y + 32 - cooldownOffset, 0, 32 - cooldownOffset, 32, cooldownOffset, 32, 32, new Color(255, 255, 255 ,255));

            Utility.performBlit(guiGraphics, manaTexture, (int) x + 2, (int) y + 34, 0, 0, 8, 8, 8, 8, new Color(255, 255, 255 ,255));
            poseStack.popPose();
            poseStack.pushPose();
            guiGraphics.drawString(mc.font, Integer.toString(spell.manaCost), (int) x + 12, (int) y + 34, 0xFFFFFFFF, true);
            poseStack.popPose();
        }
        else {
            poseStack.pushPose();
            Utility.performBlit(guiGraphics, inactiveSpellFrameTexture, (int) x, (int) y, 0, 0, 32, 32, 32, 32, new Color(255, 255, 255 ,255));
            poseStack.popPose();
        }
    }

    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        PoseStack poseStack = guiGraphics.pose();
        if (!shouldDisplayHUD()) {
            return;
        }

        int guiHeight = mc.getWindow().getGuiScaledHeight();
        int guiWidth = mc.getWindow().getGuiScaledWidth();

        float anchorPointX = guiWidth / 10f;
        float anchorPointY = guiHeight - guiHeight / 20f;


        poseStack.pushPose();
        Utility.performBlit(guiGraphics, manaTexture, (int)anchorPointX + 4, 
                (int)anchorPointY + 4, 0, 0, 8, 8, 8, 8,
                new Color(255, 255, 255, 255));
        poseStack.popPose();
        poseStack.pushPose();
        guiGraphics.drawString(mc.font, Integer.toString(ClientSpellStateData.getMana()), anchorPointX + 12, anchorPointY + 4,0xFFFFFFFF, true);
        poseStack.popPose();
        drawSpellFrame(guiGraphics, anchorPointX - 18, anchorPointY - 86, 0);
        drawSpellFrame(guiGraphics, anchorPointX + 18, anchorPointY - 86, 1);
        drawSpellFrame(guiGraphics, anchorPointX - 18, anchorPointY - 42, 2);
        drawSpellFrame(guiGraphics, anchorPointX + 18, anchorPointY - 42, 3);

    }
}
