package net.electro.elementalist.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.electro.elementalist.spells.SpellsMaster;
import net.electro.elementalist.util.Element;
import net.electro.elementalist.util.SpellIdMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.List;

public class GrimoireScreen extends Screen {
    private List<SpellsMaster> spellList = SpellIdMap.getSpellListOfElement(Element.FIRE);
    private GrimoireSpellUnlockWidget root;
    public GrimoireScreen() {
        super(Component.literal(""));
        root = new GrimoireSpellUnlockWidget(spellList.get(0), 200, 80);
        createChildrenWidgets(root);
    }

    private void createChildrenWidgets(GrimoireSpellUnlockWidget widget) {
        int childrenAmount = widget.spell.children.size();
        if (childrenAmount > 0) {
            int segmentSize = 100 / childrenAmount;
            for (int i = 0; i < childrenAmount; i++) {
                int xOffset = (int) (((float) i / childrenAmount) * segmentSize) + (segmentSize / 2) - (widget.sizeX / 2);
                SpellsMaster childSpell = SpellIdMap.map.get(widget.spell.children.get(i));
                GrimoireSpellUnlockWidget childWidget = new GrimoireSpellUnlockWidget(childSpell, widget.x - 50 + xOffset, widget.y + 40);
                widget.addChild(childWidget);
                createChildrenWidgets(childWidget);
            }
        }
    }



    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        root.draw(pPoseStack);
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
    }
}
