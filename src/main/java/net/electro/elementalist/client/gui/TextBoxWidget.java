package net.electro.elementalist.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

public class TextBoxWidget extends GuiComponent {
    private final int X1;
    private final int Y1;
    private final int X2;
    private final int Y2;
    private float scale = -1;
    private int ySpacing = 0;
    private final boolean CENTRE_HORIZONTALLY;
    private final boolean CENTRE_VERTICALLY;
    private final int COLOR;
    private List<FormattedCharSequence> textLines;
    public TextBoxWidget(int x1, int y1, int x2, int y2, boolean centreHorizontally, boolean centreVertically, int color) {
        this.X1 = x1;
        this.Y1 = y1;
        this.X2 = x2;
        this.Y2 = y2;
        this.CENTRE_HORIZONTALLY = centreHorizontally;
        this.CENTRE_VERTICALLY = centreVertically;
        this.COLOR = color;


    }

    public void resetScale() {
        this.scale = -1;
    }
    public void render(PoseStack poseStack, int anchorX, int anchorY, Font font, Component component) {
        if (scale < 0) {
            this.scale = 1.5f;
            while (Y2-Y1 <= scale * font.wordWrapHeight(component, (int)((X2 - X1) / scale))) {
                scale -= 0.02f;
            }
            if (CENTRE_VERTICALLY) {
                ySpacing = (int)Math.ceil((double) ((Y2 - Y1) - scale * font.wordWrapHeight(component, (int)((X2 - X1) / scale)))/2);
            }
        }
        poseStack.pushPose();
        poseStack.scale(scale, scale, scale);
        this.textLines = font.split(component, (int)((X2 - X1)/scale));
        for (int i = 0; i < textLines.size(); i++) {
            int xOffset = 0;
            if (CENTRE_HORIZONTALLY) {
                xOffset = (int)(((X2 - X1) - (font.width(textLines.get(i)) * scale)) / 2);
            }
            font.draw(poseStack, textLines.get(i), (X1 + xOffset + anchorX)/scale,
                    (Y1 + i * (9*scale) + anchorY + ySpacing)/scale, COLOR);
        }
        poseStack.popPose();
    }
}
