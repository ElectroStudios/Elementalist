package net.electro.elementalist.client;

import java.util.HashMap;
import java.util.Map;

public class ClientSpellStateData {
    private static int mana;
    private static Map<Integer, Integer> spellCooldowns = new HashMap<Integer, Integer>();

    public static void setMana(int amount) {
        ClientSpellStateData.mana = amount;
    }

    public static int getMana() {
        return mana;
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
