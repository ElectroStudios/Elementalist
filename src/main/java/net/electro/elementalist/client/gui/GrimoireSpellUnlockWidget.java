package net.electro.elementalist.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.electro.elementalist.Elementalist;
import net.electro.elementalist.spells.SpellsMaster;
import net.electro.elementalist.util.SpellIdMap;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class GrimoireSpellUnlockWidget extends GuiComponent {

    public final int x;
    public final int y;
    public final int sizeX = 16;
    public final int sizeY = 16;
    public final SpellsMaster spell;
    private List<GrimoireSpellUnlockWidget> children = new ArrayList<>();
    private final ResourceLocation frameIcon = new ResourceLocation(Elementalist.MOD_ID, "textures/gui/active_spell_frame.png");

    public GrimoireSpellUnlockWidget (SpellsMaster spell, int x, int y) {
        this.x = x;
        this.y = y;
        this.spell = spell;
    }
    public void draw(PoseStack poseStack) {
        performBlit(frameIcon, poseStack, x, y, 0, 0, sizeX, sizeY, sizeX, sizeY);
        performBlit(spell.spellIcon, poseStack, x, y, 0, 0, sizeX, sizeY, sizeX, sizeY);
        for (GrimoireSpellUnlockWidget widget : children) {
            widget.draw(poseStack);
            widget.drawConnection(poseStack, this);
        }
    }

    public void drawConnection(PoseStack poseStack, GrimoireSpellUnlockWidget spellWidget) {
        int spacing = this.y - spellWidget.sizeY - spellWidget.y;
        this.vLine(poseStack, this.x + this.sizeX / 2, this.y, this.y - spacing / 2, 0xFFFFFFFF);
        this.hLine(poseStack, this.x + this.sizeX / 2, spellWidget.x + spellWidget.sizeX / 2, this.y - spacing / 2, 0xFFFFFFFF);
        this.vLine(poseStack, spellWidget.x + spellWidget.sizeX / 2, this.y - spacing / 2, spellWidget.y + spellWidget.sizeY, 0xFFFFFFFF);
    }

    public void addChild(GrimoireSpellUnlockWidget widget) {
        children.add(widget);
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
