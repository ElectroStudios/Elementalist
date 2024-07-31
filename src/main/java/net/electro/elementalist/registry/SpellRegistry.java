package net.electro.elementalist.registry;

import net.electro.elementalist.Elementalist;
import net.electro.elementalist.spell.SpellBase;
import net.electro.elementalist.spell.air.AirbladeSpell;
import net.electro.elementalist.spell.fire.*;
import net.electro.elementalist.spell.ice.IceSpearSpell;
import net.electro.elementalist.spell.ice.IcicleBarrageSpell;
import net.electro.elementalist.spell.lightning.ThunderboltSpell;
import net.electro.elementalist.spell.water.WaterSlashSpell;
import net.electro.elementalist.spell.water.WaterStreamSpell;
import net.electro.elementalist.util.Element;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class SpellRegistry {
    public static final ResourceLocation SPELL_REGISTRY_NAME = new ResourceLocation(Elementalist.MOD_ID, "spell_registry");
    public static final DeferredRegister<SpellBase> ELEMENTALIST_SPELLS = DeferredRegister.create(SPELL_REGISTRY_NAME, Elementalist.MOD_ID);

    public static final Supplier<IForgeRegistry<SpellBase>> SPELLS_SUPPLIER = ELEMENTALIST_SPELLS.makeRegistry(RegistryBuilder::new);

    public static void register(IEventBus eventBus) {
        ELEMENTALIST_SPELLS.register(eventBus);
    }

    public static RegistryObject<SpellBase> addSpell(SpellBase spell) {
        return ELEMENTALIST_SPELLS.register(spell.getName(), () -> spell);
    }

    public static SpellBase getSpell(ResourceLocation resourceLocation) {
        return SPELLS_SUPPLIER.get().getValue(resourceLocation);
    }

    public static List<SpellBase> getAllSpellsOfElement(Element element) {
        return SPELLS_SUPPLIER.get()
                .getValues()
                .stream()
                .collect(Collectors.groupingBy(SpellBase::getElement)).get(element);
    }

    public static List<SpellBase> getUnlockedSpellsOfElement(Element element, List<ResourceLocation> unlockedSpells) {
        return SPELLS_SUPPLIER.get()
                .getValues()
                .stream()
                .filter(spell -> unlockedSpells.contains(spell.spellId))
                .collect(Collectors.groupingBy(SpellBase::getElement)).get(element);
    }


    public static final RegistryObject<SpellBase> FIREBALL = addSpell(new FireBallSpell());
    public static final RegistryObject<SpellBase> FIRE_BREATH = addSpell(new FireBreathSpell());
    public static final RegistryObject<SpellBase> FIRE_CLUSTER_EXPLOSION = addSpell(new FireClusterExplosionSpell());
    public static final RegistryObject<SpellBase> FIRE_EXPLOSION = addSpell(new FireExplosionSpell());
    public static final RegistryObject<SpellBase> FIRE_PULSE = addSpell(new FirePulseSpell());
    public static final RegistryObject<SpellBase> FIRE_WAVE = addSpell(new FireWaveSpell());

    public static final RegistryObject<SpellBase> AIRBLADE = addSpell(new AirbladeSpell());

    public static final RegistryObject<SpellBase> ICE_SPEAR = addSpell(new IceSpearSpell());
    public static final RegistryObject<SpellBase> ICICLE_BARRAGE = addSpell(new IcicleBarrageSpell());

    public static final RegistryObject<SpellBase> THUNDERBOLT = addSpell(new ThunderboltSpell());

    public static final RegistryObject<SpellBase> WATER_SPLASH = addSpell(new WaterSlashSpell());
    public static final RegistryObject<SpellBase> WATER_STREAM = addSpell(new WaterStreamSpell());


}
