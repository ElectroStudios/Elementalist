package net.electro.elementalist.util;

import net.electro.elementalist.spells.SpellsMaster;
import net.electro.elementalist.spells.fire.FireBallBasicSpell;
import net.electro.elementalist.spells.fire.FirePulseSpell;

import java.util.HashMap;
import java.util.Map;

public class SpellIdMap {
    public static Map<Integer, SpellsMaster> map = new HashMap<Integer, SpellsMaster>() {{
        put(0, new FireBallBasicSpell());
        put(1, new FirePulseSpell());
    }};
}
