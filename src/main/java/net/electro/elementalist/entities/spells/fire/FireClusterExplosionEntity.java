package net.electro.elementalist.entities.spells.fire;

import net.electro.elementalist.client.particle.ModParticles;
import net.electro.elementalist.entities.ModEntities;
import net.electro.elementalist.entities.spells.ShieldSpellEntity;
import net.electro.elementalist.entities.spells.SpellMasterEntity;
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
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class FireClusterExplosionEntity extends SpellMasterEntity {
    private int duration = 40;
    private final int MAX_DURATION = 40;
    private final float MAX_RADIUS = 35f;
    private static final EntityDataAccessor<Float> DATA_RADIUS = SynchedEntityData.defineId(FireClusterExplosionEntity.class, EntityDataSerializers.FLOAT);
    private Vec3 targetPos;
    public FireClusterExplosionEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public FireClusterExplosionEntity(LivingEntity owner, DamageType damageType) {
        super(ModEntities.FIRE_CLUSTER_EXPLOSION.get(), owner, damageType);

        BlockHitResult blockHitResult = this.level.clip(new ClipContext(this.position(),
                this.position().add(this.getForward().multiply(MAX_RADIUS, MAX_RADIUS, MAX_RADIUS)),
                ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        if (blockHitResult.getType() == HitResult.Type.MISS) {
            setRadius(MAX_RADIUS);
        }
        else if (blockHitResult.getType() == HitResult.Type.BLOCK || blockHitResult.getType() == HitResult.Type.ENTITY) {
            setRadius((float) blockHitResult.getLocation().subtract(this.position()).length());
        }
        this.targetPos = blockHitResult.getLocation();
        RayTraceResult rayTraceResult = Utility.rayTraceEntities(this.level, getOwner(), this.position(),
                this.getForward(), MAX_RADIUS, 1, entity -> entity instanceof ShieldSpellEntity);
        if (rayTraceResult.HIT) {
            double distanceToShield = this.position().subtract(rayTraceResult.HIT_LOCATION).length();
            if (getRadius() > distanceToShield) {
                setRadius((float)distanceToShield);
                this.targetPos = rayTraceResult.HIT_LOCATION;
            }
        }
    }

    public void setRadius(float radius) {
        if (!this.level.isClientSide) {
            this.getEntityData().set(DATA_RADIUS, radius);
        }
    }

    public float getRadius() {
        return this.getEntityData().get(DATA_RADIUS);
    }


    @Override
    public void tick() {
        super.tick();

        if (!this.level.isClientSide()) {
            Vec3 currentPos = this.position().add(Utility.multiplyVec3ByFloat(this.getForward(),
                    getRadius() * (1 - (float) duration / MAX_DURATION)));
            FireClusterExplosionPartEntity explosion = new FireClusterExplosionPartEntity(this.getOwner(), this.damageType,
                    Utility.getRandomVectorSphere(this.level.getRandom(), 6, true).add(currentPos));
            this.level.addFreshEntity(explosion);
            if (duration <= 0) {
                this.discard();
            }
        }
        duration--;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(DATA_RADIUS, MAX_RADIUS);
    }
}