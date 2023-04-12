package net.electro.elementalist.entities.spells.water;

import net.electro.elementalist.entities.ModEntities;
import net.electro.elementalist.entities.spells.ShieldSpellEntity;
import net.electro.elementalist.entities.spells.SpellMasterEntity;
import net.electro.elementalist.util.DamageDealer;
import net.electro.elementalist.util.DamageType;
import net.electro.elementalist.util.RayTraceResult;
import net.electro.elementalist.util.Utility;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class WaterStreamEntity extends SpellMasterEntity {
    private int duration = 12;
    public final float MAX_DISTANCE = 15;
    private static final EntityDataAccessor<Float> DATA_DISTANCE = SynchedEntityData.defineId(WaterStreamEntity.class, EntityDataSerializers.FLOAT);
    public WaterStreamEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public WaterStreamEntity(LivingEntity owner, DamageType damageType) {
        super(ModEntities.WATER_STREAM.get(), owner, damageType);
        this.setPos(this.position().add(0f, 0f, 0f).add(this.getForward().multiply(2.5f, 1f, 2.5f)));
        BlockHitResult blockHitResult = this.level.clip(new ClipContext(this.position(),
                this.position().add(Utility.multiplyVec3ByFloat(this.getForward(), MAX_DISTANCE)),
                ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        if (blockHitResult.getType() == HitResult.Type.BLOCK) {
            setDistance((float) blockHitResult.getLocation().subtract(this.position()).length());
        }
        RayTraceResult rayTraceResult = Utility.rayTraceEntities(this.level, getOwner(), this.position(),
                this.getForward(), MAX_DISTANCE, 1, entity -> entity instanceof ShieldSpellEntity);
        if (rayTraceResult.HIT) {
            double distanceToShield = this.position().subtract(rayTraceResult.HIT_LOCATION).length();
            if (getDistance() > distanceToShield) {
                setDistance((float)distanceToShield);
            }
        }
    }




    private void DealDamage() {
        DamageDealer dd = new DamageDealer(this.position(), getOwner(), this, this.ignoredEntities, this.damageType) {
            @Override
            public void damageEffects(LivingEntity entity, float effectAmount, Vec3 direction) {
                super.damageEffects(entity, effectAmount, direction);
            }
        };
        dd.dealDamageTube(3, getDistance(), this.getForward());
    }


    @Override
    public void tick() {
        super.tick();
        if (!this.level.isClientSide()) {
            if (duration == 8) {
                DealDamage();
            }
            if (duration <= 0) {
                this.discard();
            }
        }
        else {
            if (duration == 12) {
                this.level.playLocalSound(this.position().x, this.position().y, this.position().z, SoundEvents.PLAYER_SPLASH_HIGH_SPEED,
                        SoundSource.NEUTRAL, 2f, 1f, false);
                for (int i = 0; i < 100; i++) {
                    Vec3 particlePos = Utility.getRandomVectorSphere(this.level.random, 3, true).add(this.position());
                    Vec3 particleSpeed = Utility.multiplyVec3ByFloat(this.getForward(), 5);
                    this.level.addParticle(ParticleTypes.SPLASH, particlePos.x, particlePos.y, particlePos.z,
                            particleSpeed.x, particleSpeed.y, particleSpeed.z);
                }
            }
        }
        duration--;

    }

    public void setDistance(float distance) {
        if (!this.level.isClientSide) {
            this.getEntityData().set(DATA_DISTANCE, distance);
        }
    }

    public float getDistance() {
        return this.getEntityData().get(DATA_DISTANCE);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(DATA_DISTANCE, MAX_DISTANCE);
    }
}