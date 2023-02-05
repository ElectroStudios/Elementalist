package net.electro.elementalist.spells.water;

import net.electro.elementalist.Elementalist;
import net.electro.elementalist.entities.spells.water.WaterSlashEntity;
import net.electro.elementalist.entities.spells.water.WaterStreamEntity;
import net.electro.elementalist.spells.SpellsMaster;
import net.electro.elementalist.util.DamageType;
import net.electro.elementalist.util.Element;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class WaterStreamSpell extends SpellsMaster {
    public WaterStreamSpell() {
        damageType = new DamageType(7, Element.WATER, 0, 4);
        manaCost = 40;
        spellId = 5;
        spellCooldownTicks = 140;
        spellIcon = new ResourceLocation(Elementalist.MOD_ID, "textures/gui/spellicons/water_stream_spell_icon.png");
        spellString = "water_stream";
        children = List.of();
    }
    @Override
    protected void initialize_spell(Player player) {
        WaterStreamEntity waterStream = new WaterStreamEntity(player, damageType);
        player.level.addFreshEntity(waterStream);
    }
}
