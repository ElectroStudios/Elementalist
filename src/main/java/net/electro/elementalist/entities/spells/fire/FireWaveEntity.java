package net.electro.elementalist.entities.spells.fire;

import net.electro.elementalist.client.particle.ModParticles;
import net.electro.elementalist.entities.ModEntities;
import net.electro.elementalist.entities.spells.SpellMasterEntity;
import net.electro.elementalist.util.DamageDealer;
import net.electro.elementalist.util.DamageType;
import net.electro.elementalist.util.Utility;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class FireWaveEntity extends SpellMasterEntity {
    private int duration = 20;
    private final int DURATION_MAX = 20;
    private final int MAX_DISTANCE = 15;
    private static final EntityDataAccessor<Float> DATA_RADIUS = SynchedEntityData.defineId(FireWaveEntity.class, EntityDataSerializers.FLOAT);
    private final float ANGLE = 20f;
    private List<Vec3> targetPositions = new ArrayList<>();
    public FireWaveEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public FireWaveEntity(LivingEntity owner, DamageType damageType) {
        super(ModEntities.FIRE_WAVE.get(), owner, damageType);



    }

    public void setRadius(float radius) {
        if (!this.level.isClientSide) {
            this.getEntityData().set(DATA_RADIUS, radius);
        }
    }

    public float getRadius() {
        return this.getEntityData().get(DATA_RADIUS);
    }

    private void DealDamage(Vec3 pos) {
        DamageDealer dd = new DamageDealer(pos, getOwner(), this, this.ignoredEntities, this.damageType) {
            @Override
            public void damageEffects(LivingEntity entity, float effectAmount, Vec3 direction) {
                entity.setSecondsOnFire(damageType.ADDITIONAL_EFFECT_MULTIPLIER);
                ignoredEntities.add(entity);
                super.damageEffects(entity, effectAmount, direction);
                entity.setDeltaMovement(entity.getDeltaMovement().add(0, damageType.KNOCKBACK * 0.8f, 0));
            }
        };
        dd.dealDamageSphere(2f, 2f);
        dd.setBlocksInRadius(1, 0.1f);
    }

    @Override
    public void tick() {
        super.tick();
        if (duration == 20) {
            Vec3 direction = this.getForward().multiply(1, 0, 1).normalize();
            Vec3 currentWavePos = this.position();
            for (int i = 0; i < 15; i++)
            {
                BlockHitResult blockHitResultVertical = this.level.clip(new ClipContext(currentWavePos,
                        currentWavePos.add(0, -3, 0), ClipContext.Block.COLLIDER,
                        ClipContext.Fluid.ANY, this));
                if (blockHitResultVertical.getType() == HitResult.Type.BLOCK) {
                    targetPositions.add(blockHitResultVertical.getLocation());
                    currentWavePos = currentWavePos.subtract(0, currentWavePos.y - blockHitResultVertical.getLocation().y - 1.5f, 0);
                }
                else {
                    break;
                }
                BlockHitResult blockHitResult = this.level.clip(new ClipContext(currentWavePos,
                        currentWavePos.add(direction), ClipContext.Block.COLLIDER,
                        ClipContext.Fluid.ANY, this));
                if (blockHitResult.getType() == HitResult.Type.BLOCK) {
                    break;
                }
                currentWavePos = currentWavePos.add(direction);
            }
            if (!this.level.isClientSide() && targetPositions.isEmpty()) {
                this.discard();
            }
        }
        else if (duration == 0 && !this.level.isClientSide()) {
            for (Vec3 explodePos : targetPositions) {
                DealDamage(explodePos);
            }
            this.discard();
        }

        if (this.level.isClientSide()) {
            if (duration == 1) {
                for (Vec3 explodePos : targetPositions) {
                    for (int i = 0; i < 20; i++) {
                        Vec3 particlePos = Utility.getRandomVectorSphere(this.level.getRandom(), 2, true)
                                .multiply(1, 0.6, 1).add(explodePos);
                        this.level.addParticle(ModParticles.FIRE_FLASH_PARTICLES.get(), particlePos.x, particlePos.y, particlePos.z,
                                0, 0.8f, 0);
                    }
                }
                this.level.playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.FIRECHARGE_USE, SoundSource.NEUTRAL,
                        2f, 1f, false);
            }
            for (Vec3 explodePos : targetPositions) {
                for (int i = 0; i < 1; i++) {
                    Vec3 particlePos = Utility.getRandomVectorSphere(this.level.getRandom(), 2, true)
                            .multiply(1, 0, 1).add(explodePos);
                    this.level.addParticle(ParticleTypes.LAVA, particlePos.x, particlePos.y, particlePos.z,
                            0, 0, 0);
                }
            }
            if (duration % 3 == 0) {
                this.level.playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.LAVA_EXTINGUISH, SoundSource.NEUTRAL,
                        1f, 1f, false);
            }
        }
        duration--;


    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
    }
}