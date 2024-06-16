package net.electro.elementalist.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.electro.elementalist.Elementalist;
import net.electro.elementalist.client.ClientSpellStateData;
import net.electro.elementalist.networking.ModMessages;
import net.electro.elementalist.networking.packet.UnlockSpellC2SPacket;
import net.electro.elementalist.spells.SpellMaster;
import net.electro.elementalist.spells.SpellRegistry;
import net.electro.elementalist.util.Element;
import net.electro.elementalist.util.ElementalistMaps;
import net.electro.elementalist.util.Utility;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GrimoireScreen extends Screen {
    Minecraft mc = Minecraft.getInstance();
    private final ResourceLocation GRIMOIRE_BACKGROUND = new ResourceLocation(Elementalist.MOD_ID, "textures/gui/grimoire/grimoire_background.png");
    private final ResourceLocation EXPERIENCE_BAR = new ResourceLocation(Elementalist.MOD_ID, "textures/gui/grimoire/experience_bar.png");
    private final ResourceLocation UNLOCK_SPELL_BUTTON = new ResourceLocation(Elementalist.MOD_ID, "textures/gui/grimoire/unlock_spell.png");
    private final ResourceLocation SPELL_UNLOCKED_BUTTON = new ResourceLocation(Elementalist.MOD_ID, "textures/gui/grimoire/spell_unlocked.png");
    private List<SpellMaster> spellList;
    private GrimoireSpellUnlockWidget root;
    private HoverableWidget hoveredWidget;
    private GrimoireSpellUnlockWidget selectedWidget;
    private Element selectedElement = Element.FIRE;
    private List<Integer> levels;
    private List<Integer> currentLevelOffset;
    private final TextBoxWidget elementNameText;
    private final TextBoxWidget elementDescriptionText;
    private final TextBoxWidget skillNameText;
    private final TextBoxWidget skillDescriptionText;
    private final TextBoxWidget elementLevelText;
    private final TextBoxWidget elementLevelNumber;
    private final TextBoxWidget elementSkillPointNumber;
    private final TextBoxWidget elementSkillPointText;
    private final ButtonWidget unlockSpellButton;
    private final List<ElementTabWidget> elementTabs;
    public GrimoireScreen() {
        super(Component.literal(""));


        elementNameText = new TextBoxWidget(-91, -75, -23, -64, true,true, 0xFF646464);
        elementDescriptionText = new TextBoxWidget(-117, -54, -50, 5, false, false, 0xFF646464);
        skillNameText = new TextBoxWidget(-117, 24, -43, 33, false, true, 0xFF646464);
        skillDescriptionText = new TextBoxWidget(-117, 42, -43, 68, false, false, 0xFF646464);
        elementLevelText = new TextBoxWidget(4, 71, 27, 79, true, true, 0xFF646464);
        elementLevelNumber = new TextBoxWidget(33, 69, 45, 81, true, true, 0xFF646464);
        elementSkillPointNumber = new TextBoxWidget(4, 55, 14, 65, true, true, 0xFF646464);
        elementSkillPointText = new TextBoxWidget(16, 55, 65, 65, false, true, 0xFF646464);

        selectElement(Element.FIRE);
        unlockSpellButton = new ButtonWidget(-36, 59, -6, 69, true, true, 0xFF646464) {
            @Override
            public void onClick() {
                tryUnlockSpell();
            }
        };

        elementTabs = Arrays.asList(
                new ElementTabWidget(123, -70, 140, -58, Element.FIRE, this),
                new ElementTabWidget(123, -54, 140, -42, Element.WATER, this),
                new ElementTabWidget(123, -38, 140, -26, Element.ICE, this)
        );


    }

    public void selectElement(Element element) {
        selectedElement = element;
        spellList = ElementalistMaps.getSpellListOfElement(selectedElement);
        List<SpellMaster> spellsOfElement = SpellRegistry.getAllSpellsOfElement(selectedElement);
        if (spellsOfElement != null) {
            mc.player.sendSystemMessage(Component.literal(spellsOfElement.toString()));
        }
        calculateSpellsPerLevel();
        root = new GrimoireSpellUnlockWidget(spellList.get(0), 60, -65);
        selectedWidget = root;
        createChildrenWidgets(root, 1);
        skillNameText.resetScale();
        skillDescriptionText.resetScale();
        elementNameText.resetScale();
        elementDescriptionText.resetScale();
        elementSkillPointNumber.resetScale();
        elementLevelNumber.resetScale();
    }

    private void tryUnlockSpell() {
        ModMessages.sendToServer(new UnlockSpellC2SPacket(selectedWidget.spell.spellId));
    }

    private void calculateSpellsPerLevel() {
        levels = new ArrayList<>();
        currentLevelOffset = new ArrayList<>();
        levels.add(1);
        currentLevelOffset.add(0);
        addChildrenToLevels(spellList.get(0), 1);
    }

    private void addChildrenToLevels(SpellMaster spell, int level) {
        if (!spell.children.isEmpty()) {
            if (levels.size() <= level) {
                levels.add(0);
                currentLevelOffset.add(0);
            }
            levels.set(level, levels.get(level) + spell.children.size());
            for (int child : spell.children) {
                addChildrenToLevels(ElementalistMaps.spellMap.get(child), level+1);
            }
        }

    }

    private void createChildrenWidgets(GrimoireSpellUnlockWidget widget, int level) {
        int childrenAmount = widget.spell.children.size();
        if (childrenAmount > 0) {
            int segmentSize;
            if (levels.get(level) == 1) {
                segmentSize = 0;
            }
            else {
                segmentSize = (100 - widget.SIZE_X) / (levels.get(level) - 1);
            }
            for (int i = 0; i < childrenAmount; i++) {
                int xOffset;
                if (levels.get(level) == 1) {
                    xOffset = 42;
                }
                else {
                    xOffset = (int) ((i + currentLevelOffset.get(level)) * segmentSize);
                }
                SpellMaster childSpell = ElementalistMaps.spellMap.get(widget.spell.children.get(i));
                GrimoireSpellUnlockWidget childWidget = new GrimoireSpellUnlockWidget(childSpell, 18 + xOffset,
                        widget.Y_OFFSET + 20);
                widget.addChild(childWidget);
                createChildrenWidgets(childWidget, level + 1);
            }
            currentLevelOffset.set(level, currentLevelOffset.get(level) + childrenAmount);
        }
    }




    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void mouseMoved(double pMouseX, double pMouseY) {
        super.mouseMoved(pMouseX, pMouseY);
        hoveredWidget = null;

        int guiHeight = mc.getWindow().getGuiScaledHeight();
        int guiWidth = mc.getWindow().getGuiScaledWidth();

        int anchorPointX = guiWidth / 2;
        int anchorPointY = guiHeight / 2;

        HoverableWidget hoveredSpellUnlock = root.widgetHovered(pMouseX, pMouseY, anchorPointX, anchorPointY,
                ClientSpellStateData.getUnlockedSpells(), true);
        if (hoveredSpellUnlock != null) {
            hoveredWidget = hoveredSpellUnlock;
        }
        if (unlockSpellButton.isMouseOver(pMouseX, pMouseY, anchorPointX, anchorPointY)) {
            hoveredWidget = unlockSpellButton;
        }
        for (ButtonWidget buttonWidget : elementTabs) {
            if (buttonWidget.isMouseOver(pMouseX, pMouseY, anchorPointX, anchorPointY)) {
                hoveredWidget = buttonWidget;
            }
        }

    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (pButton == 0) {
            if (hoveredWidget instanceof GrimoireSpellUnlockWidget spellUnlockWidget) {
                selectedWidget = spellUnlockWidget;
                skillNameText.resetScale();
                skillDescriptionText.resetScale();
            }
            else if (hoveredWidget instanceof ButtonWidget buttonWidget) {
                buttonWidget.onClick();
            }
        }
        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        PoseStack poseStack = pGuiGraphics.pose();
        int guiHeight = mc.getWindow().getGuiScaledHeight();
        int guiWidth = mc.getWindow().getGuiScaledWidth();

        int anchorPointX = guiWidth / 2;
        int anchorPointY = guiHeight / 2;

        Utility.performBlit(pGuiGraphics, GRIMOIRE_BACKGROUND, anchorPointX - 135, anchorPointY - 90, 0, 0,
                270, 180, 270, 180, new Color(255, 255, 255 ,255));


        root.draw(pGuiGraphics, anchorPointX, anchorPointY, ClientSpellStateData.getUnlockedSpells(), true);

        elementNameText.render(pGuiGraphics, anchorPointX, anchorPointY, this.font,
                Component.translatable("elementalist.element_name." + ElementalistMaps.elementToStringMap.get(selectedElement)));
        elementDescriptionText.render(pGuiGraphics, anchorPointX, anchorPointY, this.font,
                Component.translatable("elementalist.element_description." + ElementalistMaps.elementToStringMap.get(selectedElement)));

        skillNameText.render(pGuiGraphics, anchorPointX, anchorPointY, this.font,
                Component.translatable("elementalist.spell_name." + selectedWidget.spell.spellName));

        skillDescriptionText.render(pGuiGraphics, anchorPointX, anchorPointY, this.font,
                Component.translatable("elementalist.spell_description." + selectedWidget.spell.spellName));

        elementLevelText.render(pGuiGraphics, anchorPointX, anchorPointY, this.font,
                Component.translatable("elementalist.level"));

        elementLevelNumber.render(pGuiGraphics, anchorPointX, anchorPointY, this.font,
                Component.literal(Integer.toString(ClientSpellStateData.getElementLevel(ElementalistMaps.elementToIndexMap.get(selectedElement)))));

        elementSkillPointNumber.render(pGuiGraphics, anchorPointX, anchorPointY, this.font,
                Component.literal(Integer.toString(ClientSpellStateData.getElementSkillPoints(ElementalistMaps.elementToIndexMap.get(selectedElement)))));

        elementSkillPointText.render(pGuiGraphics, anchorPointX, anchorPointY, this.font,
                Component.translatable("elementalist.skill_points"));

        unlockSpellButton.render(pGuiGraphics, anchorPointX, anchorPointY, this.font,
                Component.translatable((selectedWidget.isUnlocked ? "elementalist.unlocked_button" : "elementalist.unlock_button")),
                (selectedWidget.isUnlocked ? SPELL_UNLOCKED_BUTTON : UNLOCK_SPELL_BUTTON), new Color(255, 255, 255, 255));

        for (ElementTabWidget elementTab : elementTabs) {
            elementTab.renderTab(pGuiGraphics, anchorPointX, anchorPointY, elementTab.ELEMENT == selectedElement);
        }

        Utility.performBlit(pGuiGraphics, selectedWidget.spell.spellIcon, anchorPointX - 36, anchorPointY + 23,
                0f, 0f, 30, 30, 30, 30, new Color(255, 255, 255, 255));

        int elementLevel = ClientSpellStateData.getElementLevel(ElementalistMaps.elementToIndexMap.get(selectedElement));
        int elementExperience = ClientSpellStateData.getElementExperience(ElementalistMaps.elementToIndexMap.get(selectedElement));
        float elementLevelProgress = (elementExperience - Utility.getExperienceToNextLevel(elementLevel - 1)) /
                (float)(Utility.getExperienceToNextLevel(elementLevel) - Utility.getExperienceToNextLevel(elementLevel - 1));
        Utility.performBlit(pGuiGraphics, EXPERIENCE_BAR, anchorPointX + 52, anchorPointY + 74, 0, 0,
                (int)(66 * elementLevelProgress), 3, 66, 3,
                ElementalistMaps.elementToColorMap.get(selectedElement));


        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    }
}
