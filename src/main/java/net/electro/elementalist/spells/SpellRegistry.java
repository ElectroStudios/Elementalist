package net.electro.elementalist.spells;

import net.electro.elementalist.Elementalist;
import net.electro.elementalist.spells.fire.*;
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
    public static final DeferredRegister<SpellMaster> ELEMENTALIST_SPELLS = DeferredRegister.create(SPELL_REGISTRY_NAME, Elementalist.MOD_ID);

    public static final Supplier<IForgeRegistry<SpellMaster>> SPELLS_SUPPLIER = ELEMENTALIST_SPELLS.makeRegistry(RegistryBuilder::new);

    public static void register(IEventBus eventBus) {
        ELEMENTALIST_SPELLS.register(eventBus);
    }

    public static RegistryObject<SpellMaster> addSpell(SpellMaster spell) {
        return ELEMENTALIST_SPELLS.register(spell.spellName, () -> spell);
    }

    public static SpellMaster getSpell(ResourceLocation resourceLocation) {
        return SPELLS_SUPPLIER.get().getValue(resourceLocation);
    }

    public static List<SpellMaster> getAllSpellsOfElement(Element element) {
        return SPELLS_SUPPLIER.get()
                .getValues()
                .stream()
                .collect(Collectors.groupingBy(SpellMaster::getElement)).get(element);
    }


    public static final RegistryObject<SpellMaster> FIREBALL = addSpell(new FireBallSpell());
    public static final RegistryObject<SpellMaster> FIRE_BREATH = addSpell(new FireBreathSpell());
    public static final RegistryObject<SpellMaster> FIRE_CLUSTER_EXPLOSION = addSpell(new FireClusterExplosionSpell());
    public static final RegistryObject<SpellMaster> FIRE_EXPLOSION = addSpell(new FireExplosionSpell());
    public static final RegistryObject<SpellMaster> FIRE_PULSE = addSpell(new FirePulseSpell());
    public static final RegistryObject<SpellMaster> FIRE_WAVE = addSpell(new FireWaveSpell());

}
