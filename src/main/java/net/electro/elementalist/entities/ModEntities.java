package net.electro.elementalist.entities;

import net.electro.elementalist.Elementalist;
import net.electro.elementalist.entities.mobs.BakenekoEntity;
import net.electro.elementalist.entities.mobs.LesserDemonEntity;
import net.electro.elementalist.entities.mobs.WaterSpiritEntity;
import net.electro.elementalist.entities.projectiles.AirbladeProjectile;
import net.electro.elementalist.entities.projectiles.FireballBasicProjectile;
import net.electro.elementalist.entities.projectiles.IceSpearProjectile;
import net.electro.elementalist.entities.spells.MagicCircleEntity;
import net.electro.elementalist.entities.spells.ShieldSpellEntity;
import net.electro.elementalist.entities.spells.fire.*;
import net.electro.elementalist.entities.spells.ice.IcicleBarrageEntity;
import net.electro.elementalist.entities.spells.lightning.ThunderboltEntity;
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

    // Fire spells
    public static final RegistryObject<EntityType<FireballBasicProjectile>> FIREBALL_BASIC = ENTITIES.register("fireball_basic",
            () -> EntityType.Builder.<FireballBasicProjectile>of(FireballBasicProjectile::new, MobCategory.MISC)
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

    public static final RegistryObject<EntityType<FireWaveEntity>> FIRE_WAVE = ENTITIES.register("fire_wave",
            () -> EntityType.Builder.<FireWaveEntity>of(FireWaveEntity::new, MobCategory.MISC)
                    .build(new ResourceLocation(Elementalist.MOD_ID, "fire_wave").toString()));

    public static final RegistryObject<EntityType<FireClusterExplosionEntity>> FIRE_CLUSTER_EXPLOSION =
            ENTITIES.register("fire_cluster_explosion",
            () -> EntityType.Builder.<FireClusterExplosionEntity>of(FireClusterExplosionEntity::new, MobCategory.MISC)
                    .build(new ResourceLocation(Elementalist.MOD_ID, "fire_cluster_explosion").toString()));
    
    public static final RegistryObject<EntityType<FireClusterExplosionPartEntity>> FIRE_CLUSTER_EXPLOSION_PART =
            ENTITIES.register("fire_cluster_explosion_part",
            () -> EntityType.Builder.<FireClusterExplosionPartEntity>of(FireClusterExplosionPartEntity::new, MobCategory.MISC)
                    .build(new ResourceLocation(Elementalist.MOD_ID, "fire_cluster_explosion_part").toString()));

    // Water spells
    public static final RegistryObject<EntityType<WaterSlashEntity>> WATER_SLASH = ENTITIES.register("water_slash",
            () -> EntityType.Builder.<WaterSlashEntity>of(WaterSlashEntity::new, MobCategory.MISC)
                    .sized(8f, 0.6f)
                    .build(new ResourceLocation(Elementalist.MOD_ID, "water_slash").toString()));

    public static final RegistryObject<EntityType<WaterStreamEntity>> WATER_STREAM = ENTITIES.register("water_stream",
            () -> EntityType.Builder.<WaterStreamEntity>of(WaterStreamEntity::new, MobCategory.MISC)
                    .build(new ResourceLocation(Elementalist.MOD_ID, "water_stream").toString()));

    // Ice spells
    public static final RegistryObject<EntityType<IceSpearProjectile>> ICE_SPEAR = ENTITIES.register("ice_spear",
            () -> EntityType.Builder.<IceSpearProjectile>of(IceSpearProjectile::new, MobCategory.MISC)
                    .sized(0.3125F, 0.3125F)
                    .build(new ResourceLocation(Elementalist.MOD_ID, "ice_spear").toString()));

    public static final RegistryObject<EntityType<IcicleBarrageEntity>> ICICLE_BARRAGE = ENTITIES.register("icicle_barrage",
            () -> EntityType.Builder.<IcicleBarrageEntity>of(IcicleBarrageEntity::new, MobCategory.MISC)
                    .build(new ResourceLocation(Elementalist.MOD_ID, "icicle_barrage").toString()));

    // Lightning spells
    public static final RegistryObject<EntityType<ThunderboltEntity>> THUNDERBOLT = ENTITIES.register("thunderbolt",
            () -> EntityType.Builder.<ThunderboltEntity>of(ThunderboltEntity::new, MobCategory.MISC)
                    .build(new ResourceLocation(Elementalist.MOD_ID, "thunderbolt").toString()));

    // Air spells
    public static final RegistryObject<EntityType<AirbladeProjectile>> AIRBLADE = ENTITIES.register("airblade",
            () -> EntityType.Builder.<AirbladeProjectile>of(AirbladeProjectile::new, MobCategory.MISC)
                    .sized(1F, 0.1F)
                    .build(new ResourceLocation(Elementalist.MOD_ID, "airblade").toString()));

    // Misc
    public static final RegistryObject<EntityType<ShieldSpellEntity>> SHIELD_SPELL = ENTITIES.register("shield_spell",
            () -> EntityType.Builder.<ShieldSpellEntity>of(ShieldSpellEntity::new, MobCategory.MISC)
                    .sized(3f, 3f)
                    .build(new ResourceLocation(Elementalist.MOD_ID, "shield_spell").toString()));

    public static final RegistryObject<EntityType<MagicCircleEntity>> MAGIC_CIRCLE = ENTITIES.register("magic_circle",
            () -> EntityType.Builder.<MagicCircleEntity>of(MagicCircleEntity::new, MobCategory.MISC)
                    .sized(0.1f, 0.1f)
                    .build(new ResourceLocation(Elementalist.MOD_ID, "magic_circle").toString()));

    // Mobs
    public static final RegistryObject<EntityType<WaterSpiritEntity>> WATER_SPIRIT = ENTITIES.register("water_spirit",
            () -> EntityType.Builder.<WaterSpiritEntity>of(WaterSpiritEntity::new, MobCategory.MONSTER)
                    .sized(0.4f, 1.5f)
                    .build(new ResourceLocation(Elementalist.MOD_ID, "water_spirit").toString()));

    public static final RegistryObject<EntityType<LesserDemonEntity>> LESSER_DEMON = ENTITIES.register("lesser_demon",
            () -> EntityType.Builder.<LesserDemonEntity>of(LesserDemonEntity::new, MobCategory.MONSTER)
                    .sized(1f, 3f)
                    .build(new ResourceLocation(Elementalist.MOD_ID, "lesser_demon").toString()));

    public static final RegistryObject<EntityType<BakenekoEntity>> BAKENEKO = ENTITIES.register("bakeneko",
            () -> EntityType.Builder.<BakenekoEntity>of(BakenekoEntity::new, MobCategory.MONSTER)
                    .sized(2f, 1.5f)
                    .build(new ResourceLocation(Elementalist.MOD_ID, "bakeneko").toString()));
}
