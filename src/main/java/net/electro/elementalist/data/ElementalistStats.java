package net.electro.elementalist.data;

import net.electro.elementalist.Elementalist;
import net.electro.elementalist.util.Element;
import net.electro.elementalist.util.ElementalistMaps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@AutoRegisterCapability
public class ElementalistStats {
    private List<ResourceLocation> playerUnlockedSpells = Arrays.asList(
            new ResourceLocation(Elementalist.MOD_ID, "fireball"),
            new ResourceLocation(Elementalist.MOD_ID, "airblade"),
            new ResourceLocation(Elementalist.MOD_ID, "ice_spear"),
            new ResourceLocation(Elementalist.MOD_ID, "thunderbolt"),
            new ResourceLocation(Elementalist.MOD_ID, "water_slash")

    );
    private int playerLevel = 0;
    private int playerExperience = 0;

    private boolean combatMode = false;

    private List<Integer> elementExperience = Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0);

    private List<Integer> elementLevel = Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0);
    private List<Integer> elementSkillPoints = Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0);


    public void unlockSpell (ResourceLocation spellID)
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

    public void toggleCombatMode() {
        this.combatMode = !this.combatMode;
    }

    public boolean isInCombatMode() {
        return this.combatMode;
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


    public List<ResourceLocation> getUnlockedSpellsList ()
    {
        return playerUnlockedSpells;
    }

    public String getSpellsString ()
    {
        String spellString = "";
        for (ResourceLocation a : playerUnlockedSpells)
        {
            spellString += a.toString() + " ";
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
        this.combatMode = source.combatMode;
    }
    public void saveNBTData(CompoundTag nbt)
    {
        ListTag unlockedSpellsTag = new ListTag();
        for (ResourceLocation spell : playerUnlockedSpells) {
            unlockedSpellsTag.add(StringTag.valueOf(spell.toString()));
        }
        nbt.put("elementalist_unlocked_spells", unlockedSpellsTag);
        nbt.putInt("elementalist_level", playerLevel);
        nbt.putInt("elementalist_experience", playerExperience);
        nbt.putIntArray("elementalist_element_experience", elementExperience);
        nbt.putIntArray("elementalist_element_level", elementLevel);
        nbt.putIntArray("elementalist_element_skill_points", elementSkillPoints);
        nbt.putBoolean("elementalist_combat_mode", combatMode);
    }

    public void loadNBTData(CompoundTag nbt)
    {
        ListTag unlockedSpellsTag = nbt.getList("elementalist_unlocked_spells", Tag.TAG_STRING);
        playerUnlockedSpells = new ArrayList<>();
        for (int i = 0; i < unlockedSpellsTag.size(); i++)
        {
            playerUnlockedSpells.add(new ResourceLocation(unlockedSpellsTag.getString(i)));
        }

        int[] nbtData = nbt.getIntArray("elementalist_element_experience");
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
        combatMode = nbt.getBoolean("elementalist_combat_mode");
    }
}
