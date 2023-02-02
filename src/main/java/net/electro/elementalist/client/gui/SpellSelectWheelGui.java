package net.electro.elementalist.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.electro.elementalist.Elementalist;
import net.electro.elementalist.client.ClientSpellStateData;
import net.electro.elementalist.item.bracelets.BraceletMaster;
import net.electro.elementalist.networking.ModMessages;
import net.electro.elementalist.networking.packet.SelectSpellC2SPacket;
import net.electro.elementalist.spells.SpellsMaster;
import net.electro.elementalist.util.SpellIdMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.client.event.ScreenEvent;

import java.util.List;

public class SpellSelectWheelGui extends Screen {
    private final Minecraft mc = Minecraft.getInstance();
    private SpellsMaster hoveredSpell;
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
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {

        int guiHeight = mc.getWindow().getGuiScaledHeight();
        int guiWidth = mc.getWindow().getGuiScaledWidth();

        float anchorPointX = guiWidth / 2f;
        float anchorPointY = guiHeight / 2f;


        poseStack.pushPose();
        performBlit(spellSelectWheelTexture, poseStack, (int)anchorPointX - 100, (int)anchorPointY - 100,
                0, 0, 200, 200, 200, 200);
        poseStack.popPose();


        List<SpellsMaster> spellList = SpellIdMap.getSpellListOfElement(((BraceletMaster)mc.player.getMainHandItem().getItem()).ELEMENT);

        int segmentsAmount = spellList.size();

        int hoveredSegment = getHoveredSegment(mouseX, mouseY, anchorPointX, anchorPointY, segmentsAmount);
        this.hoveredSpell = spellList.get(hoveredSegment);

        for (int i = 0; i < segmentsAmount; i++) {
            renderSegment(poseStack, spellList.get(i), segmentsAmount, i, (int)anchorPointX, (int)anchorPointY, i == hoveredSegment);
        }
        super.render(poseStack, mouseX,  mouseY, partialTick);
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (!ClientSpellStateData.getIsSpellAlternateActive()) {
            if (pButton == 0) {
                ModMessages.sendToServer(new SelectSpellC2SPacket(hoveredSpell.spellId, 0));
            }
            else {
                ModMessages.sendToServer(new SelectSpellC2SPacket(hoveredSpell.spellId, 1));
            }
        }
        else {
            if (pButton == 0) {
                ModMessages.sendToServer(new SelectSpellC2SPacket(hoveredSpell.spellId, 2));
            }
            else {
                ModMessages.sendToServer(new SelectSpellC2SPacket(hoveredSpell.spellId, 3));
            }
        }

        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    private void drawSpellFrame(PoseStack poseStack, int x, int y, SpellsMaster spell, boolean isHovered) {
        int spellId = spell.spellId;
        int currentCooldown = ClientSpellStateData.getCooldown(spellId);

        int maxCooldown = spell.spellCooldownTicks;
        ResourceLocation spellIcon = spell.spellIcon;

        poseStack.pushPose();
        if (isHovered) {
            performBlit(highlightTexture, poseStack, x - 13, y - 13, 0, 0, 64, 64, 64, 64);
        }

        if (currentCooldown > 0) {
            performBlit(inactiveSpellFrameTexture, poseStack, x, y, 0, 0, 32, 32, 32, 32);
        }
        else {
            performBlit(activeSpellFrameTexture, poseStack, x, y, 0, 0, 32, 32, 32, 32);
        }

        performBlit(spellIcon, poseStack, x, y, 0, 0, 32, 32, 32, 32);

        int cooldownOffset = (int)(32 * (currentCooldown / (float)maxCooldown));

        performBlit(manaTexture, poseStack, x + 2, y + 34, 0, 0, 8, 8, 8, 8);

        performBlit(spellCooldownCoverTexture, poseStack, x, y + 32 - cooldownOffset, 0, 32 - cooldownOffset, 32, cooldownOffset, 32, 32);

        poseStack.popPose();

        poseStack.pushPose();
        Component spellName = Component.translatable("elementalist.spell_names." + spell.spellString);
        int spellNameTextWidth = mc.font.width(spellName);
        mc.font.drawShadow(poseStack, spellName, x + 16 - (spellNameTextWidth / 2), y - mc.font.lineHeight - 2, 0xFFFFFFFF);
        mc.font.drawShadow(poseStack, Integer.toString(spell.manaCost), x + 12, y + 34, 0xFFFFFFFF);
        poseStack.popPose();
    }

    private void renderSegment(PoseStack poseStack, SpellsMaster spell, int segmentsAmount,
                               int segmentIndex, int xAnchor, int yAnchor, boolean isHovered) {
        double segmentAngle = Math.toRadians(360 / (float)segmentsAmount * segmentIndex + (180 / (float)segmentsAmount));
        int x = xAnchor + (int)(-Math.sin(segmentAngle) * 80) - 16;
        int y = yAnchor + (int)(Math.cos(segmentAngle) * 80) - 16;

        drawSpellFrame(poseStack, x, y, spell, isHovered);
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
