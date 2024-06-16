package net.electro.elementalist.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.electro.elementalist.Elementalist;
import net.electro.elementalist.util.Element;
import net.electro.elementalist.util.ElementalistMaps;
import net.electro.elementalist.util.Utility;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;

public class ElementTabWidget extends ButtonWidget {
    public final Element ELEMENT;
    private final GrimoireScreen GRIMOIRE_SCREEN;
    public ElementTabWidget(int x1, int y1, int x2, int y2, Element element, GrimoireScreen grimoireScreen) {
        super(x1, y1, x2, y2, true, true, 0xFFFFFFFF);
        ELEMENT = element;
        GRIMOIRE_SCREEN = grimoireScreen;
    }

    public void renderTab(GuiGraphics guiGraphics, int anchorX, int anchorY, boolean isSelected) {
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        Utility.performBlit(guiGraphics, new ResourceLocation(Elementalist.MOD_ID, "textures/gui/grimoire/bookmark_bw.png"),
                anchorX + X1 + (isSelected ? 2 : 0), anchorY + Y1, 0, 0,
                X2 - X1 + 1, Y2 - Y1 + 1, X2 - X1 + 1, Y2 - Y1 + 1,
                (isSelected ? ElementalistMaps.elementToColorMap.get(ELEMENT) : new Color(186, 171, 141, 255)));

        Utility.performBlit(guiGraphics, new ResourceLocation(Elementalist.MOD_ID, "textures/gui/elementicons/" + ElementalistMaps.elementToStringMap.get(ELEMENT) + ".png"),
                anchorX + X1 + 1 + (isSelected ? 2 : 0), anchorY + Y1 + 1, 0, 0,
                10, 10, 10, 10, new Color(255, 255, 255, 255));
        poseStack.popPose();
    }

    @Override
    public void onClick() {
        GRIMOIRE_SCREEN.selectElement(ELEMENT);
    }
}
