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
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Objects;

public class FireClusterExplosionPartEntity extends SpellMasterEntity {
    private int duration = 15;
    public FireClusterExplosionPartEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public FireClusterExplosionPartEntity(LivingEntity owner, DamageType damageType, Vec3 pos) {
        super(ModEntities.FIRE_CLUSTER_EXPLOSION_PART.get(), owner.level);
        setOwner(owner);
        setPos(pos);
        this.noPhysics = true;
        this.damageType = damageType;
        this.ignoredEntities = new ArrayList<>();
        this.ignoredEntities.add(owner);
}




    @Override
    public void tick() {
        super.tick();

        if (this.level.isClientSide()) {
            if (duration == 15) {
                for (float i = 0; i <= 80; i++) {
                    Vec3 particlePos = Utility.getRandomVectorSphere(this.level.getRandom(), 1f, false).add(this.position());
                    this.level.addParticle(ParticleTypes.FLAME, particlePos.x, particlePos.y, particlePos.z,
                            0, 0, 0);
                }
                this.level.playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.LAVA_EXTINGUISH,
                        SoundSource.HOSTILE, 2f, 1f, false);
            }

            if (duration % 3 == 0) {
                for (float i = 0; i <= 5; i++) {
                    Vec3 particlePos = Utility.getRandomVectorSphere(this.level.getRandom(), 1f, false).add(this.position());
                    Vec3 speedVec = Utility.multiplyVec3ByFloat(Utility.getDirectionToVector(particlePos, this.position()), 0.1f);
                    this.level.addParticle(ParticleTypes.FLAME, particlePos.x, particlePos.y, particlePos.z,
                            speedVec.x, speedVec.y, speedVec.z);
                }
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
        duration--;
    }

    private void explode() {
        DamageDealer dd = new DamageDealer(this.position(), this.getOwner(), this, this.ignoredEntities, this.damageType) {
            @Override
            public void damageEffects(LivingEntity entity, float effectAmount, Vec3 direction) {
                entity.setSecondsOnFire(damageType.ADDITIONAL_EFFECT_MULTIPLIER);
                super.damageEffects(entity, effectAmount, direction);
            }
        };
        dd.dealDamageSphere(4, 4);
        dd.setBlocksInRadius(1, 0.2f);
        this.discard();
    }

    private void clientExplode() {
        RandomSource random = this.level.random;
        for (int i = 0; i < 40; i++) {
            Vec3 randomVector = Utility.getRandomVectorSphere(random, 2, true).add(this.position());
            this.level.addParticle(ModParticles.FIRE_EXPLOSION_PARTICLES.get(), randomVector.x,
                    randomVector.y, randomVector.z, 0, 0, 0);

            randomVector = Utility.getRandomVectorSphere(random, 3, true).add(this.position());
            Vec3 particleDirection = Utility.multiplyVec3ByFloat(Utility.getDirectionToVector(this.position(),
                    randomVector).normalize(), 0.3f);
            this.level.addParticle(ParticleTypes.SMOKE, randomVector.x,
                    randomVector.y, randomVector.z,
                    particleDirection.x, particleDirection.y, particleDirection.z);
        }
        this.level.playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.GENERIC_EXPLODE,
                SoundSource.NEUTRAL, 2f, 1f, false);
    }


}