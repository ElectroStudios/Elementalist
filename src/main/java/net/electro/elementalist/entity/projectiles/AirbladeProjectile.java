package net.electro.elementalist.entity.projectiles;

import net.electro.elementalist.registry.EntityRegistry;
import net.electro.elementalist.util.DamageType;
import net.electro.elementalist.util.RayTraceResult;
import net.electro.elementalist.util.Utility;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class AirbladeProjectile extends MasterSpellProjectile {
    private final int MAX_DURATION = 100;
    private int duration = 100;
    private final AABB originalBoundingBox;
    private final float MAX_SIZE = 10f;

    public AirbladeProjectile(EntityType<? extends AirbladeProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.originalBoundingBox = this.getBoundingBox();
    }

    public AirbladeProjectile(LivingEntity owner, DamageType damageType) {
        super(EntityRegistry.AIRBLADE.get(), owner, 0.5f, damageType);
        this.originalBoundingBox = this.getBoundingBox();
        this.setRot(owner.getYRot(), owner.getXRot());
    }

    @Override
    protected void projectileTickEffects() {
        Vec3 position = Utility.getRandomVectorCube(this.random, this.getBoundingBox().inflate(this.tickCount*0.2));
        this.level().addParticle(ParticleTypes.CLOUD, position.x, this.getY(), position.z,
                0, 0, 0);
    }

    @Override
    public void explodeClient() {
        for (int i = 0; i < 30; i++) {
            Vec3 particlePos = Utility.getRandomVectorSphere(this.level().getRandom(), 2f, true).add(this.position());
            this.level().addParticle(ParticleTypes.CLOUD, true,
                    particlePos.x, particlePos.y, particlePos.z, 0f, 0f, 0f);
        }
        this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.GENERIC_EXPLODE, SoundSource.NEUTRAL,
                3f, 1f, false);
    }

    @Override
    protected void onHit(HitResult pResult) {
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide()) {
            checkInsideBlocks();

            Vec3 rightVector = this.getForward().cross(new Vec3(0, 1, 0));
            Vec3 leftVector = rightVector.reverse();

            RayTraceResult rayTraceResult = Utility.rayTraceEntities(this.level(), this, this.position().add(rightVector
                            .multiply(0.5f, 0.5f, 0.5f))
                    .add(Utility.multiplyVec3ByFloat(rightVector, this.tickCount*0.2f)).add(this.getForward()),
                    leftVector, 1 + this.tickCount*0.4f, 4,
                    entity -> {return entity instanceof LivingEntity;});

            List<Entity> hitEntities = rayTraceResult.HIT_ENTITIES;
            if (!hitEntities.isEmpty()) {
                for (Entity hitEntity : hitEntities) {
                    ((LivingEntity)hitEntity).knockback(damageType.KNOCKBACK, this.getForward().x, this.getForward().z);
                    hitEntity.hurt(this.damageSources().indirectMagic(this, this.getOwner()),
                            damageType.calculateDamage((LivingEntity) this.getOwner(), hitEntity));
                }
                this.explode();
            }

        }
        duration--;
    }



    @Override
    protected void checkInsideBlocks() {
        Vec3 rightVector = Utility.getRightVector(this);
        Vec3 leftVector = Utility.getLeftVector(this);
        BlockHitResult hitResult = this.level().clip(new ClipContext(
                this.position().add(rightVector.multiply(0.5f, 0.5f, 0.5f))
                        .add(Utility.multiplyVec3ByFloat(rightVector, this.tickCount*0.2f)),
                this.position().add(leftVector.multiply(0.5f, 0.5f, 0.5f))
                        .add(Utility.multiplyVec3ByFloat(leftVector, this.tickCount*0.2f)),
                ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        if (hitResult.getType() != HitResult.Type.MISS) {
            this.explode();
        }
    }


    @Override
    protected void onInsideBlock(BlockState pState) {
        this.discard();
    }
}
