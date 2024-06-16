package net.electro.elementalist.client.gui;

import net.electro.elementalist.Elementalist;
import net.electro.elementalist.spells.SpellMaster;
import net.electro.elementalist.util.Utility;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GrimoireSpellUnlockWidget implements HoverableWidget {

    public final int X_OFFSET;
    public final int Y_OFFSET;
    public final int SIZE_X = 16;
    public final int SIZE_Y = 16;
    public final SpellMaster spell;
    public boolean isHovered = false;
    public boolean isUnlocked = false;
    private List<GrimoireSpellUnlockWidget> children = new ArrayList<>();
    private final ResourceLocation frameIconUnlocked = new ResourceLocation(Elementalist.MOD_ID, "textures/gui/active_spell_frame.png");
    private final ResourceLocation frameIconLocked = new ResourceLocation(Elementalist.MOD_ID, "textures/gui/inactive_spell_frame.png");

    public GrimoireSpellUnlockWidget (SpellMaster spell, int xOffset, int yOffset) {
        this.X_OFFSET = xOffset;
        this.Y_OFFSET = yOffset;
        this.spell = spell;
    }
    public void draw(GuiGraphics guiGraphics, int anchorX, int anchorY, List<Integer> unlockedSpells, boolean previousUnlocked) {
        boolean unlocked = unlockedSpells.contains(spell.spellId);
        isUnlocked = unlocked;
        int brightness = (int)((previousUnlocked ? (isHovered ? 1f : 0.8f) : 0f) * 255);
        Utility.performBlit(guiGraphics, (unlocked ? frameIconUnlocked : frameIconLocked),
                X_OFFSET + anchorX, Y_OFFSET + anchorY, 0, 0, SIZE_X, SIZE_Y, SIZE_X, SIZE_Y,
                new Color(brightness, brightness, brightness, 255));
        Utility.performBlit(guiGraphics, spell.spellIcon, X_OFFSET + anchorX, Y_OFFSET + anchorY, 0, 0,
                SIZE_X, SIZE_Y, SIZE_X, SIZE_Y, new Color(brightness, brightness, brightness, 255));
        for (GrimoireSpellUnlockWidget widget : children) {
            widget.draw(guiGraphics, anchorX, anchorY, unlockedSpells, unlocked);
            widget.drawConnection(guiGraphics, this, anchorX, anchorY);
        }
    }

    public void drawConnection(GuiGraphics guiGraphics, GrimoireSpellUnlockWidget spellWidget, int anchorX, int anchorY) {
        int spacing = this.Y_OFFSET - spellWidget.SIZE_Y - spellWidget.Y_OFFSET;
        guiGraphics.vLine(anchorX + this.X_OFFSET + this.SIZE_X / 2, anchorY + this.Y_OFFSET,
                anchorY + this.Y_OFFSET - spacing / 2,
                0xFF646464);
        guiGraphics.hLine(anchorX + this.X_OFFSET + this.SIZE_X / 2,
                anchorX + spellWidget.X_OFFSET + spellWidget.SIZE_X / 2, anchorY + this.Y_OFFSET - spacing / 2,
                0xFF646464);
        guiGraphics.vLine(anchorX + spellWidget.X_OFFSET + spellWidget.SIZE_X / 2,
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
}
