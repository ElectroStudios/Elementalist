package net.electro.elementalist.util;

import net.electro.elementalist.spell.SpellBase;
import net.electro.elementalist.spell.air.AirbladeSpell;
import net.electro.elementalist.spell.fire.*;
import net.electro.elementalist.spell.ice.IceSpearSpell;
import net.electro.elementalist.spell.ice.IcicleBarrageSpell;
import net.electro.elementalist.spell.lightning.ThunderboltSpell;
import net.electro.elementalist.spell.water.WaterSlashSpell;
import net.electro.elementalist.spell.water.WaterStreamSpell;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ElementalistMaps {
    public static Map<Integer, SpellBase> spellMap = new HashMap<Integer, SpellBase>() {{
        put(0, new FireBallSpell());
        put(1, new FirePulseSpell());
        put(2, new FireBreathSpell());
        put(3, new FireExplosionSpell());
        put(4, new WaterSlashSpell());
        put(5, new WaterStreamSpell());
        put(6, new IceSpearSpell());
        put(7, new FireWaveSpell());
        put(8, new FireClusterExplosionSpell());
        put(9, new ThunderboltSpell());
        put(10, new AirbladeSpell());
        put(11, new IcicleBarrageSpell());
    }};

    public static Map<Element, Integer> elementToIndexMap = new HashMap<Element, Integer>() {{
        put(Element.FIRE, 0);
        put(Element.WATER, 1);
        put(Element.AIR, 2);
        put(Element.LIGHTNING, 3);
        put(Element.ICE, 4);
        put(Element.LIFE, 5);
        put(Element.EARTH, 6);
        put(Element.DEATH, 7);
    }
    };

    public static Map<Element, String> elementToStringMap = new HashMap<Element, String>() {{
        put(Element.FIRE, "fire");
        put(Element.WATER, "water");
        put(Element.AIR, "air");
        put(Element.LIGHTNING, "lightning");
        put(Element.ICE, "ice");
        put(Element.LIFE, "life");
        put(Element.EARTH, "earth");
        put(Element.DEATH, "death");
    }
    };

    public static Map<Element, Color> elementToColorMap = new HashMap<Element, Color>() {{
        put(Element.FIRE, new Color(255, 139, 118, 255));
        put(Element.WATER, new Color(116, 143, 206, 255));
        put(Element.AIR, new Color(225, 241, 241, 255));
        put(Element.LIGHTNING, new Color(166, 143, 241, 255));
        put(Element.ICE, new Color(60, 222, 245, 255));
        put(Element.LIFE, new Color(160, 243, 117, 255));
        put(Element.EARTH, new Color(152, 88, 76, 255));
        put(Element.DEATH, new Color(80, 15, 15, 255));
    }
    };

    public static List<SpellBase> getSpellListOfElement(Element element) {
        List<SpellBase> list = new ArrayList<>();
        for (SpellBase spell : spellMap.values()) {
            if (spell.damageType.ELEMENT == element) {
                list.add(spell);
            }
        }
        return list;
    }

    public static List<SpellBase> getUnlockedSpellListOfElement(Element element, List<Integer> unlockedSpells) {
        List<SpellBase> list = new ArrayList<>();
        for (SpellBase spell : spellMap.values()) {
            if (spell.damageType.ELEMENT == element && unlockedSpells.contains(spell.spellId)) {
                list.add(spell);
            }
        }
        return list;
    }
}
