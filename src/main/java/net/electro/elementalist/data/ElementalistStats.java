package net.electro.elementalist.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;

import java.util.ArrayList;
import java.util.List;

@AutoRegisterCapability
public class ElementalistStats {
    private List<Integer> playerUnlockedSpells = new ArrayList<>();

    public void unlockSpell (Integer spellID)
    {
        this.playerUnlockedSpells.add(spellID);
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
    }
    public void saveNBTData(CompoundTag nbt)
    {
        nbt.putIntArray("unlocked_spells", playerUnlockedSpells);
    }

    public void loadNBTData(CompoundTag nbt)
    {
        int[] nbtData = nbt.getIntArray("unlocked_spells");
        for (int t : nbtData)
        {
            playerUnlockedSpells.add(t);
        }
    }
}
