package net.electro.elementalist.util;

import net.electro.elementalist.spells.SpellsMaster;
import net.electro.elementalist.spells.fire.FireBallSpell;
import net.electro.elementalist.spells.fire.FireBreathSpell;
import net.electro.elementalist.spells.fire.FirePulseSpell;
import net.electro.elementalist.spells.fire.FireExplosionSpell;
import net.electro.elementalist.spells.ice.IceSpearSpell;
import net.electro.elementalist.spells.water.WaterSlashSpell;
import net.electro.elementalist.spells.water.WaterStreamSpell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpellIdMap {
    public static Map<Integer, SpellsMaster> map = new HashMap<Integer, SpellsMaster>() {{
        put(0, new FireBallSpell());
        put(1, new FirePulseSpell());
        put(2, new FireBreathSpell());
        put(3, new FireExplosionSpell());
        put(4, new WaterSlashSpell());
        put(5, new WaterStreamSpell());
        put(6, new IceSpearSpell());
    }};

    public static List<SpellsMaster> getSpellListOfElement(Element element) {
        List<SpellsMaster> list = new ArrayList<>();
        for (SpellsMaster spell : map.values()) {
            if (spell.damageType.ELEMENT == element) {
                list.add(spell);
            }
        }
        return list;
    }
}
