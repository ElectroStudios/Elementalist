package net.electro.elementalist.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientSpellStateData {
    private static int mana;
    private static List<Integer> unlockedSpells = new ArrayList<>();
    private static Map<Integer, Integer> spellCooldowns = new HashMap<Integer, Integer>();

    private static int playerLevel = 0;

    private static Map<Integer, Integer> elementLevels = new HashMap<Integer, Integer>() {{
        put(0, 0);
        put(1, 0);
        put(2, 0);
        put(3, 0);
        put(4, 0);
        put(5, 0);
        put(6, 0);
        put(7, 0);
    }};

    private static Map<Integer, Integer> elementExperience = new HashMap<Integer, Integer>() {{
        put(0, 0);
        put(1, 0);
        put(2, 0);
        put(3, 0);
        put(4, 0);
        put(5, 0);
        put(6, 0);
        put(7, 0);
    }};

    private static Map<Integer, Integer> elementSkillPoints = new HashMap<Integer, Integer>() {{
        put(0, 0);
        put(1, 0);
        put(2, 0);
        put(3, 0);
        put(4, 0);
        put(5, 0);
        put(6, 0);
        put(7, 0);
    }};

    private static boolean isSpellAlternateActive;
    private static int altInterval;

    public static void setUnlockedSpells(List<Integer> unlockedSpells) {
        ClientSpellStateData.unlockedSpells = unlockedSpells;
    }

    public static List<Integer> getUnlockedSpells() {
        return unlockedSpells;
    }

    public static boolean getIsSpellAlternateActive() {
        return isSpellAlternateActive;
    }

    public static void setIsSpellAlternateActive(boolean value) {
        ClientSpellStateData.isSpellAlternateActive = value;
    }


    public static void setMana(int amount) {
        ClientSpellStateData.mana = amount;
    }

    public static int getMana() {
        return mana;
    }

    public static void setPlayerLevel(int level) {
        ClientSpellStateData.playerLevel = level;
    }

    public static int getPlayerLevel() {
        return playerLevel;
    }

    public static void setElementLevel(int level, int element) {
        ClientSpellStateData.elementLevels.replace(element, level);
    }

    public static int getElementLevel(int element) {
        return elementLevels.get(element);
    }

    public static void setElementExperience(int experience, int element) {
        ClientSpellStateData.elementExperience.replace(element, experience);
    }

    public static int getElementExperience(int element) {
        return elementExperience.get(element);
    }

    public static void setElementSkillPoints(int skillPoints, int element) {
        ClientSpellStateData.elementSkillPoints.replace(element, skillPoints);
    }

    public static int getElementSkillPoints(int element) {
        return elementSkillPoints.get(element);
    }

    public static void setAltInterval(int amount) {
        ClientSpellStateData.altInterval = amount;
    }

    public static boolean isInAltInterval() {
        return altInterval > 0;
    }

    public static void decrementAltInterval() {
        ClientSpellStateData.altInterval = Math.max(0, altInterval -1);
    }

    public static void setSpellCooldown(int spellId, int amount) {
        spellCooldowns.put(spellId, amount);
    }

    public static void decreaseSpellCooldowns() {
        spellCooldowns.replaceAll((k, v) -> Math.max(v - 5, 0));
    }

    public static int getCooldown(int spellId) {
        return (spellCooldowns.get(spellId) == null ? 0 : spellCooldowns.get(spellId));
    }
}
