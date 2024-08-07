package net.electro.elementalist.entity.spells.fire;

import net.electro.elementalist.registry.ParticleRegistry;
import net.electro.elementalist.registry.EntityRegistry;
import net.electro.elementalist.entity.spells.MasterSpellEntity;
import net.electro.elementalist.util.DamageDealer;
import net.electro.elementalist.util.DamageType;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class FireBreathEntity extends MasterSpellEntity {
    private int duration = 20;
    private final int DURATION_MAX = 20;
    private final float MAX_RADIUS = 25f;
    private static final EntityDataAccessor<Float> DATA_RADIUS = SynchedEntityData.defineId(FireBreathEntity.class, EntityDataSerializers.FLOAT);
    private final float ANGLE = 20f;
    public FireBreathEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public FireBreathEntity(LivingEntity owner, DamageType damageType) {
        super(EntityRegistry.FIRE_BREATH.get(), owner, damageType);

        BlockHitResult blockHitResult = this.level().clip(new ClipContext(this.position(),
                this.position().add(this.getForward().multiply(MAX_RADIUS, MAX_RADIUS, MAX_RADIUS)),
                ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        if (blockHitResult.getType() == HitResult.Type.MISS) {
            setRadius(MAX_RADIUS);
        }
        else if (blockHitResult.getType() == HitResult.Type.BLOCK) {
            setRadius((float) blockHitResult.getLocation().subtract(this.position()).length() + 1);
        }
    }

    public void setRadius(float radius) {
        if (!this.level().isClientSide) {
            this.getEntityData().set(DATA_RADIUS, radius);
        }
    }

    public float getRadius() {
        return this.getEntityData().get(DATA_RADIUS);
    }

    private void DealDamage() {
        DamageDealer dd = new DamageDealer(this.position(), getOwner(), this, this.ignoredEntities, this.damageType) {
            @Override
            public void damageEffects(LivingEntity entity, float effectAmount, Vec3 direction) {
                entity.setSecondsOnFire(damageType.ADDITIONAL_EFFECT_MULTIPLIER);
                ignoredEntities.add(entity);
                super.damageEffects(entity, effectAmount, direction);
            }
        };
        dd.dealDamageCone(ANGLE, (1 - (duration / (float) DURATION_MAX)) * getRadius());
    }

    @Override
    public void tick() {
        super.tick();
        duration--;
        RandomSource random = this.level().random;

        if (this.level().isClientSide()) {
            if (duration % 2 == 0) {
                for (int i = 0; i < 40; i++) {
                    double randomAngle = Math.toRadians(this.getYRot() + (random.nextFloat() * ANGLE) - (ANGLE / 2) + 90);
                    float currentRadius = (1 - (duration / (float) DURATION_MAX)) * getRadius();
                    this.level().addParticle(ParticleRegistry.FIRE_FLASH_PARTICLES.get(),
                            Math.cos(randomAngle) * currentRadius + this.getX(),
                            this.getY() + (3 * random.nextFloat() - 1.5f) - Math.sin(Math.toRadians(this.getXRot())) * currentRadius,
                            Math.sin(randomAngle) * currentRadius + this.getZ(),
                            0, 0.25f, 0);
                }
                this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.FIRECHARGE_USE, SoundSource.HOSTILE, 2f, 1f, false);
            }
        }
        else {

            if (duration % 2 == 0) {
                DealDamage();
            }

            if (duration <= 0) {
                this.discard();
            }
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(DATA_RADIUS, MAX_RADIUS);
    }
}