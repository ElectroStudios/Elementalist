package net.electro.elementalist.spells.water;

import net.electro.elementalist.Elementalist;
import net.electro.elementalist.entities.projectiles.FireballBasic;
import net.electro.elementalist.entities.spells.water.WaterSlashEntity;
import net.electro.elementalist.spells.SpellsMaster;
import net.electro.elementalist.util.DamageType;
import net.electro.elementalist.util.Element;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class WaterSlashSpell extends SpellsMaster {
    public WaterSlashSpell() {
        damageType = new DamageType(10, Element.WATER, 0, 2);
        manaCost = 25;
        spellId = 4;
        spellCooldownTicks = 50;
        spellIcon = new ResourceLocation(Elementalist.MOD_ID, "textures/gui/spellicons/water_slash_spell_icon.png");
        spellString = "water_slash";
        children = List.of(5);
    }
    @Override
    protected void initialize_spell(Player player) {
        WaterSlashEntity waterSlash = new WaterSlashEntity(player, damageType);
        player.level.addFreshEntity(waterSlash);
    }
}
