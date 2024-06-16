package net.electro.elementalist.data;

import net.electro.elementalist.util.Element;
import net.electro.elementalist.util.ElementalistMaps;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@AutoRegisterCapability
public class ElementalistStats {
    private List<Integer> playerUnlockedSpells = Arrays.asList(0, 4, 6, 9, 10);
    private int playerLevel = 0;
    private int playerExperience = 0;

    private List<Integer> elementExperience = Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0);

    private List<Integer> elementLevel = Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0);
    private List<Integer> elementSkillPoints = Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0);


    public void unlockSpell (int spellID)
    {
        this.playerUnlockedSpells.add(spellID);
    }


    public void addExperience (int amount, Element element) {
        this.playerExperience += amount;
        int elementId = ElementalistMaps.elementToIndexMap.get(element);
        this.elementExperience.set(elementId, this.elementExperience.get(elementId) + amount);
    }


    public void newLevel() {
        this.playerLevel++;
    }

    public void newElementLevel(Element element) {
        int elementIndex = ElementalistMaps.elementToIndexMap.get(element);
        this.elementLevel.set(elementIndex, this.elementLevel.get(elementIndex) + 1);
        this.elementSkillPoints.set(elementIndex, this.elementSkillPoints.get(elementIndex) + 1);
    }

    public int getPlayerLevel() {
        return playerLevel;
    }

    public int getElementSkillPoints(Element element) {
        return elementSkillPoints.get(ElementalistMaps.elementToIndexMap.get(element));
    }

    public void reduceElementSkillPoints(Element element, int amount) {
        int elementIndex = ElementalistMaps.elementToIndexMap.get(element);
        this.elementSkillPoints.set(elementIndex, this.elementSkillPoints.get(elementIndex) - amount);
    }

    public int getElementLevel(Element element) {
        return elementLevel.get(ElementalistMaps.elementToIndexMap.get(element));
    }

    public int getPlayerExperience() {
        return playerExperience;
    }

    public int getElementExperience(Element element) {
        return elementExperience.get(ElementalistMaps.elementToIndexMap.get(element));
    }


    public List<Integer> getUnlockedSpellsList ()
    {
        return playerUnlockedSpells;
    }

    public String getSpellsString ()
    {
        String spellString = "";
        for (Integer a : playerUnlockedSpells)
        {
            spellString += a + " ";
        }
        return spellString;
    }

    public void copyFrom(ElementalistStats source)
    {
        this.playerUnlockedSpells = source.playerUnlockedSpells;
        this.playerLevel = source.playerLevel;
        this.playerExperience = source.playerExperience;
        this.elementExperience = source.elementExperience;
        this.elementLevel = source.elementLevel;
        this.elementSkillPoints = source.elementSkillPoints;
    }
    public void saveNBTData(CompoundTag nbt)
    {
        nbt.putIntArray("unlocked_spells", playerUnlockedSpells);
        nbt.putInt("elementalist_level", playerLevel);
        nbt.putInt("elementalist_experience", playerExperience);
        nbt.putIntArray("elementalist_element_experience", elementExperience);
        nbt.putIntArray("elementalist_element_level", elementLevel);
        nbt.putIntArray("elementalist_element_skill_points", elementSkillPoints);
    }

    public void loadNBTData(CompoundTag nbt)
    {
        int[] nbtData = nbt.getIntArray("unlocked_spells");
        playerUnlockedSpells = new ArrayList<>();
        for (int t : nbtData)
        {
            playerUnlockedSpells.add(t);
        }

        nbtData = nbt.getIntArray("elementalist_element_experience");
        elementExperience = new ArrayList<>();
        for (int t : nbtData)
        {
            elementExperience.add(t);
        }

        nbtData = nbt.getIntArray("elementalist_element_level");
        elementLevel = new ArrayList<>();
        for (int t : nbtData)
        {
            elementLevel.add(t);
        }

        nbtData = nbt.getIntArray("elementalist_element_skill_points");
        elementSkillPoints = new ArrayList<>();
        for (int t : nbtData)
        {
            elementSkillPoints.add(t);
        }

        playerLevel = nbt.getInt("elementalist_level");
        playerExperience = nbt.getInt("elementalist_experience");
    }
}
