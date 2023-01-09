package net.electro.elementalist.entities;

import net.electro.elementalist.Elementalist;
import net.electro.elementalist.entities.projectiles.FireballBasic;
import net.electro.elementalist.entities.spells.FirePulseEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Elementalist.MOD_ID);

    public static final RegistryObject<EntityType<FireballBasic>> FIREBALL_BASIC = ENTITIES.register("fireball_basic",
            () -> EntityType.Builder.<FireballBasic>of(FireballBasic::new, MobCategory.MISC)
                    .sized(0.3125F, 0.3125F)
                    .build(new ResourceLocation(Elementalist.MOD_ID, "fireball_basic").toString()));

    public static final RegistryObject<EntityType<FirePulseEntity>> FIRE_PULSE = ENTITIES.register("fire_pulse",
            () -> EntityType.Builder.<FirePulseEntity>of(FirePulseEntity::new, MobCategory.MISC)
                    .build(new ResourceLocation(Elementalist.MOD_ID, "fire_pulse").toString()));
}
