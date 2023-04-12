package net.electro.elementalist.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.electro.elementalist.util.ClickableWidget;
import net.electro.elementalist.util.Utility;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;

import java.awt.*;
import java.util.List;

public class ButtonWidget extends GuiComponent implements ClickableWidget {
    protected final int X1;
    protected final int Y1;
    protected final int X2;
    protected final int Y2;
    private float scale = -1;
    private int ySpacing = 0;
    private final boolean CENTRE_HORIZONTALLY;
    private final boolean CENTRE_VERTICALLY;
    private final int TEXT_COLOR;
    private ResourceLocation TEXTURE;
    private List<FormattedCharSequence> textLines;
    public ButtonWidget(int x1, int y1, int x2, int y2, boolean centreHorizontally, boolean centreVertically,
                        int textColor) {
        this.X1 = x1;
        this.Y1 = y1;
        this.X2 = x2;
        this.Y2 = y2;
        this.CENTRE_HORIZONTALLY = centreHorizontally;
        this.CENTRE_VERTICALLY = centreVertically;
        this.TEXT_COLOR = textColor;
    }

    public void resetScale() {
        this.scale = -1;
    }

    public boolean isMouseOver(double mouseX, double mouseY, int anchorX, int anchorY) {
        return mouseX >= X1 + anchorX && mouseX <= X2 + anchorX && mouseY >= Y1 + anchorY && mouseY <= Y2 + anchorY;
    }

    public void renderNoText(PoseStack poseStack, int anchorX, int anchorY, ResourceLocation texture, Color color) {
        poseStack.pushPose();
        Utility.performBlit(texture, poseStack, anchorX + X1, anchorY + Y1, 0, 0,
                X2 - X1 + 1, Y2 - Y1 + 1, X2 - X1 + 1, Y2 - Y1 + 1, color);
        poseStack.popPose();
    }

    public void render(PoseStack poseStack, int anchorX, int anchorY, Font font, Component component, ResourceLocation texture, Color color) {
        if (scale < 0) {
            this.scale = 1.5f;
            while (Y2-Y1-2 <= scale * font.wordWrapHeight(component, (int)((X2 - X1 - 2) / scale))) {
                scale -= 0.02f;
            }
            if (CENTRE_VERTICALLY) {
                ySpacing = (int)Math.ceil((double) ((Y2 - Y1 - 2) - scale * font.wordWrapHeight(component, (int)((X2 - X1 - 2) / scale)))/2);
            }
        }
        poseStack.pushPose();
        Utility.performBlit(texture, poseStack, anchorX + X1, anchorY + Y1, 0, 0,
                X2 - X1 + 1, Y2 - Y1 + 1, X2 - X1 + 1, Y2 - Y1 + 1, color);
        poseStack.popPose();
        poseStack.pushPose();
        poseStack.scale(scale, scale, scale);
        this.textLines = font.split(component, (int)((X2 - X1 - 2)/scale));
        for (int i = 0; i < textLines.size(); i++) {
            int xOffset = 0;
            if (CENTRE_HORIZONTALLY) {
                xOffset = (int)(((X2 - X1 - 2) - (font.width(textLines.get(i)) * scale)) / 2);
            }
            font.draw(poseStack, textLines.get(i), (X1 + 1 + xOffset + anchorX)/scale,
                    (Y1 + 1 + i * (9*scale) + anchorY + ySpacing)/scale, TEXT_COLOR);
        }
        poseStack.popPose();
    }

    @Override
    public void onClick() {

    }
}
