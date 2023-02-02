package net.electro.elementalist.client;

import java.util.HashMap;
import java.util.Map;

public class ClientSpellStateData {
    private static int mana;
    private static Map<Integer, Integer> spellCooldowns = new HashMap<Integer, Integer>();
    private static boolean isSpellAlternateActive;
    private static int altInterval;

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
