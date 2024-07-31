package net.electro.elementalist.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.electro.elementalist.Elementalist;
import net.electro.elementalist.client.ClientSpellStateData;
import net.electro.elementalist.item.bracelets.ChargedStaff;
import net.electro.elementalist.registry.MessageRegistry;
import net.electro.elementalist.networking.SelectSpellC2SPacket;
import net.electro.elementalist.registry.SpellRegistry;
import net.electro.elementalist.spell.SpellBase;
import net.electro.elementalist.util.ElementalistMaps;
import net.electro.elementalist.util.Utility;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;

import java.awt.*;
import java.util.List;

public class SpellSelectWheelGui extends Screen {
    private final Minecraft mc = Minecraft.getInstance();
    private SpellBase hoveredSpell;
    ResourceLocation spellSelectWheelTexture = new ResourceLocation(Elementalist.MOD_ID, "textures/gui/spell_select_wheel.png");
    ResourceLocation activeSpellFrameTexture = new ResourceLocation(Elementalist.MOD_ID, "textures/gui/active_spell_frame.png");
    ResourceLocation inactiveSpellFrameTexture = new ResourceLocation(Elementalist.MOD_ID, "textures/gui/inactive_spell_frame.png");
    ResourceLocation spellCooldownCoverTexture = new ResourceLocation(Elementalist.MOD_ID, "textures/gui/cooldown_cover.png");
    ResourceLocation highlightTexture = new ResourceLocation(Elementalist.MOD_ID, "textures/gui/highlight.png");
    ResourceLocation manaTexture = new ResourceLocation(Elementalist.MOD_ID, "textures/gui/mana.png");

    public SpellSelectWheelGui() {
        super(Component.literal(""));
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public SpellSelectWheelGui(Component pTitle) {
        super(pTitle);
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        PoseStack poseStack = pGuiGraphics.pose();
        int guiHeight = mc.getWindow().getGuiScaledHeight();
        int guiWidth = mc.getWindow().getGuiScaledWidth();

        float anchorPointX = guiWidth / 2f;
        float anchorPointY = guiHeight / 2f;


        poseStack.pushPose();
        Utility.performBlit(pGuiGraphics, spellSelectWheelTexture, (int)anchorPointX - 100, (int)anchorPointY - 100,
                0, 0, 200, 200, 200, 200, new Color(255, 255, 255, 255));
        poseStack.popPose();


        List<SpellBase> spellList = SpellRegistry.getUnlockedSpellsOfElement(((ChargedStaff)mc.player.getMainHandItem().getItem()).ELEMENT,
                ClientSpellStateData.getUnlockedSpells());

        int segmentsAmount = spellList.size();

        int hoveredSegment = getHoveredSegment(pMouseX, pMouseY, anchorPointX, anchorPointY, segmentsAmount);
        this.hoveredSpell = spellList.get(hoveredSegment);

        for (int i = 0; i < segmentsAmount; i++) {
            renderSegment(pGuiGraphics, spellList.get(i), segmentsAmount, i, (int)anchorPointX, (int)anchorPointY, i == hoveredSegment);
        }
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (!ClientSpellStateData.getIsSpellAlternateActive()) {
            if (pButton == 0) {
                MessageRegistry.sendToServer(new SelectSpellC2SPacket(hoveredSpell.spellId, 0));
            }
            else {
                MessageRegistry.sendToServer(new SelectSpellC2SPacket(hoveredSpell.spellId, 1));
            }
        }
        else {
            if (pButton == 0) {
                MessageRegistry.sendToServer(new SelectSpellC2SPacket(hoveredSpell.spellId, 2));
            }
            else {
                MessageRegistry.sendToServer(new SelectSpellC2SPacket(hoveredSpell.spellId, 3));
            }
        }

        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    private void drawSpellFrame(GuiGraphics guiGraphics, int x, int y, SpellBase spell, boolean isHovered) {
        PoseStack poseStack = guiGraphics.pose();
        ResourceLocation spellId = spell.spellId;
        int currentCooldown = ClientSpellStateData.getCooldown(spellId);

        int maxCooldown = spell.spellCooldownTicks;
        ResourceLocation spellIcon = spell.spellIcon;

        poseStack.pushPose();
        if (isHovered) {
            Utility.performBlit(guiGraphics, highlightTexture, x - 13, y - 13, 0, 0, 64,
                    64, 64, 64, new Color(255, 255, 255, 255));
        }

        if (currentCooldown > 0) {
            Utility.performBlit(guiGraphics, inactiveSpellFrameTexture, x, y, 0, 0, 32, 32,
                    32, 32, new Color(255, 255, 255, 255));
        }
        else {
            Utility.performBlit(guiGraphics, activeSpellFrameTexture, x, y, 0, 0, 32, 32,
                    32, 32, new Color(255, 255, 255, 255));
        }

        Utility.performBlit(guiGraphics, spellIcon, x, y, 0, 0, 32, 32, 32,
                32, new Color(255, 255, 255, 255));

        int cooldownOffset = (int)(32 * (currentCooldown / (float)maxCooldown));

        Utility.performBlit(guiGraphics, manaTexture, x + 2, y + 34, 0, 0, 8, 8,
                8, 8, new Color(255, 255, 255, 255));

        Utility.performBlit(guiGraphics, spellCooldownCoverTexture, x, y + 32 - cooldownOffset, 0,
                32 - cooldownOffset, 32, cooldownOffset, 32, 32, new Color(255, 255, 255, 255));

        poseStack.popPose();

        poseStack.pushPose();
        Component spellName = Component.translatable("elementalist.spell_name." + spell.getName());
        int spellNameTextWidth = mc.font.width(spellName);
        
        guiGraphics.drawString(this.font, spellName, x + 16 - (spellNameTextWidth / 2), y - mc.font.lineHeight - 2, 0xFFFFFFFF, true);
        guiGraphics.drawString(this.font, Integer.toString(spell.manaCost), x + 12, y + 34, 0xFFFFFFFF, true);
        poseStack.popPose();
    }

    private void renderSegment(GuiGraphics guiGraphics, SpellBase spell, int segmentsAmount,
                               int segmentIndex, int xAnchor, int yAnchor, boolean isHovered) {
        double segmentAngle = Math.toRadians(360 / (float)segmentsAmount * segmentIndex + (180 / (float)segmentsAmount));
        int x = xAnchor + (int)(-Math.sin(segmentAngle) * 80) - 16;
        int y = yAnchor + (int)(Math.cos(segmentAngle) * 80) - 16;

        drawSpellFrame(guiGraphics, x, y, spell, isHovered);
    }

    private int getHoveredSegment(int mouseX, int mouseY, float anchorPointX, float anchorPointY, int segmentsAmount) {
        Vec2 mouseVector = new Vec2(mouseX - anchorPointX, mouseY - anchorPointY);
        Vec2 upVector = new Vec2(0, 1);

        double mouseSmallestAngle = Math.toDegrees(acosOfAngleBetweenVectors(mouseVector, upVector));
        double mouseAngle = (isMouseInLeftHalf(mouseVector, upVector) ? mouseSmallestAngle : 360 - mouseSmallestAngle);
        double segmentSize = 360f / segmentsAmount;
        return Math.min((int)(mouseAngle / segmentSize), segmentsAmount - 1);
    }

    private double acosOfAngleBetweenVectors(Vec2 vec1, Vec2 vec2) {
        return Math.acos(vec1.dot(vec2)/(vec1.length() * vec2.length()));
    }

    private boolean isMouseInLeftHalf(Vec2 vec1, Vec2 vec2) {
        return (vec2.x * vec1.y - vec2.y * vec1.x) > 0;
    }
}
