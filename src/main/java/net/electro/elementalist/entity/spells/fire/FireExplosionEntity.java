package net.electro.elementalist.entity.spells.fire;

import net.electro.elementalist.registry.ParticleRegistry;
import net.electro.elementalist.registry.EntityRegistry;
import net.electro.elementalist.entity.spells.ShieldSpellEntity;
import net.electro.elementalist.entity.spells.MasterSpellEntity;
import net.electro.elementalist.util.DamageType;
import net.electro.elementalist.util.RayTraceResult;
import net.electro.elementalist.util.Utility;
import net.minecraft.core.particles.ParticleTypes;
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

public class FireExplosionEntity extends MasterSpellEntity {
    private int duration = 40;
    private final float MAX_RADIUS = 30f;
    private static final EntityDataAccessor<Float> DATA_RADIUS = SynchedEntityData.defineId(FireExplosionEntity.class, EntityDataSerializers.FLOAT);
    private Vec3 targetPos;
    public FireExplosionEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public FireExplosionEntity(LivingEntity owner, DamageType damageType) {
        super(EntityRegistry.FIRE_EXPLOSION.get(), owner, damageType);

        BlockHitResult blockHitResult = this.level().clip(new ClipContext(this.position(),
                this.position().add(this.getForward().multiply(MAX_RADIUS, MAX_RADIUS, MAX_RADIUS)),
                ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        if (blockHitResult.getType() == HitResult.Type.MISS) {
            setRadius(MAX_RADIUS);
        }
        else if (blockHitResult.getType() == HitResult.Type.BLOCK || blockHitResult.getType() == HitResult.Type.ENTITY) {
            setRadius((float) blockHitResult.getLocation().subtract(this.position()).length());
        }
        this.targetPos = blockHitResult.getLocation();
        RayTraceResult rayTraceResult = Utility.rayTraceEntities(this.level(), getOwner(), this.position(),
                this.getForward(), MAX_RADIUS, 1, entity -> entity instanceof ShieldSpellEntity || entity instanceof LivingEntity);
        if (rayTraceResult.HIT) {
            double distanceToEntity = this.position().subtract(rayTraceResult.HIT_LOCATION).length();
            if (getRadius() > distanceToEntity) {
                setRadius((float)distanceToEntity);
                this.targetPos = rayTraceResult.HIT_LOCATION;
            }
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


    @Override
    public void tick() {
        super.tick();
        duration--;

        if (this.level().isClientSide()) {
            if (duration % 4 == 0) {
                float radius = getRadius();
                Vec3 targetPositionClient = this.position().add(this.getForward().multiply(radius, radius, radius));
                for (float i = 0; i <= 1; i += 0.02) {
                    Vec3 particlePos = Utility.vectorInterp(this.position(), targetPositionClient, i);
                    this.level().addParticle(ParticleTypes.FLAME, particlePos.x, particlePos.y, particlePos.z, 0, 0, 0);
                }
                this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.FIRECHARGE_USE, SoundSource.HOSTILE, 2f, 1f, false);
            }
            if (duration == 1) {
                clientExplode();
            }
        }
        else {
            if (duration <= 0) {
                explode();
            }
        }
    }

    private void explode() {
        this.level().explode(getOwner(), targetPos.x, targetPos.y, targetPos.z, 3, true, Level.ExplosionInteraction.MOB);
        this.discard();
    }

    private void clientExplode() {
        RandomSource random = this.level().random;
        float radius = getRadius();
        Vec3 targetPositionClient = this.position().add(this.getForward().multiply(radius, radius, radius));
        for (float i = 0; i <= 1; i += 0.02) {
            Vec3 particlePos = Utility.vectorInterp(this.position(), targetPositionClient, i);
            this.level().addParticle(ParticleRegistry.FIRE_EXPLOSION_PARTICLES.get(), particlePos.x, particlePos.y, particlePos.z, 0, 0, 0);
        }
        for (int i = 0; i < 150; i++) {
            Vec3 randomVector = Utility.getRandomVectorSphere(random, 4, true).add(targetPositionClient);
            this.level().addParticle(ParticleRegistry.FIRE_EXPLOSION_PARTICLES.get(), randomVector.x,
                    randomVector.y, randomVector.z, 0, 0, 0);

            randomVector = Utility.getRandomVectorSphere(random, 5, true).add(targetPositionClient);
            Vec3 particleDirection = Utility.getDirectionToVector(targetPositionClient, randomVector).normalize();
            this.level().addParticle(ParticleRegistry.FIRE_FLASH_PARTICLES.get(), randomVector.x,
                    randomVector.y, randomVector.z,
                    particleDirection.x, particleDirection.y, particleDirection.z);
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(DATA_RADIUS, MAX_RADIUS);
    }
}