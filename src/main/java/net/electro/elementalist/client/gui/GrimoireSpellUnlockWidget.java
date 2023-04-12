package net.electro.elementalist.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.electro.elementalist.Elementalist;
import net.electro.elementalist.spells.SpellsMaster;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class GrimoireSpellUnlockWidget extends GuiComponent {

    public final int X_OFFSET;
    public final int Y_OFFSET;
    public final int SIZE_X = 16;
    public final int SIZE_Y = 16;
    public final SpellsMaster spell;
    public boolean isHovered = false;
    public boolean isUnlocked = false;
    private List<GrimoireSpellUnlockWidget> children = new ArrayList<>();
    private final ResourceLocation frameIconUnlocked = new ResourceLocation(Elementalist.MOD_ID, "textures/gui/active_spell_frame.png");
    private final ResourceLocation frameIconLocked = new ResourceLocation(Elementalist.MOD_ID, "textures/gui/inactive_spell_frame.png");

    public GrimoireSpellUnlockWidget (SpellsMaster spell, int xOffset, int yOffset) {
        this.X_OFFSET = xOffset;
        this.Y_OFFSET = yOffset;
        this.spell = spell;
    }
    public void draw(PoseStack poseStack, int anchorX, int anchorY, List<Integer> unlockedSpells, boolean previousUnlocked) {
        boolean unlocked = unlockedSpells.contains(spell.spellId);
        isUnlocked = unlocked;
        performBlit((unlocked ? frameIconUnlocked : frameIconLocked), poseStack,
                X_OFFSET + anchorX, Y_OFFSET + anchorY, 0, 0, SIZE_X, SIZE_Y, SIZE_X, SIZE_Y,
                (previousUnlocked ? (isHovered ? 1f : 0.8f) : 0f));
        performBlit(spell.spellIcon, poseStack, X_OFFSET + anchorX, Y_OFFSET + anchorY, 0, 0,
                SIZE_X, SIZE_Y, SIZE_X, SIZE_Y, (previousUnlocked ? (isHovered ? 1f : 0.8f) : 0f));
        for (GrimoireSpellUnlockWidget widget : children) {
            widget.draw(poseStack, anchorX, anchorY, unlockedSpells, unlocked);
            widget.drawConnection(poseStack, this, anchorX, anchorY);
        }
    }

    public void drawConnection(PoseStack poseStack, GrimoireSpellUnlockWidget spellWidget, int anchorX, int anchorY) {
        int spacing = this.Y_OFFSET - spellWidget.SIZE_Y - spellWidget.Y_OFFSET;
        this.vLine(poseStack, anchorX + this.X_OFFSET + this.SIZE_X / 2, anchorY + this.Y_OFFSET,
                anchorY + this.Y_OFFSET - spacing / 2,
                0xFF646464);
        this.hLine(poseStack, anchorX + this.X_OFFSET + this.SIZE_X / 2,
                anchorX + spellWidget.X_OFFSET + spellWidget.SIZE_X / 2, anchorY + this.Y_OFFSET - spacing / 2,
                0xFF646464);
        this.vLine(poseStack, anchorX + spellWidget.X_OFFSET + spellWidget.SIZE_X / 2,
                anchorY + this.Y_OFFSET - spacing / 2, anchorY + spellWidget.Y_OFFSET + spellWidget.SIZE_Y,
                0xFF646464);
    }

    public GrimoireSpellUnlockWidget widgetHovered(double MouseX, double MouseY, int anchorX, int anchorY,
                                                   List<Integer> unlockedSpells, boolean previousUnlocked) {
        boolean unlocked = unlockedSpells.contains(spell.spellId);
        if (this.X_OFFSET + anchorX <= MouseX && this.X_OFFSET + this.SIZE_X + anchorX >= MouseX
                && anchorY + this.Y_OFFSET <= MouseY && anchorY + this.Y_OFFSET + this.SIZE_Y >= MouseY &&
                previousUnlocked) {
            this.isHovered = true;
            return this;
        }
        else {
            this.isHovered = false;
            for (GrimoireSpellUnlockWidget child : children) {
                GrimoireSpellUnlockWidget childHoveredWidget = child.widgetHovered(MouseX, MouseY, anchorX, anchorY,
                        unlockedSpells, unlocked);
                if (childHoveredWidget != null) {
                    return childHoveredWidget;
                }
            }
            return null;
        }
    }

    public void addChild(GrimoireSpellUnlockWidget widget) {
        children.add(widget);
    }


    private void performBlit(ResourceLocation texture, PoseStack pPoseStack, int pX, int pY,
                             float pUOffset, float pVOffset, int pWidth, int pHeight,
                             int pTextureWidth, int pTextureHeight, float brightness) {
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(brightness, brightness, brightness, 1f);
        RenderSystem.setShaderTexture(0, texture);
        blit(pPoseStack, pX, pY, pUOffset, pVOffset, pWidth, pHeight, pTextureWidth, pTextureHeight);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
    }
}
