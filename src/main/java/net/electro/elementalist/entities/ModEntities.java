package net.electro.elementalist.entities;

import net.electro.elementalist.Elementalist;
import net.electro.elementalist.entities.projectiles.FireballBasic;
import net.electro.elementalist.entities.projectiles.IceSpear;
import net.electro.elementalist.entities.spells.ShieldSpellEntity;
import net.electro.elementalist.entities.spells.fire.FireBreathEntity;
import net.electro.elementalist.entities.spells.fire.FireExplosionEntity;
import net.electro.elementalist.entities.spells.fire.FirePulseEntity;
import net.electro.elementalist.entities.spells.water.WaterSlashEntity;
import net.electro.elementalist.entities.spells.water.WaterStreamEntity;
import net.minecraft.resources.ResourceLocation;
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

    public static final RegistryObject<EntityType<FireBreathEntity>> FIRE_BREATH = ENTITIES.register("fire_breath",
            () -> EntityType.Builder.<FireBreathEntity>of(FireBreathEntity::new, MobCategory.MISC)
                    .build(new ResourceLocation(Elementalist.MOD_ID, "fire_breath").toString()));

    public static final RegistryObject<EntityType<FireExplosionEntity>> FIRE_EXPLOSION = ENTITIES.register("fire_explosion",
            () -> EntityType.Builder.<FireExplosionEntity>of(FireExplosionEntity::new, MobCategory.MISC)
                    .build(new ResourceLocation(Elementalist.MOD_ID, "fire_explosion").toString()));

    public static final RegistryObject<EntityType<WaterSlashEntity>> WATER_SLASH = ENTITIES.register("water_slash",
            () -> EntityType.Builder.<WaterSlashEntity>of(WaterSlashEntity::new, MobCategory.MISC)
                    .sized(8f, 0.6f)
                    .build(new ResourceLocation(Elementalist.MOD_ID, "water_slash").toString()));

    public static final RegistryObject<EntityType<WaterStreamEntity>> WATER_STREAM = ENTITIES.register("water_stream",
            () -> EntityType.Builder.<WaterStreamEntity>of(WaterStreamEntity::new, MobCategory.MISC)
                    .build(new ResourceLocation(Elementalist.MOD_ID, "water_stream").toString()));

    public static final RegistryObject<EntityType<IceSpear>> ICE_SPEAR = ENTITIES.register("ice_spear",
            () -> EntityType.Builder.<IceSpear>of(IceSpear::new, MobCategory.MISC)
                    .sized(0.3125F, 0.3125F)
                    .clientTrackingRange(4)
                    .build(new ResourceLocation(Elementalist.MOD_ID, "ice_spear").toString()));

    public static final RegistryObject<EntityType<ShieldSpellEntity>> SHIELD_SPELL = ENTITIES.register("shield_spell",
            () -> EntityType.Builder.<ShieldSpellEntity>of(ShieldSpellEntity::new, MobCategory.MISC)
                    .sized(3f, 3f)
                    .build(new ResourceLocation(Elementalist.MOD_ID, "shield_spell").toString()));
}
